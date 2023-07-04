package monitors;

import Storage.AlmacenN;
import Storage.Producto;

public class MonitorSynch {
	
	private AlmacenN almacen;
	
	private int availables;
	private int tamMax;
	
	public MonitorSynch(int tamMax) {
		almacen = new AlmacenN(tamMax);
		
		this.tamMax = tamMax;
		this.availables = 0;
	}
	
	public synchronized void almacenar (Producto p) {
		try {
			while (availables == tamMax)
				wait();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		almacen.almacenar(p);
		availables++;
		notifyAll();
	}
	
	public synchronized Producto extraer() {
		Producto p = null;
		
		try {
			while (availables == 0)
				wait();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		p = almacen.extraer();
		
		availables--;
		notifyAll();
		return p;
	}

}
