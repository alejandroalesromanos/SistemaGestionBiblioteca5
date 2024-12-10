package modelo;

import java.util.Date;

public class Prestamo {

	private int id;
	private int idLibro;
	private int idUsuario;
	private Date fechaPrestamo;
	private Date fechaDevolucion;
	private float multa;
	
	
	public Prestamo(int id, int idLibro, int idUsuario, Date fechaPrestamo, Date fechaDevolucion, float multa) {
		super();
		this.id = id;
		this.idLibro = idLibro;
		this.idUsuario = idUsuario;
		this.fechaPrestamo = fechaPrestamo;
		this.fechaDevolucion = fechaDevolucion;
		this.multa = multa;
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


	public Date getFechaPrestamo() {
		return fechaPrestamo;
	}


	public void setFechaPrestamo(Date fechaPrestamo) {
		this.fechaPrestamo = fechaPrestamo;
	}


	public Date getFechaDevolucion() {
		return fechaDevolucion;
	}


	public void setFechaDevolucion(Date fechaDevolucion) {
		this.fechaDevolucion = fechaDevolucion;
	}


	public float getMulta() {
		return multa;
	}


	public void setMulta(float multa) {
		this.multa = multa;
	}
	
	
	
}
