package SistemZaPlaniranjeProslava.Model;

public class Sto {
    private int id;
    private Objekat objekat;
    private int broj_mijesta;

    public Sto(int id, Objekat objekat, int broj_mijesta) {
        this.id = id;
        this.objekat = objekat;
        this.broj_mijesta = broj_mijesta;
    }

    @Override
    public String toString() {
        return "Sto{" +
                "id=" + id +
                ", objekat=" + objekat.getId() + " " + objekat.getNaziv() +
                ", broj_mijesta=" + broj_mijesta +
                '}';
    }
}