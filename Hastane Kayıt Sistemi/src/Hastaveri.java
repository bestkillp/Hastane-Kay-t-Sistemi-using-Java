import java.io.*;
import java.time.LocalDateTime;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Hastaveri {
    private static final String DATA_FILE = "hastalar.txt";

    // Hasta kaydetme metodu
    public static void savePatient(Hasta patient) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            writer.write(patient.getName() + "," + patient.getSurname() + "," + 
                        patient.getIdNumber() + "," + patient.getAge() + "," + 
                        patient.getPhone() + "," + patient.getSex() + "," +
                        patient.getRegistrationTime());
            writer.newLine();
        }
    }

    // Tüm hastaları getirme metodu
    public static List<Hasta> getAllPatients() throws IOException {
        List<Hasta> patients = new ArrayList<>();
        File file = new File(DATA_FILE);
        
        if (!file.exists()) {
            return patients;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 6) {
                    Hasta patient = new Hasta(
                        parts[0], parts[1], Long.parseLong(parts[2]), 
                        Integer.parseInt(parts[3]), Long.parseLong(parts[4]), parts[5]);
                    
                    // Set registration time if it exists in the file
                    if (parts.length == 7) {
                        patient.registrationTime = LocalDateTime.parse(parts[6]);
                    }
                    
                    patients.add(patient);
                }
            }
        }
        return patients;
    }

    // TC'ye göre hasta arama
    public static Hasta findPatientByTc(int tcNo) throws IOException {
        List<Hasta> patients = getAllPatients();
        for (Hasta patient : patients) {
            if (patient.idNumber==tcNo) {
                return patient;
            }
        }
        return null;
    }

    // Hasta silme metodu
    public static boolean deletePatient(long tcNo) throws IOException {
        List<Hasta> patients = getAllPatients();
        boolean removed = patients.removeIf(p -> p.getIdNumber() == tcNo);
        
        if (removed) {
            // Delete medical info file first
            deleteMedicalInfoFile(tcNo);
            
            // Dosyayı yeniden yaz
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
                for (Hasta patient : patients) {
                    writer.write(patient.getName() + "," + patient.getSurname() + "," + 
                               patient.getIdNumber() + "," + patient.getAge() + "," + 
                               patient.getPhone() + "," + patient.getSex() + "," +
                               patient.getRegistrationTime());
                    writer.newLine();
                }
            }
        }
        return removed;
    }

private static void deleteMedicalInfoFile(long tcNo) {
    String fileName = "hasta_" + tcNo + "_bilgi.txt";
    try {
        Files.deleteIfExists(Paths.get(fileName));
    } catch (IOException e) {
        System.err.println("Medical info file could not be deleted: " + e.getMessage());
        // We'll continue even if this fails, as the main patient record is more important
    }
}}