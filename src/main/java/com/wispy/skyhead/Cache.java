package com.wispy.skyhead;

import java.util.concurrent.ConcurrentHashMap;
/**
 * Class used for storing hashmaps of players and levels with functionality for those hashmaps.
 */
public class Cache { // stores the levels of bedwars and skywars at the same time and the functions change based on the mode
	
	private static ConcurrentHashMap<String, String> playerCacheSW = new ConcurrentHashMap<String, String>(); // Skywars level cache
	private static ConcurrentHashMap<String, String> playerCacheBW = new ConcurrentHashMap<String, String>(); // Bedwars level cache
	
	public static void addPlayer(String player, String level) { // add player to cache
		switch (SkyHead.mode) {
			case 0: playerCacheSW.put(player, level); break;
			case 1: playerCacheBW.put(player, level); break;
		}
	}
	
	public static String queryCache(String player) { // get level for player from cache
		switch (SkyHead.mode) {
			case 0: return playerCacheSW.get(player);
			case 1: return playerCacheBW.get(player);
			default: return "";
		}
	}
	
	public static boolean inCache(String player) { // check if a player is in the cache
		switch (SkyHead.mode) {
			case 0:
				for (String key : playerCacheSW.keySet()) {
					if (key.equals(player)) {
						if (playerCacheSW.get(key).equals(" §fLimit") || playerCacheSW.get(key).equals(" §fbadkey")) return false; // if a level wasn't grabbed before try and get it again
						return true; // otherwise yes it is in the cache
					}
				}
				return false;
			case 1:
				for (String key : playerCacheBW.keySet()) { // same thing but for bedwars
					if (key.equals(player)) {
						if (playerCacheBW.get(key).equals(" §fLimit") || playerCacheBW.get(key).equals(" §fbadkey")) return false; // if a level wasn't grabbed before try and get it again
						return true;
					}
				}
				return false;
			default: return false;
		}
	}
	
	public static int getSize() { // get size of cache
		switch (SkyHead.mode) {
			case 0: return playerCacheSW.size();
			case 1: return playerCacheBW.size();
			default: return 0;
		}
	}
	
	public static void clearCache() { // clear the cache
		switch (SkyHead.mode) {
			case 0: playerCacheSW.clear(); break;
			case 1: playerCacheBW.clear(); break;
		}
	}

}
