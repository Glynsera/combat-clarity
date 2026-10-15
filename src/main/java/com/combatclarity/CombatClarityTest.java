
package com.combatclarity;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class CombatClarityTest
{
    public static void main(String[] args) throws Exception
    {
        ExternalPluginManager.loadBuiltin(CombatClarityPlugin.class);
        RuneLite.main(args);
    }
}
