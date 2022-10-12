package com.github.lunarconcerto.mrt.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.lunarconcerto.mrt.gui.MRTApp;
import com.github.lunarconcerto.mrt.util.FileUtil;
import lombok.Getter;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Getter
public class Configuration {

    @JsonIgnore
    private Properties customProperties;

    private boolean enableProxy = false ;

    private String proxyHost = "127.0.0.1" ;

    private String proxyPort = "7890" ;

    private String defaultPath = "C://" ;

    private boolean dirShowOnly = true ;

    private boolean alwaysOnTop = false ;

    private List<String> historyPaths = new ArrayList<>();

    public Configuration() {}

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * Some Config Control
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void addHistoryPath(String path) {
        if (historyPaths.size() > 10){
            historyPaths.remove(historyPaths.size()-1);
        }

        historyPaths.add(path);
    }

    public void applyConfig() {
        /* 代理 */
        if (enableProxy) enableProxy();
        else disableProxy();
        /* 置顶 */
        MRTApp.mainStage.setAlwaysOnTop(alwaysOnTop);
    }

    public void enableProxy(){
        System.getProperties().setProperty("proxySet" , "true");
        System.setProperty("http.proxyHost" , getProxyHost());
        System.setProperty("https.proxyHost" , getProxyHost());
        System.setProperty("http.proxyPort" , getProxyPort());
        System.setProperty("https.proxyPort" , getProxyPort());
    }

    public void disableProxy() {
        System.getProperties().setProperty("proxySet" , "false");
    }

    public static void initLogger(){
        Properties logProperties = new Properties() ;
        try {
            /* 读取文件 */
            logProperties.load(FileUtil.getResourceAsStream("config/log4j.properties"));
            /* 日志等级 */
            String rootLogger = "%s,console".formatted("debug");
            logProperties.put("log4j.rootLogger" , rootLogger );
            System.setProperty("org.slf4j.simpleLogger.defaultLogLevel" , "debug");
            /* 提交设置 */
            PropertyConfigurator.configure(logProperties) ;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    * Setter / Getter
    * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public Configuration setCustomProperties(Properties customProperties) {
        this.customProperties = customProperties;
        return this;
    }

    public Configuration setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
        return this;
    }

    public Configuration setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
        return this;
    }

    public Configuration setDefaultPath(String defaultPath) {
        this.defaultPath = defaultPath;
        return this;
    }

    public Configuration setDirShowOnly(boolean dirShowOnly) {
        this.dirShowOnly = dirShowOnly;
        return this;
    }

    public Configuration setAlwaysOnTop(boolean alwaysOnTop) {
        this.alwaysOnTop = alwaysOnTop;
        return this;
    }

    public Configuration setEnableProxy(boolean enableProxy) {
        this.enableProxy = enableProxy;
        return this;
    }

    public Configuration setHistoryPaths(List<String> historyPaths) {
        this.historyPaths = historyPaths;
        return this;
    }

    /* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
     * PropertyRegistry
     * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    public void registerProperty(Property<?> property){
        ConfigurationManager.getManager().registerProperty(property);
    }

    public void unregisterProperty(Property<?> property){
        ConfigurationManager.getManager().unregisterProperty(property);
    }

    public void unregisterProperty(String key){
        ConfigurationManager.getManager().unregisterProperty(key);
    }
}
