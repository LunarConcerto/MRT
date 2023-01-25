package com.github.lunarconcerto.mrt.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lunarconcerto.mrt.control.RuleDefiningPane;
import com.github.lunarconcerto.mrt.exc.MRTRuntimeException;
import com.github.lunarconcerto.mrt.rule.io.SerializableRulePreset;
import com.github.lunarconcerto.mrt.util.DateFormatter;
import com.github.lunarconcerto.mrt.util.Json;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Getter
public class ConfigurationManager {

    public static final String userDir = System.getProperty("user.dir");
    public static final File propertiesFile = new File(userDir + "/data/mrt.properties");
    public static final File configFile = new File(userDir + "/data/mrt_config.json");
    public static final File presetFile = new File(userDir + "/data/mrt_preset.json");
    private static final ConfigurationManager manager = new ConfigurationManager();
    private final Properties properties = new Properties();
    private Configuration configuration;
    private List<SerializableRulePreset> presetList;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getManager() {
        return manager;
    }

    protected InputStream openInputStream() {
        try {
            return new FileInputStream(ConfigurationManager.propertiesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected OutputStream openOutputStream() {
        try {
            return new FileOutputStream(ConfigurationManager.propertiesFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void ensureDirectoryExists() {
        File file = new File(userDir + "/data");
        if (!file.exists()) {
            boolean b = file.mkdir();
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    protected void ensureFileExists() {
        if (!ConfigurationManager.propertiesFile.exists()) {
            try {
                ConfigurationManager.propertiesFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void load() {
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
        if (jsonConfig != null && !jsonConfig.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            configuration = mapper.readValue(jsonConfig, Configuration.class);
        } else {
            configuration = new Configuration();
        }
    }

    protected void loadPreset() throws JsonProcessingException {
        String jsonConfig = getJson(presetFile);
        if (jsonConfig != null && !jsonConfig.isEmpty()) {
            ObjectMapper mapper = new ObjectMapper();
            presetList = mapper.readValue(jsonConfig, new TypeReference<>() {
            });
        } else {
            presetList = new ArrayList<>();
        }
    }

    protected String getJson(@NotNull File file) {
        if (file.exists()) {
            try {
                return String.join("", Files.readAllLines(file.toPath()));
            } catch (IOException e) {
                throw new MRTRuntimeException(e);
            }
        } else {
            return null;
        }
    }

    public void save() {
        ensureDirectoryExists();
        try {
            saveObjectConfig();
            savePreset();

            properties.store(openOutputStream(), "RenameTools properties , last change:" +
                    DateFormatter.getFormattedNowTime());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveObjectConfig() throws IOException {
        String configStr = Json.MAPPER.writeValueAsString(configuration);

        saveJson(configFile, configStr);
    }

    protected void savePreset() throws IOException {
        if (!presetList.isEmpty()) {
            String value = Json.MAPPER.writeValueAsString(presetList);
            saveJson(presetFile, value);
        }
    }

    protected void saveJson(@NotNull File file, String content) throws IOException {
        if (!file.exists()) {
            boolean b = file.createNewFile();
        }
        Files.write(file.toPath(), Collections.singleton(content), StandardCharsets.UTF_8);
    }

    public void addPreset(String presetName, @NotNull List<RuleDefiningPane> definers) {
        SerializableRulePreset preset = SerializableRulePreset.createNewPreset(presetName, definers);

        checkPresetIfExistThanRemove(preset);
        presetList.add(preset);
    }

    public void addPreset(SerializableRulePreset preset) {
        checkPresetIfExistThanRemove(preset);
        presetList.add(preset);
    }

    public void deletePreset(SerializableRulePreset preset) {
        presetList.remove(preset);
    }

    void checkPresetIfExistThanRemove(SerializableRulePreset newPreset) {
        presetList.removeIf(existPreset -> existPreset.getPresetName().equals(newPreset.getPresetName()));
    }


    public void save(@NotNull Configuration configuration) {
        this.configuration = configuration;
        save();
    }

    public List<SerializableRulePreset> getPresetList() {
        return presetList;
    }
}
