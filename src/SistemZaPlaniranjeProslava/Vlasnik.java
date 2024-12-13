package SistemZaPlaniranjeProslava;

public class Vlasnik{
    private int id;
    private String ime;
    private String prezime;
    private String jmbg;
    private String broj_racuna;
    private String korisnicko_ime;
    private String lozinka;

    public Vlasnik(int id, String ime, String prezime, String jmbg, String broj_racuna, String korisnicko_ime, String lozinka) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.jmbg = jmbg;
        this.broj_racuna = broj_racuna;
        this.korisnicko_ime = korisnicko_ime;
        this.lozinka = lozinka;
    }

    @Override
    public String toString() {
        return "Vlasnik{" +
                "id=" + id +
                ", ime='" + ime + '\'' +
                ", prezime='" + prezime + '\'' +
                ", jmbg='" + jmbg + '\'' +
                ", broj_racuna='" + broj_racuna + '\'' +
                ", korisnicko_ime='" + korisnicko_ime + '\'' +
                ", lozinka='" + lozinka + '\'' +
                '}';
    }
}