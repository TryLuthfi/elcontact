package indonesia.ima.com.el_contact.json;

public class Kontak {

    String id_kontak;
    String id_group;
    String nama_kontak;
    String no_hp;
    String status_show;

    public Kontak(String id_kontak, String id_group, String nama_kontak, String no_hp, String status_show) {
        this.id_kontak = id_kontak;
        this.id_group = id_group;
        this.nama_kontak = nama_kontak;
        this.no_hp = no_hp;
        this.status_show = status_show;
    }

    public String getId_kontak() {
        return id_kontak;
    }

    public String getId_group() {
        return id_group;
    }

    public String getNama_kontak() {
        return nama_kontak;
    }

    public String getNo_hp() {
        return no_hp;
    }

    public String getStatus_show() {
        return status_show;
    }
}
