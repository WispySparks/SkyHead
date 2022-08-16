package com.wispy.skyhead.gui;

import com.wispy.skyhead.Cache;
import com.wispy.skyhead.SkyHead;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
/**
 * This class is used to interact with the visible elements on the screen to set player names.
 */
public class Display {
	
	public static String currentLevel = ""; // current level to add to a player's name
	
    public static void newTag(EntityPlayer player, String level) { // set players level and add to cache
    	Cache.addPlayer(player.getName(), level);
    	currentLevel = level;
    	player.refreshDisplayName(); // call name format event
    	if (SkyHead.tabEnabled) { // if tab mode is enabled set a player's name in tab to include level
			setTabName(player, level);
    	}
    }
    
    public static void oldTag(EntityPlayer player) { // get player from cache and set level
    	String level = Cache.queryCache(player.getName());
    	currentLevel = level;
    	player.refreshDisplayName(); // call name format event
    	if (SkyHead.tabEnabled) { // if tab mode is enabled set a player's name in tab to include level
    		setTabName(player, level);
    	}
    }

	private static void setTabName(EntityPlayer player, String level) {
		GuiPlayerTabOverlay tabList = Minecraft.getMinecraft().ingameGUI.getTabList(); // get tablist
		NetworkPlayerInfo playerInfo = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(player.getUniqueID()); // get player tab name identifier
		String tabName = tabList.getPlayerName(playerInfo); // get players tab name
		if (!tabName.contains(level)) { // don't add duplicate levels
			playerInfo.setDisplayName(new ChatComponentText(tabName + " " + level)); // set tab name to what it already is plus level
		}
	}

}
