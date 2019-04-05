package indonesia.ima.com.el_contact.json;

public class Group {

    String id_group;
    String nama_group;
    String gambar_group;

    public Group(String id_group, String nama_group, String gambar_group) {
        this.id_group = id_group;
        this.nama_group = nama_group;
        this.gambar_group = gambar_group;
    }

    public String getId_group() {
        return id_group;
    }

    public String getNama_group() {
        return nama_group;
    }

    public String getGambar_group() {
        return gambar_group;
    }
}
