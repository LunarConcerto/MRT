package com.github.lunarconcerto.mrt.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lunarconcerto.mrt.util.FileUtil;
import com.github.lunarconcerto.mrt.util.TimeUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.HashMap;
import java.util.Properties;

@Getter
public class ConfigurationManager {

    private final Properties properties = new Properties();

    private Configuration configuration ;

    private final HashMap<String, Property<?>> propertyHashMap = new HashMap<>();

    public static final File propertiesPath = new File(FileUtil.getUserPath() + "//.settings.prop");

    private static final ConfigurationManager manager = new ConfigurationManager();

    public static ConfigurationManager getManager() {
        return manager;
    }

    private ConfigurationManager() {}

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

    public void registerProperty(Property<?> property){
        if (property!=null){
            this.propertyHashMap.put(property.getKey(), property);
        }
    }

    public void unregisterProperty(Property<?> property){
        if (property!=null){
            this.propertyHashMap.remove(property.getKey());
        }
    }

    public void unregisterProperty(String key){
        this.propertyHashMap.remove(key);
    }

}
