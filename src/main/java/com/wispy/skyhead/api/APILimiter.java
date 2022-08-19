package com.wispy.skyhead.api;

/** 
 * Helper class to limit the API from going over the request limit of 120 a minute.
 */ 
public class APILimiter {
	
	public static int requests = 0; // number of requests made this minute
	private static boolean started = false; // whether or not the limiter clock has been started
	private static int timeLeft; // how much time left until next reset
	
	public static void start(int timeLeft) { // start the clock and get an inital time left from the api
		if (started == false) { 
			APILimiter.timeLeft = timeLeft*1000; // convert seconds to milliseconds
			started = true;
			main.start();
		}
	}
	
	private static Thread main = new Thread(new Runnable() { // reset requests made to 0 every minute
		@Override
		public void run() {
			while (true) { // every minute after initial time left set requests back to 0
				try {
					Thread.sleep(timeLeft);
					APILimiter.requests = 0;
					APILimiter.timeLeft = 60000;
				} catch (InterruptedException e) {System.out.println(e);}
			}
		}
	});

}
