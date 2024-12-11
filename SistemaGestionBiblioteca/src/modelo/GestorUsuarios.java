package modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GestorUsuarios {

    private Db db; // Referencia a la clase Db para gestionar la conexión a la base de datos

    public GestorUsuarios(Db db) {
        this.db = db;
    }

    // Método para insertar un usuario en la base de datos
    public boolean insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO Usuarios (DNI, Nombre, Apellidos, Email, Telefono, Rol, Password) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = db.getConnection(); // Obtiene una conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara la consulta
            stmt.setString(1, usuario.getDni()); // Asigna el valor del DNI
            stmt.setString(2, usuario.getNombre()); // Asigna el nombre del usuario
            stmt.setString(3, usuario.getApellidos()); // Asigna los apellidos
            stmt.setString(4, usuario.getEmail()); // Asigna el email
            stmt.setString(5, usuario.getTelefono()); // Asigna el teléfono
            stmt.setString(6, usuario.getRol().name()); // Convierte el rol a texto y lo asigna
            stmt.setString(7, usuario.getPassword()); // Asigna la contraseña
            return stmt.executeUpdate() > 0; // Ejecuta la consulta y devuelve true si se inserta correctamente
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en la consola
            return false;
        }
    }

    // Método para obtener todos los usuarios de la base de datos
    public List<Usuario> obtenerTodosLosUsuarios() {
        List<Usuario> usuarios = new ArrayList<>(); // Lista para almacenar los usuarios
        String sql = "SELECT * FROM Usuarios";
        try (Connection conn = db.getConnection(); // Obtiene la conexión
             Statement stmt = conn.createStatement(); // Crea un objeto Statement
             ResultSet rs = stmt.executeQuery(sql)) { // Ejecuta la consulta
            while (rs.next()) { // Itera por los resultados
                Usuario usuario = new Usuario(
                        rs.getInt("ID"), // Obtiene el ID del usuario
                        rs.getString("DNI"), // Obtiene el DNI
                        rs.getString("Nombre"), // Obtiene el nombre
                        rs.getString("Apellidos"), // Obtiene los apellidos
                        rs.getString("Email"), // Obtiene el email
                        rs.getString("Telefono"), // Obtiene el teléfono
                        Usuario.Rol.valueOf(rs.getString("Rol")), // Convierte el rol desde texto
                        rs.getString("Password") // Obtiene la contraseña
                );
                usuarios.add(usuario); // Agrega el usuario a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en la consola
        }
        return usuarios; // Devuelve la lista de usuarios
    }

    // Método para actualizar un usuario existente en la base de datos
    public boolean actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE Usuarios SET DNI = ?, Nombre = ?, Apellidos = ?, Email = ?, Telefono = ?, Rol = ?, Password = ? WHERE ID = ?";
        try (Connection conn = db.getConnection(); // Obtiene la conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara la consulta
            stmt.setString(1, usuario.getDni()); // Asigna el valor del DNI
            stmt.setString(2, usuario.getNombre()); // Asigna el nombre
            stmt.setString(3, usuario.getApellidos()); // Asigna los apellidos
            stmt.setString(4, usuario.getEmail()); // Asigna el email
            stmt.setString(5, usuario.getTelefono()); // Asigna el teléfono
            stmt.setString(6, usuario.getRol().name()); // Convierte y asigna el rol
            stmt.setString(7, usuario.getPassword()); // Asigna la contraseña
            stmt.setInt(8, usuario.getId()); // Asigna el ID del usuario
            return stmt.executeUpdate() > 0; // Ejecuta la consulta y devuelve true si se actualiza correctamente
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en la consola
            return false;
        }
    }

    // Método para eliminar un usuario por su ID
    public boolean eliminarUsuario(int id) {
        String sql = "DELETE FROM Usuarios WHERE ID = ?";
        try (Connection conn = db.getConnection(); // Obtiene la conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara la consulta
            stmt.setInt(1, id); // Asigna el ID del usuario a eliminar
            return stmt.executeUpdate() > 0; // Ejecuta la consulta y devuelve true si se elimina correctamente
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en la consola
            return false;
        }
    }

    // Método para buscar un usuario por su ID
    public Usuario buscarUsuarioPorId(int id) {
        String sql = "SELECT * FROM Usuarios WHERE ID = ?";
        try (Connection conn = db.getConnection(); // Obtiene la conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara la consulta
            stmt.setInt(1, id); // Asigna el ID del usuario a buscar
            try (ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta
                if (rs.next()) { // Verifica si hay un resultado
                    return new Usuario(
                            rs.getInt("ID"), // Obtiene el ID del usuario
                            rs.getString("DNI"), // Obtiene el DNI
                            rs.getString("Nombre"), // Obtiene el nombre
                            rs.getString("Apellidos"), // Obtiene los apellidos
                            rs.getString("Email"), // Obtiene el email
                            rs.getString("Telefono"), // Obtiene el teléfono
                            Usuario.Rol.valueOf(rs.getString("Rol")), // Convierte el rol desde texto
                            rs.getString("Password") // Obtiene la contraseña
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en la consola
        }
        return null; // Devuelve null si no se encuentra el usuario
    }

    // Método para autenticar un usuario por nombre y contraseña
    public boolean autenticarUsuario(String nombre, String password) {
        String sql = "SELECT COUNT(*) FROM Usuarios WHERE Nombre = ? AND Password = ?";
        try (Connection conn = db.getConnection(); // Obtiene la conexión
             PreparedStatement stmt = conn.prepareStatement(sql)) { // Prepara la consulta
            stmt.setString(1, nombre); // Asigna el nombre del usuario
            stmt.setString(2, password); // Asigna la contraseña del usuario
            try (ResultSet rs = stmt.executeQuery()) { // Ejecuta la consulta
                if (rs.next()) { // Verifica si hay un resultado
                    return rs.getInt(1) > 0; // Devuelve true si hay coincidencias
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Imprime el error en la consola
        }
        return false; // Devuelve false si no se autentica el usuario
    }
}
