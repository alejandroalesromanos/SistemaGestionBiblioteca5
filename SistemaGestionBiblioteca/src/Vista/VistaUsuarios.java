package Vista;

import modelo.Db;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.*;

public class VistaUsuarios extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTable userTable;
    private DefaultTableModel tableModel;

    private JButton addUserButton;
    private JButton deleteUserButton;
    private JButton editUserButton;
    private JButton backButton;

    public VistaUsuarios(boolean isAdmin, String currentUser) {
        setTitle("Gestión de Usuarios");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Fondo personalizado con gradiente
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
        fondoPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        fondoPanel.setLayout(new GridBagLayout());
        setContentPane(fondoPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Etiqueta de título
        JLabel titleLabel = new JLabel("Lista de Usuarios", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial Black", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        fondoPanel.add(titleLabel, gbc);

        // Tabla de usuarios
        tableModel = new DefaultTableModel(new Object[][] {},
                new String[] { "ID", "DNI", "Nombre", "Apellidos", "Email", "Teléfono", "Rol" });
        userTable = new JTable(tableModel);
        userTable.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(userTable);
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        fondoPanel.add(scrollPane, gbc);

        gbc.weighty = 0; // Reinicia el peso vertical para los botones

        // Botones
        if (isAdmin) {
            addUserButton = createButton("Añadir Usuario", e -> showAddUserForm());
            gbc.gridy = 2;
            gbc.gridwidth = 1;
            gbc.weightx = 0.5;
            fondoPanel.add(addUserButton, gbc);

            deleteUserButton = createButton("Eliminar Usuario", e -> deleteUser());
            gbc.gridx = 1;
            fondoPanel.add(deleteUserButton, gbc);

            editUserButton = createButton("Editar Usuario", e -> editUser());
            gbc.gridy = 3;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            fondoPanel.add(editUserButton, gbc);
        }

        backButton = createButton("Volver al Menú Principal", e -> {
            dispose();
            new MenuPrincipal(isAdmin, currentUser).setVisible(true);
        });
        gbc.gridy = 4;
        fondoPanel.add(backButton, gbc);

        // Cargar usuarios
        loadUsers();
    }

    /**
     * Método para crear botones con estilo consistente.
     */
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.addActionListener(action);
        return button;
    }

    /**
     * Método para cargar usuarios en la tabla.
     */
    private void loadUsers() {
        try (Connection connection = new Db().getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(
                     "SELECT ID, DNI, Nombre, Apellidos, Email, Telefono, Rol FROM usuarios")) {

            tableModel.setRowCount(0); // Limpiar la tabla antes de cargar

            while (resultSet.next()) {
                tableModel.addRow(new Object[] {
                        resultSet.getInt("ID"),
                        resultSet.getString("DNI"),
                        resultSet.getString("Nombre"),
                        resultSet.getString("Apellidos"),
                        resultSet.getString("Email"),
                        resultSet.getString("Telefono"),
                        resultSet.getString("Rol")
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al cargar usuarios: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para añadir un usuario.
     */
    private void showAddUserForm() {
        JPanel panel = new JPanel(new GridLayout(7, 2, 10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField dniField = new JTextField();
        JTextField nombreField = new JTextField();
        JTextField apellidosField = new JTextField();
        JTextField emailField = new JTextField();
        JTextField telefonoField = new JTextField();
        JComboBox<String> roleComboBox = new JComboBox<>(new String[] { "Administrador", "Usuario estándar" });
        JPasswordField passwordField = new JPasswordField();

        panel.add(new JLabel("DNI:"));
        panel.add(dniField);
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Apellidos:"));
        panel.add(apellidosField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Teléfono:"));
        panel.add(telefonoField);
        panel.add(new JLabel("Rol:"));
        panel.add(roleComboBox);
        panel.add(new JLabel("Contraseña:"));
        panel.add(passwordField);

        int result = JOptionPane.showConfirmDialog(this, panel, "Añadir Usuario", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String dni = dniField.getText();
            String nombre = nombreField.getText();
            String apellidos = apellidosField.getText();
            String email = emailField.getText();
            String telefono = telefonoField.getText();
            String rol = (String) roleComboBox.getSelectedItem();
            String password = new String(passwordField.getPassword());

            if (dni.isEmpty() || nombre.isEmpty() || apellidos.isEmpty() || email.isEmpty() || telefono.isEmpty()
                    || rol == null || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error",
                        JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(this, "Error al añadir usuario: " + e.getMessage(), "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    /**
     * Método para eliminar un usuario.
     */
    private void deleteUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para eliminar.", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);

        try (Connection connection = new Db().getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM usuarios WHERE ID = ?")) {
            ps.setInt(1, userId);
            ps.executeUpdate();
            loadUsers();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al eliminar usuario: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Método para editar un usuario.
     */
    private void editUser() {
        int selectedRow = userTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un usuario para editar.", "Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        String dni = (String) tableModel.getValueAt(selectedRow, 1);
        String nombre = (String) tableModel.getValueAt(selectedRow, 2);
        String apellidos = (String) tableModel.getValueAt(selectedRow, 3);
        String email = (String) tableModel.getValueAt(selectedRow, 4);
        String telefono = (String) tableModel.getValueAt(selectedRow, 5);
        String rol = (String) tableModel.getValueAt(selectedRow, 6);

        JTextField dniField = new JTextField(dni);
        JTextField nombreField = new JTextField(nombre);
        JTextField apellidosField = new JTextField(apellidos);
        JTextField emailField = new JTextField(email);
        JTextField telefonoField = new JTextField(telefono);
        String[] roles = { "Administrador", "Usuario estándar" };
        JComboBox<String> roleComboBox = new JComboBox<>(roles);
        roleComboBox.setSelectedItem(rol);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("DNI:"));
        panel.add(dniField);
        panel.add(new JLabel("Nombre:"));
        panel.add(nombreField);
        panel.add(new JLabel("Apellidos:"));
        panel.add(apellidosField);
        panel.add(new JLabel("Email:"));
        panel.add(emailField);
        panel.add(new JLabel("Teléfono:"));
        panel.add(telefonoField);
        panel.add(new JLabel("Rol:"));
        panel.add(roleComboBox);

        int result = JOptionPane.showConfirmDialog(this, panel, "Editar Usuario", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        try (Connection connection = new Db().getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "UPDATE usuarios SET DNI = ?, Nombre = ?, Apellidos = ?, Email = ?, Telefono = ?, Rol = ? WHERE ID = ?")) {
            ps.setString(1, dniField.getText());
            ps.setString(2, nombreField.getText());
            ps.setString(3, apellidosField.getText());
            ps.setString(4, emailField.getText());
            ps.setString(5, telefonoField.getText());
            ps.setString(6, (String) roleComboBox.getSelectedItem());
            ps.setInt(7, userId);
            ps.executeUpdate();
            loadUsers();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al editar usuario: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
