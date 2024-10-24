package com.github.lunarconcerto.mrt;

import com.github.lunarconcerto.mrt.config.Configuration;
import com.github.lunarconcerto.mrt.config.ConfigurationManager;
import com.github.lunarconcerto.mrt.rule.RuleManager;
import io.github.palexdev.materialfx.i18n.I18N;
import io.github.palexdev.materialfx.i18n.Language;
import javafx.application.Application;

public class MRTStarter {

    public static void beforeUIStart() {
        Configuration.initLogger();
        ConfigurationManager manager = ConfigurationManager.getManager();
        manager.load();
        MRTApplication.configuration = manager.getConfiguration();
        I18N.setLanguage(Language.SIMPLIFIED_CHINESE);
        RuleManager.getInstance();
    }

    public static void main(String[] args) {
        beforeUIStart();
        Application.launch(MRTApplication.class);
    }

}
