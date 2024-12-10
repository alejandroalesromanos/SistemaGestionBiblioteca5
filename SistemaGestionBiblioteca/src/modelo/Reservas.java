package modelo;

import java.util.Date;

public class Reservas {

	private int id;
	private int idLibro;
	private int idUsuario;
	private Date fechaReserva;
	private boolean disponible;
	
	public Reservas(int id, int idLibro, int idUsuario, Date fechaReserva, boolean disponible) {
		super();
		this.id = id;
		this.idLibro = idLibro;
		this.idUsuario = idUsuario;
		this.fechaReserva = fechaReserva;
		this.disponible = disponible;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdLibro() {
		return idLibro;
	}

	public void setIdLibro(int idLibro) {
		this.idLibro = idLibro;
	}

	public int getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(int idUsuario) {
		this.idUsuario = idUsuario;
	}

	public Date getFechaReserva() {
		return fechaReserva;
	}

	public void setFechaReserva(Date fechaReserva) {
		this.fechaReserva = fechaReserva;
	}

	public boolean isDisponible() {
		return disponible;
	}

	public void setDisponible(boolean disponible) {
		this.disponible = disponible;
	}
	
	
	
}
