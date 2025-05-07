import javax.swing.*;
import java.time.format.DateTimeFormatter;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class HastaListe extends JFrame {
    private DefaultTableModel model;
    private JTable table;

    public HastaListe() {
        initUI();
    }

    private void initUI() {
        setTitle("Hasta Kayıt - Hasta Listesi");
        setSize(900, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        Color lightGreen = new Color(138, 194, 127);
        Color lightRed = new Color(255,127,138);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(lightGreen);
        ImageIcon logo = new ImageIcon("C:\\Hastane Kayıt Sistemi\\hastanefoto.jpg"); // Logo dosya yolu
        Image img = logo.getImage().getScaledInstance(55, 55, Image.SCALE_SMOOTH); // Logo boyutlandırma
        logo = new ImageIcon(img);
        
        JLabel titleLabel = new JLabel("Kayıtlı Hastalar", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        titleLabel.setIcon(logo);

        // Tablo paneli
        JPanel tablePanel = new JPanel(new BorderLayout());
        
        model = new DefaultTableModel() {
            // Override kısmı
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model.addColumn("Ad");
        model.addColumn("Soyad");
        model.addColumn("T.C Kimlik NO");
        model.addColumn("Yaş");
        model.addColumn("Telefon");
        model.addColumn("Cinsiyet");
        model.addColumn("Kayıt Tarihi");

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(table);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // Buton paneli alt tarafa
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 7));
        
        JButton deleteButton = new JButton("Seçili Hastayı Sil");
        deleteButton.addActionListener(this::deleteSelectedPatient);
        buttonPanel.add(deleteButton);

        JButton refreshButton = new JButton("Listeyi Yenile");
        refreshButton.addActionListener(e -> refreshPatientList());
        buttonPanel.add(refreshButton);

        // Geri butonu 
        JButton backButton = new JButton("Geri Dön");
        backButton.addActionListener(e -> {
            this.dispose(); // Bu pencereyi kapat
            new Menu().setVisible(true); // Ana menüyü aç
        });
        buttonPanel.add(backButton);
        
         JButton hastaozellikBtn = new JButton("Hasta Detay");
        
        hastaozellikBtn.addActionListener(this::openselectedinfo);
        buttonPanel.add(hastaozellikBtn);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
        refreshPatientList();
    }

    private void refreshPatientList() {
        model.setRowCount(0);
        
        try {
            List<Hasta> patients = Hastaveri.getAllPatients();
            for (Hasta patient : patients) {
                model.addRow(new Object[]{
                    patient.getName(),
                    patient.getSurname(),
                    patient.getIdNumber(),
                    patient.getAge(),
                    patient.getPhone(),
                    patient.getSex(),
                    patient.getFormattedRegistrationTime()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Hasta listesi yüklenirken hata oluştu: " + e.getMessage(), 
                "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void openselectedinfo(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Lütfen Detay görmek istediğiniz hastayı seçiniz.", 
                "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long idNumber = (long) model.getValueAt(selectedRow, 2);
        String patientName = (String) model.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            patientName + " isimli hastayı seçmek istediğinize emin misiniz?",
            "Hasta Seçme Onayı", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            HastalikBilgi hastalikBilgi = new HastalikBilgi(idNumber);
            hastalikBilgi.setVisible(true);
        }
    }
    

	private void deleteSelectedPatient(ActionEvent e) {
        int selectedRow = table.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Lütfen silmek istediğiniz hastayı seçiniz.", 
                "Uyarı", JOptionPane.WARNING_MESSAGE);
            return;
        }

        long idNumber = (long) model.getValueAt(selectedRow, 2);
        String patientName = (String) model.getValueAt(selectedRow, 0);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            patientName + " isimli hastayı silmek istediğinize emin misiniz?",
            "Hasta Silme Onayı", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                if (Hastaveri.deletePatient(idNumber)) {
                    JOptionPane.showMessageDialog(this, 
                        "Hasta başarıyla silindi.", 
                        "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                    refreshPatientList();
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Hasta silinirken bir hata oluştu.", 
                        "Hata", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, 
                    "Hata: " + ex.getMessage(), 
                    "Hata", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}