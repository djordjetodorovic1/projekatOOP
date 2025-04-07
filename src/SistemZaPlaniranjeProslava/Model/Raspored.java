package SistemZaPlaniranjeProslava.Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class Raspored {
    private Sto sto;
    private Proslava proslava;
    private ArrayList<String> gosti = new ArrayList<>();

    public Raspored(Sto sto, Proslava proslava, String gosti) {
        this.sto = sto;
        this.proslava = proslava;
        if (!gosti.isEmpty())
            this.gosti = new ArrayList<>(Arrays.asList(gosti.split(", ", -1)));
    }

    public Raspored(Sto sto, Proslava proslava, ArrayList<String> gosti) {
        this.sto = sto;
        this.proslava = proslava;
        this.gosti.addAll(gosti);
    }

    public Sto getSto() {
        return sto;
    }

    public Proslava getProslava() {
        return proslava;
    }

    public ArrayList<String> getGosti() {
        return gosti;
    }

    public void setGosti(ArrayList<String> gosti) {
        this.gosti = gosti;
    }

    public final static Comparator<String> porediPoImenu = String::compareTo;
    public final static Comparator<Sto> porediPoStolovima = (sto1, sto2) -> Integer.compare(sto1.getId(), sto2.getId());

    @Override
    public String toString() {
        return "Raspored{" + sto + ", proslava=" + proslava + ", gosti='" + gosti + "}";
    }
}