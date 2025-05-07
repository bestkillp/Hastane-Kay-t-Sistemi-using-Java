import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.lang.reflect.Field;

public class HastaEkleme extends JFrame {
    private JTextField nameField, surnameField, idField, ageField, phoneField;
    private JComboBox<String> sexComboBox; 
    
    public HastaEkleme() {
        initUI();
    }

    private void initUI() {
        setTitle("Hasta Kayıt - Yeni Hasta");
        setSize(500, 400);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        Color lightGreen = new Color(138, 194, 127);
        Color lightRed = new Color(255,97,138);
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(lightGreen);

        ImageIcon logo = new ImageIcon("C:\\Hastane Kayıt Sistemi\\hastanefoto.jpg"); // Logo dosya yolu
        Image img = logo.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH); // Logo boyutlandırma
        logo = new ImageIcon(img);
        
        JLabel titleLabel = new JLabel("Yeni Hasta Ekle", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(titleLabel, BorderLayout.NORTH);
        titleLabel.setForeground(lightRed); // Lacivert ton
        titleLabel.setBackground(lightRed);
        titleLabel.setIcon(logo);

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 6, 6));
        
        nameField = new JTextField();
        surnameField = new JTextField();
        idField = new JTextField();
        ageField = new JTextField();
        phoneField = new JTextField();
        
        // Create combo box for gender selection
        String[] genders = {"Seçiniz","Erkek", "Kadın", "Diğer"};
        sexComboBox = new JComboBox<>(genders);
        sexComboBox.setSelectedIndex(0); // Default selection

        addFormField(formPanel, "Ad:", nameField);
        addFormField(formPanel, "Soyad:", surnameField);
        addFormField(formPanel, "T.C Kimlik No:", idField);
        addFormField(formPanel, "Yaş:", ageField);
        addFormField(formPanel, "Telefon:", phoneField);
        formPanel.setBackground(lightGreen);
        
        // Add combo box instead of text field
        formPanel.add(new JLabel("Cinsiyet:"));
        formPanel.add(sexComboBox);

        panel.add(formPanel, BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        JButton saveButton = new JButton("Kaydet");
        JButton backButton = new JButton("Geri");
        JButton helpButton = new JButton("Yardım");
        saveButton.addActionListener(this::savePatient);
        backButton.addActionListener(e -> {
            dispose();
            new Menu().setVisible(true); // Ana menüyü aç
        });

        helpButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                """
                Bu form, yeni bir hastayı sisteme kaydetmek için kullanılır.

                ➤ Tüm alanlar eksiksiz doldurulmalıdır.
                ➤ T.C Kimlik No: 11 haneli olmalıdır.
                ➤ Yaş: 1-99 arasında olmalıdır.
                ➤ Telefon: 5xxx xxx xxxx formatına uygun 11 haneli sayı girilmelidir.

                'Kaydet' ile hastayı ekleyebilir, 'Geri' ile ana menüye dönebilirsiniz.
                """,
                "Yardım - Hasta Ekleme Ekranı", JOptionPane.INFORMATION_MESSAGE);
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        panel.add(buttonPanel,BorderLayout.SOUTH);
        buttonPanel.add(helpButton);
        panel.setBackground(lightGreen);
        buttonPanel.setBackground(lightGreen);
        add(panel);
    }

    private void addFormField(JPanel panel, String label, JTextField field) {
        panel.add(new JLabel(label));
        panel.add(field);
        field.setBackground(new Color(200, 255, 255)); // Açık mavi tonu
        field.setOpaque(true);
    }

    private void savePatient(ActionEvent e) {
        try {
            String name = nameField.getText().trim();
            String surname = surnameField.getText().trim();
            long id = Long.parseLong(idField.getText().trim()); 
            int age = Integer.parseInt(ageField.getText().trim());
            long phone = Long.parseLong(phoneField.getText().trim());
            String sex = (String) sexComboBox.getSelectedItem(); // Get selected gender
            
            if (name.isEmpty()) {
                throw new IllegalArgumentException("Tüm ad alanı doldurunuz!");
            }
            if (surname.isEmpty()) {
                throw new IllegalArgumentException("Tüm soyad alanı doldurunuz!");
            }

            // TC Kimlik No ayarı
            if (id <= 9_999_999_999L || id >= 100_000_000_000L) {
                throw new IllegalArgumentException("TC Kimlik No 11 haneli olmalıdır!");
            }
            //tel no ayarı
            if (phone <= 5_000_000_000L || phone >= 6_000_000_000L) {
                throw new IllegalArgumentException("Hatalı Telefon Numarası!");
            }
            if (age >= 100 || age <= 0 ) {
                throw new IllegalArgumentException("Yaş aralığınız Hatalıdır!");
            }
            Hasta patient = new Hasta(name, surname, id, age, phone, sex);
            Hastaveri.savePatient(patient);

            JOptionPane.showMessageDialog(this, "Hasta başarıyla kaydedildi!", 
                "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            openMainMenu();
            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Geçersiz sayı formatı! Lütfen doğru bilgileri girin.", 
                "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Kayıt sırasında hata oluştu: " + ex.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void openMainMenu() {
        Menu mainMenu = new Menu();
        mainMenu.setVisible(true);
    }

    private void clearForm() {
        nameField.setText("");
        surnameField.setText("");
        idField.setText("");
        ageField.setText("");
        phoneField.setText("");
        sexComboBox.setSelectedIndex(0); // Reset to default selection
    }
}