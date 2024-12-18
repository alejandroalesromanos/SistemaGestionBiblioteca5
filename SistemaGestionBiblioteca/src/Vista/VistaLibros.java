package Vista;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
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
        setLocationRelativeTo(null);

        // Fondo personalizado
        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(0, 0, new Color(41, 128, 185), 0, height, new Color(109, 213, 250));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
                g2d.dispose();
            }
        };
        fondoPanel.setLayout(new BorderLayout());
        fondoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(fondoPanel);

        // Título de la ventana
        JLabel titleLabel = new JLabel("Lista de Libros", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        fondoPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabla de libros
        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Título", "Autor", "Género"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setRowHeight(25);
        bookTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        bookTable.getTableHeader().setBackground(new Color(41, 128, 185));
        bookTable.getTableHeader().setForeground(Color.WHITE);
        bookTable.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(bookTable);
        fondoPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        if (isAdmin) {
            JButton addBookButton = new JButton("Añadir Libro");
            addBookButton.addActionListener(e -> addBook());
            buttonPanel.add(addBookButton);

            JButton deleteBookButton = new JButton("Eliminar Libro");
            deleteBookButton.addActionListener(e -> deleteBook());
            buttonPanel.add(deleteBookButton);
        }

        JButton backButton = new JButton("Volver al Menú Principal");
        backButton.addActionListener(e -> {
            dispose();
            new MenuPrincipal(isAdmin, currentUser).setVisible(true);
        });
        buttonPanel.add(backButton);

        fondoPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Cargar libros al inicio
        loadBooks();
    }

    private void loadBooks() {
        try (Connection connection = new Db().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ID, Titulo, Autor, Genero FROM libros")) {

            tableModel.setRowCount(0); // Limpiar la tabla
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String titulo = resultSet.getString("Titulo");
                String autor = resultSet.getString("Autor");
                String genero = resultSet.getString("Genero");
                tableModel.addRow(new Object[]{id, titulo, autor, genero});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar libros: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addBook() {
        // Crear panel para los campos de entrada
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField titleField = new JTextField();
        JTextField authorField = new JTextField();
        JTextField genreField = new JTextField();

        panel.add(new JLabel("Título:"));
        panel.add(titleField);
        panel.add(new JLabel("Autor:"));
        panel.add(authorField);
        panel.add(new JLabel("Género:"));
        panel.add(genreField);

        // Mostrar el formulario en un JOptionPane
        int option = JOptionPane.showConfirmDialog(this, panel, "Agregar Nuevo Libro", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String titulo = titleField.getText().trim();
            String autor = authorField.getText().trim();
            String genero = genreField.getText().trim();

            if (titulo.isEmpty() || autor.isEmpty() || genero.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = new Db().getConnection();
                 PreparedStatement ps = connection.prepareStatement("INSERT INTO libros (Titulo, Autor, Genero) VALUES (?, ?, ?)")) {
                ps.setString(1, titulo);
                ps.setString(2, autor);
                ps.setString(3, genero);
                ps.executeUpdate();
                loadBooks();
                JOptionPane.showMessageDialog(this, "Libro añadido con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al añadir libro: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un libro para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int bookId = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = new Db().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM libros WHERE ID = ?")) {
            ps.setInt(1, bookId);
            ps.executeUpdate();
            loadBooks();
            JOptionPane.showMessageDialog(this, "Libro eliminado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar libro: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
