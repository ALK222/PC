package Storage;

public class Almacen1 implements Almacen {
	
	private Producto buff;
	
	public Almacen1() {
		buff = null;
	}

	@Override
	public void almacenar(Producto producto) {
		if(buff == null) {
			buff = producto;
		} else {
			System.out.println("[ERROR] Storaged product error");
		}

	}

	@Override
	public Producto extraer() {
		if(buff == null) {
			System.out.println("[ERROR] Product extraction error");
			return null;
		}
		
		Producto aux = buff;
		buff = null;
		return aux;
	}

}
