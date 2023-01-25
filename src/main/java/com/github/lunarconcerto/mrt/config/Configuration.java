package com.github.lunarconcerto.mrt.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lunarconcerto.mrt.MRTApplication;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Data
public class Configuration {

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * 局部变量
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    @JsonIgnore
    private Properties customProperties;

    private boolean enableProxy = false;

    private String proxyHost = "127.0.0.1";

    private String proxyPort = "7890";

    private String defaultPath = "C://";

    private boolean dirShowOnly = true;

    private boolean alwaysOnTop = false;

    private boolean hideLogger = false;

    private int directoryTreeDepth = 1;

    private boolean enableTrayIcon = true;

    private boolean exitOnStageClose = false;

    private List<String> historyPaths = new ArrayList<>();

    public Configuration() {
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * Some Config Control
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public static void initLogger() {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "debug");
    }

    public void addHistoryPath(String path) {
        if (historyPaths.size() > 10) {
            historyPaths.remove(historyPaths.size() - 1);
        }

        historyPaths.add(0, path);
    }

    public void initProxy() {
        /* 代理 */
        if (enableProxy) enableProxy();
        else disableProxy();
    }

    public void updateUI() {
        /* 目录树深度 */
        MRTApplication.mainController.getDirectoryTree().setDepth(directoryTreeDepth);
    }

    public void enableProxy() {
        System.getProperties().setProperty("proxySet", "true");
        System.setProperty("http.proxyHost", getProxyHost());
        System.setProperty("https.proxyHost", getProxyHost());
        System.setProperty("http.proxyPort", getProxyPort());
        System.setProperty("https.proxyPort", getProxyPort());
    }

    public void disableProxy() {
        System.getProperties().setProperty("proxySet", "false");
    }

}
