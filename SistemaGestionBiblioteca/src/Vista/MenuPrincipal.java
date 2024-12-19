package Vista;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    public MenuPrincipal(boolean isAdmin, String currentUser) {
        setTitle("Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
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
        fondoPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        fondoPanel.setLayout(new GridBagLayout());
        setContentPane(fondoPanel);

        // Configuración del layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Etiqueta de bienvenida
        JLabel welcomeLabel = new JLabel("Bienvenido, " + currentUser, SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("Arial Black", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        gbc.gridy = 0;
        fondoPanel.add(welcomeLabel, gbc);

        // Botón para gestión de usuarios (solo para administradores)
        if (isAdmin) {
            JButton userManagementButton = new JButton("Gestión de Usuarios");
            userManagementButton.setFont(new Font("Arial", Font.PLAIN, 14));
            userManagementButton.addActionListener(e -> {
                new VistaUsuarios(isAdmin, currentUser).setVisible(true);
                dispose();
            });
            gbc.gridy = 1;
            fondoPanel.add(userManagementButton, gbc);
        }

        // Botón para vista de libros
        JButton bookManagementButton = new JButton("Vista de Libros");
        bookManagementButton.setFont(new Font("Arial", Font.PLAIN, 14));
        bookManagementButton.addActionListener(e -> {
            new VistaLibros(isAdmin, currentUser).setVisible(true);
            dispose();
        });
        gbc.gridy = 2;
        fondoPanel.add(bookManagementButton, gbc);

        // Botón para préstamos y devoluciones
        JButton loansAndReturnsButton = new JButton("Préstamos y Devoluciones");
        loansAndReturnsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        loansAndReturnsButton.addActionListener(e -> {
        	new VistaPrestamos(isAdmin, currentUser).setVisible(true);
        });
        gbc.gridy = 3;
        fondoPanel.add(loansAndReturnsButton, gbc);

        // Botón para notificaciones
        JButton notificationsButton = new JButton("Notificaciones");
        notificationsButton.setFont(new Font("Arial", Font.PLAIN, 14));
        notificationsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Acceso a Notificaciones.");
        });
        gbc.gridy = 4;
        fondoPanel.add(notificationsButton, gbc);

        // Botón para cerrar sesión
        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 14));
        logoutButton.addActionListener(e -> {
            dispose();
            new VistaLogin().setVisible(true);
        });
        gbc.gridy = 5;
        fondoPanel.add(logoutButton, gbc);
    }
}
