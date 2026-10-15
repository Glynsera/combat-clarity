
package com.combatclarity;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("combatclarity")
public interface CombatClarityConfig extends Config
{
    @ConfigItem(
            keyName = "showNpcIndicators",
            name = "NPC combat indicators",
            description = "Show combat style icons above NPCs",
            position = 0
    )
    default boolean showNpcIndicators()
    {
        return true;
    }

    @Range(min = 12, max = 30)
    @ConfigItem(
            keyName = "indicatorSize",
            name = "Icon size",
            description = "Adjust the size of combat style icons",
            position = 1
    )
    default int indicatorSize()
    {
        return 20;
    }
}
