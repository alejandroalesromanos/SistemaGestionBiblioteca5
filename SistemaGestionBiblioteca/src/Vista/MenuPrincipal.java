package Vista;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.EmptyBorder;

public class MenuPrincipal extends JFrame {

    private static final long serialVersionUID = 1L;

    public MenuPrincipal(boolean isAdmin, String currentUser) {
        setTitle("Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        FondoPantalla fondo = new FondoPantalla();
        fondo.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(fondo);
        setLocationRelativeTo(null);

        GridBagLayout gridBagLayout = new GridBagLayout();
        getContentPane().setLayout(gridBagLayout);

        JLabel welcomeLabel = new JLabel("Bienvenido, " + currentUser);
        welcomeLabel.setFont(new Font("Arial Black", Font.BOLD, 18));
        welcomeLabel.setForeground(Color.WHITE);
        GridBagConstraints gbc_welcomeLabel = new GridBagConstraints();
        gbc_welcomeLabel.insets = new Insets(10, 10, 10, 10);
        gbc_welcomeLabel.gridx = 1;
        gbc_welcomeLabel.gridy = 0;
        getContentPane().add(welcomeLabel, gbc_welcomeLabel);

        if (isAdmin) {
            JButton userManagementButton = new JButton("Gestión de Usuarios");
            userManagementButton.addActionListener(e -> {
                new VistaUsuarios(isAdmin, currentUser).setVisible(true);
                dispose();
            });
            GridBagConstraints gbc_userManagementButton = new GridBagConstraints();
            gbc_userManagementButton.insets = new Insets(10, 10, 10, 10);
            gbc_userManagementButton.gridx = 1;
            gbc_userManagementButton.gridy = 1;
            getContentPane().add(userManagementButton, gbc_userManagementButton);
        }

        JButton bookManagementButton = new JButton("Vista de Libros");
        bookManagementButton.addActionListener(e -> {
            new VistaLibros(isAdmin, currentUser).setVisible(true); // Agrega currentUser si es necesario
            dispose();
        });

        GridBagConstraints gbc_bookManagementButton = new GridBagConstraints();
        gbc_bookManagementButton.insets = new Insets(10, 10, 10, 10);
        gbc_bookManagementButton.gridx = 1;
        gbc_bookManagementButton.gridy = 2;
        getContentPane().add(bookManagementButton, gbc_bookManagementButton);

        JButton loansAndReturnsButton = new JButton("Préstamos y Devoluciones");
        loansAndReturnsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Acceso a Préstamos y Devoluciones.");
        });
        GridBagConstraints gbc_loansAndReturnsButton = new GridBagConstraints();
        gbc_loansAndReturnsButton.insets = new Insets(10, 10, 10, 10);
        gbc_loansAndReturnsButton.gridx = 1;
        gbc_loansAndReturnsButton.gridy = 3;
        getContentPane().add(loansAndReturnsButton, gbc_loansAndReturnsButton);

        JButton notificationsButton = new JButton("Notificaciones");
        notificationsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Acceso a Notificaciones.");
        });
        GridBagConstraints gbc_notificationsButton = new GridBagConstraints();
        gbc_notificationsButton.insets = new Insets(10, 10, 10, 10);
        gbc_notificationsButton.gridx = 1;
        gbc_notificationsButton.gridy = 4;
        getContentPane().add(notificationsButton, gbc_notificationsButton);

        JButton logoutButton = new JButton("Cerrar Sesión");
        logoutButton.addActionListener(e -> {
            dispose();
            new MenuInicioSesion().setVisible(true);
        });
        GridBagConstraints gbc_logoutButton = new GridBagConstraints();
        gbc_logoutButton.insets = new Insets(10, 10, 10, 10);
        gbc_logoutButton.gridx = 1;
        gbc_logoutButton.gridy = 5;
        getContentPane().add(logoutButton, gbc_logoutButton);
    }
}
