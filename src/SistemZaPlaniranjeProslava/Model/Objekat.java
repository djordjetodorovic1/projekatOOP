package SistemZaPlaniranjeProslava.Model;

import SistemZaPlaniranjeProslava.Controller;

public class Objekat {
    private int id;
    private Vlasnik vlasnik;
    private String naziv;
    private double cijena_rezervacije;
    private String grad;
    private String adresa;
    private int broj_mijesta;
    private int broj_stolova;
    private String datumi;
    private double zarada;
    private StatusObjekta status;

    public Objekat(int id, int vlasnikID, String naziv, double cijena_rezervacije, String grad, String adresa, int broj_mijesta, int broj_stolova, String datumi, double zarada, String statusObjekta) {
        this.id = id;
        this.vlasnik = Controller.getVlasnik(vlasnikID);
        this.naziv = naziv;
        this.cijena_rezervacije = cijena_rezervacije;
        this.grad = grad;
        this.adresa = adresa;
        this.broj_mijesta = broj_mijesta;
        this.broj_stolova = broj_stolova;
        this.datumi = datumi;
        this.zarada = zarada;
        switch (statusObjekta) {
            case ("NA_CEKANJU"):
                this.status = StatusObjekta.NA_CEKANJU;
                break;
            case ("ODOBREN"):
                this.status = StatusObjekta.ODOBREN;
                break;
            case ("ODBIJEN"):
                this.status = StatusObjekta.ODBIJEN;
                break;
            default:
                this.status = StatusObjekta.NA_CEKANJU;
        }
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

    public double getCijena_rezervacije() {
        return cijena_rezervacije;
    }

    public String getGrad() {
        return grad;
    }

    public String getAdresa() {
        return adresa;
    }

    public int getBroj_mijesta() {
        return broj_mijesta;
    }

    public int getBroj_stolova() {
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
    public String toString() {
        return "Objekat{" +
                "id=" + id +
                ", vlasnik=" + vlasnik +
                ", naziv='" + naziv + '\'' +
                ", cijena_rezervacije=" + cijena_rezervacije +
                ", grad='" + grad + '\'' +
                ", adresa='" + adresa + '\'' +
                ", broj_mijesta=" + broj_mijesta +
                ", broj_stolova=" + broj_stolova +
                ", datumi='" + datumi + '\'' +
                ", zarada=" + zarada +
                ", status=" + status +
                '}';
    }
}