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

import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;

public class API {

    public static String apikey = ""; // api key
    private static Lock lock = new ReentrantLock();

    private static String getUUID(String name) { // get the uuid from a players username
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response;
        String json = null;
		try {
			HttpGet request = new HttpGet("https://playerdb.co/api/player/minecraft/" + name); // make a get request to the api
			response = client.execute(request);
	        json = new BasicResponseHandler().handleResponse(response);
		} catch (Exception e) {} 
		if (json != null) { // parse the json and grab the raw id 
			JsonElement jelement = new JsonParser().parse(json);
	        JsonObject  jsonObject = jelement.getAsJsonObject();
	        if (!jsonObject.get("data").getAsJsonObject().get("player").isJsonNull()) {
	            return jsonObject.get("data").getAsJsonObject().get("player").getAsJsonObject().get("raw_id").getAsString();
	        }
		}
        return null;
    }
    
    public static String getLevel(String name) { // get a player's skywars level using their uuid and the hypixel api
        String uuid = getUUID(name);
        if (uuid != null && !API.apikey.equals("")) {
        	HttpClient client = HttpClientBuilder.create().build();
            HttpResponse response = null;
            String json = null;
        	lock.lock();
        	try {
        		if (APILimiter.requests < 110) { // make sure not over the 120 requests per minute limit
        			APILimiter.requests += 1;
    	    		try {
    	    			response = client.execute(new HttpGet("https://api.hypixel.net/player?key=" + apikey + "&uuid=" + uuid)); // make get request
    	    			Header[] timeLimit = response.getHeaders("RateLimit-Reset"); // get the initial time left for starting the clock
    	    			APILimiter.start(Integer.parseInt(timeLimit[0].getValue())); // start clock with that time left
    	    	        json = new BasicResponseHandler().handleResponse(response);
    	    		} catch (Exception e) {} 
    	    		if (json != null) { // parse the json and grab the player's level or do level 1 if nothing is there
    	    			JsonElement jelement = new JsonParser().parse(json);
    	                JsonObject  jsonObject = jelement.getAsJsonObject();
    	                if (!jsonObject.get("player").isJsonNull()) {
    	                	if (jsonObject.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("SkyWars").isJsonNull() || jsonObject.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject().get("levelFormatted").isJsonNull()) {
    	                    	return " §71⋆";
    	                	}
    	            		return " " + jsonObject.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject().get("levelFormatted").getAsString();
    	                }
    	    		} else if (response.getStatusLine().getStatusCode() == 403 && SkyHead.enabled) { // if unsuccessful invalid api key
    	    			Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("Invalid API Key, Please Set a Correct Key"));
	                	SkyHead.enabled = false;
	                	return "";
    	    		} else if (response.getStatusLine().getStatusCode() == 403) {
    	    			return "";
    	    		}
                }
        	} finally {
        		lock.unlock();
        	}
            return " §fLimit"; // hit the request limit
        }
        return ""; // no player found in hypixel api
    }

}
