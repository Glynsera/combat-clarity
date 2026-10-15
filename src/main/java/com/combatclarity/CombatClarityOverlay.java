
package com.combatclarity;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.EnumSet;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.Perspective;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;

public class CombatClarityOverlay extends Overlay
{
    private static final int BASE_SIZE = 24;
    private static final int GAP = 3;

    private final Client client;
    private final CombatClarityConfig config;
    private final NpcCombatStyles npcCombatStyles;

    private final BufferedImage meleeIcon;
    private final BufferedImage rangedIcon;
    private final BufferedImage magicIcon;

    @Inject
    public CombatClarityOverlay(
            Client client,
            CombatClarityConfig config,
            NpcCombatStyles npcCombatStyles
    )
    {
        this.client = client;
        this.config = config;
        this.npcCombatStyles = npcCombatStyles;

        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ABOVE_SCENE);

        meleeIcon = createIcon(
                NpcCombatStyles.Style.MELEE
        );

        rangedIcon = createIcon(
                NpcCombatStyles.Style.RANGED
        );

        magicIcon = createIcon(
                NpcCombatStyles.Style.MAGIC
        );
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        if (!config.showNpcIndicators())
        {
            return null;
        }

        int size = Math.max(12, config.indicatorSize());

        Object oldHint = graphics.getRenderingHint(
                RenderingHints.KEY_INTERPOLATION
        );

        graphics.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR
        );

        for (NPC npc : client.getNpcs())
        {
            if (npc == null || npc.isDead())
            {
                continue;
            }

            EnumSet<NpcCombatStyles.Style> styles =
                    npcCombatStyles.getStyles(npc);

            if (styles.isEmpty())
            {
                continue;
            }

            net.runelite.api.Point point =
                    Perspective.localToCanvas(
                            client,
                            npc.getLocalLocation(),
                            client.getPlane(),
                            npc.getLogicalHeight() + 40
                    );

            if (point == null)
            {
                continue;
            }

            int count = styles.size();
            int totalWidth = count * size + (count - 1) * GAP;

            int x = point.getX() - totalWidth / 2;
            int y = point.getY() - size / 2;

            // Fixed order: Melee, Ranged, Magic.
            if (styles.contains(NpcCombatStyles.Style.MELEE))
            {
                graphics.drawImage(
                        meleeIcon, x, y, size, size, null
                );
                x += size + GAP;
            }

            if (styles.contains(NpcCombatStyles.Style.RANGED))
            {
                graphics.drawImage(
                        rangedIcon, x, y, size, size, null
                );
                x += size + GAP;
            }

            if (styles.contains(NpcCombatStyles.Style.MAGIC))
            {
                graphics.drawImage(
                        magicIcon, x, y, size, size, null
                );
            }
        }

        if (oldHint != null)
        {
            graphics.setRenderingHint(
                    RenderingHints.KEY_INTERPOLATION,
                    oldHint
            );
        }

        return null;
    }

    private BufferedImage createIcon(NpcCombatStyles.Style style)
    {
        BufferedImage image = new BufferedImage(
                BASE_SIZE,
                BASE_SIZE,
                BufferedImage.TYPE_INT_ARGB
        );

        Graphics2D g = image.createGraphics();

        // Pixel-art look: no antialiasing.
        g.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF
        );

        Color border;

        switch (style)
        {
            case MELEE:
                border = new Color(245, 188, 45);
                break;

            case RANGED:
                border = new Color(58, 218, 110);
                break;

            case MAGIC:
                border = new Color(65, 155, 255);
                break;

            default:
                border = Color.WHITE;
        }

        // Dark square background.
        g.setColor(new Color(8, 12, 20, 225));
        g.fillRoundRect(0, 0, 23, 23, 6, 6);

        // Colored frame.
        g.setColor(border);
        g.setStroke(new BasicStroke(2));
        g.drawRoundRect(1, 1, 21, 21, 5, 5);

        if (style == NpcCombatStyles.Style.MELEE)
        {
            drawSword(g);
        }
        else if (style == NpcCombatStyles.Style.RANGED)
        {
            drawBow(g);
        }
        else if (style == NpcCombatStyles.Style.MAGIC)
        {
            drawWizardHat(g);
        }

        g.dispose();
        return image;
    }

    private void drawSword(Graphics2D g)
    {
        // Dark outline of diagonal sword.
        g.setColor(new Color(42, 37, 28));
        g.fillPolygon(
                new int[]{5, 8, 19, 20, 16},
                new int[]{16, 19, 8, 4, 5},
                5
        );

        // Silver blade.
        g.setColor(new Color(200, 210, 222));
        g.fillPolygon(
                new int[]{8, 11, 18, 19, 16},
                new int[]{15, 17, 8, 5, 7},
                5
        );

        // Blade highlight.
        g.setColor(Color.WHITE);
        g.drawLine(11, 14, 17, 8);

        // Golden crossguard.
        g.setColor(new Color(255, 191, 40));
        g.setStroke(new BasicStroke(3));
        g.drawLine(6, 13, 11, 18);

        // Handle.
        g.setColor(new Color(160, 100, 30));
        g.drawLine(7, 17, 5, 19);

        // Golden pommel.
        g.setColor(new Color(255, 215, 65));
        g.fillRect(4, 18, 3, 3);
    }

    private void drawBow(Graphics2D g)
    {
        // Wooden bow, curved to the right.
        g.setColor(new Color(151, 88, 34));
        g.setStroke(new BasicStroke(
                3,
                BasicStroke.CAP_ROUND,
                BasicStroke.JOIN_ROUND
        ));

        g.drawArc(8, 4, 11, 16, -95, 190);

        // Bowstring.
        g.setColor(new Color(215, 224, 220));
        g.setStroke(new BasicStroke(1));
        g.drawLine(13, 4, 13, 20);

        // Diagonal arrow.
        g.setColor(new Color(183, 195, 195));
        g.setStroke(new BasicStroke(2));
        g.drawLine(5, 18, 19, 6);

        // Silver arrowhead.
        g.setColor(new Color(230, 238, 240));
        g.fillPolygon(
                new int[]{18, 21, 20},
                new int[]{5, 4, 8},
                3
        );

        // Green fletching.
        g.setColor(new Color(65, 230, 115));
        g.fillPolygon(
                new int[]{5, 4, 9},
                new int[]{15, 20, 19},
                3
        );
    }

    private void drawWizardHat(Graphics2D g)
    {
        // Hat silhouette.
        g.setColor(new Color(15, 62, 172));
        g.fillPolygon(
                new int[]{5, 8, 12, 15, 18, 17, 19},
                new int[]{17, 12, 4, 5, 8, 10, 17},
                7
        );

        // Brighter blue front.
        g.setColor(new Color(48, 118, 255));
        g.fillPolygon(
                new int[]{8, 12, 14, 12, 10},
                new int[]{16, 6, 6, 11, 17},
                5
        );

        // Curved tip.
        g.setColor(new Color(27, 89, 220));
        g.fillRect(14, 5, 3, 3);
        g.fillRect(16, 7, 2, 3);

        // Gold hat band.
        g.setColor(new Color(255, 190, 40));
        g.fillRect(7, 15, 11, 3);

        // Wide blue brim.
        g.setColor(new Color(22, 77, 200));
        g.fillOval(4, 17, 16, 4);

        // Brim highlight.
        g.setColor(new Color(80, 155, 255));
        g.drawLine(7, 18, 17, 18);
    }
}
