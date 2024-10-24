package com.github.lunarconcerto.mrt.util;

import com.github.lunarconcerto.mrt.config.ConfigurationManager;

import static com.github.lunarconcerto.mrt.MRTApplication.configuration;
import static com.github.lunarconcerto.mrt.MRTApplication.mainController;

public class Exit {

    private Exit() {
    }

    public static void exit() {
        /* 保存预设 */
        ConfigurationManager.getManager().addPreset(mainController.saveRuleToPreset("default"));
        /* 保存设置 */
        ConfigurationManager.getManager().save(configuration);
        /* 退出 */
        System.exit(0);
    }

}
