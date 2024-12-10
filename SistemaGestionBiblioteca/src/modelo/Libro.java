package modelo;

import java.util.Date;

public class Libro {

	public enum Generos {
		FANTASIA,
		CIENCIA_FICCION,
		TERROR,
		MISTERIO,
		ROMANCE,
		AVENTURA,
		HISTORICO,
		BIOGRAFIA,
		POESIA,
		DRAMA,
    }
	
	private int id;
	private String titulo;
	private String autor;
	private Generos genero;
	private boolean disponibilidad;
	private Date fechaDePublicacion;
	
	
	public Libro(int id, String titulo, String autor, Generos genero, boolean disponibilidad, Date fechaDePublicacion) {
		super();
		this.id = id;
		this.titulo = titulo;
		this.autor = autor;
		this.genero = genero;
		this.disponibilidad = disponibilidad;
		this.fechaDePublicacion = fechaDePublicacion;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public String getAutor() {
		return autor;
	}


	public void setAutor(String autor) {
		this.autor = autor;
	}


	public Generos getGenero() {
		return genero;
	}


	public void setGenero(Generos genero) {
		this.genero = genero;
	}


	public boolean isDisponibilidad() {
		return disponibilidad;
	}


	public void setDisponibilidad(boolean disponibilidad) {
		this.disponibilidad = disponibilidad;
	}


	public Date getFechaDePublicacion() {
		return fechaDePublicacion;
	}


	public void setFechaDePublicacion(Date fechaDePublicacion) {
		this.fechaDePublicacion = fechaDePublicacion;
	}
	
	
}
