package monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// Monitor readers/writers implementado con lock & conditions
public class ReadersWritersController {

    private int numReaders; // número actual de lectores
    private int numWriters; // número actual de escritores
    private final Lock lock; // lock para garantizar que los métodos del monitor se ejecutan en exclusión mutua
    private final Condition readyToRead; // variable condicional para indicar que se puede leer
    private final Condition readyToWrite; // variable condicional para indicar que se puede escribir

    public ReadersWritersController() {

        numReaders = 0;
        numWriters = 0;
        lock = new ReentrantLock(true);
        readyToRead = lock.newCondition();
        readyToWrite = lock.newCondition();

    }

    public boolean requestRead() {

        lock.lock();

        // Si hay writers, esperar a que nos digan que se puede leer
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

        // Si no quedan readers, indicar que se puede escribir
        if (numReaders == 0)
            readyToWrite.signal();

        lock.unlock();

    }

    public boolean requestWrite() {

        lock.lock();

        // Si hay readers o writers, esperar a que nos digan que se puede escribir
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

        // Indicar que se puede leer y escribir
        readyToWrite.signal();
        readyToRead.signalAll();

        lock.unlock();

    }

}
