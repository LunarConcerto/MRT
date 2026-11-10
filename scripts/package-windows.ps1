# Requires a clean Maven build and a Windows x64 JDK 17 in JAVA_HOME.
param([switch]$SmokeTest)

$ErrorActionPreference = 'Stop'
$root = Split-Path -Parent $PSScriptRoot
$target = Join-Path $root 'target'
$inputDir = Join-Path $target 'portable-input'
$outputDir = Join-Path $target 'portable'
$image = Join-Path $outputDir 'MRT'
$archive = Join-Path $target 'MRT-windows-x64.zip'
$jdkRelease = Get-Content -Raw (Join-Path $env:JAVA_HOME 'release')
if ($jdkRelease -notmatch '(?m)^OS_ARCH="(?:x86_64|amd64)"' -or
    $jdkRelease -notmatch '(?m)^JAVA_VERSION="17[."]' -or $env:OS -ne 'Windows_NT') {
    throw 'Set JAVA_HOME to a Windows x64 JDK 17.'
}
if (Test-Path -LiteralPath $image) {
    throw 'Run mvn clean verify before packaging again.'
}

$version = ([xml](Get-Content -Raw (Join-Path $root 'pom.xml'))).project.version
New-Item -ItemType Directory -Path $inputDir -Force | Out-Null
Copy-Item -LiteralPath (Join-Path $target 'MRT.jar') -Destination $inputDir
Copy-Item -LiteralPath (Join-Path $target 'lib') -Destination $inputDir -Recurse

# JDK modules needed by the UI, JSON/settings, HTTPS and filename encodings.
$modules = 'java.desktop,java.logging,java.management,java.naming,java.scripting,java.sql,jdk.charsets,jdk.crypto.ec,jdk.localedata,jdk.unsupported'
$jpackage = Join-Path $env:JAVA_HOME 'bin/jpackage.exe'
& $jpackage --type app-image --name MRT --app-version $version --dest $outputDir `
    --input $inputDir --main-jar MRT.jar --main-class com.github.lunarconcerto.mrt.MRTStarter `
    --add-modules $modules --java-options '-Dmrt.packaged=true' `
    --jlink-options '--strip-debug --no-man-pages --no-header-files --strip-native-commands --compress=2'
if ($LASTEXITCODE -ne 0) { throw "jpackage failed with exit code $LASTEXITCODE" }

if ($SmokeTest) {
    $oldAppData = $env:LOCALAPPDATA
    $process = $null
    try {
        $env:LOCALAPPDATA = Join-Path $target "smoke-profile-$([guid]::NewGuid())"
        $smokeData = Join-Path $env:LOCALAPPDATA 'MRT'
        $stdout = Join-Path $target 'smoke-stdout.log'
        $stderr = Join-Path $target 'smoke-stderr.log'
        $process = Start-Process -FilePath (Join-Path $image 'MRT.exe') `
            -ArgumentList '--smoke-test' -WorkingDirectory $target -PassThru -WindowStyle Hidden `
            -RedirectStandardOutput $stdout -RedirectStandardError $stderr
        if (!$process.WaitForExit(30000) -or $process.ExitCode -ne 0 -or
            !(Test-Path -LiteralPath (Join-Path $smokeData 'mrt_preset.json'))) {
            Get-Content -LiteralPath $stdout, $stderr
            throw 'MRT failed to start or save presets in the smoke test.'
        }
        Write-Output 'MRT launched and saved presets successfully.'
    } finally {
        if ($null -ne $process -and !$process.HasExited) { Stop-Process -Id $process.Id }
        $env:LOCALAPPDATA = $oldAppData
    }
}

Compress-Archive -LiteralPath $image -DestinationPath $archive -CompressionLevel Optimal -Force
Get-Item -LiteralPath $archive
