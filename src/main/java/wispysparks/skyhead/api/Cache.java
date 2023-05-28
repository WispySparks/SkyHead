package wispysparks.skyhead.api;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import wispysparks.skyhead.Config;

/**
 * Stores all the hashmaps for each mode and provides convenience methods to do operations on the current mode's cache.
 */
public class Cache { 
	
	private static Map<String, String> playerCacheSW = new ConcurrentHashMap<>(); 
	private static Map<String, String> playerCacheBW = new ConcurrentHashMap<>(); 
	
	public static void addPlayer(String player, String level) { 
		switch (Config.getMode()) {
			case SKYWARS: playerCacheSW.put(player, level); break;
			case BEDWARS: playerCacheBW.put(player, level); break;
			default: throw new IllegalArgumentException("Invalid Mode");
		}
	}
	
	/**
	 * Users should make a call to {@link Cache#contains()} before this method as otherwise it could return null.
	 * @param player name
	 * @return player's level
	 */
	public static String query(String player) {
		switch (Config.getMode()) {
			case SKYWARS: return playerCacheSW.get(player);
			case BEDWARS: return playerCacheBW.get(player);
			default: throw new IllegalArgumentException("Invalid Mode");
		}
	}
	
	public static boolean contains(String player) { 
		switch (Config.getMode()) {
			case SKYWARS: return playerCacheSW.containsKey(player);
			case BEDWARS: return playerCacheBW.containsKey(player);
			default: throw new IllegalArgumentException("Invalid Mode");
		}
	}
	
	public static int getSize() {
		switch (Config.getMode()) {
			case SKYWARS: return playerCacheSW.size();
			case BEDWARS: return playerCacheBW.size();
			default: throw new IllegalArgumentException("Invalid Mode");
		}
	}
	
	public static void clear() { 
		switch (Config.getMode()) {
			case SKYWARS: playerCacheSW.clear(); break;
			case BEDWARS: playerCacheBW.clear(); break;
			default: throw new IllegalArgumentException("Invalid Mode");
		}
	}

}
