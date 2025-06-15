package lol.zt8.AntiRevertAddon.hud;

import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.utils.render.color.Color;

public class HudWarning extends HudElement {
    /**
     * The {@code name} parameter should be in kebab-case.
     */
    public static final HudElementInfo<HudWarning> INFO = new HudElementInfo<>(lol.zt8.AntiRevertAddon.AntiRevertAddon.HUD_GROUP, "AntiRevert warning", "Warns you when AntiRevert is off", HudWarning::new);
    private static final String text = "AntiRevert is disabled!";

    public HudWarning() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {
        setSize(renderer.textWidth(text, true)+20, renderer.textHeight(true)+20);

        if(!Modules.get().get("AntiRevert").isActive()) {
            // Render background
            renderer.quad(x, y, getWidth(), getHeight(), Color.RED);

            // Render text
            renderer.text(text, x+10, y+10, Color.WHITE, true);
        }

    }
}
