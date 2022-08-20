package com.wispy.skyhead.gui;

import com.wispy.skyhead.Cache;

import net.minecraft.entity.player.EntityPlayer;
/**
 * This class is used to interact with the visible elements on the screen to set player names.
 */
public class Display {
	
    public static void newTag(EntityPlayer player, String level) { // set players level and add to cache
    	Cache.addPlayer(player.getName(), level);
    	player.refreshDisplayName(); // call name format event
    }
    
    public static void oldTag(EntityPlayer player) { // get player from cache and set level
    	player.refreshDisplayName(); // call name format event
    }

}
