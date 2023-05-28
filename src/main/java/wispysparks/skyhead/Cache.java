package wispysparks.skyhead;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Class used for storing hashmaps of players and levels with functionality for those hashmaps.
 */
public class Cache { 
	
	private static ConcurrentHashMap<String, String> playerCacheSW = new ConcurrentHashMap<String, String>(); 
	private static ConcurrentHashMap<String, String> playerCacheBW = new ConcurrentHashMap<String, String>(); 
	
	public static void addPlayer(String player, String level) { 
		switch (Config.getMode()) {
			case SKYWARS: playerCacheSW.put(player, level); break;
			case BEDWARS: playerCacheBW.put(player, level); break;
			default: throw new IllegalArgumentException("Invalid Mode");
		}
	}
	
	public static String queryCache(String player) {
		switch (Config.getMode()) {
			case SKYWARS: return playerCacheSW.get(player);
			case BEDWARS: return playerCacheBW.get(player);
			default: throw new IllegalArgumentException("Invalid Mode");
		}
	}
	
	public static boolean inCache(String player, Boolean display) { 
		switch (Config.getMode()) {
			case SKYWARS:
				if (playerCacheSW.containsKey(player)) {
					if (display) return true; // for soley rendering the key
					if (playerCacheSW.get(player).equals(" §fLimit") || playerCacheSW.get(player).equals(" §fbadkey") || playerCacheSW.get(player).equals("")) return false; // if a level wasn't grabbed before try and get it again
					return true; // otherwise yes it is in the cache
				}
				return false;
			case BEDWARS:
				if (playerCacheBW.containsKey(player)) {
					if (display) return true; // for soley rendering the key
					if (playerCacheBW.get(player).equals(" §fLimit") || playerCacheBW.get(player).equals(" §fbadkey") || playerCacheBW.get(player).equals("")) return false; // if a level wasn't grabbed before try and get it again
					return true; // otherwise yes it is in the cache
				}
				return false;
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
	
	public static void clearCache() { 
		switch (Config.getMode()) {
			case SKYWARS: playerCacheSW.clear(); break;
			case BEDWARS: playerCacheBW.clear(); break;
			default: throw new IllegalArgumentException("Invalid Mode");
		}
	}

}
