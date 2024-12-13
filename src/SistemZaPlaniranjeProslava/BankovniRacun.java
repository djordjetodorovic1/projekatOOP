package SistemZaPlaniranjeProslava;

public class BankovniRacun {
    private int id;
    private String broj_racuna;
    private String jmbg;
    private double stanje;

    public BankovniRacun(int id, String broj_racuna, String jmbg, double stanje) {
        this.id = id;
        this.broj_racuna = broj_racuna;
        this.jmbg = jmbg;
        this.stanje = stanje;
    }

    @Override
    public String toString() {
        return "BankovniRacun{" +
                "id=" + id +
                ", broj_racuna='" + broj_racuna + '\'' +
                ", jmbg='" + jmbg + '\'' +
                ", stanje=" + stanje +
                '}';
    }
}