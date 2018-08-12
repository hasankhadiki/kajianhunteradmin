package tehhutan.app.kajianhunteradmin.model;

/**
 * Created by tehhutan on 02/10/17.
 */

public class BookingList {
    private String Departemen;
    private String Kegiatan;
    private String Nama;
    private String JamMulai;
    private String JamAkhir;

    private String LinkTempat;
    private Double Latitude;
    private Double Longitude;

    public BookingList() {
    }


    public BookingList(String departemen, String kegiatan, String nama, String jamMulai, String jamAkhir, String linkTempat, Double latitude, Double longitude) {
        Departemen = departemen;
        Kegiatan = kegiatan;
        Nama = nama;
        JamMulai = jamMulai;
        JamAkhir = jamAkhir;
        LinkTempat = linkTempat;
        Latitude = latitude;
        Longitude = longitude;
    }

    public String getDepartemen() {
        return Departemen;
    }

    public void setDepartemen(String departemen) {
        Departemen = departemen;
    }

    public String getKegiatan() {
        return Kegiatan;
    }

    public void setKegiatan(String kegiatan) {
        Kegiatan = kegiatan;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getJamMulai() {
        return JamMulai;
    }

    public void setJamMulai(String jamMulai) {
        JamMulai = jamMulai;
    }

    public String getJamAkhir() {
        return JamAkhir;
    }

    public void setJamAkhir(String jamAkhir) {
        JamAkhir = jamAkhir;
    }

    public String getLinkTempat() {
        return LinkTempat;
    }

    public void setLinkTempat(String linkTempat) {
        LinkTempat = linkTempat;
    }

    public Double getLatitude() {
        return Latitude;
    }

    public void setLatitude(Double latitude) {
        Latitude = latitude;
    }

    public Double getLongitude() {
        return Longitude;
    }

    public void setLongitude(Double longitude) {
        Longitude = longitude;
    }
}
