package SistemZaPlaniranjeProslava.Model;

public class Korisnik extends Osoba {
    private String jmbg;
    private String broj_racuna;

    public Korisnik(int id, String ime, String prezime, String jmbg, String broj_racuna, String korisnicko_ime, String lozinka) {
        super(id, ime, prezime, korisnicko_ime, lozinka);
        this.jmbg = jmbg;
        this.broj_racuna = broj_racuna;
    }

    public String getJmbg() {
        return this.jmbg;
    }

    public String getBrojRacuna() {
        return this.broj_racuna;
    }

    @Override
    public String toString() {
        return super.toString() + ", jmbg=" + this.jmbg + ", broj_racuna=" + this.broj_racuna;
    }
}