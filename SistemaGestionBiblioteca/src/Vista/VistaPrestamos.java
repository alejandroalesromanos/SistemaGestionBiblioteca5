package Vista;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import modelo.Db;

public class VistaPrestamos extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable prestamosTable;
    private DefaultTableModel tableModel;

    public VistaPrestamos(boolean isAdmin, String currentUser) {
        setTitle("Vista de Préstamos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        JLabel titleLabel = new JLabel("Lista de Préstamos", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        fondoPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabla de préstamos
        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"Libro", "Usuario", "Fecha Préstamo", "Fecha Devolución", "Multa", "Estado", "ID"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        prestamosTable = new JTable(tableModel);
        prestamosTable.setRowHeight(25);
        prestamosTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        prestamosTable.getTableHeader().setBackground(new Color(41, 128, 185));
        prestamosTable.getTableHeader().setForeground(Color.WHITE);
        prestamosTable.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(prestamosTable);
        fondoPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new GridLayout(0, 1, 10, 10)); // Una columna y botones apilados verticalmente
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton allLoansButton = new JButton("Mostrar Todos los Préstamos");
        allLoansButton.addActionListener(e -> loadPrestamos("ALL"));
        buttonPanel.add(allLoansButton);

        JButton returnedLoansButton = new JButton("Mostrar Préstamos Devueltos");
        returnedLoansButton.addActionListener(e -> loadPrestamos("RETURNED"));
        buttonPanel.add(returnedLoansButton);

        JButton notReturnedLoansButton = new JButton("Mostrar Préstamos No Devueltos");
        notReturnedLoansButton.addActionListener(e -> loadPrestamos("NOT_RETURNED"));
        buttonPanel.add(notReturnedLoansButton);

        if (isAdmin) {
            JButton addLoanButton = new JButton("Añadir Préstamo");
            addLoanButton.addActionListener(e -> addPrestamo());
            buttonPanel.add(addLoanButton);

            JButton markAsReturnedButton = new JButton("Marcar como Devuelto");
            markAsReturnedButton.addActionListener(e -> updateReturnStatus(true));
            buttonPanel.add(markAsReturnedButton);

            JButton markAsNotReturnedButton = new JButton("Marcar como No Devuelto");
            markAsNotReturnedButton.addActionListener(e -> updateReturnStatus(false));
            buttonPanel.add(markAsNotReturnedButton);
        }

        JButton backButton = new JButton("Volver al Menú Principal");
        backButton.addActionListener(e -> {
            dispose();
            new MenuPrincipal(isAdmin, currentUser).setVisible(true);
        });
        buttonPanel.add(backButton);

        fondoPanel.add(buttonPanel, BorderLayout.EAST);

        // Cargar préstamos al inicio
        loadPrestamos("ALL");
    }

    private void loadPrestamos(String filter) {
        try (Connection connection = new Db().getConnection()) {
            String query = "SELECT l.Titulo, u.Nombre || ' ' || u.Apellidos AS Usuario, " +
                           "p.Fecha_Prestamo, p.Fecha_Devolucion, p.Multa_Generada, p.ID " +
                           "FROM prestamos p " +
                           "JOIN libros l ON p.ID_Libro = l.ID " +
                           "JOIN usuarios u ON p.ID_Usuario = u.ID";

            if ("NOT_RETURNED".equals(filter)) {
                query += " WHERE p.Fecha_Devolucion IS NULL";
            } else if ("RETURNED".equals(filter)) {
                query += " WHERE p.Fecha_Devolucion IS NOT NULL";
            }

            try (PreparedStatement statement = connection.prepareStatement(query);
                 ResultSet resultSet = statement.executeQuery()) {

                tableModel.setRowCount(0); // Limpiar la tabla
                while (resultSet.next()) {
                    String estado = resultSet.getDate("Fecha_Devolucion") != null ? "Devuelto" : "No Devuelto";
                    tableModel.addRow(new Object[]{
                            resultSet.getString("Titulo"),
                            resultSet.getString("Usuario"),
                            resultSet.getDate("Fecha_Prestamo"),
                            resultSet.getDate("Fecha_Devolucion"),
                            resultSet.getFloat("Multa_Generada"),
                            estado,
                            resultSet.getInt("ID")
                    });
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al cargar los préstamos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addPrestamo() {
        JTextField bookIdField = new JTextField(10);
        JTextField userIdField = new JTextField(10);
        JTextField loanDateField = new JTextField(10);

        Object[] form = {
            "ID del Libro:", bookIdField,
            "ID del Usuario:", userIdField,
            "Fecha de Préstamo (YYYY-MM-DD):", loanDateField
        };

        int option = JOptionPane.showConfirmDialog(this, form, "Añadir Nuevo Préstamo", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String bookId = bookIdField.getText().trim();
            String userId = userIdField.getText().trim();
            String loanDate = loanDateField.getText().trim();

            if (bookId.isEmpty() || userId.isEmpty() || loanDate.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = new Db().getConnection();
                 PreparedStatement ps = connection.prepareStatement("INSERT INTO prestamos (ID_Libro, ID_Usuario, Fecha_Prestamo) VALUES (?, ?, ?)")) {
                ps.setInt(1, Integer.parseInt(bookId));
                ps.setInt(2, Integer.parseInt(userId));
                ps.setDate(3, Date.valueOf(loanDate));
                ps.executeUpdate();
                loadPrestamos("ALL");
                JOptionPane.showMessageDialog(this, "Préstamo añadido con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al añadir préstamo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateReturnStatus(boolean markAsReturned) {
        int selectedRow = prestamosTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un préstamo para actualizar su estado.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPrestamo = (int) tableModel.getValueAt(selectedRow, 6); // Obtener el ID de la columna 6

        try (Connection connection = new Db().getConnection()) {
            String query = markAsReturned ? 
                "UPDATE prestamos SET Fecha_Devolucion = CURRENT_DATE WHERE ID = ?" :
                "UPDATE prestamos SET Fecha_Devolucion = NULL WHERE ID = ?";

            try (PreparedStatement ps = connection.prepareStatement(query)) {
                ps.setInt(1, idPrestamo);
                ps.executeUpdate();
                loadPrestamos("ALL");
                JOptionPane.showMessageDialog(this, "Estado del préstamo actualizado con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al actualizar el estado del préstamo: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
