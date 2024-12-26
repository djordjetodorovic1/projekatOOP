package SistemZaPlaniranjeProslava.Model;

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

    public Objekat(int id, Vlasnik vlasnik, String naziv, double cijena_rezervacije, String grad, String adresa, int broj_mijesta, int broj_stolova, String datumi, double zarada, StatusObjekta statusObjekta) {
        this.id = id;
        this.vlasnik = vlasnik;
        this.naziv = naziv;
        this.cijena_rezervacije = cijena_rezervacije;
        this.grad = grad;
        this.adresa = adresa;
        this.broj_mijesta = broj_mijesta;
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

    public void setCijena_rezervacije(double cijena_rezervacije) {
        this.cijena_rezervacije = cijena_rezervacije;
    }

    public void setGrad(String grad) {
        this.grad = grad;
    }

    public void setAdresa(String adresa) {
        this.adresa = adresa;
    }

    public void setBroj_mijesta(int broj_mijesta) {
        this.broj_mijesta = broj_mijesta;
    }

    public void setBroj_stolova(int broj_stolova) {
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
                ", vlasnik=" + vlasnik.getKorisnicko_ime() +
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