package modelo;

import java.sql.Date;

public class Notificacion {
	int id;
	String mensaje;
	int idUsuario;
	int idLibro;
	Date fecha;
	boolean leida;
	
	public Notificacion(int id, String mensaje, int idUsuario, int idLibro, Date fecha, boolean leida) {
		this.id = id;
		this.mensaje = mensaje;
		this.idUsuario = idUsuario;
		this.idLibro = idLibro;
		this.fecha = fecha;
		this.leida = leida;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public int getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public boolean isLeida() {
		return leida;
	}

	public void setLeida(boolean leida) {
		this.leida = leida;
	}
	
	
}