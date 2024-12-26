package SistemZaPlaniranjeProslava;

import SistemZaPlaniranjeProslava.Model.BankovniRacun;
import SistemZaPlaniranjeProslava.Model.Klijent;
import SistemZaPlaniranjeProslava.Model.Korisnik;
import SistemZaPlaniranjeProslava.Model.Vlasnik;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static boolean validacijaIme(String ime) {
        Pattern pattern = Pattern.compile("^[A-Z][a-zA-Z]+([a-zA-Z\\s]+)*");
        Matcher matcher = pattern.matcher(ime);
        return matcher.matches();
    }

    private static <T extends Korisnik> boolean validacijaJMBG(String jmbg, Map<String, T> korisnici) {
        Pattern pattern = Pattern.compile("[0-9]{13}");
        Matcher matcher = pattern.matcher(jmbg);

        if (matcher.matches()) {
            for (T korisnik : korisnici.values())
                if (korisnik.getJmbg().equals(jmbg))
                    return false;
            return true;
        }
        return false;
    }

    private static boolean validacijaRacun(String brojRacuna, String jmbg, Map<String, BankovniRacun> bankovniRacuni) {
        Pattern pattern = Pattern.compile("[0-9]{16}");
        Matcher matcher = pattern.matcher(brojRacuna);
        return matcher.matches() && bankovniRacuni.containsKey(brojRacuna) && bankovniRacuni.get(brojRacuna).getJmbg().equals(jmbg);
    }

    private static <T extends Korisnik> boolean validacijaKorisnickoIme(String korisnickoIme, Map<String, T> korisnici) {
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_\\.]+");
        Matcher matcher = pattern.matcher(korisnickoIme);
        return matcher.matches() && !korisnici.containsKey(korisnickoIme);
    }

    public static boolean verifikujLozinku(String lozinka, String potvrdaLozinke) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\.]+");
        Matcher matcher = pattern.matcher(lozinka);
        return matcher.matches() && lozinka.equals(potvrdaLozinke);
    }

    public static boolean provjeriPrijavu(TextField tfKorisnickoIme, PasswordField pfLozinka) {
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_\\.]+");
        Matcher matcherK = pattern.matcher(tfKorisnickoIme.getText());
        pattern = Pattern.compile("^[a-zA-Z0-9][a-zA-Z0-9_\\.]+");
        Matcher matcherP = pattern.matcher(pfLozinka.getText());
        return matcherK.matches() && matcherP.matches();
    }

    private static boolean validacijaAdresa(String adresa) {
        Pattern pattern = Pattern.compile("^[A-Z][a-zA-Z]+([a-zA-Z\\s]+)*(\\d|bb|BB)?");
        Matcher matcher = pattern.matcher(adresa);
        return matcher.matches();
    }

    public static boolean validacijaBroj(TextField tfBrojStolova) {
        try {
            Integer.parseInt(tfBrojStolova.getText());
            return true;
        } catch (NumberFormatException e) {
            Main.ocistiPolje(tfBrojStolova);
            return false;
        }
    }

    public static boolean validacijaMeni(TextField meni, TextField cijena) {
        boolean detektorGreske = false;
        Pattern pattern = Pattern.compile("[a-zA-Z\\s]+");
        Matcher matcher = pattern.matcher(meni.getText());
        if (!matcher.matches() || meni.getText().length() > 45) {
            Main.ocistiPolje(meni);
            detektorGreske = true;
        }

        try {
            Double.parseDouble(cijena.getText());
        } catch (NumberFormatException e) {
            Main.ocistiPolje(cijena);
            detektorGreske = true;
        }

        if (detektorGreske)
            Main.upozorenje("Nekorektan unos! Pokusajte ponovo");
        return !detektorGreske;
    }

    public static boolean provjeriNoviNalog(TextField tfIme, TextField tfPrezime, TextField tfJMBG, TextField tfBrojUBanci,
                                            TextField tfKorisnickoIme, PasswordField pfLozinka, PasswordField pfPotvrdaLozinke, ComboBox<String> cbTipNaloga,
                                            Map<String, BankovniRacun> bankovniRacuni, Map<String, Klijent> klijenti, Map<String, Vlasnik> vlasnici) {
        boolean detektorGreske = false;
        if (!validacijaIme(tfIme.getText())) {
            Main.ocistiPolje(tfIme);
            detektorGreske = true;
        }
        if (!validacijaIme(tfPrezime.getText())) {
            Main.ocistiPolje(tfPrezime);
            detektorGreske = true;
        }
        if (!validacijaRacun(tfBrojUBanci.getText(), tfJMBG.getText(), bankovniRacuni)) {
            Main.ocistiPolje(tfBrojUBanci);
            detektorGreske = true;
        }
        if (cbTipNaloga.getValue().equals("Klijent")) {
            if (!validacijaKorisnickoIme(tfKorisnickoIme.getText(), klijenti)) {
                Main.ocistiPolje(tfKorisnickoIme);
                detektorGreske = true;
            }
            if (!validacijaJMBG(tfJMBG.getText(), klijenti)) {
                Main.ocistiPolje(tfJMBG);
                detektorGreske = true;
            }
        }
        if (cbTipNaloga.getValue().equals("Vlasnik")) {
            if (!validacijaKorisnickoIme(tfKorisnickoIme.getText(), vlasnici)) {
                Main.ocistiPolje(tfKorisnickoIme);
                detektorGreske = true;
            }
            if (!validacijaJMBG(tfJMBG.getText(), vlasnici)) {
                Main.ocistiPolje(tfJMBG);
                detektorGreske = true;
            }
        }
        if (!verifikujLozinku(pfLozinka.getText(), pfPotvrdaLozinke.getText())) {
            Main.ocistiPolje(pfLozinka);
            Main.ocistiPolje(pfPotvrdaLozinke);
            detektorGreske = true;
        }
        return !detektorGreske;
    }

    public static boolean provjeraObjektaZaUnos(TextField tfNaziv, TextField tfGrad, TextField tfAdresa,
                                                TextField tfCijenaRezervacije, TextField tfBrojMijesta, TextField tfBrojStolova) {
        boolean detektorGreske = false;
        if (!validacijaIme(tfGrad.getText())) {
            Main.ocistiPolje(tfGrad);
            detektorGreske = true;
        }
        if (!validacijaAdresa(tfAdresa.getText())) {
            Main.ocistiPolje(tfAdresa);
            detektorGreske = true;
        }

        try {
            Double.parseDouble(tfCijenaRezervacije.getText());
        } catch (NumberFormatException e) {
            Main.ocistiPolje(tfCijenaRezervacije);
            detektorGreske = true;
        }

        try {
            Double.parseDouble(tfBrojMijesta.getText());
        } catch (NumberFormatException e) {
            Main.ocistiPolje(tfBrojMijesta);
            detektorGreske = true;
        }

        if (!validacijaBroj(tfBrojStolova))
            detektorGreske = true;

        return !detektorGreske;
    }
}