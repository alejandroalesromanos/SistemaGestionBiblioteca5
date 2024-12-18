package Vista;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;
import modelo.GestorLogin;

public class VistaLogin extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField emailField;
    private JPasswordField passwordField;  // Changed to JPasswordField
    private JButton loginButton;
    private JPanel contentPanel;
    
    // Colors
    private Color primaryColor = new Color(25, 118, 210);
    private Color accentColor = new Color(66, 165, 245);
    private Color backgroundColor = new Color(236, 239, 241);
    private Color errorColor = new Color(220, 53, 69);
    private Color validColor = new Color(40, 167, 69);
    
    // Dimensions
    private static final int MIN_WIDTH = 300;
    private static final int MIN_HEIGHT = 400;
    private static final Dimension MINIMUM_SIZE = new Dimension(MIN_WIDTH, MIN_HEIGHT);

    public VistaLogin() {
        setTitle("Sistema Gestor de Biblioteca");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setMinimumSize(MINIMUM_SIZE);
        setSize(450, 600);
        setLocationRelativeTo(null);

        // Add resize listener
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                revalidateLayout();
            }
        });

        // Main background panel with gradient
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, primaryColor, 0, h, accentColor);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, w, h);
                g2d.dispose();
            }
        };
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        setContentPane(mainPanel);

        // Content Panel
        contentPanel = createContentPanel();
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(contentPanel);
        mainPanel.add(Box.createVerticalGlue());
    }

    private void revalidateLayout() {
        int width = getWidth();
        
        // Calculate responsive font sizes
        int titleFontSize = Math.max(24, Math.min(32, width / 15));
        int fieldFontSize = Math.max(12, Math.min(14, width / 30));
        int buttonFontSize = Math.max(12, Math.min(14, width / 30));
        
        // Calculate responsive field width
        int fieldWidth = Math.max(200, Math.min(300, width - 100));
        
        // Update component sizes
        updateComponentSizes(fieldWidth, titleFontSize, fieldFontSize, buttonFontSize);
        
        // Revalidate and repaint
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void updateComponentSizes(int fieldWidth, int titleFontSize, int fieldFontSize, int buttonFontSize) {
        // Update title font
        Component[] components = contentPanel.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                if (label.getText().equals("Sistema Gestor de Biblioteca")) {
                    label.setFont(new Font("Segoe UI", Font.BOLD, titleFontSize));
                } else {
                    label.setFont(new Font("Segoe UI", Font.BOLD, fieldFontSize));
                }
            }
        }

        // Update fields size
        Dimension fieldDimension = new Dimension(fieldWidth, 60);
        emailField.setPreferredSize(fieldDimension);
        emailField.setMaximumSize(fieldDimension);
        emailField.setMinimumSize(fieldDimension);
        passwordField.setPreferredSize(fieldDimension);  // Updated to passwordField
        passwordField.setMaximumSize(fieldDimension);
        passwordField.setMinimumSize(fieldDimension);
        
        // Update button size
        Dimension buttonDimension = new Dimension(Math.min(200, fieldWidth), 40);
        loginButton.setPreferredSize(buttonDimension);
        loginButton.setMaximumSize(buttonDimension);
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, buttonFontSize));
    }

    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        
        // Dynamic padding based on window size
        panel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Logo and Title
        JLabel titleLabel = new JLabel("Sistema Gestor de Biblioteca");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(titleLabel);
        
        panel.add(Box.createVerticalStrut(30));

        // Form fields
        emailField = createStyledTextField("Ingrese su correo electrónico");
        passwordField = createPasswordField();  // Changed to createPasswordField
        
        panel.add(createFieldPanel("Correo Electrónico", emailField));
        panel.add(Box.createVerticalStrut(20));
        panel.add(createFieldPanel("Contraseña", passwordField));
        panel.add(Box.createVerticalStrut(30));

        // Login button
        loginButton = createStyledButton("Iniciar Sesión");
        panel.add(loginButton);

        return panel;
    }

    private JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(255, 255, 255, 230));
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, Color.WHITE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        // Add placeholder
        field.setText(placeholder);
        field.setForeground(Color.GRAY);
        
        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, accentColor),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, Color.WHITE),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
                validateField(field);
            }
        });
        
        // Make fields responsive
        Dimension fieldSize = new Dimension(300, 60); // Fixed height and width
        field.setPreferredSize(fieldSize);
        field.setMaximumSize(fieldSize);
        field.setMinimumSize(fieldSize);

        // Add input validation
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                if (!field.getText().equals(placeholder)) {
                    validateField(field);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                if (!field.getText().equals(placeholder)) {
                    validateField(field);
                }
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                if (!field.getText().equals(placeholder)) {
                    validateField(field);
                }
            }
        });

        return field;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBackground(new Color(255, 255, 255, 230));
        field.setForeground(Color.BLACK);
        field.setCaretColor(Color.BLACK);
        field.setBorder(BorderFactory.createCompoundBorder(
            new RoundedBorder(8, Color.WHITE),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));

        field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals("Ingrese su contraseña")) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, accentColor),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (field.getPassword().length == 0) {
                    field.setText("Ingrese su contraseña");
                    field.setForeground(Color.GRAY);
                }
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, Color.WHITE),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }
        });
        
        // Make fields responsive
        Dimension fieldSize = new Dimension(300, 60); // Fixed height and width
        field.setPreferredSize(fieldSize);
        field.setMaximumSize(fieldSize);
        field.setMinimumSize(fieldSize);

        return field;
    }

    private void validateField(JTextField field) {
        if (field == emailField && !field.getText().equals("Ingrese su correo electrónico")) {
            String email = field.getText();
            if (!email.isEmpty() && !isValidEmail(email)) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, errorColor),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            } else if (!email.isEmpty()) {
                field.setBorder(BorderFactory.createCompoundBorder(
                    new RoundedBorder(8, validColor),
                    BorderFactory.createEmptyBorder(8, 10, 8, 10)
                ));
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        return email.matches(emailRegex);
    }

    private JPanel createFieldPanel(String labelText, JTextField field) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(Color.WHITE);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        label.setBorder(BorderFactory.createEmptyBorder(0, 2, 5, 0));

        panel.add(label);
        panel.add(field);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isPressed()) {
                    g2.setColor(primaryColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(primaryColor.brighter());
                } else {
                    g2.setColor(primaryColor);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(primaryColor);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addActionListener(e -> handleLogin());

        // Make button responsive
        Dimension buttonSize = new Dimension(200, 40);
        button.setPreferredSize(buttonSize);
        button.setMaximumSize(buttonSize);
        
        return button;
    }

    private void handleLogin() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // Check if fields contain placeholder text
        if (email.equals("Ingrese su correo electrónico") || 
            password.equals("Ingrese su contraseña")) {
            showErrorMessage("Por favor, complete todos los campos.");
            return;
        }

        if (email.isEmpty() || password.isEmpty()) {
            showErrorMessage("Por favor, complete todos los campos.");
            return;
        }

        if (!isValidEmail(email)) {
            showErrorMessage("Por favor, ingrese un correo electrónico válido.");
            return;
        }

        // Si la validación de credenciales es exitosa
        GestorLogin gestionLogin = new GestorLogin();
        boolean valid = gestionLogin.validateCredentials(email, password, this);

        if (valid) {
            // Cierra la ventana de login si las credenciales son correctas
            this.dispose();
        } else {
            showErrorMessage("Credenciales incorrectas, por favor intente nuevamente.");
        }
    }


    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(
            this,
            message,
            "Error",
            JOptionPane.ERROR_MESSAGE
        );
    }

    private static class RoundedBorder implements Border {
        private final int radius;
        private final Color color;

        RoundedBorder(int radius, Color color) {
            this.radius = radius;
            this.color = color;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(color);
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) {
            return new Insets(radius, radius, radius, radius);
        }

        @Override
        public boolean isBorderOpaque() {
            return false;
        }
    }
}
