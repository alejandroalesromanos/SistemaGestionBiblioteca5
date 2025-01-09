package Vista;

import java.awt.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import modelo.Db;

public class VistaNotificaciones extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTable notificationsTable;
    private DefaultTableModel tableModel;

    public VistaNotificaciones(boolean isAdmin, String currentUser, String emailUser) {
        setTitle("Vista de Notificaciones");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);// Forzar pantalla completa

        // Fondo personalizado
        JPanel fondoPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                int width = getWidth();
                int height = getHeight();
                GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 152, 219), 0, height, new Color(174, 214, 241));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
                g2d.dispose();
            }
        };
        fondoPanel.setLayout(new BorderLayout());
        fondoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(fondoPanel);

        // Título de la ventana
        JLabel titleLabel = new JLabel("Lista de Notificaciones", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        fondoPanel.add(titleLabel, BorderLayout.NORTH);

        // Tabla de notificaciones
        tableModel = new DefaultTableModel(new Object[][]{}, new String[]{"ID", "Mensaje", "Fecha"}) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Hacer que la tabla no sea editable
            }
        };
        notificationsTable = new JTable(tableModel);
        notificationsTable.setRowHeight(25);
        notificationsTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        notificationsTable.getTableHeader().setBackground(new Color(52, 152, 219));
        notificationsTable.getTableHeader().setForeground(Color.WHITE);
        notificationsTable.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(notificationsTable);
        fondoPanel.add(scrollPane, BorderLayout.CENTER);

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setOpaque(false);

        if (isAdmin) {
            JButton addNotificationButton = new JButton("Añadir Notificación");
            addNotificationButton.addActionListener(e -> addNotification());
            buttonPanel.add(addNotificationButton);

            JButton deleteNotificationButton = new JButton("Eliminar Notificación");
            deleteNotificationButton.addActionListener(e -> deleteNotification());
            buttonPanel.add(deleteNotificationButton);
        }

        JButton backButton = new JButton("Volver al Menú Principal");
        backButton.addActionListener(e -> {
            dispose();
            new MenuPrincipal(isAdmin, currentUser, emailUser).setVisible(true);
        });
        buttonPanel.add(backButton);

        fondoPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Cargar notificaciones al inicio
        loadNotifications();
    }

    private void loadNotifications() {
        try (Connection connection = new Db().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT ID, Mensaje, Fecha_Notificacion FROM notificaciones")) {

            tableModel.setRowCount(0); // Limpiar la tabla
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String mensaje = resultSet.getString("Mensaje");
                String fecha = resultSet.getString("Fecha_Notificacion");
                tableModel.addRow(new Object[]{id, mensaje, fecha});
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar notificaciones : " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addNotification() {
        // Crear panel para los campos de entrada
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField messageField = new JTextField();
        JTextField dateField = new JTextField();

        panel.add(new JLabel("Mensaje:"));
        panel.add(messageField);
        panel.add(new JLabel("Fecha (YYYY-MM-DD):"));
        panel.add(dateField);

        // Mostrar el formulario en un JOptionPane
        int option = JOptionPane.showConfirmDialog(this, panel, "Agregar Nueva Notificación", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (option == JOptionPane.OK_OPTION) {
            String mensaje = messageField.getText().trim();
            String fecha = dateField.getText().trim();

            if (mensaje.isEmpty() || fecha.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try (Connection connection = new Db().getConnection();
                 PreparedStatement ps = connection.prepareStatement("INSERT INTO notificaciones (Mensaje, Fecha) VALUES (?, ?)")) {
                ps.setString(1, mensaje);
                ps.setString(2, fecha);
                ps.executeUpdate();
                loadNotifications();
                JOptionPane.showMessageDialog(this, "Notificación añadida con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error al añadir notificación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void deleteNotification() {
        int selectedRow = notificationsTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione una notificación para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int notificationId = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = new Db().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM notificaciones WHERE ID = ?")) {
            ps.setInt(1, notificationId);
            ps.executeUpdate();
            loadNotifications();
            JOptionPane.showMessageDialog(this, "Notificación eliminada con éxito.", "Información", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar notificación: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
