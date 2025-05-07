import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                new GirisEkrani().setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Sistem başlatılırken hata oluştu: " + e.getMessage(), 
                    "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}