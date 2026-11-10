import com.fasterxml.jackson.core.type.TypeReference;
import com.github.lunarconcerto.mrt.exc.MRTRuleException;
import com.github.lunarconcerto.mrt.rule.Rule;
import com.github.lunarconcerto.mrt.rule.RuleManager;
import com.github.lunarconcerto.mrt.rule.io.SerializableRulePreset;
import com.github.lunarconcerto.mrt.util.Json;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RuleManagerTest {
    @Test
    void resolvesLegacyPresetClassNames() throws IOException {
        try (InputStream input = getClass().getResourceAsStream("/mrt_preset.json")) {
            List<SerializableRulePreset> presets = Json.MAPPER.readValue(input, new TypeReference<>() {});
            for (SerializableRulePreset preset : presets) {
                for (var info : preset.getInfos()) {
                    Rule rule = RuleManager.createRule(info.getRuleClassName());
                    assertEquals(info.getRuleClassName(), rule.getClass().getName());
                    assertNotSame(rule, RuleManager.createRule(info.getRuleClassName()));
                }
            }
        }
    }

    @Test
    void rejectsUnregisteredClassNames() {
        assertThrows(MRTRuleException.class, () -> RuleManager.createRule("java.lang.String"));
    }
}
