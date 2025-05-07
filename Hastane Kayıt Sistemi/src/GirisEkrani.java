import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GirisEkrani extends JFrame {
    public GirisEkrani() {
        initUI();
    }

    private void initUI() {
        setTitle("Hasta Kayıt - Giriş");
        setSize(400, 300);  // Slightly increased height for better spacing
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
       
        Color lightGreen = new Color(138, 194, 127);

        // Main panel with BorderLayout
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(lightGreen);  // Matching the Menu's background

        ImageIcon logo = new ImageIcon("C:\\Hastane Kayıt Sistemi\\hastanefoto.jpg"); // Logo dosya yolu
        Image img = logo.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH); // Logo boyutlandırma
        logo = new ImageIcon(img);

        // Title label (centered at top)
        JLabel titleLabel = new JLabel("Hasta Kayıt Sistemi", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);
        titleLabel.setIcon(logo);

        // Input panel (center)
        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 10));
        inputPanel.setBackground(lightGreen);
        
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        
        // Style the labels to match
        JLabel userLabel = new JLabel("Kullanıcı Adı:");
        userLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        JLabel passLabel = new JLabel("Şifre:");
        passLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        inputPanel.add(userLabel);
        inputPanel.add(usernameField);
        inputPanel.add(passLabel);
        inputPanel.add(passwordField);

        // Button panel (south)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(lightGreen);
        
        // Using the same button style as Menu
        JButton loginButton = createMenuButton("Giriş Yap");
        loginButton.addActionListener((ActionEvent e) -> {
            if (authenticate(usernameField.getText(), new String(passwordField.getPassword()))) {
                openMainMenu();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Geçersiz giriş bilgileri!", 
                    "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonPanel.add(loginButton);
        
        // Add components to main panel
        panel.add(inputPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        add(panel);
    }

    private boolean authenticate(String username, String password) {
        return "admin".equals(username) && "admin123".equals(password) ||
               "akdemir".equals(username) && "akdemir".equals(password) ||
               "bakit".equals(username) && "bakit".equals(password) ||
               "kaya".equals(username) && "kaya".equals(password) ||
               "demirel".equals(username) && "demirel".equals(password);
    }

    // Same button style as in Menu class
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setPreferredSize(new Dimension(200, 60));
        button.setFocusPainted(false);
        return button;
    }

    private void openMainMenu() {
        Menu mainMenu = new Menu();
        mainMenu.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GirisEkrani().setVisible(true);
        });
    }
}