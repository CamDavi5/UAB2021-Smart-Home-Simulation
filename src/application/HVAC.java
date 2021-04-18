package application;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class HVAC {
	private int temperatureSet;
	private int temperatureCurrent;
	private int temperatureOutside;
	private Timer timer = new Timer();

	private void handleSetTemperature() {
		int temperatureDifference = temperatureCurrent - temperatureSet;
		long delay = 0;
		long period = 1000;
		if (temperatureDifference != 0) {
			timer.scheduleAtFixedRate(new TimerTask() {
				public void run() {
					if (temperatureDifference < 0) {
						temperatureCurrent++;
					}
					else {
						timer.cancel();
					}
				}
			}, delay, period);
		}
	}
	
	private void handleOutsideTemperature() {
		
	}
	
	private void handleDoorEvent() {
		
	}
}
