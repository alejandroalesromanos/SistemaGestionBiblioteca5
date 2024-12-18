package modelo;

import javax.swing.*;
import java.sql.*;

public class GestorLogin {

    public boolean validateCredentials(String email, String password, Vista.VistaLogin vista) {
        try (Connection connection = new Db().getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT Nombre, Apellidos, Rol FROM usuarios WHERE Email = ? AND password = ?")) {
            
            ps.setString(1, email);
            ps.setString(2, password);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("Rol");
                    String nombreCompleto = rs.getString("Nombre") + " " + rs.getString("Apellidos");
                    
                    // Close the login window and open the main menu based on role
                    vista.dispose();
                    openMainMenu(role.equals("Administrador"), nombreCompleto);
                    return true;
                } else {
                    vista.showErrorMessage("Credenciales incorrectas.");
                    return false;
                }
            }
        } catch (Exception e) {
            vista.showErrorMessage("Error al conectarse a la base de datos: " + e.getMessage());
            return false;
        }
    }

    private void openMainMenu(boolean isAdmin, String nombreCompleto) {
        SwingUtilities.invokeLater(() -> new Vista.MenuPrincipal(isAdmin, nombreCompleto).setVisible(true));
    }
}
