package com.github.lunarconcerto.magicalrenametool.config;

import com.github.lunarconcerto.magicalrenametool.util.FileUtil;
import com.github.lunarconcerto.magicalrenametool.util.TimeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Properties;

public class ConfigurationLoader {

    public final Properties properties ;

    public static final File propertiesPath = new File(FileUtil.getUserPath() + "//.settings.prop");

    private static ConfigurationLoader loader ;

    private ConfigurationLoader() {
        properties = new Properties();
    }

    public static ConfigurationLoader getLoader() {
        if (loader==null){
            loader=new ConfigurationLoader();
        }

        return loader;
    }

    public Properties getProperties(){
        return properties ;
    }

    protected InputStream openInputStream(){
        try {
            return new FileInputStream(propertiesPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }

    protected OutputStream openOutputStream(){
        try {
            return new FileOutputStream(propertiesPath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void loadProperties(){
        if (!propertiesPath.exists()){
            try {
                propertiesPath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        load();
    }

    public Properties load(){
        try {
            properties.load(openInputStream());
            return properties ;
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("读取配置文件失败。");
    }

    public void save(){
        try {
            properties.store(openOutputStream() , "RenameTools properties , last change:"+ TimeUtil.getFormattedNowTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(@NotNull Configuration configuration){
        properties.setProperty(Configuration.keyProxyHost, configuration.getProxyHost());
        properties.setProperty(Configuration.keyProxyPort, configuration.getProxyPort());

        File defaultPath = configuration.getDefaultPath();
        if (defaultPath!=null){
            properties.setProperty(Configuration.keyDefaultPath, defaultPath.getPath());
        }

        properties.setProperty(Configuration.keyDirShowOnly, String.valueOf(configuration.isDirShowOnly()));

        properties.setProperty(Configuration.keySelectedRule , configuration.getSelectedRule());

        save();
    }

    public Configuration toConfiguration(){
        loadProperties();
        Configuration configuration = new Configuration();

        configuration.setProperties(properties);

        configuration.setProxyHost(properties.getProperty(Configuration.keyProxyHost));
        configuration.setProxyPort(properties.getProperty(Configuration.keyProxyPort));

        String defaultPath = properties.getProperty(Configuration.keyDefaultPath);
        if (defaultPath!=null){
            configuration.setDefaultPath(new File(defaultPath));
        }

        String dirShowOnly = properties.getProperty(Configuration.keyDirShowOnly);
        if (dirShowOnly!=null){
            configuration.setDirShowOnly(Boolean.parseBoolean(dirShowOnly));
        }else {
            configuration.setDirShowOnly(true);
        }

        String selectedRule = properties.getProperty(Configuration.keySelectedRule);
        if (selectedRule!=null){
            configuration.setSelectedRule(selectedRule);
        }

        return configuration ;
    }
}
