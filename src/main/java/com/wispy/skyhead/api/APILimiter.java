package com.wispy.skyhead.api;

public class APILimiter {
	
	public static int requests = 0;
	private static boolean started = false;
	private static int timeLeft;
	
	public static void start(int timeLeft) { // start the clock
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
				} catch (InterruptedException e) {}
			}
		}
	});

}
