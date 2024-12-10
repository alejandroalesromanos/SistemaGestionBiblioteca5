package controlador;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		Scanner s = new Scanner(System.in);
		int nota = s.nextInt();
		if (nota >= 0 && nota <= 10) {
			System.out.println("La nota es correcta");
		} else {
			System.out.println("La nota no es muy correcta");
		}
		int prod = 6456;
		float suma = prod + 5;
	
		
	}


}
