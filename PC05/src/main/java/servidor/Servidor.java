package servidor;

import java.util.ArrayList;

import usuario.Usuario;

public class Servidor {
	
	private String ip;
	private int port;
	private ArrayList<Usuario> userList;
	

	public static int main(String args[]) {
		if(args.length != 2) {
			System.out.println("No se ha introducido ip o puerto");
			return -1;
		}
		
		return 0;
	}
}
