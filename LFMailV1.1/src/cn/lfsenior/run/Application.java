package cn.lfsenior.run;

import java.util.Timer;
import java.util.TimerTask;

import cn.lfsenior.space.ReadMail;

/**
 * 
 * @author lfsenior
 *
 * 上午10:06:17
 */
public class Application {
	public static void main(String[] args) {
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				try {
					new ReadMail().ReadAllEmails();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		};
		Timer timer = new Timer();
		long delay = 0;
		long intevalPeriod = 1 * 1000*60*60;
		// schedules the task to be run in an interval
		timer.scheduleAtFixedRate(task, delay, intevalPeriod);
		
		
	}
}
