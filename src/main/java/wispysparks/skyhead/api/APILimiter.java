package wispysparks.skyhead.api;

import wispysparks.skyhead.SkyHead;

/** 
 * Helper class to limit the API from going over the request limit of 120 a minute.
 */ 
public class APILimiter {
	
	public static final int MAX_REQUESTS = 110;
	private static int requests = 0; // number of requests made this minute
	private static boolean started = false; 
	private static int timeLeft; 
	
	public static void start(int timeLeft) { 
		if (started == false) { 
			APILimiter.timeLeft = timeLeft*1000; // convert seconds to milliseconds
			started = true;
			timer.start();
		}
	}

	public static synchronized void updateRequests(int amount) {
		requests += amount;
	}

	public static int getRequests() {
		return requests;
	}
	
	private static Thread timer = new Thread(() -> {
		while (true) { // every minute after initial time left set requests back to 0
			try {
				Thread.sleep(timeLeft);
				APILimiter.requests = 0;
				APILimiter.timeLeft = 60000;
			} catch (InterruptedException e) {
				SkyHead.LOGGER.error("API Limiter Error", e);
			}
		}
	});

}
