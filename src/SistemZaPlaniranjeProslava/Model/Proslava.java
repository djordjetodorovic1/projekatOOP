package SistemZaPlaniranjeProslava.Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Proslava {
    private int id;
    private Objekat objekat;
    private Klijent klijent;
    private Meni meni;
    private LocalDate datum;
    private int broj_gostiju;
    private double ukupna_cijena;
    private double uplacen_iznos;
    private StatusProslave status;

    public Proslava(int id, Objekat objekat, Klijent klijent, LocalDate datum) {
        init(id, objekat, klijent, datum);
    }

    public Proslava(int id, Objekat objekat, Klijent klijent, Meni meni, LocalDate datum, int broj_gostiju, double ukupna_cijena, double uplacen_iznos) {
        init(id, objekat, klijent, datum);
        this.meni = meni;
        this.broj_gostiju = broj_gostiju;
        this.ukupna_cijena = ukupna_cijena;
        this.uplacen_iznos = uplacen_iznos;
    }

    private void init(int id, Objekat objekat, Klijent klijent, LocalDate datum) {
        this.id = id;
        this.objekat = objekat;
        this.klijent = klijent;
        this.datum = datum;

        LocalDate danas = LocalDate.now();
        if (datum.equals(LocalDate.of(1980, 1, 1)))
            this.status = StatusProslave.OTKAZANA;
        else if (datum.isBefore(danas))
            this.status = StatusProslave.PROTEKLA;
        else
            this.status = StatusProslave.AKTIVNA;
    }

    public int getId() {
        return id;
    }

    public Objekat getObjekat() {
        return objekat;
    }

    public Klijent getKlijent() {
        return klijent;
    }

    public Meni getMeni() {
        return meni;
    }

    public void setMeni(Meni meni) {
        this.meni = meni;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public int getBrojGostiju() {
        return broj_gostiju;
    }

    public void setBrojGostiju(int broj_gostiju) {
        this.broj_gostiju = broj_gostiju;
    }

    public double getUkupnaCijena() {
        return ukupna_cijena;
    }

    public void setUkupnaCijena(double ukupna_cijena) {
        this.ukupna_cijena = ukupna_cijena;
    }

    public double getUplacenIznos() {
        return uplacen_iznos;
    }

    public void setUplacenIznos(double uplacen_iznos) {
        this.uplacen_iznos = uplacen_iznos;
    }

    public StatusProslave getStatus() {
        return status;
    }

    public void setStatus(StatusProslave status) {
        this.status = status;
    }

    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    public boolean provjeraDatumZaUredjivanje() {
        LocalDate danas = LocalDate.now();
        long daniDoProslave = ChronoUnit.DAYS.between(danas, this.datum);
        return daniDoProslave > 3;
    }

    public boolean getPotpunaUplata() {
        return this.ukupna_cijena != 0 && this.ukupna_cijena == this.uplacen_iznos;
    }

    @Override
    public String toString() {
        String ispis = "klijent=" + klijent.getKorisnickoIme();
        if (!datum.equals(LocalDate.of(1980, 1, 1)))
            ispis += ", datum=" + datum;
        return ispis;
    }
}