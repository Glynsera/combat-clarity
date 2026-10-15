
package com.combatclarity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.runelite.api.NPC;

public final class NpcCombatStyles
{
    public enum Style
    {
        MELEE,
        MAGIC,
        RANGED
    }

    private static final Map<Integer, EnumSet<Style>> STYLES = loadDatabase();

    private NpcCombatStyles()
    {
    }

    public static EnumSet<Style> getStyles(NPC npc)
    {
        if (npc == null)
        {
            return EnumSet.noneOf(Style.class);
        }

        EnumSet<Style> styles = STYLES.get(npc.getId());

        return styles == null
                ? EnumSet.noneOf(Style.class)
                : EnumSet.copyOf(styles);
    }

    private static Map<Integer, EnumSet<Style>> loadDatabase()
    {
        Map<Integer, EnumSet<Style>> database = new HashMap<>();

        try (InputStream stream = NpcCombatStyles.class
                .getResourceAsStream("/npc-combat-styles.json"))
        {
            if (stream == null)
            {
                System.err.println("[Combat Clarity] NPC database not found.");
                return database;
            }

            Type listType = new TypeToken<List<NpcEntry>>() {}.getType();

            List<NpcEntry> entries = new Gson().fromJson(
                    new InputStreamReader(stream, StandardCharsets.UTF_8),
                    listType
            );

            if (entries == null)
            {
                return database;
            }

            for (NpcEntry entry : entries)
            {
                if (entry == null || entry.styles == null)
                {
                    continue;
                }

                EnumSet<Style> styles = EnumSet.noneOf(Style.class);

                for (String style : entry.styles)
                {
                    if (style == null)
                    {
                        continue;
                    }

                    try
                    {
                        styles.add(Style.valueOf(style.toUpperCase()));
                    }
                    catch (IllegalArgumentException ignored)
                    {
                        // Ignore unknown styles.
                    }
                }

                if (!styles.isEmpty())
                {
                    database.merge(entry.npcId, styles, (existing, added) ->
                    {
                        existing.addAll(added);
                        return existing;
                    });
                }
            }
        }
        catch (Exception e)
        {
            System.err.println(
                    "[Combat Clarity] Failed to load NPC database: "
                            + e.getMessage()
            );
        }

        return database;
    }

    private static class NpcEntry
    {
        int npcId;
        String name;
        List<String> styles;
    }
}
