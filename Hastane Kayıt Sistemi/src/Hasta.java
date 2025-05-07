import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Hasta {
    private String name;
    private String surname;
    long idNumber;
    private int age;
    long tel;
    String Sex;
    LocalDateTime registrationTime; // New field

    public Hasta(String name, String surname, long id, int age, long phone, String sex) {
        this.name = name;
        this.surname = surname;
        this.idNumber = id;
        this.age = age;
        this.tel = phone;
        this.Sex = sex;
        this.registrationTime = LocalDateTime.now(); // Set current time when creating patient
    }

    // Getter methods
    public String getName() { return name; }
    public String getSurname() { return surname; }
    public long getIdNumber() { return idNumber; }
    public int getAge() { return age; }
    public Long getPhone() { return tel; }
    public String getSex() { return Sex; }
    public LocalDateTime getRegistrationTime() { return registrationTime; }
    public String getFormattedRegistrationTime() {
        return registrationTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"));
    }

    // Override
    public String toString() {
        return name + " " + surname + " (" + idNumber +" " + Sex + " " + ")";
    }
}