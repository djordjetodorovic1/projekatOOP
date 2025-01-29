package SistemZaPlaniranjeProslava.Model;

public class Admin extends Osoba {
    public Admin(int id, String ime, String prezime, String korisnicko_ime, String lozinka) {
        super(id, ime, prezime, korisnicko_ime, lozinka);
    }

    @Override
    public String toString() {
        return "Admin: " + super.toString();
    }
}