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

    private static final File dataDirectory = getDataDirectory();
    public static final File propertiesFile = new File(dataDirectory, "mrt.properties");
    public static final File configFile = new File(dataDirectory, "mrt_config.json");
    public static final File presetFile = new File(dataDirectory, "mrt_preset.json");
    private static final ConfigurationManager manager = new ConfigurationManager();
    private final Properties properties = new Properties();
    private Configuration configuration;
    private List<SerializableRulePreset> presetList;

    private ConfigurationManager() {
    }

    public static ConfigurationManager getManager() {
        return manager;
    }

    private static File getDataDirectory() {
        if (!Boolean.getBoolean("mrt.packaged")) {
            return new File(System.getProperty("user.dir"), "data");
        }
        String localAppData = System.getenv("LOCALAPPDATA");
        if (localAppData == null || localAppData.isBlank()) {
            localAppData = System.getProperty("user.home");
        }
        return new File(localAppData, "MRT");
    }


    public void load() {
        try {
            if (propertiesFile.isFile()) {
                try (InputStream input = new FileInputStream(propertiesFile)) {
                    properties.load(input);
                }
            }
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

    protected void loadPreset() throws IOException {
        String jsonConfig = getJson(presetFile);
        if (jsonConfig == null) {
            try (InputStream defaults = ConfigurationManager.class.getResourceAsStream("/mrt_preset.json")) {
                presetList = Json.MAPPER.readValue(defaults, new TypeReference<>() {});
            }
        } else if (!jsonConfig.isEmpty()) {
            presetList = Json.MAPPER.readValue(jsonConfig, new TypeReference<>() {});
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
        try {
            Files.createDirectories(dataDirectory.toPath());
            saveObjectConfig();
            savePreset();

            try (OutputStream output = new FileOutputStream(propertiesFile)) {
                properties.store(output, "RenameTools properties , last change:" +
                        DateFormatter.getFormattedNowTime());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void saveObjectConfig() throws IOException {
        String configStr = Json.MAPPER.writeValueAsString(configuration);

        saveJson(configFile, configStr);
    }

    protected void savePreset() throws IOException {
        String value = Json.MAPPER.writeValueAsString(presetList);
        saveJson(presetFile, value);
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
