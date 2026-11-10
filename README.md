# MRT · 批量重命名工具

[![构建状态](https://github.com/LunarConcerto/MRT/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/LunarConcerto/MRT/actions/workflows/maven.yml)

MRT 是一个用 Java 和 JavaFX 编写的桌面工具，可批量重命名文件和文件夹。把原名、序号、固定文字等规则按顺序组合，预览新旧名称，确认后再应用。常用的规则组合可以保存为预设。

**[下载 Windows x64 便携版](https://github.com/LunarConcerto/MRT/releases/latest/download/MRT-windows-x64.zip)** · [查看所有版本](https://github.com/LunarConcerto/MRT/releases)

## 下载与启动

1. 下载 `MRT-windows-x64.zip` 并完整解压。
2. 在解压后的 `MRT` 文件夹中双击 `MRT.exe`。

便携包已包含所需的 Java 运行时，无需另外安装 Java。移动程序时，请一起移动整个 `MRT` 文件夹。

## 快速上手

1. 选择 **文件 → 打开…**，进入目标目录，在目录树中勾选要重命名的项目。要处理文件时，可取消 **文件 → 仅显示文件夹**。
2. 在规则列表中右键选择 **添加**，配置所需规则，再拖动规则调整执行顺序。已有预设可从 **规则 → 载入预设** 打开。
3. 选择 **运行 → 开始运行**，在确认窗口核对原名和新名。如需修正，选中一行后修改新名并按 Enter，或右键删除该条结果。
4. 点击 **确认并继续** 应用改名。常用组合可通过 **规则 → 保存预设** 留作下次使用。

例如，给文件名加前缀时，先用 **添加文字** 输入 `旅行_`，再用 **可用选项** 选择 **原名**。`照片.jpg` 会变成 `旅行_照片.jpg`。

当前版本的撤销菜单为占位项。

## 内置规则

| 规则 | 用途 |
| --- | --- |
| 添加文字 | 在当前生成的名称中追加固定文字 |
| 可用选项 | 追加原名或按选择顺序生成的序号 |
| 替换文字 | 替换当前生成的名称中的文字 |
| 删除文字 | 删除当前生成的名称中的指定文字 |
| 替换拓展名 | 修改选中文件的扩展名 |
| 乱码解除 | 尝试修正常见的日文乱码。只想修复原名时，先加入 **可用选项 → 原名** |
| Dlsite音声可选项 | 查询 DLsite 作品资料，将 RJ 号、作品名、社团、声优等字段加入名称 |

规则从上到下执行。要替换或删除原名中的文字，先用 **可用选项 → 原名** 把原名加入新名称，再应用替换或删除规则。

DLsite 规则需要联网。查询失败时，可以在 **文件 → 设置… → 网络设置** 中检查代理开关、主机和端口。

## 从旧版迁移

新版将设置和预设保存在 `%LOCALAPPDATA%\MRT`。首次启动新版前，创建该目录，再把旧版 `data` 目录中已有的下列文件复制过来。

- `mrt.properties`
- `mrt_config.json`
- `mrt_preset.json`

## 开发与发布

使用 **JDK 17** 和 **Maven**。在仓库根目录执行以下命令来运行测试和启动应用。

```powershell
mvn -B clean verify
mvn javafx:run
```

在 Windows x64 上，将 `JAVA_HOME` 指向 x64 版 JDK 17。完成 Maven 构建后，运行打包脚本。

```powershell
./scripts/package-windows.ps1 -SmokeTest
```

ZIP 生成在 `target/MRT-windows-x64.zip`。`-SmokeTest` 会检查打包后的 EXE 能否启动并保存预设。依赖本地代理和 DLsite 的联网测试可单独运行。

```powershell
mvn "-DexcludedGroups=" "-Dtest=TestJsoup" test
```

推送到 `master` 或向它提交 PR 后，GitHub Actions 会构建、测试并上传 Windows 包。发布时先更新 `pom.xml` 的版本号，再推送对应的 `v<版本号>` 标签，例如 `v1.2.0`。标签构建成功后，工作流会创建 Release 并上传 ZIP。 [GitHub Actions](https://github.com/LunarConcerto/MRT/actions/workflows/maven.yml)

新增内置规则时，在 `RuleManager` 中注册构造方法。旧预设以规则的全限定类名作为标识，移动或重命名规则类时需要保留旧名称的映射。

## 反馈

遇到问题时，请在反馈中附上 MRT 版本、Windows 版本、复现步骤和错误日志。 [提交 Issue](https://github.com/LunarConcerto/MRT/issues/new)

MRT 最初为整理 DLsite 同人音声目录而开发。 [前身项目](https://github.com/LunarConcerto/AutoRenameToolForDoujinOnsei)
