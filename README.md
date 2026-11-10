# MRT简介

  MRT是一款使用java编写的对文件/文件夹进行批量重命名的工具，UI的实现采用javafx。源自<a href="https://github.com/LunarConcerto/AutoRenameToolForDoujinOnsei">Dlsite同人音声文件夹批量重命名工具</a>，MRT是其改良重制版。
  
  目前该工具已经有多样的规则定义，不仅可以处理DL音声的任务，也加入了其他的功能。
  
# 下载

在发行页下载 `MRT-windows-x64.zip`，完整解压后双击 `MRT.exe`。这个 Windows x64 便携包包含裁剪后的 Java 运行时，电脑无需另装 Java。解压目录里的文件请一起保留。 [GitHub Releases](https://github.com/LunarConcerto/MRT/releases)

新版将设置和预设保存在 `%LOCALAPPDATA%\MRT`。升级时可将旧版 `data` 目录里的 `mrt.properties`、`mrt_config.json` 和 `mrt_preset.json` 复制到这里，再启动新版。
   
# 如何使用

   请参阅页面 -> <a href="https://lunarconcerto.cafe/mrt">MRT使用手册</a>

# 注意事项

+ 在使用dlsite规则时，程序会到dlsite网站中爬取对应作品的数据来进行重命名。可能会出现网络相关的问题，请前往"设置...->代理"来修改代理设置选项。
+ 程序测试尚不充分，如果有报错或者崩溃，请联系我或提交issue。

## 构建与发布

使用 JDK 17 和 Maven 构建。在 Windows x64 上，将 `JAVA_HOME` 指向对应的 JDK，然后在 PowerShell 中执行下面两条命令。

```powershell
mvn -B clean verify
./scripts/package-windows.ps1 -SmokeTest
```

构建会生成 `target/MRT.jar` 和对应的 `target/lib`。开发时可用 `mvn javafx:run` 启动。打包脚本会生成 `target/MRT-windows-x64.zip`，`-SmokeTest` 会先检查打包后的应用能否启动并保存预设。依赖本地代理和 DLsite 的手工测试可用 `mvn "-DexcludedGroups=" "-Dtest=TestJsoup" test` 单独运行。

向 `master` 推送或提交 PR 后，GitHub Actions 会构建、测试、打包并上传运行产物。发布新版本时，先更新 `pom.xml` 中的版本号，再推送与之对应的 `v<版本号>` 标签。标签构建通过后，工作流会创建 Release 并上传同一个 ZIP。

新增内置命名规则时，在 `RuleManager` 中注册构造方法。预设仍用规则的全限定类名保存，因此移动或重命名规则类时，需要保留旧名称的映射。若要生成 GraalVM Native Image，还需要验证 JavaFX、托盘，以及 FXML 和 JSON 的反射与资源配置。
