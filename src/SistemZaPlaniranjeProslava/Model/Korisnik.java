package SistemZaPlaniranjeProslava.Model;

public interface Korisnik {
    int getId();

    String getIme();

    String getPrezime();

    String getJmbg();

    String getBroj_racuna();

    String getKorisnicko_ime();

    String getLozinka();

    void setLozinka(String novaLozinka);
}