package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorPrestamos {

    private Db db;

    public GestorPrestamos(Db db) {
        this.db = db;
    }

    // Método para insertar un préstamo
    public boolean insertarPrestamo(Prestamo prestamo) {
        String sql = "INSERT INTO Prestamos (ID_Libro, ID_Usuario, Fecha_Prestamo, Fecha_Devolucion, Multa) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prestamo.getIdLibro());
            stmt.setInt(2, prestamo.getIdUsuario());
            stmt.setDate(3, new java.sql.Date(prestamo.getFechaPrestamo().getTime()));
            stmt.setDate(4, prestamo.getFechaDevolucion() != null ? new java.sql.Date(prestamo.getFechaDevolucion().getTime()) : null);
            stmt.setFloat(5, prestamo.getMulta());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener todos los préstamos
    public List<Prestamo> obtenerTodosLosPrestamos() {
        List<Prestamo> prestamos = new ArrayList<>();
        String sql = "SELECT * FROM Prestamos";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Prestamo prestamo = new Prestamo(
                        rs.getInt("ID"),
                        rs.getInt("ID_Libro"),
                        rs.getInt("ID_Usuario"),
                        rs.getDate("Fecha_Prestamo"),
                        rs.getDate("Fecha_Devolucion"),
                        rs.getFloat("Multa")
                );
                prestamos.add(prestamo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return prestamos;
    }

    // Método para actualizar un préstamo
    public boolean actualizarPrestamo(Prestamo prestamo) {
        String sql = "UPDATE Prestamos SET ID_Libro = ?, ID_Usuario = ?, Fecha_Prestamo = ?, Fecha_Devolucion = ?, Multa = ? WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, prestamo.getIdLibro());
            stmt.setInt(2, prestamo.getIdUsuario());
            stmt.setDate(3, new java.sql.Date(prestamo.getFechaPrestamo().getTime()));
            stmt.setDate(4, prestamo.getFechaDevolucion() != null ? new java.sql.Date(prestamo.getFechaDevolucion().getTime()) : null);
            stmt.setFloat(5, prestamo.getMulta());
            stmt.setInt(6, prestamo.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un préstamo
    public boolean eliminarPrestamo(int id) {
        String sql = "DELETE FROM Prestamos WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar un préstamo por ID
    public Prestamo buscarPrestamoPorId(int id) {
        String sql = "SELECT * FROM Prestamos WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Prestamo(
                            rs.getInt("ID"),
                            rs.getInt("ID_Libro"),
                            rs.getInt("ID_Usuario"),
                            rs.getDate("Fecha_Prestamo"),
                            rs.getDate("Fecha_Devolucion"),
                            rs.getFloat("Multa")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}