package com.wispy.skyhead;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class Cache {
	
	private static HashMap<String, String> playerCache = new HashMap<String, String>();
	
	public static void addPlayer(String player, String level) { // add player to cache
		playerCache.put(player, level);
	}
	
	public static String queryCache(String player) { // get level for player from cache
		return playerCache.get(player);
	}
	
	public static boolean inCache(String player) { // check if a player is in the cache
		for (String key : playerCache.keySet()) {
			if (key.equals(player)) {
				if (playerCache.get(key).equals("§fLimit")) return false; // if a level wasn't grabbed before try and get it again
				return true;
			}
		}
		return false;
	}
	
	public static int getSize() { // get size of cache
		return playerCache.size();
	}

}
