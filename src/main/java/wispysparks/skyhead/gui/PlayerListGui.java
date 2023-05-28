package wispysparks.skyhead.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.scoreboard.ScorePlayerTeam;
import wispysparks.skyhead.api.Cache;

public class PlayerListGui extends GuiPlayerTabOverlay {

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
    
}