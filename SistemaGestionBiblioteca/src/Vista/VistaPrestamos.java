package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import modelo.Db;
import modelo.GestorPrestamos;

public class VistaPrestamos extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTable prestamosTable;
	private DefaultTableModel tableModel;

	public VistaPrestamos(boolean isAdmin, String currentUser) {
		setTitle("Vista de Préstamos");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(800, 600);
		FondoPantalla fondo = new FondoPantalla();
		fondo.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(fondo);
		setLocationRelativeTo(null);

		GridBagLayout gridBagLayout = new GridBagLayout();
		getContentPane().setLayout(gridBagLayout);

		JLabel label = new JLabel("Lista de Préstamos");
		label.setFont(new Font("Arial Black", Font.BOLD, 18));
		label.setForeground(Color.WHITE);
		GridBagConstraints gbc_label = new GridBagConstraints();
		gbc_label.insets = new Insets(10, 10, 10, 10);
		gbc_label.gridx = 0;
		gbc_label.gridy = 0;
		gbc_label.gridwidth = 2;
		getContentPane().add(label, gbc_label);

		// Tabla de préstamos
		tableModel = new DefaultTableModel(new Object[][] {},
				new String[] { "ID", "Libro", "Usuario", "Fecha Préstamo", "Fecha Devolución", "Multa" });
		prestamosTable = new JTable(tableModel);
		JScrollPane scrollPane = new JScrollPane(prestamosTable);
		GridBagConstraints gbc_table = new GridBagConstraints();
		gbc_table.insets = new Insets(10, 10, 10, 10);
		gbc_table.gridx = 0;
		gbc_table.gridy = 1;
		gbc_table.gridwidth = 2;
		gbc_table.fill = GridBagConstraints.BOTH;
		gbc_table.weightx = 1.0;
		gbc_table.weighty = 1.0;
		getContentPane().add(scrollPane, gbc_table);

		// Filtros para los préstamos
		JButton filterAllButton = new JButton("Mostrar Todos los Préstamos");
		filterAllButton.addActionListener(e -> loadPrestamos("ALL"));
		GridBagConstraints gbc_filterAllButton = new GridBagConstraints();
		gbc_filterAllButton.insets = new Insets(10, 10, 10, 10);
		gbc_filterAllButton.gridx = 0;
		gbc_filterAllButton.gridy = 2;
		gbc_filterAllButton.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(filterAllButton, gbc_filterAllButton);

		JButton filterReturnedButton = new JButton("Mostrar Préstamos Devueltos");
		filterReturnedButton.addActionListener(e -> loadPrestamos("RETURNED"));
		GridBagConstraints gbc_filterReturnedButton = new GridBagConstraints();
		gbc_filterReturnedButton.insets = new Insets(10, 10, 10, 10);
		gbc_filterReturnedButton.gridx = 1;
		gbc_filterReturnedButton.gridy = 2;
		gbc_filterReturnedButton.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(filterReturnedButton, gbc_filterReturnedButton);

		JButton filterNotReturnedButton = new JButton("Mostrar Préstamos No Devueltos");
		filterNotReturnedButton.addActionListener(e -> loadPrestamos("NOT_RETURNED"));
		GridBagConstraints gbc_filterNotReturnedButton = new GridBagConstraints();
		gbc_filterNotReturnedButton.insets = new Insets(10, 10, 10, 10);
		gbc_filterNotReturnedButton.gridx = 0;
		gbc_filterNotReturnedButton.gridy = 3;
		gbc_filterNotReturnedButton.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(filterNotReturnedButton, gbc_filterNotReturnedButton);

		// Botón para añadir préstamos (solo para administradores)
		if (isAdmin) {
			JButton addPrestamoButton = new JButton("Añadir Préstamo");
			addPrestamoButton.addActionListener(e -> addPrestamo());
			GridBagConstraints gbc_addPrestamoButton = new GridBagConstraints();
			gbc_addPrestamoButton.insets = new Insets(10, 10, 10, 10);
			gbc_addPrestamoButton.gridx = 1;
			gbc_addPrestamoButton.gridy = 3;
			gbc_addPrestamoButton.fill = GridBagConstraints.HORIZONTAL;
			getContentPane().add(addPrestamoButton, gbc_addPrestamoButton);

			// Botón para marcar préstamos como devueltos
			JButton returnPrestamoButton = new JButton("Marcar como Devuelto");
			returnPrestamoButton.addActionListener(e -> returnPrestamo());
			GridBagConstraints gbc_returnPrestamoButton = new GridBagConstraints();
			gbc_returnPrestamoButton.insets = new Insets(10, 10, 10, 10);
			gbc_returnPrestamoButton.gridx = 1;
			gbc_returnPrestamoButton.gridy = 4;
			gbc_returnPrestamoButton.fill = GridBagConstraints.HORIZONTAL;
			getContentPane().add(returnPrestamoButton, gbc_returnPrestamoButton);
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
		gbc_backButton.gridy = 5;
		gbc_backButton.gridwidth = 2;
		gbc_backButton.fill = GridBagConstraints.HORIZONTAL;
		getContentPane().add(backButton, gbc_backButton);

		loadPrestamos("ALL");
	}

	private void loadPrestamos(String filter) {
		try (Connection connection = new Db().getConnection()) {
			String query = "SELECT p.ID, l.Titulo, u.Nombre, u.Apellidos, p.Fecha_Prestamo, p.Fecha_Devolucion, p.Multa_Generada "
					+ "FROM prestamos p " + "JOIN libros l ON p.ID_Libro = l.ID "
					+ "JOIN usuarios u ON p.ID_Usuario = u.ID ";

			if ("NOT_RETURNED".equals(filter)) {
				query += "WHERE p.Fecha_Devolucion IS NULL";
			} else if ("RETURNED".equals(filter)) {
				query += "WHERE p.Fecha_Devolucion IS NOT NULL";
			}

			try (PreparedStatement statement = connection.prepareStatement(query)) {
				ResultSet resultSet = statement.executeQuery();

				tableModel.setRowCount(0); // Limpiar tabla antes de cargar nuevos datos

				while (resultSet.next()) {
					int prestamoId = resultSet.getInt("ID");
					String libroTitulo = resultSet.getString("Titulo");
					String usuarioNombre = resultSet.getString("Nombre") + " " + resultSet.getString("Apellidos");
					Date fechaPrestamo = resultSet.getDate("Fecha_Prestamo");
					Date fechaDevolucion = resultSet.getDate("Fecha_Devolucion");
					float multa = resultSet.getFloat("Multa_Generada");

					tableModel.addRow(new Object[] { prestamoId, libroTitulo, usuarioNombre, fechaPrestamo,
							fechaDevolucion, multa });
				}

				// Ocultar la columna del ID
				prestamosTable.getColumnModel().getColumn(0).setMinWidth(0);
				prestamosTable.getColumnModel().getColumn(0).setMaxWidth(0);
				prestamosTable.getColumnModel().getColumn(0).setWidth(0);

			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al cargar los préstamos: " + e.getMessage());
		}
	}

	// Añadir préstamo
	private void addPrestamo() {
		String idLibro = JOptionPane.showInputDialog(this, "ID del libro:");
		String idUsuario = JOptionPane.showInputDialog(this, "ID del usuario:");
		String fechaPrestamo = JOptionPane.showInputDialog(this, "Fecha del préstamo (yyyy-mm-dd):");
		String fechaDevolucion = JOptionPane.showInputDialog(this, "Fecha de devolución (yyyy-mm-dd):");
		String multa = JOptionPane.showInputDialog(this, "Multa:");

		if (idLibro == null || idUsuario == null || fechaPrestamo == null || fechaDevolucion == null || multa == null) {
			return;
		}

		try (Connection connection = new Db().getConnection();
				PreparedStatement ps = connection.prepareStatement(
						"INSERT INTO Prestamos (ID_Libro, ID_Usuario, Fecha_Prestamo, Fecha_Devolucion, Multa_Generada) VALUES (?, ?, ?, ?, ?)")) {
			ps.setInt(1, Integer.parseInt(idLibro));
			ps.setInt(2, Integer.parseInt(idUsuario));
			ps.setDate(3, Date.valueOf(fechaPrestamo));
			ps.setDate(4, Date.valueOf(fechaDevolucion));
			ps.setFloat(5, Float.parseFloat(multa));
			ps.executeUpdate();
			loadPrestamos("ALL"); // Recargar préstamos después de agregar uno nuevo
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al añadir préstamo: " + e.getMessage());
		}
	}

	private void returnPrestamo() {
		// Obtener la fila seleccionada
		int selectedRow = prestamosTable.getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione un préstamo para marcar como devuelto.");
			return;
		}

		// Obtener el ID del préstamo desde la tabla, ajustando correctamente
		int prestamoId = (int) tableModel.getValueAt(selectedRow, 0); // Ahora se obtiene correctamente el ID

		// Obtener la fecha de devolución
		String fechaDevolucion = JOptionPane.showInputDialog(this, "Ingrese la fecha de devolución (yyyy-mm-dd):");

		// Actualizar el préstamo
		try (Connection connection = new Db().getConnection();
				PreparedStatement ps = connection
						.prepareStatement("UPDATE prestamos SET Fecha_Devolucion = ? WHERE ID = ?")) {
			ps.setDate(1, Date.valueOf(fechaDevolucion));
			ps.setInt(2, prestamoId);
			ps.executeUpdate();
			loadPrestamos("ALL"); // Recargar préstamos después de marcar como devuelto
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "Error al marcar préstamo como devuelto: " + e.getMessage());
		}
	}
}
