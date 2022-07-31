package com.wispy.skyhead.gui;

import java.util.HashMap;
import java.util.List;

import com.wispy.skyhead.Cache;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;

public class Display {
	
	public static String currentLevel = "";
	
    public static void newTag(EntityPlayer player, String level) { // set players level and add to cache
    	Cache.addPlayer(player.getName(), level);
    	currentLevel = level;
    	player.refreshDisplayName(); // call name format event
    }
    
    public static void oldTag(EntityPlayer player) { // get player from cache and set level
    	currentLevel = Cache.queryCache(player.getName());
    	player.refreshDisplayName(); // call name format event
    }

}
