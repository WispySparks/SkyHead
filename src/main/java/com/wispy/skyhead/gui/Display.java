package com.wispy.skyhead.gui;

import com.wispy.skyhead.Cache;

import net.minecraft.entity.player.EntityPlayer;

public class Display {
	
	public static String currentLevel = "";
	
    public static void newTag(EntityPlayer player, String level) { // set players level and add to cache
    	Cache.addPlayer(player.getName(), level);
    	currentLevel = level;
    	player.refreshDisplayName(); // call name format event
//    	NetworkPlayerInfo info = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(player.getUniqueID()); // get player tab name stuff
//    	if (info.getDisplayName() == null) { // if no current display name set it to players name plus level
////    		info.setDisplayName(new ChatComponentText(player.getName() + " " + level));
//    	}
//    	else { // otherwise set it to their display name plus level
//    		info.setDisplayName(info.getDisplayName().appendText(" " + level));
//    	}
    }
    
    public static void oldTag(EntityPlayer player) { // get player from cache and set level
    	currentLevel = Cache.queryCache(player.getName());
    	String level = currentLevel;
    	player.refreshDisplayName(); // call name format event
//    	NetworkPlayerInfo info = Minecraft.getMinecraft().getNetHandler().getPlayerInfo(player.getUniqueID()); // get player tab name stuff
//    	if (info.getDisplayName() == null) { // if no current display name set it to players name plus level
////    		info.setDisplayName(new ChatComponentText(player.getName() + " " + level));
//    	}
//    	else if (!info.getDisplayName().toString().contains(level)) { // otherwise set it to their display name plus level
//    		info.setDisplayName(info.getDisplayName().appendText(" " + level));
//    	}
    }

}
