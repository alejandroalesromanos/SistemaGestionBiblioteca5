package Vista;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import modelo.Db;

public class MenuInicioSesion extends JFrame {

	 private static final long serialVersionUID = 1L;
	    private JTextField emailField;
	    private JPasswordField passwordField;

	    public MenuInicioSesion() {
	        setTitle("Inicio de Sesión");
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setSize(400, 300);
	        FondoPantalla fondo = new FondoPantalla();
	        fondo.setBorder(new EmptyBorder(5, 5, 5, 5));
	        setContentPane(fondo);
	        setLocationRelativeTo(null);

	        GridBagLayout gridBagLayout = new GridBagLayout();
	        gridBagLayout.rowWeights = new double[]{1.0, 1.0, 1.0, 1.0};
	        gridBagLayout.columnWeights = new double[]{1.0, 1.0, 1.0, 1.0, 1.0};
	        getContentPane().setLayout(gridBagLayout);

	        // Etiqueta y campo de correo electrónico
	        JLabel emailLabel = new JLabel("Correo Electrónico:");
	        emailLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
	        emailLabel.setForeground(Color.WHITE);
	        GridBagConstraints gbc_emailLabel = new GridBagConstraints();
	        gbc_emailLabel.gridwidth = 2;
	        gbc_emailLabel.insets = new Insets(0, 0, 5, 5);
	        gbc_emailLabel.gridx = 0;
	        gbc_emailLabel.gridy = 1;
	        getContentPane().add(emailLabel, gbc_emailLabel);

	        emailField = new JTextField();
	        emailField.setColumns(10);
	        GridBagConstraints gbc_emailField = new GridBagConstraints();
	        gbc_emailField.gridwidth = 3;
	        gbc_emailField.insets = new Insets(0, 0, 5, 5);
	        gbc_emailField.fill = GridBagConstraints.HORIZONTAL;
	        gbc_emailField.gridx = 2;
	        gbc_emailField.gridy = 1;
	        getContentPane().add(emailField, gbc_emailField);

	        // Etiqueta y campo de contraseña
	        JLabel passLabel = new JLabel("Contraseña:");
	        passLabel.setFont(new Font("Arial Black", Font.BOLD, 14));
	        passLabel.setForeground(Color.WHITE);
	        GridBagConstraints gbc_passLabel = new GridBagConstraints();
	        gbc_passLabel.gridwidth = 2;
	        gbc_passLabel.insets = new Insets(0, 0, 5, 5);
	        gbc_passLabel.gridx = 0;
	        gbc_passLabel.gridy = 2;
	        getContentPane().add(passLabel, gbc_passLabel);

	        passwordField = new JPasswordField();
	        passwordField.setColumns(10);
	        GridBagConstraints gbc_passwordField = new GridBagConstraints();
	        gbc_passwordField.gridwidth = 3;
	        gbc_passwordField.insets = new Insets(0, 0, 5, 5);
	        gbc_passwordField.fill = GridBagConstraints.HORIZONTAL;
	        gbc_passwordField.gridx = 2;
	        gbc_passwordField.gridy = 2;
	        getContentPane().add(passwordField, gbc_passwordField);

	        // Botón de inicio de sesión
	        JButton loginButton = new JButton("Iniciar Sesión");
	        loginButton.addActionListener(e -> {
	            String email = emailField.getText();
	            char[] password = passwordField.getPassword();

	            if (email.isEmpty() || password.length == 0) {
	                JOptionPane.showMessageDialog(this, "Por favor, ingrese ambos campos.");
	            } else if (!emailValido(email)) {
	                JOptionPane.showMessageDialog(this, "Correo electrónico no válido.");
	            } else {
	                validarCredenciales(email, new String(password));
	            }
	        });

	        GridBagConstraints gbc_loginButton = new GridBagConstraints();
	        gbc_loginButton.fill = GridBagConstraints.HORIZONTAL;
	        gbc_loginButton.gridwidth = 3;
	        gbc_loginButton.insets = new Insets(0, 0, 5, 5);
	        gbc_loginButton.gridx = 2;
	        gbc_loginButton.gridy = 3;
	        getContentPane().add(loginButton, gbc_loginButton);
	    }

	    // Validación del formato de correo electrónico
	    private boolean emailValido(String email) {
	        return email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$");
	    }

	    // Método para validar credenciales
	    private void validarCredenciales(String email, String password) {
	        try (Connection connection = new Db().getConnection()) {
	            String query = "SELECT Nombre, Apellidos, Rol FROM usuarios WHERE Email = ? AND password = ?";
	            PreparedStatement ps = connection.prepareStatement(query);
	            ps.setString(1, email);
	            ps.setString(2, password);
	            ResultSet rs = ps.executeQuery();

	            if (rs.next()) {
	                String role = rs.getString("Rol");
	                String nombre = rs.getString("Nombre");
	                String apellidos = rs.getString("Apellidos");
	                String nombreCompleto = nombre + " " + apellidos;

	                dispose();

	                // Enviar nombre completo y rol al siguiente menú (MenuPrincipal)
	                new MenuPrincipal(role.equals("Administrador"), nombreCompleto).setVisible(true);
	            } else {
	                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
	            }
	        } catch (Exception e) {
	            JOptionPane.showMessageDialog(this, "Error al conectarse a la base de datos: " + e.getMessage());
	        }
	    }
	}