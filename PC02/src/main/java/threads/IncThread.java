package threads;

import locks.Lock;

public class IncThread extends Thread {

	public static final int NUM_ITERS = 50;

	private Entero _e;
	private int _id;
	private Lock _lock;

	public IncThread(Entero e, int id, Lock lock) {
		_e = e;
		_id = id;
		_lock = lock;
	}

	public void run() {
		for (int i = 0; i < NUM_ITERS; i++) {
			_lock.takeLock(_id);
			_e.incrementar();
			_lock.releaseLock(_id);

		}
	}
}
