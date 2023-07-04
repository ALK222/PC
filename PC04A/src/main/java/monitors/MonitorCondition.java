package monitors;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import Storage.AlmacenN;
import Storage.Producto;

public class MonitorCondition {

	private AlmacenN almacen;

	private int availables;
	private int tamMax;

	private final Lock l;
	private final Condition condProd;
	private final Condition condCons;

	public MonitorCondition(int tamMax) {
		almacen = new AlmacenN(tamMax);

		this.tamMax = tamMax;
		this.availables = 0;
		l = new ReentrantLock();
		condCons = l.newCondition();
		condProd = l.newCondition();
	}

	public void almacenar (Producto p[], int num) {
		l.lock();
        try {
            while (availables + num > tamMax)
                condProd.await();

            // Caben num
            for (int i = 0; i < num; ++i) {
                almacen.almacenar(p[i]);
            }
            availables += num;
            condCons.signalAll();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }
	}

	public Producto[] extraer(int num) {
		l.lock();
        Producto[] p = new Producto[num];

        try {
            while (availables < num)
                condCons.await();

            for (int i = 0; i < num; ++i) {
                p[i] = almacen.extraer();
            }
            availables -= num;
            condProd.signalAll();

            System.out.println(availables);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            l.unlock();
        }

        return p;
	}

}
