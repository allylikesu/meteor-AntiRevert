package lol.zt8.AntiRevertAddon;

import lol.zt8.AntiRevertAddon.hud.HudWarning;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class AntiRevertAddon extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("AntiRevert");
    public static final HudGroup HUD_GROUP = new HudGroup("AntiRevert");

    @Override
    public void onInitialize() {
        LOG.info("Initializing Meteor AntiRevert");

        // Modules
        Modules.get().add(new lol.zt8.AntiRevertAddon.modules.AntiRevertModule());

        // HUD
        Hud.get().register(HudWarning.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "lol.zt8.AntiRevertAddon";
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("allylikesu", "meteor-AntiRevert");
    }
}
