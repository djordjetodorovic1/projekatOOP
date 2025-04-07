package SistemZaPlaniranjeProslava.Model;

public class Obavjestenje implements Comparable<Obavjestenje> {
    private int id;
    private Objekat objekat;
    private String tekst;

    public Obavjestenje(int id, Objekat objekat, String tekst) {
        this.id = id;
        this.objekat = objekat;
        this.tekst = tekst;
    }

    public int getId() {
        return id;
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public String getTekst() {
        return tekst;
    }

    @Override
    public int compareTo(Obavjestenje o) {
        return this.getObjekat().getStatus().compareTo(o.getObjekat().getStatus());
    }

    @Override
    public String toString() {
        return "\"" + objekat.getNaziv() + "\" " + tekst;
    }
}