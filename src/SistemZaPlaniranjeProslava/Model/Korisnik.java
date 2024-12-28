package SistemZaPlaniranjeProslava.Model;

public interface Korisnik {
    int getId();

    String getIme();

    String getPrezime();

    String getJmbg();

    String getBrojRacuna();

    String getKorisnickoIme();

    String getLozinka();

    void setLozinka(String novaLozinka);
}