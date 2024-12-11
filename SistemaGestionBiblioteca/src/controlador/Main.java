package controlador;

import java.awt.EventQueue;

import Vista.InicioSesion;
import Vista.Interfaz;

public class Main {

	public static void main(String[] args) {
		
		
		//La ventana de alejandro comentada por que no se puede modificar desde el desing
		/*
		EventQueue.invokeLater(() -> {
            try {
                Interfaz frame = new Interfaz();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
		*/
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InicioSesion frame = new InicioSesion();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
	}


}
