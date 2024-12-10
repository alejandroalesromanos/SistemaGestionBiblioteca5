package Vista;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.GroupLayout;
import javax.swing.SwingConstants;

public class Interfaz extends JFrame {

    private static final long serialVersionUID = 1L;
    private boolean isAdmin; // Variable para controlar el tipo de usuario

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Interfaz frame = new Interfaz();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Create the frame.
     */
    public Interfaz() {
        setTitle("Sistema de Gestión de Biblioteca");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600, 400));
        setLocationRelativeTo(null);

        // Mostrar el panel de inicio de sesión
        showLoginPanel();
    }

    private void showLoginPanel() {
        JPanel loginPanel = new JPanel();
        GroupLayout layout = new GroupLayout(loginPanel);
        loginPanel.setLayout(layout);

        JLabel userLabel = new JLabel("Usuario:");
        JTextField userField = new JTextField(20);

        JLabel passwordLabel = new JLabel("Contraseña:");
        JPasswordField passwordField = new JPasswordField(20);

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.addActionListener(e -> {
            String username = userField.getText();
            String password = new String(passwordField.getPassword());

            if (validateCredentials(username, password)) {
                isAdmin = "admin".equals(username);
                showMainMenu();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas. Intente de nuevo.");
            }
        });

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(userLabel)
                    .addComponent(userField))
                .addGroup(layout.createSequentialGroup()
                    .addComponent(passwordLabel)
                    .addComponent(passwordField))
                .addComponent(loginButton)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(userLabel)
                    .addComponent(userField))
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(passwordLabel)
                    .addComponent(passwordField))
                .addComponent(loginButton)
        );

        setContentPane(loginPanel);
        revalidate();
        repaint();
    }

    private boolean validateCredentials(String username, String password) {
        return ("admin".equals(username) && "admin123".equals(password))
                || ("user".equals(username) && "user123".equals(password));
    }

    private void showMainMenu() {
        JPanel mainPanel = new JPanel();
        GroupLayout layout = new GroupLayout(mainPanel);
        mainPanel.setLayout(layout);

        JLabel header = new JLabel("Bienvenido al Sistema de Gestión de Biblioteca", SwingConstants.CENTER);
        header.setFont(new Font("Arial", Font.BOLD, 18));

        JButton booksButton = new JButton("Gestión de Libros");
        booksButton.setEnabled(isAdmin);
        booksButton.addActionListener(e -> openBookManagement());

        JButton reservationsButton = new JButton("Reservas");
        reservationsButton.setEnabled(!isAdmin);
        reservationsButton.addActionListener(e -> openReservationManagement());

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> showLoginPanel());

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
            layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                .addComponent(header)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(booksButton)
                    .addComponent(reservationsButton))
                .addComponent(logoutButton)
        );

        layout.setVerticalGroup(
            layout.createSequentialGroup()
                .addComponent(header)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                    .addComponent(booksButton)
                    .addComponent(reservationsButton))
                .addComponent(logoutButton)
        );

        setContentPane(mainPanel);
        revalidate();
        repaint();
    }

    private void openBookManagement() {
        JOptionPane.showMessageDialog(this, "Abrir Gestión de Libros (Solo para Administradores)");
    }

    private void openReservationManagement() {
        JOptionPane.showMessageDialog(this, "Abrir Reservas (Solo para Usuarios Estándar)");
    }
}
