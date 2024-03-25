package wispysparks.skyhead.api;

import java.io.IOException;
import java.util.UUID;

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

	// private static final String url = "https://skyhead-api.wispysparks.workers.dev/?uuid=";
	private static final String url = "http://127.0.0.1:8787?uuid=";

	/**
	 * Get's a player's level from HypixelAPI based on current Config.getMode()
	 * @param uuid player uuid
	 * @return level
	 */
    public static synchronized String getLevel(UUID uuid) { 
		if (!SkyHead.enabled()) return "";
		int statusCode = -1;
		String body;
		try {
			HttpClient client = HttpClientBuilder.create().build(); 
			HttpResponse response = client.execute(new HttpGet(url + uuid.toString())); 
			statusCode = response.getStatusLine().getStatusCode();
			body = new BasicResponseHandler().handleResponse(response);
		} catch (IOException e) {
			SkyHead.LOGGER.error("SkyHead API Error. Response Status Code: " + statusCode, e);
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
