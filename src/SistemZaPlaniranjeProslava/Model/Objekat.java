package SistemZaPlaniranjeProslava.Model;

public class Objekat implements Comparable<Objekat> {
    private int id;
    private Vlasnik vlasnik;
    private String naziv;
    private double cijena_rezervacije;
    private String grad;
    private String adresa;
    private int broj_mjesta;
    private int broj_stolova;
    private String datumi;
    private double zarada;
    private StatusObjekta status;

    public Objekat(int id, Vlasnik vlasnik, String naziv, double cijena_rezervacije, String grad, String adresa, int broj_mjesta, int broj_stolova, String datumi, double zarada, StatusObjekta statusObjekta) {
        this.id = id;
        this.vlasnik = vlasnik;
        this.naziv = naziv;
        this.cijena_rezervacije = cijena_rezervacije;
        this.grad = grad;
        this.adresa = adresa;
        this.broj_mjesta = broj_mjesta;
        this.broj_stolova = broj_stolova;
        this.datumi = datumi;
        this.zarada = zarada;
        this.status = statusObjekta;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVlasnik(Vlasnik vlasnik) {
        this.vlasnik = vlasnik;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setCijenaRezervacije(double cijena_rezervacije) {
        this.cijena_rezervacije = cijena_rezervacije;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public void setBrojMjesta(int broj_mjesta) {
        this.broj_mjesta = broj_mjesta;
    }

    public void setBrojStolova(int broj_stolova) {
        this.broj_stolova = broj_stolova;
    }

    public void setDatumi(String datumi) {
        this.datumi = datumi;
    }

    public void setZarada(double zarada) {
        this.zarada = zarada;
    }

    public void setStatus(StatusObjekta status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public Vlasnik getVlasnik() {
        return vlasnik;
    }

    public String getNaziv() {
        return naziv;
    }

    public double getCijenaRezervacije() {
        return cijena_rezervacije;
    }

    public String getGrad() {
        return grad;
    }

    public String getAdresa() {
        return adresa;
    }

    public int getBrojMjesta() {
        return broj_mjesta;
    }

    public int getBrojStolova() {
        return broj_stolova;
    }

    public String getDatumi() {
        return datumi;
    }

    public double getZarada() {
        return zarada;
    }

    public StatusObjekta getStatus() {
        return status;
    }

    @Override
    public int compareTo(Objekat o) {
        return this.getNaziv().compareTo(o.getNaziv());
    }

    @Override
    public String toString() {
        return naziv + " - " + cijena_rezervacije + " KM";
    }
}