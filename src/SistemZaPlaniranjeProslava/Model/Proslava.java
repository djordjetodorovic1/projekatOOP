package SistemZaPlaniranjeProslava.Model;

import java.time.LocalDate;

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
        if (datum.isAfter(danas))
            this.status = StatusProslave.AKTIVNA;
        else if (datum.isBefore(danas))
            this.status = StatusProslave.PROTEKLA;
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

    public Klijent getKlijent() {
        return klijent;
    }

    public void setKlijent(Klijent klijent) {
        this.klijent = klijent;
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

    public void setDatum(LocalDate datum) {
        this.datum = datum;
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

    public String getPotpunaUplata() {
        if (this.ukupna_cijena != 0 && this.ukupna_cijena == this.uplacen_iznos)
            return "DA";
        return "NE";
    }

    @Override
    public String toString() {
        return "Proslava{" +
                "id=" + id +
                ", objekat=" + objekat +
                ", klijent=" + klijent +
                ", meni=" + meni +
                ", datum=" + datum +
                ", broj_gostiju=" + broj_gostiju +
                ", ukupna_cijena=" + ukupna_cijena +
                ", uplacen_iznos=" + uplacen_iznos +
                ", status=" + status +
                '}';
    }
}