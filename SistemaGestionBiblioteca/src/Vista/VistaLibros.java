package Vista;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import modelo.Db;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.Year;

public class VistaLibros extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable bookTable;
    private DefaultTableModel tableModel;

    public VistaLibros(boolean isAdmin, String currentUser) {
        setTitle("Vista de Libros");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(800, 600);
        setMinimumSize(new Dimension(800, 600));
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
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Custom button style
        class StyledButton extends JButton {
            public StyledButton(String text) {
                super(text);
                setFont(new Font("Arial", Font.BOLD, 14));
                setForeground(Color.WHITE);
                setBackground(new Color(52, 152, 219));
                setBorderPainted(true);
                setFocusPainted(false);
                setContentAreaFilled(false);
                setOpaque(true);
                setPreferredSize(new Dimension(250, 40));
                setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2));
                addMouseListener(new java.awt.event.MouseAdapter() {
                    public void mouseEntered(java.awt.event.MouseEvent evt) {
                        setBackground(new Color(41, 128, 185));
                        setBorder(BorderFactory.createLineBorder(new Color(52, 152, 219), 2));
                    }
                    public void mouseExited(java.awt.event.MouseEvent evt) {
                        setBackground(new Color(52, 152, 219));
                        setBorder(BorderFactory.createLineBorder(new Color(41, 128, 185), 2));
                    }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed()) {
                    g.setColor(new Color(31, 97, 141));
                } else {
                    g.setColor(getBackground());
                }
                g.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, 10, 10);
                super.paintComponent(g);
            }
        }


        if (isAdmin) {
            JButton addBookButton = new StyledButton("Añadir Libro");
            addBookButton.addActionListener(e -> addBook());
            buttonPanel.add(addBookButton);

            JButton deleteBookButton = new StyledButton("Eliminar Libro");
            deleteBookButton.addActionListener(e -> deleteBook());
            buttonPanel.add(deleteBookButton);
        }

        JButton backButton = new StyledButton("Volver al Menú Principal");
        backButton.addActionListener(e -> {
            dispose();
            new MenuPrincipal(isAdmin, currentUser).setVisible(true);
        });
        buttonPanel.add(backButton);

        fondoPanel.add(buttonPanel, BorderLayout.EAST);

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
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField titleField = new JTextField(20);
        JTextField authorField = new JTextField(20);
        JTextField isbnField = new JTextField(20);
        JTextField publisherField = new JTextField(20);
        JComboBox<String> genreComboBox = new JComboBox<>(new String[]{"Ficción", "No ficción", "Misterio", "Ciencia Ficción", "Fantasía", "Romance", "Thriller", "Otro"});
        JSpinner yearSpinner = new JSpinner(new SpinnerNumberModel(Year.now().getValue(), 1000, Year.now().getValue(), 1));
        JSpinner copiesSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 1000, 1));

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Título:"), gbc);
        gbc.gridx = 1;
        panel.add(titleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Autor:"), gbc);
        gbc.gridx = 1;
        panel.add(authorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("ISBN:"), gbc);
        gbc.gridx = 1;
        panel.add(isbnField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Editorial:"), gbc);
        gbc.gridx = 1;
        panel.add(publisherField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(new JLabel("Género:"), gbc);
        gbc.gridx = 1;
        panel.add(genreComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(new JLabel("Año de publicación:"), gbc);
        gbc.gridx = 1;
        panel.add(yearSpinner, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(new JLabel("Número de copias:"), gbc);
        gbc.gridx = 1;
        panel.add(copiesSpinner, gbc);

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(this, "Agregar Nuevo Libro");
        dialog.setVisible(true);

        if (optionPane.getValue() != null && (Integer) optionPane.getValue() == JOptionPane.OK_OPTION) {
            String titulo = titleField.getText().trim();
            String autor = authorField.getText().trim();
            String isbn = isbnField.getText().trim();
            String editorial = publisherField.getText().trim();
            String genero = (String) genreComboBox.getSelectedItem();
            int anio = (Integer) yearSpinner.getValue();
            int copias = (Integer) copiesSpinner.getValue();

            if (titulo.isEmpty() || autor.isEmpty() || isbn.isEmpty() || editorial.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = new Db().getConnection();
                 PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO libros (Titulo, Autor, ISBN, Editorial, Genero, AnioPublicacion, NumCopias) VALUES (?, ?, ?, ?, ?, ?, ?)")) {
                ps.setString(1, titulo);
                ps.setString(2, autor);
                ps.setString(3, isbn);
                ps.setString(4, editorial);
                ps.setString(5, genero);
                ps.setInt(6, anio);
                ps.setInt(7, copias);
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

        int confirmOption = JOptionPane.showConfirmDialog(this, "¿Está seguro de que desea eliminar este libro?", "Confirmar Eliminación", JOptionPane.YES_NO_OPTION);
        if (confirmOption == JOptionPane.YES_OPTION) {
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

}

