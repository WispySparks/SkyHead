package wispysparks.skyhead.gui;

import java.lang.reflect.Field;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import wispysparks.skyhead.SkyHead;
import wispysparks.skyhead.api.Cache;

public class PlayerListGui extends GuiPlayerTabOverlay {

	private static final Field HEADER = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175256_i", "header"});
	private static final Field FOOTER = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175255_h", "footer"});
	private static final Field TIME = ReflectionHelper.findField(GuiPlayerTabOverlay.class, new String[]{"field_175253_j", "lastTimeOpened"});
    static {TIME.setAccessible(true);}

    public PlayerListGui(Minecraft mcIn, GuiIngame guiIngameIn) {
        super(mcIn, guiIngameIn);
    }

    @Override
    public String getPlayerName(NetworkPlayerInfo networkPlayerInfoIn) {
        if (Cache.contains(networkPlayerInfoIn.getGameProfile().getName())) {
            if (networkPlayerInfoIn.getDisplayName() != null) { 
                return networkPlayerInfoIn.getDisplayName().getFormattedText() + " " + Cache.query(networkPlayerInfoIn.getGameProfile().getName());
            }
            else { 
                return ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName() + " " + Cache.query(networkPlayerInfoIn.getGameProfile().getName()));
            }
        }
        else if (networkPlayerInfoIn.getDisplayName() != null) {
            return networkPlayerInfoIn.getDisplayName().getFormattedText(); 
        } 
        else  {
            return ScorePlayerTeam.formatPlayerName(networkPlayerInfoIn.getPlayerTeam(), networkPlayerInfoIn.getGameProfile().getName()); 
        }
    }

    public void updateValues(GuiPlayerTabOverlay tabList) {
        try {
            setHeader((IChatComponent) HEADER.get(tabList));
            setFooter((IChatComponent) FOOTER.get(tabList));
            TIME.set(this, (long) TIME.get(tabList));
        } catch (IllegalArgumentException | IllegalAccessException e) {
            SkyHead.LOGGER.error("SkyHead Tab List Render Error", e);
       }
    }
    
}