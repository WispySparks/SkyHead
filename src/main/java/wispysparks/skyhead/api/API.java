package wispysparks.skyhead.api;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import wispysparks.skyhead.Config;
import wispysparks.skyhead.SkyHead;
import wispysparks.skyhead.util.Text;

/** 
 * Class used to interact with the Hypixel API and retrieve the preferred data for use.
 */ 
public class API {

	/**
	 * Get's a player's level from HypixelAPI based on current Config.getMode()
	 * @param uuid player uuid
	 * @return level
	 */
    public static synchronized String getLevel(String uuid) { 
		if (!SkyHead.enabled()) return "";
		String key = Config.getAPIKey();
        if (!key.trim().isEmpty()) {
			if (APILimiter.getRequests() < APILimiter.MAX_REQUESTS) {
				HttpResponse response;
            	String body;
				APILimiter.incrementRequests();
				try {
					HttpClient client = HttpClientBuilder.create().build(); 
					response = client.execute(new HttpGet("https://api.hypixel.net/player?key=" + key + "&uuid=" + uuid)); 
					Header[] headers = response.getHeaders("RateLimit-Reset"); // get the initial time left for starting the clock
					if (headers.length > 0) {
						APILimiter.start(Integer.parseInt(headers[0].getValue()) * 1000); // start clock with that time left
					}
					body = new BasicResponseHandler().handleResponse(response);
				} catch (IOException e) {
					SkyHead.LOGGER.error("SkyHead API Error", e);
					return "";
				} 
				if (body != null) { 
					JsonElement json = new JsonParser().parse(body);
					JsonObject  jsonObject = json.getAsJsonObject();
					if (!jsonObject.get("player").isJsonNull()) { 
						switch (Config.getMode()) { 
							case SKYWARS: 
								if (jsonObject.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("SkyWars").isJsonNull() || jsonObject.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject().get("levelFormatted").isJsonNull()) {
									return " §71⋆"; 
								}
								return " " + jsonObject.get("player").getAsJsonObject().get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject().get("levelFormatted").getAsString(); // level from api
							case BEDWARS: 
								if (jsonObject.get("player").getAsJsonObject().get("achievements").isJsonNull() || jsonObject.get("player").getAsJsonObject().get("achievements").getAsJsonObject().get("bedwars_level").isJsonNull()) {
									return " §71✫";
								}
								String level = jsonObject.get("player").getAsJsonObject().get("achievements").getAsJsonObject().get("bedwars_level").getAsString();
								return getFormattedBWLevel(level); 
							}
					}
				} else if (response.getStatusLine().getStatusCode() == 403) { // if unsuccessful invalid api key
					Config.setEnabled(false);
					Minecraft.getMinecraft().thePlayer.addChatMessage(Text.ChatText("Invalid API Key, Please Set a Correct Key", "§6"));
					return " §fbadkey";
				} 
				return ""; // no player found in hypixel api
			}
            return " §fLimit"; 
        }
		Config.setEnabled(false);
		Minecraft.getMinecraft().thePlayer.addChatMessage(Text.ChatText("No API Key Set", "§6"));
        return ""; 
    }
    
    private static String getFormattedBWLevel(String level) { 
		String colorCode = "§7";
		String assembledLevel = " " + colorCode + level.trim() + "✫";
		Boolean rainbow = false;
		String rainbowLevel = "";
    	int levelNum = Integer.parseInt(level);
    	if (levelNum < 100) colorCode = "§7"; 
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

	private static String getRainbowString(String string) { 
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
