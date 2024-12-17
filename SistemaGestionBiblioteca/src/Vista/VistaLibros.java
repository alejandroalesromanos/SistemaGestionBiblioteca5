package Vista;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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

public class VistaLibros extends JFrame {
	private static final long serialVersionUID = 1L;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public VistaLibros(boolean isAdmin, String currentUser) {
        setTitle("Vista de Libros");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        FondoPantalla fondo = new FondoPantalla();
        fondo.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(fondo);
        setLocationRelativeTo(null);

        GridBagLayout gridBagLayout = new GridBagLayout();
        getContentPane().setLayout(gridBagLayout);

        JLabel label = new JLabel("Lista de Libros");
        label.setFont(new Font("Arial Black", Font.BOLD, 18));
        label.setForeground(Color.WHITE);
        GridBagConstraints gbc_label = new GridBagConstraints();
        gbc_label.insets = new Insets(10, 10, 10, 10);
        gbc_label.gridx = 0;
        gbc_label.gridy = 0;
        gbc_label.gridwidth = 2;
        getContentPane().add(label, gbc_label);

        // Tabla de libros
        tableModel = new DefaultTableModel(new Object[][] {}, new String[] { "ID", "Título", "Autor", "Género" });
        bookTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(bookTable);
        GridBagConstraints gbc_table = new GridBagConstraints();
        gbc_table.insets = new Insets(10, 10, 10, 10);
        gbc_table.gridx = 0;
        gbc_table.gridy = 1;
        gbc_table.gridwidth = 2;
        gbc_table.fill = GridBagConstraints.BOTH;
        gbc_table.weightx = 1.0;
        gbc_table.weighty = 1.0;
        getContentPane().add(scrollPane, gbc_table);

        // Botón para añadir libros (solo para administradores)
        if (isAdmin) {
            JButton addBookButton = new JButton("Añadir Libro");
            addBookButton.addActionListener(e -> addBook());
            GridBagConstraints gbc_addBookButton = new GridBagConstraints();
            gbc_addBookButton.insets = new Insets(10, 10, 10, 10);
            gbc_addBookButton.gridx = 0;
            gbc_addBookButton.gridy = 2;
            gbc_addBookButton.fill = GridBagConstraints.HORIZONTAL;
            getContentPane().add(addBookButton, gbc_addBookButton);

            // Botón para eliminar libros
            JButton deleteBookButton = new JButton("Eliminar Libro");
            deleteBookButton.addActionListener(e -> deleteBook());
            GridBagConstraints gbc_deleteBookButton = new GridBagConstraints();
            gbc_deleteBookButton.insets = new Insets(10, 10, 10, 10);
            gbc_deleteBookButton.gridx = 1;
            gbc_deleteBookButton.gridy = 2;
            gbc_deleteBookButton.fill = GridBagConstraints.HORIZONTAL;
            getContentPane().add(deleteBookButton, gbc_deleteBookButton);
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

        loadBooks();
    }

    // Cargar libros en la tabla
    private void loadBooks() {
        try (Connection connection = new Db().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ID, Titulo, Autor, Genero FROM libros")) {

            tableModel.setRowCount(0); // Limpiar tabla

            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String titulo = resultSet.getString("Titulo");
                String autor = resultSet.getString("Autor");
                String genero = resultSet.getString("Genero");
                tableModel.addRow(new Object[]{id, titulo, autor, genero});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + e.getMessage());
        }
    }

    // Añadir libro
    private void addBook() {
        String titulo = JOptionPane.showInputDialog(this, "Título del libro:");
        String autor = JOptionPane.showInputDialog(this, "Autor del libro:");
        String genero = JOptionPane.showInputDialog(this, "Género del libro:");

        if (titulo == null || autor == null || genero == null) {
            return;
        }

        try (Connection connection = new Db().getConnection();
             PreparedStatement ps = connection.prepareStatement("INSERT INTO libros (Titulo, Autor, Genero) VALUES (?, ?, ?)")) {
            ps.setString(1, titulo);
            ps.setString(2, autor);
            ps.setString(3, genero);
            ps.executeUpdate();
            loadBooks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al añadir libro: " + e.getMessage());
        }
    }

    // Eliminar libro
    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar.");
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = new Db().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM libros WHERE ID = ?")) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
            loadBooks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar libro: " + e.getMessage());
        }
    }
}