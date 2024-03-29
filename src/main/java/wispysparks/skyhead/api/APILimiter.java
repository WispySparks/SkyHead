package wispysparks.skyhead.api;

import wispysparks.skyhead.SkyHead;
import wispysparks.skyhead.gui.Display;

/** 
 * Helper class to limit the API from going over the request limit of 60 a minute.
 */ 
public class APILimiter {
	
	public static final int MAX_REQUESTS = 60;
	private static int requests = 0; // number of requests made this minute
	private static boolean started = false; 
	private static int timeLeft; 
	
	public static void start(int timeLeftMilliseconds) { 
		if (started == false) { 
			started = true;
			timeLeft = timeLeftMilliseconds;
			timer.start();
		}
	}

	public static void incrementRequests() {
		requests++;
	}

	public static int getRequests() {
		return requests;
	}
	
	private static Thread timer = new Thread(() -> {
		while (true) { // every minute after initial time left set requests back to 0
			try {
				Thread.sleep(timeLeft);
				requests = 0;
				timeLeft = 60000;
				Display.setLevels();
			} catch (InterruptedException e) {
				SkyHead.LOGGER.error("SkyHead API Limiter Error", e);
			}
		}
	});

}
