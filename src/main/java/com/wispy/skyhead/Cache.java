package com.wispy.skyhead;

import java.util.HashMap;

public class Cache { // stores the levels of bedwars and skywars at the same time and the functions change based on the mode
	
	private static HashMap<String, String> playerCacheSW = new HashMap<String, String>();
	private static HashMap<String, String> playerCacheBW = new HashMap<String, String>();
	
	public static void addPlayer(String player, String level) { // add player to cache
		if (SkyHead.mode == 0) {
			playerCacheSW.put(player, level);
		} else {
			playerCacheBW.put(player, level);
		}
	}
	
	public static String queryCache(String player) { // get level for player from cache
		if (SkyHead.mode == 0) {
			return playerCacheSW.get(player);
		} else {
			return playerCacheBW.get(player);
		}
	}
	
	public static boolean inCache(String player) { // check if a player is in the cache
		if (SkyHead.mode == 0) {
			for (String key : playerCacheSW.keySet()) {
				if (key.equals(player)) {
					if (playerCacheSW.get(key).equals(" §fLimit") || playerCacheSW.get(key).equals(" §fbadkey")) return false; // if a level wasn't grabbed before try and get it again
					return true;
				}
			}
			return false;
		} else {
			for (String key : playerCacheBW.keySet()) {
				if (key.equals(player)) {
					if (playerCacheBW.get(key).equals(" §fLimit") || playerCacheBW.get(key).equals(" §fbadkey")) return false; // if a level wasn't grabbed before try and get it again
					return true;
				}
			}
			return false;
		}
	}
	
	public static int getSize() { // get size of cache
		if (SkyHead.mode == 0) {
			return playerCacheSW.size();
		} else {
			return playerCacheBW.size();
		}
	}
	
	public static void clearCache() { // clear the cache
		if (SkyHead.mode == 0) {
			playerCacheSW.clear();
		} else {
			playerCacheBW.clear();
		}
	}

}
