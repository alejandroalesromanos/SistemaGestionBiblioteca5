package Vista;

import modelo.Db;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class VistaUsuarios extends JFrame {

	private static final long serialVersionUID = 1L;
	private JTable userTable;
	private DefaultTableModel tableModel;

	public VistaUsuarios(boolean isAdmin, String currentUser) {
		setTitle("Gestión de Usuarios");
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(800, 600);
		FondoPantalla fondo = new FondoPantalla();
		fondo.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(fondo);
		setLocationRelativeTo(null);

		GridBagLayout gridBagLayout = new GridBagLayout();
		getContentPane().setLayout(gridBagLayout);

		JLabel label = new JLabel("Lista de Usuarios");
		label.setFont(new Font("Arial Black", Font.BOLD, 18));
		label.setForeground(Color.WHITE);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(10, 10, 10, 10);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		gbc_label.gridwidth = 2;
		getContentPane().add(label, gbc_label);

		// Tabla de usuarios
		tableModel = new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "DNI", "Nombre", "Apellidos", "Email", "Teléfono", "Rol" });
		userTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(userTable);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(10, 10, 10, 10);
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		gbc_table.gridwidth = 2;
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.weightx = 1.0;
		gbc_table.weighty = 1.0;
		getContentPane().add(scrollPane, gbc_table);

		// Botón para añadir usuarios (solo para administradores)
		if (isAdmin) {
			JButton addUserButton = new JButton("Añadir Usuario");
			addUserButton.addActionListener(e -> addUser());
			GridBagConstraints gbc_addUserButton = new GridBagConstraints();
			gbc_addUserButton.insets = new Insets(10, 10, 10, 10);
			gbc_addUserButton.gridx = 0;
			gbc_addUserButton.gridy = 2;
			gbc_addUserButton.fill = GridBagConstraints.HORIZONTAL;
			getContentPane().add(addUserButton, gbc_addUserButton);

			// Botón para eliminar usuarios
			JButton deleteUserButton = new JButton("Eliminar Usuario");
			deleteUserButton.addActionListener(e -> deleteUser());
			GridBagConstraints gbc_deleteUserButton = new GridBagConstraints();
			gbc_deleteUserButton.insets = new Insets(10, 10, 10, 10);
			gbc_deleteUserButton.gridx = 1;
			gbc_deleteUserButton.gridy = 2;
			gbc_deleteUserButton.fill = GridBagConstraints.HORIZONTAL;
			getContentPane().add(deleteUserButton, gbc_deleteUserButton);
		}

		// Botón para volver al menú principal
		JButton backButton = new JButton("Volver al Menú Principal");
		backButton.addActionListener(e -> {
			dispose();
			new MenuPrincipal(isAdmin, currentUser).setVisible(true);
		});
		GridBagConstraints gbc_backButton = new GridBagConstraints();
		gbc_backButton.insets = new Insets(10, 10, 10, 10);
		gbc_backButton.gridx = 0;
		gbc_backButton.gridy = 3;
		gbc_backButton.gridwidth = 2;
		gbc_backButton.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(backButton, gbc_backButton);

		loadUsers();
	}

	// Cargar usuarios en la tabla
	private void loadUsers() {
		try (Connection connection = new Db().getConnection();
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement
						.executeQuery("SELECT ID, DNI, Nombre, Apellidos, Email, Telefono, Rol FROM usuarios")) {

			tableModel.setRowCount(0); // Limpiar tabla

			while (resultSet.next()) {
				int id = resultSet.getInt("ID");
				String dni = resultSet.getString("DNI");
				String nombre = resultSet.getString("Nombre");
				String apellidos = resultSet.getString("Apellidos");
				String email = resultSet.getString("Email");
				String telefono = resultSet.getString("Telefono");
				String rol = resultSet.getString("Rol");
				tableModel.addRow(new Object[] { id, dni, nombre, apellidos, email, telefono, rol });
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage());
		}
	}

	// Añadir usuario
	private void addUser() {
		String dni = JOptionPane.showInputDialog(this, "DNI del usuario:");
		String nombre = JOptionPane.showInputDialog(this, "Nombre del usuario:");
		String apellidos = JOptionPane.showInputDialog(this, "Apellidos del usuario:");
		String email = JOptionPane.showInputDialog(this, "Email del usuario:");
		String telefono = JOptionPane.showInputDialog(this, "Teléfono del usuario:");
		String[] roles = { "Administrador", "Usuario estándar" };
		JComboBox<String> roleComboBox = new JComboBox<>(roles);
		int result = JOptionPane.showConfirmDialog(this, roleComboBox, "Seleccione el rol del usuario",
				JOptionPane.OK_CANCEL_OPTION);
		if (result != JOptionPane.OK_OPTION) {
			return;
		}
		String rol = (String) roleComboBox.getSelectedItem();
		String password = JOptionPane.showInputDialog(this, "Contraseña del usuario:");

		if (dni == null || nombre == null || apellidos == null || email == null || telefono == null || rol == null
				|| password == null) {
			return;
		}

		try (Connection connection = new Db().getConnection();
				PreparedStatement ps = connection.prepareStatement(
						"INSERT INTO usuarios (DNI, Nombre, Apellidos, Email, Telefono, Rol, Password) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
			ps.setString(1, dni);
			ps.setString(2, nombre);
			ps.setString(3, apellidos);
			ps.setString(4, email);
			ps.setString(5, telefono);
			ps.setString(6, rol);
			ps.setString(7, password);
			ps.executeUpdate();
			loadUsers();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al añadir usuario: " + e.getMessage());
		}
	}

	// Eliminar usuario
	private void deleteUser() {
		int selectedRow = userTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.");
			return;
		}

		int userId = (int) tableModel.getValueAt(selectedRow, 0);

		try (Connection connection = new Db().getConnection();
				PreparedStatement ps = connection.prepareStatement("DELETE FROM usuarios WHERE ID = ?")) {
			ps.setInt(1, userId);
			ps.executeUpdate();
			loadUsers();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + e.getMessage());
		}
	}
}