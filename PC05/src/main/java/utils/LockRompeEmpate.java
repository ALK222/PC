package utils;

public class LockRompeEmpate implements Lock {

	private volatile int[] _last;
	private volatile int[] _in;
	private int _numThreads;

	public LockRompeEmpate(int numThreads) {
		_numThreads = numThreads;

		_last = new int[numThreads + 1];
		_in = new int[numThreads + 1];
	}

	public int takeLock(int pid) {
		for (int i = 1; i <= _numThreads; ++i) {

			_in[pid] = i;
			_in = _in;

			_last[i] = pid;
			_last = _last;

			for (int j = 1; j <= _numThreads; ++j)
				if (j != pid)
					while (_in[j] >= _in[pid] && _last[i] == pid)
						;

		}
		return 0;
	}

	public int releaseLock(int pid) {
		_in[pid] = 0;
		_in = _in;
		return 0;
	}

	public void incNumThreads() {
		_numThreads++;
	}

	public void decNumThreads() {
		_numThreads--;
	}
}
