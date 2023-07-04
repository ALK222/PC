package Storage;

public class AlmacenN implements Almacen {
	
	private Producto[] buff;
	
	private int tam;
	private int front;
	private int rear;
	private int numInsert;
	private int numExtr;
	
	public AlmacenN(int tam) {
        this.buff = new Producto[tam];
        this.tam = tam;
        this.front = 0;
        this.rear = 0;
        this.numInsert = 0;
        this.numExtr = 0;
    }

	@Override
	public void almacenar(Producto producto) {
		if (numInsert - numExtr < tam) { 
            buff[rear] = producto;
            numInsert++;
            rear = (rear + 1) % tam;
        } else {
            System.out.println("[ERROR] Storaged product error");
        }
	}

	@Override
	public Producto extraer() {
		Producto aux = null;
        if (numInsert > numExtr) { 
            aux = buff[front];
            numExtr++;
            front = (front + 1) % tam;
        } else {
            System.out.println("[ERROR] Product extraction error");
        }
        return aux;
	}

}
