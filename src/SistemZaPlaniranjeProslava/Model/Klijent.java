package SistemZaPlaniranjeProslava.Model;

public class Klijent extends Korisnik {
    public Klijent(int id, String ime, String prezime, String jmbg, String broj_racuna, String korisnicko_ime, String lozinka) {
        super(id, ime, prezime, jmbg, broj_racuna, korisnicko_ime, lozinka);
    }

    @Override
    public String toString() {
        return "Klijent: " + super.toString();
    }
}