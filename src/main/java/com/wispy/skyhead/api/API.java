package com.wispy.skyhead.api;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wispy.skyhead.SkyHead;
import com.wispy.skyhead.util.Text;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
/** 
 * Class used to interact with the Hypixel API and retrieve the preferred data for use.
 */ 
public class API {

    public static String apikey = ""; // api key
    private static Lock lock = new ReentrantLock();

    public static String getLevel(String uuid) { // get a player's skywars level using their uuid and the hypixel api
        if (!API.apikey.equals("")) {
        	HttpClient client = HttpClientBuilder.create().build(); // http request stuff
            HttpResponse response = null;
            String json = null;
        	lock.lock(); // lock so the APILimiter doesn't get confused
        	try {
        		if (APILimiter.requests < 110) { // make sure not over the 120 requests per minute limit
        			APILimiter.requests += 1;
    	    		try {
    	    			response = client.execute(new HttpGet("https://api.hypixel.net/player?key=" + apikey + "&uuid=" + uuid)); // make get request
    	    			Header[] timeLimit = response.getHeaders("RateLimit-Reset"); // get the initial time left for starting the clock
    	    			APILimiter.start(Integer.parseInt(timeLimit[0].getValue())); // start clock with that time left
    	    	        json = new BasicResponseHandler().handleResponse(response);
    	    		} catch (Exception e) {SkyHead.logger.error(e);} 
    	    		if (json != null) { // parse the json and grab the player's level or do level 1 if nothing is there
    	    			JsonElement jelement = new JsonParser().parse(json);
    	                JsonObject  jsonObject = jelement.getAsJsonObject();
    	                if (!jsonObject.get("player").isJsonNull()) { // check if there is a player
    	                	switch (SkyHead.mode) { // get level for current mode
    	                	case 0: // skywars
    	                		if (jsonObject.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("SkyWars").isJsonNull() || jsonObject.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject().get("levelFormatted").isJsonNull()) {
        	                    	return " §71⋆"; // lowest level
        	                	}
        	            		return " " + jsonObject.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject().get("levelFormatted").getAsString(); // level from api
    	                	case 1: // bedwars
    	                		if (jsonObject.get("player").getAsJsonObject().get("achievements").isJsonNull() || jsonObject.get("player").getAsJsonObject().get("achievements").getAsJsonObject().get("bedwars_level").isJsonNull()) {
        	                    	return " §71✫"; //lowest level
        	                	}
    	                		String level = jsonObject.get("player").getAsJsonObject().get("achievements").getAsJsonObject().get("bedwars_level").getAsString();
    	                		return getFormattedBWLevel(level); // level from api
    	                	}
    	                }
    	    		} else if (response.getStatusLine().getStatusCode() == 403 && SkyHead.enabled) { // if unsuccessful invalid api key
    	    			Minecraft.getMinecraft().thePlayer.addChatMessage(Text.ChatText("Invalid API Key, Please Set a Correct Key", "§6"));
	                	SkyHead.enabled = false;
	                	return " §fbadkey";
    	    		} else if (response.getStatusLine().getStatusCode() == 403) {  // if unsuccessful invalid api key
    	    			return " §fbadkey";
    	    		}
	                return ""; // no player found in hypixel api
                }
        	} finally {
        		lock.unlock();
        	}
            return " §fLimit"; // hit the request limit
        }
        return ""; // no key
    }
    
    private static String getFormattedBWLevel(String level) { // get the correct bedwars color for that level
		String colorCode = "§7";
		String assembledLevel = " " + colorCode + level.trim() + "✫";
		Boolean rainbow = false;
		String rainbowLevel = "";
    	int levelNum = Integer.parseInt(level);
    	if (levelNum < 100) colorCode = "§7"; // check what color it is
    	else if (levelNum < 200) colorCode = "§f";
    	else if (levelNum < 300) colorCode = "§6";
    	else if (levelNum < 400) colorCode = "§b";
    	else if (levelNum < 500) colorCode = "§2";
    	else if (levelNum < 600) colorCode = "§3";
    	else if (levelNum < 700) colorCode = "§4";
    	else if (levelNum < 800) colorCode = "§d";
    	else if (levelNum < 900) colorCode = "§9";
    	else if (levelNum < 1000) colorCode = "§5";
    	else {
			rainbowLevel = getRainbowString(level + "✫");
			rainbow = true;
		}  
		if (rainbow) {
			assembledLevel = " " + rainbowLevel;
		} 
		else {
			assembledLevel = " " + colorCode + level.trim() + "✫";
		}
		return assembledLevel;
    }

	private static String getRainbowString(String string) { // get a string with a different colorcode for each character to look like a rainbow
		String rainbowString = "";
		String[] colorChar = {
			EnumChatFormatting.RED.toString(),
			EnumChatFormatting.GOLD.toString(),
			EnumChatFormatting.YELLOW.toString(),
			EnumChatFormatting.GREEN.toString(),
			EnumChatFormatting.AQUA.toString(),
			EnumChatFormatting.BLUE.toString(),
			EnumChatFormatting.LIGHT_PURPLE.toString(),
			EnumChatFormatting.DARK_PURPLE.toString()
		};
		for (int i = 0; i < string.length(); i++) {
			rainbowString += colorChar[i%8] + string.charAt(i);
		}
		return rainbowString;
	}

}
