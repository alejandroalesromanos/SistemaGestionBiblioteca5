package Vista;

import java.awt.EventQueue;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import modelo.Db;

import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class MenuInicioSesion extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTextField emailField;
	private JPasswordField passField;


	public MenuInicioSesion() {
		setTitle("Inicio de Sesión");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(400, 300);
		setLocationRelativeTo(null);

		JLabel emailLabel = new JLabel("Correo Electrónico:");
		emailField = new JTextField();
		emailField.setColumns(10);

		JLabel passLabel = new JLabel("Contraseña:");
		passField = new JPasswordField();
		passField.setColumns(10);

		JButton loginButton = new JButton("Iniciar Sesión");
		loginButton
				.addActionListener(e -> validarCredenciales(emailField.getText(), new String(passField.getPassword())));

		JPanel panel = new JPanel();
		GroupLayout layout = new GroupLayout(panel);
		panel.setLayout(layout);
		layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);

		layout.setHorizontalGroup(layout.createSequentialGroup()
				.addGroup(
						layout.createParallelGroup(Alignment.LEADING).addComponent(emailLabel).addComponent(passLabel))
				.addGroup(layout.createParallelGroup(Alignment.LEADING).addComponent(emailField).addComponent(passField)
						.addComponent(loginButton)));

		layout.setVerticalGroup(layout.createSequentialGroup()
				.addGroup(layout.createParallelGroup(Alignment.BASELINE).addComponent(emailLabel)
						.addComponent(emailField))
				.addGroup(
						layout.createParallelGroup(Alignment.BASELINE).addComponent(passLabel).addComponent(passField))
				.addComponent(loginButton));

		add(panel);
	}

	private void validarCredenciales(String email, String password) {
		try (Connection connection = new Db().getConnection()) {
			String query = "SELECT Rol FROM usuarios WHERE Email = ? AND Password = ?";
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, email);
			ps.setString(2, password);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
				String role = rs.getString("Rol");
				dispose();
				new MenuPrincipal("Administrador".equals(role)).setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "Credenciales incorrectas.");
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
		}
	}
}
