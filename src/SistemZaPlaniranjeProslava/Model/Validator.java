package SistemZaPlaniranjeProslava.Model;

import SistemZaPlaniranjeProslava.Main;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    private static boolean validacijaIme(String ime) {
        Pattern pattern = Pattern.compile("^[A-Z][a-zA-Z]+(\\s?[a-zA-Z]+)*");
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

    public static boolean provjeriNoviNalog(TextField tfIme, TextField tfPrezime, TextField tfJMBG, TextField tfBrojUBanci, TextField tfKorisnickoIme, PasswordField pfLozinka,
                                            PasswordField pfPotvrdaLozinke, ComboBox<String> cbTipNaloga, Map<String, BankovniRacun> bankovniRacuni, Map<String, Klijent> klijenti, Map<String, Vlasnik> vlasnici) {
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
}