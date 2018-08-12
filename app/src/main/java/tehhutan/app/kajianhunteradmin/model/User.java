package tehhutan.app.kajianhunteradmin.model;

import java.io.Serializable;

/**
 * Created by tehhutan on 27/09/17.
 */

public class User implements Serializable {

    private String Email;
    private String Nama;
    private String Password;
    private String Photo;
    private String Wa;
    public User() {
    }

    public User(String email, String nama, String password, String photo, String wa) {
        Email = email;
        Nama = nama;
        Password = password;
        Photo = photo;
        Wa = wa;
    }

    public String getWa() {
        return Wa;
    }

    public void setWa(String wa) {
        Wa = wa;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
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
