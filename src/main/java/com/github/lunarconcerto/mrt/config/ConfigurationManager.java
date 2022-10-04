package com.github.lunarconcerto.mrt.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.rule.RuleDefiner;
import com.github.lunarconcerto.mrt.rule.SerializableRulePreset;
import com.github.lunarconcerto.mrt.util.TimeUtil;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

@Getter
public class ConfigurationManager {

    private final Properties properties = new Properties();

    private Configuration configuration ;

    private List<SerializableRulePreset> presetList ;

    private final HashMap<String, Property<?>> propertyHashMap = new HashMap<>();

    public static final String userDir = System.getProperty("user.dir");

    public static final File propertiesFile = new File(userDir + "/data/mrt.properties");

    public static final File configFile = new File(userDir + "/data/mrt_config.json");

    public static final File presetFile = new File(userDir + "/data/mrt_preset.json");

    private static final ConfigurationManager manager = new ConfigurationManager();

    public static ConfigurationManager getManager() {
        return manager;
    }

    private ConfigurationManager() {}

    protected InputStream openInputStream(){
        try {
            return new FileInputStream(ConfigurationManager.propertiesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }

    protected OutputStream openOutputStream(){
        try {
            return new FileOutputStream(ConfigurationManager.propertiesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null ;
    }

    protected void ensureDirectoryExists(){
        File file = new File(userDir + "/data");
        if (!file.exists()){
            boolean b = file.mkdir();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void ensureFileExists(){
        if (!ConfigurationManager.propertiesFile.exists()) {
            try {
                ConfigurationManager.propertiesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void load(){
        ensureDirectoryExists();
        ensureFileExists();
        try {
            properties.load(openInputStream());

            loadObjectConfig();
            loadPreset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected void loadObjectConfig() throws JsonProcessingException {
        String jsonConfig = getJson(configFile);
        if (jsonConfig!=null && !jsonConfig.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();
            configuration = mapper.readValue(jsonConfig, Configuration.class);
        }else {
            configuration = new Configuration();
        }
    }

    protected void loadPreset() throws JsonProcessingException {
        String jsonConfig = getJson(presetFile);
        if (jsonConfig!=null && !jsonConfig.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();
            presetList = mapper.readValue(jsonConfig, new TypeReference<>() {});
        }else {
            presetList = new ArrayList<>();
        }
    }

    protected String getJson(@NotNull File file){
        if (file.exists()){
            try {
                return String.join("", Files.readAllLines(file.toPath()));
            } catch (IOException e) {
                throw new MRTRuntimeException(e);
            }
        }else {
            return null ;
        }
    }

    public void save(){
        ensureDirectoryExists();
        try {
            saveObjectConfig();
            savePreset();

            properties.store(openOutputStream() , "RenameTools properties , last change:"+
                    TimeUtil.getFormattedNowTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveObjectConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String configStr = mapper.writeValueAsString(configuration);

        saveJson(configFile, configStr);
    }

    protected void savePreset() throws IOException {
        if (!presetList.isEmpty()){
            ObjectMapper mapper = new ObjectMapper();

            String value = mapper.writeValueAsString(presetList);
            saveJson(presetFile, value);
        }
    }

    protected void saveJson(@NotNull File file, String content) throws IOException {
        if (!file.exists()){
            boolean b = file.createNewFile();
        }
        Files.write(file.toPath(), Collections.singleton(content), StandardCharsets.UTF_8);
    }

    public void addPreset(String presetName, @NotNull List<RuleDefiner> definers){
        SerializableRulePreset preset = SerializableRulePreset.createNewPreset(presetName, definers);

        checkPresetIfExistThanRemove(preset);
        presetList.add(preset);
    }

    public void addPreset(SerializableRulePreset preset){
        checkPresetIfExistThanRemove(preset);
        presetList.add(preset);
    }

    void checkPresetIfExistThanRemove(SerializableRulePreset newPreset){
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

    public List<SerializableRulePreset> getPresetList() {
        return presetList;
    }
}
