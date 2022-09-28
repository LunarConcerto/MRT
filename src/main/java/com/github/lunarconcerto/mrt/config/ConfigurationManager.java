package com.github.lunarconcerto.mrt.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.RuleSettingPreset;
import com.github.lunarconcerto.mrt.util.FileUtil;
import com.github.lunarconcerto.mrt.util.TimeUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

@Getter
public class ConfigurationManager {

    private final Properties properties = new Properties();

    private Configuration configuration ;

    private List<RuleSettingPreset> presetList = new ArrayList<>();

    private final HashMap<String, Property<?>> propertyHashMap = new HashMap<>();

    public static final File propertiesFile = new File(FileUtil.getUserPath() + "//.settings.prop");

    private static final ConfigurationManager manager = new ConfigurationManager();

    public static ConfigurationManager getManager() {
        return manager;
    }

    private ConfigurationManager() {}

    protected InputStream openInputStream(){
        try {
            return new FileInputStream(propertiesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }

    protected OutputStream openOutputStream(){
        try {
            return new FileOutputStream(propertiesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void ensurePropertiesFileExists(){
        if (!propertiesFile.exists()) {
            try {
                propertiesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void load(){
        ensurePropertiesFileExists();
        try {
            properties.load(openInputStream());

            loadObjectConfig();
            loadPreset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected void loadObjectConfig() throws JsonProcessingException {
        String jsonConfig = properties.getProperty("mrt.objects.config");
        if (jsonConfig!=null && !jsonConfig.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();
            configuration = mapper.readValue(jsonConfig, Configuration.class);
        }else {
            configuration = new Configuration();
        }
    }

    protected void loadPreset() throws JsonProcessingException {
        String jsonConfig = properties.getProperty("mrt.rule.preset");
        if (jsonConfig!=null && !jsonConfig.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();
            presetList = mapper.readValue(jsonConfig, new TypeReference<>() {});
        }
    }

    public void save(){
        try {
            saveObjectConfig();
            savePreset();

            properties.store(openOutputStream() , "RenameTools properties , last change:"+
                    TimeUtil.getFormattedNowTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveObjectConfig() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String configStr = mapper.writeValueAsString(configuration);

        properties.setProperty("mrt.objects.config" , configStr);
    }

    protected void savePreset() throws JsonProcessingException {
        if (!presetList.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();

            String value = mapper.writeValueAsString(presetList);
            properties.setProperty("mrt.rule.preset", value);
        }
    }

    public void addPreset(String presetName, @NotNull List<RuleDefiner> definers){
        RuleSettingPreset preset = RuleSettingPreset.createNewPreset(presetName, definers);

        checkPresetIfExistThanRemove(preset);
        presetList.add(preset);
    }

    public void addPreset(RuleSettingPreset preset){
        checkPresetIfExistThanRemove(preset);
        presetList.add(preset);
    }

    void checkPresetIfExistThanRemove(RuleSettingPreset newPreset){
        presetList.removeIf(existPreset -> existPreset.getPresetName().equals(newPreset.getPresetName()));
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

    public List<RuleSettingPreset> getPresetList() {
        return presetList;
    }
}
