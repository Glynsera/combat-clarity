
package com.combatclarity;

import com.google.inject.Provides;
import javax.inject.Inject;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
        name = "Combat Clarity",
        description = "Displays known NPC combat styles using clear pixel-art icons",
        tags = {"combat", "npc", "melee", "magic", "ranged"}
)
public class CombatClarityPlugin extends Plugin
{
    @Inject
    private OverlayManager overlayManager;

    @Inject
    private CombatClarityOverlay overlay;

    @Override
    protected void startUp()
    {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown()
    {
        overlayManager.remove(overlay);
    }

    @Provides
    CombatClarityConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(CombatClarityConfig.class);
    }
}
