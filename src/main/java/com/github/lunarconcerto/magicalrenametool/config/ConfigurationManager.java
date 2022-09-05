package com.github.lunarconcerto.magicalrenametool.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lunarconcerto.magicalrenametool.core.MRTApp;
import com.github.lunarconcerto.magicalrenametool.util.FileUtil;
import com.github.lunarconcerto.magicalrenametool.util.TimeUtil;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Properties;

public class ConfigurationManager {

    private final Properties properties = new Properties();

    private Configuration configuration ;

    public static final File propertiesPath = new File(FileUtil.getUserPath() + "//.settings.prop");

    private static final ConfigurationManager manager = new ConfigurationManager();

    public static ConfigurationManager getManager() {
        return manager;
    }

    private ConfigurationManager() {}

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
    public void load(){
        if (!propertiesPath.exists()){
            try {
                propertiesPath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            properties.load(openInputStream());
            loadObjectConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadObjectConfig(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonConfig = properties.getProperty("mrt.objects.config");
            if (jsonConfig!=null && !jsonConfig.isEmpty()){
                configuration = mapper.readValue(jsonConfig, Configuration.class);
            }else {
                configuration = new Configuration();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(){
        try {
            ObjectMapper mapper = new ObjectMapper();
            String configStr = mapper.writeValueAsString(configuration);

            properties.setProperty("mrt.objects.config" , configStr);
            properties.store(openOutputStream() , "RenameTools properties , last change:"+
                    TimeUtil.getFormattedNowTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(@NotNull Configuration configuration){
        this.configuration = configuration;
        save();
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
