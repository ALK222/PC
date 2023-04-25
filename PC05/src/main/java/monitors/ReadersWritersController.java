package monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Monitor readers/writers implementado con lock & conditions
public class ReadersWritersController {

    private int numReaders;
    private int numWriters;
    private final Lock lock; 
    private final Condition readyToRead; 
    private final Condition readyToWrite;

    public ReadersWritersController() {

        numReaders = 0;
        numWriters = 0;
        lock = new ReentrantLock(true);
        readyToRead = lock.newCondition();
        readyToWrite = lock.newCondition();

    }

    public boolean requestRead() {

        lock.lock();

        while (numWriters > 0) {
            try {
                readyToRead.await();
            }
            catch (InterruptedException e) {

                System.err.println("ERROR: interrupted thread");
                lock.unlock();

                return false;

            }
        }

        numReaders++;

        lock.unlock();

        return true;

    }

    public void releaseRead() {

        lock.lock();

        numReaders--;

        if (numReaders == 0)
            readyToWrite.signal();

        lock.unlock();

    }

    public boolean requestWrite() {

        lock.lock();

        while (numReaders > 0 || numWriters > 0) {
            try {
                readyToWrite.await();
            }
            catch (InterruptedException e) {

                System.err.println("ERROR: interrupted thread");
                lock.unlock();

                return false;

            }
        }

        numWriters++;

        lock.unlock();

        return true;

    }

    public void releaseWrite() {

        lock.lock();

        numWriters--;

        readyToWrite.signal();
        readyToRead.signalAll();

        lock.unlock();

    }

}
