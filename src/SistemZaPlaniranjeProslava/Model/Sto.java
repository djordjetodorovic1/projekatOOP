package SistemZaPlaniranjeProslava.Model;

import java.util.Comparator;

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

    public Objekat getObjekat() {
        return objekat;
    }

    public int getBrojMjesta() {
        return broj_mjesta;
    }

    public final static Comparator<Sto> porediPoStolovima = (sto1, sto2) -> Integer.compare(sto1.getId(), sto2.getId());

    @Override
    public String toString() {
        return "Sto " + id;
    }
}