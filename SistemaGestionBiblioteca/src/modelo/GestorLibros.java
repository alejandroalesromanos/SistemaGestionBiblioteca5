package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorLibros {

    private Db db;

    public GestorLibros(Db db) {
        this.db = db;
    }

    // Método para insertar un libro
    public boolean insertarLibro(Libro libro) {
        String sql = "INSERT INTO Libros (Titulo, Autor, Genero, Disponibilidad, Fecha_Publicacion) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getGenero().name());
            stmt.setBoolean(4, libro.isDisponibilidad());
            stmt.setDate(5, new java.sql.Date(libro.getFechaDePublicacion().getTime()));
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener todos los libros
    public List<Libro> obtenerTodosLosLibros() {
        List<Libro> libros = new ArrayList<>();
        String sql = "SELECT * FROM Libros";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Libro libro = new Libro(
                        rs.getInt("ID"),
                        rs.getString("Titulo"),
                        rs.getString("Autor"),
                        Libro.Generos.valueOf(rs.getString("Genero")),
                        rs.getBoolean("Disponibilidad"),
                        rs.getDate("Fecha_Publicacion")
                );
                libros.add(libro);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return libros;
    }

    // Método para actualizar un libro
    public boolean actualizarLibro(Libro libro) {
        String sql = "UPDATE Libros SET Titulo = ?, Autor = ?, Genero = ?, Disponibilidad = ?, Fecha_Publicacion = ? WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, libro.getTitulo());
            stmt.setString(2, libro.getAutor());
            stmt.setString(3, libro.getGenero().name());
            stmt.setBoolean(4, libro.isDisponibilidad());
            stmt.setDate(5, new java.sql.Date(libro.getFechaDePublicacion().getTime()));
            stmt.setInt(6, libro.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un libro
    public boolean eliminarLibro(int id) {
        String sql = "DELETE FROM Libros WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar un libro por ID
    public Libro buscarLibroPorId(int id) {
        String sql = "SELECT * FROM Libros WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Libro(
                            rs.getInt("ID"),
                            rs.getString("Titulo"),
                            rs.getString("Autor"),
                            Libro.Generos.valueOf(rs.getString("Genero")),
                            rs.getBoolean("Disponibilidad"),
                            rs.getDate("Fecha_Publicacion")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}