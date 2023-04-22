package locks;

/**
 * Lock using bakery algorithm.
 */
public class LockBakery implements Lock {

	/** Total number of threads. */
	public int _numThreads;

	/** Turn array with the turn of each thread. */
	public volatile int _turn[];

	/**
	 * Instantiates a new lock bakery.
	 *
	 * @param nunThreads number of threads.
	 */
	public LockBakery(int numThreads) {
		_numThreads = numThreads;

		_turn = new int[numThreads];

		for (int i = 0; i < _numThreads; i++) {
			_turn[i] = 0;
		}

		_turn = _turn;

	}

	/**
	 * Optimization for Bakery algorithm.
	 *
	 * @param a turn of the first thread.
	 * @param b pid of the first thread.
	 * @param c turn of the second thread.
	 * @param d pid of the second thread.
	 * @return true, turn of b to get the lock.
	 */
	private boolean op(int a, int b, int c, int d) {
		return a > c || (a == c && b > d);
	}

	@Override
	public int takeLock(int pid) {
		_turn[pid] = 1;
		_turn = _turn;
		_turn[pid] = max() + 1;
		_turn = _turn;
		for (int i = 0; i < _numThreads; i++) {
			while ((_turn[i] != 0) && op(_turn[pid], pid, _turn[i], i))
				;
		}

		return 1;
	}

	@Override
	public int releaseLock(int pid) {
		_turn[pid] = 0;
		_turn = _turn;

		return 1;
	}

	/**
	 * Max turn in array.
	 *
	 * @return max turn on all threads.
	 */
	private int max() {
		int max = _turn[0];

		for (int i = 0; i < _turn.length; i++) {
			if (_turn[i] > max)
				max = _turn[i];
		}
		return max;
	}

}
