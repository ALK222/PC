package Actors;

import semaphores.Shared;

public class Consumer extends Thread{
	
	private Shared s;
	private int toConsume;
	
	public Consumer(Shared s, int toConsume) {
		this.s = s;
		this.toConsume = toConsume;
	}
	
	@Override
	public void run() {
		for(int i = 0; i < toConsume; ++i) {
			try {
				s.full.acquire();
				s.mutexC.acquire();
				s.buffer.extraer();
				s.mutexC.release();
				s.empty.release();
			} catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
