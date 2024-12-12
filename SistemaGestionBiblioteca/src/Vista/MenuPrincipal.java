package Vista;

import java.awt.EventQueue;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class MenuPrincipal extends JFrame {
    public MenuPrincipal(boolean isAdmin) {
        setTitle("Menú Principal");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridLayout(4, 1, 10, 10));

        JButton librosButton = new JButton("Visualización de Libros");
        librosButton.addActionListener(e -> new VistaLibros().setVisible(true));

        JButton prestamosButton = new JButton("Gestión de Préstamos");
        prestamosButton.addActionListener(e -> new VistaPrestamos().setVisible(true));

        JButton usuariosButton = new JButton("Gestión de Usuarios");
        usuariosButton.setEnabled(isAdmin);
        usuariosButton.addActionListener(e -> new VistaUsuarios().setVisible(true));

        JButton cerrarSesionButton = new JButton("Cerrar Sesión");
        cerrarSesionButton.addActionListener(e -> {
            dispose();
            new MenuInicioSesion().setVisible(true);
        });

        mainPanel.add(librosButton);
        mainPanel.add(prestamosButton);
        mainPanel.add(usuariosButton);
        mainPanel.add(cerrarSesionButton);

        add(mainPanel);
    }
}