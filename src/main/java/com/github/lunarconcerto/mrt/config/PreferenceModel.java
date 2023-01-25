package com.github.lunarconcerto.mrt.config;

import com.dlsc.formsfx.model.validators.IntegerRangeValidator;
import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.github.lunarconcerto.mrt.MRTApplication;
import javafx.beans.property.*;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class PreferenceModel {


    protected BooleanProperty enableProxy;

    protected StringProperty proxyHost;

    protected IntegerProperty proxyPort;

    protected ObjectProperty<File> defaultPath;

    protected IntegerProperty directoryTreeDepth;

    protected BooleanProperty enableTrayIcon;

    protected BooleanProperty exitOnStageClose;

    protected Configuration configuration;

    public PreferenceModel(@NotNull Configuration configuration) {
        this.configuration = configuration;
        enableProxy = new SimpleBooleanProperty(configuration.isEnableProxy());
        proxyHost = new SimpleStringProperty(configuration.getProxyHost());
        proxyPort = new SimpleIntegerProperty(Integer.parseInt(configuration.getProxyPort()));
        defaultPath = new SimpleObjectProperty<>(new File(configuration.getDefaultPath()));
        directoryTreeDepth = new SimpleIntegerProperty(configuration.getDirectoryTreeDepth());
        directoryTreeDepth.addListener((observable, oldValue, newValue) -> {
            MRTApplication.mainController.getDirectoryTree().setDepth(newValue.intValue());
        });
        enableTrayIcon = new SimpleBooleanProperty(configuration.isEnableTrayIcon());
        exitOnStageClose = new SimpleBooleanProperty(configuration.isExitOnStageClose());
    }

    public Category getCategory() {
        return Category.of("主选项").subCategories(
                Category.of("系统设置",
                        Group.of("文件",
                                Setting.of("默认打开路径", defaultPath, true),
                                Setting.of("目录树最大层级", directoryTreeDepth, 1, 10,
                                        null)
                        ),
                        Group.of("系统",
                                Setting.of("启用托盘程序", enableTrayIcon),
                                Setting.of("关闭窗口时退出程序", exitOnStageClose)
                        )
                ),
                Category.of("网络设置",
                        Group.of("网络",
                                Setting.of("启用代理", enableProxy),
                                Setting.of("代理主机", proxyHost),
                                Setting.of("代理端口", proxyPort)
                                        .validate(IntegerRangeValidator
                                                .between(0, 65536,
                                                        "非法的端口号,应在0 ~ 65536范围内."))
                        )
                )
        );
    }

    public void save() {
        configuration.setEnableProxy(enableProxy.get());
        configuration.setProxyHost(proxyHost.get());
        configuration.setProxyPort(String.valueOf(proxyPort.get()));
        configuration.setDefaultPath(defaultPath.get().getPath());
        configuration.setDirectoryTreeDepth(directoryTreeDepth.get());
        configuration.setEnableTrayIcon(enableTrayIcon.get());
        configuration.setExitOnStageClose(exitOnStageClose.get());
    }

}
