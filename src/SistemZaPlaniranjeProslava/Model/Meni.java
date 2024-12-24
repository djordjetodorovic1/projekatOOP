package SistemZaPlaniranjeProslava.Model;

import SistemZaPlaniranjeProslava.Controller;

public class Meni {
    private int id;
    private Objekat objekat;
    private String opis;
    private double cijena_po_osobi;

    public Meni(int id, Objekat objekat, String opis, double cijena_po_osobi) {
        this.id = id;
        this.objekat = objekat;
        this.opis = opis;
        this.cijena_po_osobi = cijena_po_osobi;
    }

    public Meni(int id, int objekatID, String opis, double cijena_po_osobi) {
        this.id = id;
        this.objekat = Controller.getObjekat(objekatID);
        this.opis = opis;
        this.cijena_po_osobi = cijena_po_osobi;
    }

    @Override
    public String toString() {
        return "Meni{" +
                "id=" + id +
                ", objekat=" + objekat +
                ", opis='" + opis + '\'' +
                ", cijena_po_osobi=" + cijena_po_osobi +
                '}';
    }
}