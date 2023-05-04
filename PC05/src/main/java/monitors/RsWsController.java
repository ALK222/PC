package monitors;

import locks.Lock;
import locks.LockRompeEmpate;

public class RsWsController {

	private int numReaders;
	private int numWriters;
	private final Lock lock;
	private boolean readyToRead;
	private boolean readyToWrite;

	public RsWsController() {

		numReaders = 0;
		numWriters = 0;
		lock = new LockRompeEmpate(10000);
		readyToRead = true;
		readyToWrite = true;

	}

	public boolean requestRead() {

		lock.takeLock(numReaders + 1);

		while (numWriters > 0 && !readyToRead) {
			try {
				if(readyToRead) {
					break;
				}
			} catch (Exception e) {

				System.err.println("[MONITOR] ERROR: interrupted thread");
				lock.releaseLock(numReaders + 1);

				return false;

			}
		}

		numReaders++;

		lock.releaseLock(numReaders + 1);

		return true;

	}

	public void releaseRead() {

		lock.takeLock(numReaders);

		numReaders--;

		if (numReaders == 0)
			readyToWrite = true;

		lock.releaseLock(numReaders + 1);

	}

	public boolean requestWrite() {

		lock.takeLock(numWriters + 1);

		while (numReaders > 0 || numWriters > 0) {
			try {
				if(readyToWrite) {
					break;
				}
			} catch (Exception e) {

				System.err.println("[MONITOR] ERROR: interrupted thread");
				lock.releaseLock(numWriters + 1);

				return false;

			}
		}

		numWriters++;

		lock.releaseLock(numWriters);

		return true;

	}

	public void releaseWrite() {

		lock.takeLock(numWriters);

		numWriters--;

		readyToWrite = true;
		readyToRead = true;

		lock.releaseLock(numWriters + 1);

	}

}
