package com.github.lunarconcerto.magicalrenametool.config;

import com.github.lunarconcerto.magicalrenametool.core.RenameToolApplication;
import com.github.lunarconcerto.magicalrenametool.core.RenameToolController;
import com.github.lunarconcerto.magicalrenametool.rule.EmptyRule;
import com.github.lunarconcerto.magicalrenametool.rule.Rule;
import com.github.lunarconcerto.magicalrenametool.util.FileNode;
import com.github.lunarconcerto.magicalrenametool.util.FileUtil;
import lombok.Data;
import org.apache.log4j.PropertyConfigurator;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

@Data
public class Configuration {

    private RenameToolController controller  ;

    public static final String keyProxyHost = "proxy-host" ;
    public static final String keyProxyPort = "proxy-post" ;
    public static final String keyDefaultPath = "default-path" ;
    public static final String keyDirShowOnly = "dir-show-only";
    public static final String keySelectedRule = "selected-rule";

    private Properties properties ;

    private String proxyHost ;

    private String proxyPort ;

    private File defaultPath ;

    private boolean dirShowOnly ;

    private String selectedRule ;

    public void initUI(@NotNull RenameToolController controller){
        this.controller = controller ;

        controller.getInput_proxy_host().setText(getProxyHost());
        controller.getInput_proxy_post().setText(getProxyPort());
        if (dirShowOnly){
            controller.getDir_only().setSelected(true);
        }

        if (defaultPath!=null){
            updateDefaultPathUI();
        }
    }

    public String getProxyHost() {
        if (proxyHost==null){
            return "127.0.0.1";
        }

        return proxyHost;
    }

    public String getProxyPort() {
        if (proxyPort==null){
            return "7890" ;
        }

        return proxyPort;
    }

    public void setSelectedRule(){
        Rule rule = RenameToolApplication.ruleMap.get(selectedRule);
        if (controller!=null){
            if (rule == null){
                rule = RenameToolApplication.ruleMap.get(EmptyRule.class.getName());
            }

            controller.getCombe_box_rename_rule().getSelectionModel().select(rule);
            controller.setSelectedRule(rule);
        }
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;

    }

    public void setProxy(){
        System.getProperties().setProperty("proxySet" , "true");
        System.setProperty("http.proxyHost" , getProxyHost());
        System.setProperty("https.proxyHost" , getProxyHost());
        System.setProperty("http.proxyPort" , getProxyPort());
        System.setProperty("https.proxyPort" , getProxyPort());
    }

    public void setDefaultPath(@NotNull File defaultPath) {
        this.defaultPath = defaultPath;

        if (controller != null){
            updateDefaultPathUI();
        }
    }

    private void updateDefaultPathUI(){
        controller.getInput_default_file_path().setText(defaultPath.getPath());
        controller.setSelectingPath(new FileNode(defaultPath));
        controller.buildTree();
    }

    public void setSelectedRule(String selectedRule) {
        this.selectedRule = selectedRule;
    }

    public void setDirShowOnlyAndUpdateUI(boolean dirShowOnly) {
        this.dirShowOnly = dirShowOnly ;

        controller.buildTree();
    }

    public void setDirShowOnly(boolean dirShowOnly) {
        this.dirShowOnly = dirShowOnly;
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
}
