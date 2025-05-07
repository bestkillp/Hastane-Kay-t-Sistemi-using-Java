import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class Menu extends JFrame {
    public Menu() {
        initUI();
    }

    private void initUI() {
        setTitle("Hasta Kayıt - Ana Menü");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color lightGreen = new Color(138, 194, 127);

        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(lightGreen);

        ImageIcon logo = new ImageIcon("C:\\Hastane Kayıt Sistemi\\hastanefoto.jpg"); // Logo dosya yolu
        Image img = logo.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH); // Logo boyutlandırma
        logo = new ImageIcon(img);

        JLabel titleLabel = new JLabel("Ana Menü", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalTextPosition(JLabel.RIGHT); // Yazı, logonun sağına gelsin
        titleLabel.setIconTextGap(10); // Yazı ile logo arası boşluk
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel, BorderLayout.NORTH);
        titleLabel.setIcon(logo);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10)); 
        JButton addPatientBtn = createMenuButton("Hasta Ekle");
        JButton listPatientsBtn = createMenuButton("Hasta Listele");
        JButton LoginScreenBtn = createMenuButton("Kullanıcı Değiştir");
        JButton exitBtn = createMenuButton("Çıkış");

        addPatientBtn.addActionListener((ActionEvent e) -> {
            new HastaEkleme().setVisible(true);
            dispose();
        });

        listPatientsBtn.addActionListener((ActionEvent e) -> {
            new HastaListe().setVisible(true);
            dispose();
        });

        LoginScreenBtn.addActionListener((ActionEvent e) -> {
        	new GirisEkrani().setVisible(true);
        	dispose();
        }); 

        exitBtn.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        buttonPanel.add(addPatientBtn);
        buttonPanel.add(listPatientsBtn);
        buttonPanel.add(LoginScreenBtn);
        buttonPanel.add(exitBtn);

        panel.add(buttonPanel, BorderLayout.CENTER);
        panel.setBackground(lightGreen);
        add(panel);
    }

    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.PLAIN, 18));
        button.setPreferredSize(new Dimension(200, 60));
        return button;
    }

    // Main metodu ekleniyor
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Menu().setVisible(true);
        });
    }
}