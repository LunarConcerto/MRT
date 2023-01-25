package com.github.lunarconcerto.mrt.config;

import com.dlsc.preferencesfx.PreferencesFx;
import com.dlsc.preferencesfx.PreferencesFxEvent;
import com.dlsc.preferencesfx.model.Category;
import com.github.lunarconcerto.mrt.MRTApplication;
import com.github.lunarconcerto.mrt.util.FileUtil;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * <a href="https://github.com/dlsc-software-consulting-gmbh/PreferencesFX#maven">PreferencesFX</a>
 */
public class PreferencePaneManager {

    private static final PreferencePaneManager manager = new PreferencePaneManager();
    private final List<Category> categories = new ArrayList<>();
    private PreferencesFx preferencesFx;
    private PreferenceModel preferenceModel;

    public static PreferencePaneManager get() {
        return manager;
    }

    public void showPreferences() {
        if (preferencesFx == null) {
            try {
                createPreferences();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        preferencesFx.show();
    }

    public void addCategory(@NotNull Category... category) {
        categories.addAll(Arrays.asList(category));
    }

    private void createPreferences() throws IOException {
        createDefaultCategory();

        Category[] categoryArray = new Category[categories.size()];
        for (int i = 0, categoriesSize = categories.size(); i < categoriesSize; i++) {
            categoryArray[i] = categories.get(i);
        }

        preferencesFx = PreferencesFx.of(PreferenceModel.class, categoryArray)
                .dialogIcon(new Image(FileUtil.getResourceAsStream("img/icon.cafe.png")))
                .dialogTitle("设置");
        preferencesFx.addEventHandler(PreferencesFxEvent.EVENT_PREFERENCES_SAVED,
                event -> saveConfig());
        ((Stage) preferencesFx.getView().getScene().getWindow()).setAlwaysOnTop(true);
    }

    private void saveConfig() {
        preferenceModel.save();

        ConfigurationManager manager = ConfigurationManager.getManager();
        manager.getConfiguration().initProxy();
        manager.getConfiguration().updateUI();
        manager.save();
    }

    private void createDefaultCategory() {
        preferenceModel = new PreferenceModel(MRTApplication.configuration);
        addCategory(preferenceModel.getCategory());
    }

}
