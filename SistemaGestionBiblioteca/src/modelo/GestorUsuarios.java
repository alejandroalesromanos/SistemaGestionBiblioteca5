package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {

    private Db db;

    public GestorUsuarios(Db db) {
        this.db = db;
    }

    // Método para insertar un usuario
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (DNI, Nombre, Apellidos, Email, Telefono, Rol) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getDni());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getApellidos());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefono());
            stmt.setString(6, usuario.getRol().name());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para obtener todos los usuarios
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM Usuarios";
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("ID"),
                        rs.getString("DNI"),
                        rs.getString("Nombre"),
                        rs.getString("Apellidos"),
                        rs.getString("Email"),
                        rs.getString("Telefono"),
                        Usuario.Rol.valueOf(rs.getString("Rol"))
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }

    // Método para actualizar un usuario
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE Usuarios SET DNI = ?, Nombre = ?, Apellidos = ?, Email = ?, Telefono = ?, Rol = ? WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getDni());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getApellidos());
            stmt.setString(4, usuario.getEmail());
            stmt.setString(5, usuario.getTelefono());
            stmt.setString(6, usuario.getRol().name());
            stmt.setInt(7, usuario.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para eliminar un usuario
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM Usuarios WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Método para buscar un usuario por ID
    public Usuario buscarUsuarioPorId(int id) {
        String sql = "SELECT * FROM Usuarios WHERE ID = ?";
        try (Connection conn = db.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getInt("ID"),
                            rs.getString("DNI"),
                            rs.getString("Nombre"),
                            rs.getString("Apellidos"),
                            rs.getString("Email"),
                            rs.getString("Telefono"),
                            Usuario.Rol.valueOf(rs.getString("Rol"))
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}