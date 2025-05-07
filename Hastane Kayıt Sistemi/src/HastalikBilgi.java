import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HastalikBilgi extends JFrame {
    private JTextArea tehsis;
    private JTextArea tedavi;
    private long hastaTcNo;
    private String fileName;

    public HastalikBilgi(long hastaTcNo) {
        this.hastaTcNo = hastaTcNo;
        this.fileName = "hasta_" + hastaTcNo + "_bilgi.txt";
        initUI();
        loadMedicalInfo(); // Dosyadan bilgileri yükle
    }

    private void initUI() {
        setTitle("Hastalık Bilgileri - TC: " + hastaTcNo);
        setSize(600, 500);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("Hastalık ve Tedavi Bilgileri (TC: " + hastaTcNo + ")", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        // Teşhis alanı
        JPanel diagnosisPanel = new JPanel(new BorderLayout());
        diagnosisPanel.add(new JLabel("Teşhis:"), BorderLayout.NORTH);
        tehsis = new JTextArea();
        tehsis.setLineWrap(true);
        tehsis.setWrapStyleWord(true);
        JScrollPane diagnosisScroll = new JScrollPane(tehsis);
        diagnosisPanel.add(diagnosisScroll, BorderLayout.CENTER);

        // Tedavi alanı
        JPanel treatmentPanel = new JPanel(new BorderLayout());
        treatmentPanel.add(new JLabel("Tedavi:"), BorderLayout.NORTH);
        tedavi = new JTextArea();
        tedavi.setLineWrap(true);
        tedavi.setWrapStyleWord(true);
        JScrollPane treatmentScroll = new JScrollPane(tedavi);
        treatmentPanel.add(treatmentScroll, BorderLayout.CENTER);

        formPanel.add(diagnosisPanel);
        formPanel.add(treatmentPanel);
        mainPanel.add(formPanel, BorderLayout.CENTER);

        // Buton paneli
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 6));
        JButton saveButton = new JButton("Kaydet");
        JButton backButton = new JButton("Geri");
        
        saveButton.addActionListener(this::saveMedicalInfo);
        backButton.addActionListener(e -> {
            dispose();
            
        });

        buttonPanel.add(saveButton);
        buttonPanel.add(backButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadMedicalInfo() {
        if (!Files.exists(Paths.get(fileName))) {
            return; // Dosya yoksa hiçbir şey yapma
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder diagnosis = new StringBuilder();
            StringBuilder treatment = new StringBuilder();
            boolean isDiagnosis = false;
            boolean isTreatment = false;

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Teşhis: ")) {
                    diagnosis.append(line.substring(8));
                    isDiagnosis = true;
                    isTreatment = false;
                } else if (line.startsWith("Tedavi: ")) {
                    treatment.append(line.substring(8));
                    isTreatment = true;
                    isDiagnosis = false;
                } else if (isDiagnosis) {
                    diagnosis.append("\n").append(line);
                } else if (isTreatment) {
                    treatment.append("\n").append(line);
                }
            }

            tehsis.setText(diagnosis.toString());
            tedavi.setText(treatment.toString());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Dosya okuma hatası: " + ex.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void saveMedicalInfo(ActionEvent e) {
        String diagnosis = tehsis.getText().trim();
        String treatment = tedavi.getText().trim();
        
        if (diagnosis.isEmpty() || treatment.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Lütfen teşhis ve tedavi bilgilerini doldurunuz!", 
                "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Hasta TC: " + hastaTcNo);
            writer.newLine();
            writer.write("Teşhis: " + diagnosis);
            writer.newLine();
            writer.write("Tedavi: " + treatment);
            writer.newLine();
            
            JOptionPane.showMessageDialog(this, 
                "Hastalık bilgileri başarıyla kaydedildi!\nDosya: " + fileName, 
                "Başarılı", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, 
                "Dosya yazma hatası: " + ex.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
}