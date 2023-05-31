package wispysparks.skyhead.api;

import static wispysparks.skyhead.SkyHead.MC;

import java.io.IOException;
import java.util.UUID;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

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
    public static synchronized String getLevel(UUID uuid) { 
		if (!SkyHead.enabled()) return "";
		String key = Config.getAPIKey();
        if (key.trim().isEmpty()) {
			Config.setEnabled(false);
			MC.thePlayer.addChatMessage(Text.ChatText("No API Key Set", "§6"));
			return ""; 
        }
		int currentRequests = APILimiter.getRequests();
		if (currentRequests < APILimiter.MAX_REQUESTS) {
			HttpResponse response;
			int statusCode = -1;
			String body;
			APILimiter.incrementRequests();
			try {
				HttpClient client = HttpClientBuilder.create().build(); 
				response = client.execute(new HttpGet("https://api.hypixel.net/player?key=" + key + "&uuid=" + uuid.toString())); 
				statusCode = response.getStatusLine().getStatusCode();
				Header[] headers = response.getHeaders("RateLimit-Reset"); // get the initial time left for starting the clock
				if (headers.length > 0) {
					APILimiter.start(Integer.parseInt(headers[0].getValue()) * 1000); // start clock with that time left
				}
				if (statusCode == 403) { // Forbidden, invalid key
					Config.setEnabled(false);
					MC.thePlayer.addChatMessage(Text.ChatText("Invalid API Key, Please Set a Correct Key", "§6"));
					return "";
				} 
				if (statusCode == 429) { // Request limit reached, technically this shouldn't happen because of the limiter
					return " §fLimit"; 
				}
				body = new BasicResponseHandler().handleResponse(response);
			} catch (IOException e) {
				SkyHead.LOGGER.error("SkyHead API Error. Requests: " + currentRequests + ". Response Status Code: " + statusCode, e);
				return "";
			} 
			if (body != null) { 
				JsonElement json = new JsonParser().parse(body).getAsJsonObject().get("player");
				if (!json.isJsonNull()) { 
					JsonObject playerJson = json.getAsJsonObject();
					switch (Config.getMode()) { 
						case SKYWARS: return getSkywarsLevel(playerJson);
						case BEDWARS: return getBedwarsLevel(playerJson);
						default: throw new IllegalArgumentException("Invalid Mode");
					}
				}
			} 
			return ""; // Not a real player UUID
		}
		return " §fLimit"; 
    }

	private static String getSkywarsLevel(JsonObject playerJson) {
		if (
			playerJson.get("stats") == null 
			|| playerJson.get("stats").getAsJsonObject().get("SkyWars") == null 
			|| playerJson.get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject().get("levelFormatted") == null
		) {
			return " §71⋆"; 
		}
		return " " + playerJson.get("stats").getAsJsonObject().get("SkyWars").getAsJsonObject().get("levelFormatted").getAsString(); 
	}

	private static String getBedwarsLevel(JsonObject playerJson) {
		if (
			playerJson.get("achievements") == null 
			|| playerJson.get("achievements").getAsJsonObject().get("bedwars_level") == null
		) {
			return " §71✫";
		}
		String level = playerJson.get("achievements").getAsJsonObject().get("bedwars_level").getAsString();
		return Text.getFormattedBWLevel(level); 
	}
    
}
