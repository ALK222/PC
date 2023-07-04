package Actors;

import monitors.MonitorSynch;

public class Consumer extends Thread{
	
	private MonitorSynch s;
	private int toConsume;
	private int batch;
	
	public Consumer(MonitorSynch s, int toConsume, int batch) {
		this.s = s;
		this.toConsume = toConsume;
		this.batch = batch;
	}
	
	public Consumer(MonitorSynch s, int toConsume) {
		this.s = s;
		this.toConsume = toConsume;
	}
	
	@Override
	public void run() {
		// MONITOR SYNC
		for(int i = 0; i < toConsume; ++i) {
			s.extraer();
		}
		
		// MONITOR COND
		/*for(int i = 0; i < toConsume / batch; ++i) {
			s.extraer(batch);
		}*/
	}

}
