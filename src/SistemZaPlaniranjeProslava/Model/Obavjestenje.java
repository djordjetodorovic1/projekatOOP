package SistemZaPlaniranjeProslava.Model;

import SistemZaPlaniranjeProslava.Controller;

public class Obavjestenje {
    private int id;
    private Objekat objekat;
    private String tekst;

    public Obavjestenje(int id, Objekat objekat, String tekst) {
        this.id = id;
        this.objekat = objekat;
        this.tekst = tekst;
    }

    public Obavjestenje(int id, int objekatID, String tekst) {
        this.id = id;
        this.objekat = Controller.getObjekat(objekatID);
        this.tekst = tekst;
    }

    @Override
    public String toString() {
        return "Obavjestenje{" +
                "id=" + id +
                ", objekat=" + objekat +
                ", tekst='" + tekst + '\'' +
                '}';
    }
}