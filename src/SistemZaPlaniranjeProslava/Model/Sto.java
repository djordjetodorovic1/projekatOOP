package SistemZaPlaniranjeProslava.Model;

public class Sto {
    private int id;
    private Objekat objekat;
    private int broj_mjesta;

    public Sto(int id, Objekat objekat, int broj_mjesta) {
        this.id = id;
        this.objekat = objekat;
        this.broj_mjesta = broj_mjesta;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public void setObjekat(Objekat objekat) {
        this.objekat = objekat;
    }

    public int getBrojMjesta() {
        return broj_mjesta;
    }

    public void setBrojMjesta(int broj_mjesta) {
        this.broj_mjesta = broj_mjesta;
    }

    @Override
    public String toString() {
        return "Sto{" +
                "id=" + id +
                ", objekat=" + objekat.getId() + " " + objekat.getNaziv() +
                ", broj_mjesta=" + broj_mjesta +
                '}';
    }
}