package tehhutan.app.kajianhunteradmin.model;

/**
 * Created by tehhutan on 27/09/17.
 */

public class User {

    private String Email;
    private String Nama;
    private String Password;

    public User() {
    }


    public User(String email, String nama, String password) {
        Email = email;
        Nama = nama;
        Password = password;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }
}
