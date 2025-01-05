package SistemZaPlaniranjeProslava.Model;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Main;

import java.util.Map;

public class Objekat implements Comparable<Objekat> {
    private int id;
    private Vlasnik vlasnik;
    private String naziv;
    private double cijena_rezervacije;
    private String grad;
    private String adresa;
    private int broj_mjesta;
    private int broj_stolova;
    private double zarada;
    private StatusObjekta status;

    public Objekat(int id, Vlasnik vlasnik, String naziv, double cijena_rezervacije, String grad, String adresa, int broj_mjesta, int broj_stolova, double zarada, StatusObjekta statusObjekta) {
        this.id = id;
        this.vlasnik = vlasnik;
        this.naziv = naziv;
        this.cijena_rezervacije = cijena_rezervacije;
        this.grad = grad;
        this.adresa = adresa;
        this.broj_mjesta = broj_mjesta;
        this.broj_stolova = broj_stolova;
        this.zarada = zarada;
        this.status = statusObjekta;
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

    public double getZarada() {
        return zarada;
    }

    public void setZarada(double zarada) {
        this.zarada = zarada;
    }

    public StatusObjekta getStatus() {
        return status;
    }

    private static boolean provjeraCijeneMenijaZaOdobrenje(Objekat objekat) {
        Map<Integer, Meni> meniji = Controller.getMeni();
        int brojMenija = 0;
        int brojMenijaObjekta = 0;
        double ukupnaCijenaSvihMenija = 0.0;
        double ukupnaCijenaMenijaObjekta = 0.0;

        for (Meni meni : meniji.values()) {
            if (meni.getObjekat().getId() == objekat.getId()) {
                brojMenijaObjekta++;
                ukupnaCijenaMenijaObjekta += meni.getCijenaPoOsobi();
            } else {
                brojMenija++;
                ukupnaCijenaSvihMenija += meni.getCijenaPoOsobi();
            }
        }
        if (brojMenija == 0)
            return true;
        double prosjecnaCijenaSvihMenija = ukupnaCijenaSvihMenija / brojMenija;
        double prosjecnaCijenaMenijaObjekta = ukupnaCijenaMenijaObjekta / brojMenijaObjekta;
        return Math.floor(prosjecnaCijenaMenijaObjekta / prosjecnaCijenaSvihMenija) < 10;
    }

    private static boolean provjeraMjestaZaOdobrenje(Objekat objekat) {
        Map<Integer, Sto> stolovi = Controller.getStolovi();
        int sumaMjesta = 0;
        for (Sto sto : stolovi.values())
            if (sto.getObjekat().getId() == objekat.getId())
                sumaMjesta += sto.getBrojMjesta();
        return sumaMjesta == objekat.getBrojMjesta();
    }

    public static void provjeriObjekatZaOdobrenje(Objekat objekat) {
        boolean detektorGreske = false;
        if (!provjeraCijeneMenijaZaOdobrenje(objekat)) {
            Main.upozorenje("Cijene menija nisu uskladjene sa cijenama u ostalim objektima!");
            detektorGreske = true;
        }
        if (!provjeraMjestaZaOdobrenje(objekat)) {
            Main.upozorenje("Maksimalan broj mjesta u salonu se ne podudara sa ukupnim brojem mjesta svih stolova!");
            detektorGreske = true;
        }
        if (!detektorGreske)
            Main.informacija("Objekat zadovoljava sve uslove!");
    }

    public boolean equals(Objekat o) {
        return this.getId() == o.getId();
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