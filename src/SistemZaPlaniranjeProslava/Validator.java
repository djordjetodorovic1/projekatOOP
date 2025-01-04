package SistemZaPlaniranjeProslava;

import SistemZaPlaniranjeProslava.Model.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    public synchronized static boolean provjeraRegIzraza(String string, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);
        return matcher.matches();
    }

    private static boolean validacijaIme(String ime) {
        return provjeraRegIzraza(ime, "^[A-Z][a-zA-Z]+([a-zA-Z\\s]+)*");
    }

    private static <T extends Korisnik> boolean validacijaJMBG(String jmbg, Map<String, T> korisnici) {
        if (provjeraRegIzraza(jmbg, "[0-9]{13}")) {
            for (T korisnik : korisnici.values())
                if (korisnik.getJmbg().equals(jmbg))
                    return false;
            return true;
        }
        return false;
    }

    private static boolean validacijaRacun(String brojRacuna, String jmbg, Map<String, BankovniRacun> bankovniRacuni) {
        return provjeraRegIzraza(brojRacuna, "[0-9]{16}") && bankovniRacuni.containsKey(brojRacuna) && bankovniRacuni.get(brojRacuna).getJmbg().equals(jmbg);
    }

    private static <T extends Korisnik> boolean validacijaKorisnickoIme(String korisnickoIme, Map<String, T> korisnici) {
        return provjeraRegIzraza(korisnickoIme, "^[a-zA-Z][a-zA-Z0-9_\\.]+") && !korisnici.containsKey(korisnickoIme);
    }

    public static boolean verifikujLozinku(String lozinka, String potvrdaLozinke) {
        return provjeraRegIzraza(lozinka, "^[a-zA-Z0-9][a-zA-Z0-9_\\.]+") && lozinka.equals(potvrdaLozinke);
    }

    public static boolean provjeriPrijavu(String korisnickoIme, String lozinka) {
        return provjeraRegIzraza(korisnickoIme, "^[a-zA-Z][a-zA-Z0-9_\\.]+") && provjeraRegIzraza(lozinka, "^[a-zA-Z0-9][a-zA-Z0-9_\\.]+");
    }

    private static boolean validacijaAdresa(String adresa) {
        return provjeraRegIzraza(adresa, "^[A-Z][a-zA-Z]+([a-zA-Z\\s]+)*(\\d+|bb|BB)?");
    }

    public static boolean validacijaIntBroj(TextField tfIntBroj) {
        try {
            int br = Integer.parseInt(tfIntBroj.getText());
            if (br > 0)
                return true;
            Main.ocistiPolje(tfIntBroj);
            return false;
        } catch (NumberFormatException e) {
            Main.ocistiPolje(tfIntBroj);
            return false;
        }
    }

    private static boolean validacijaDoubleBroj(TextField tfDoubleBroj) {
        try {
            double dbl = Double.parseDouble(tfDoubleBroj.getText());
            if (dbl > 0)
                return true;
            Main.ocistiPolje(tfDoubleBroj);
            return false;
        } catch (NumberFormatException e) {
            Main.ocistiPolje(tfDoubleBroj);
            return false;
        }
    }

    public static boolean validacijaMeni(TextField meni, TextField cijena) {
        boolean detektorGreske = false;
        if (meni.getText().length() > 45 || !provjeraRegIzraza(meni.getText(), "[a-zA-Z\\s]+")) {
            Main.ocistiPolje(meni);
            detektorGreske = true;
        }
        if (!validacijaDoubleBroj(cijena))
            detektorGreske = true;
        if (detektorGreske)
            Main.upozorenje("Nekorektan unos! Pokušajte ponovo");
        return !detektorGreske;
    }

    public static boolean provjeriNoviNalog(TextField tfIme, TextField tfPrezime, TextField tfJMBG, TextField tfBrojUBanci,
                                            TextField tfKorisnickoIme, PasswordField pfLozinka, PasswordField pfPotvrdaLozinke, ChoiceBox<String> cbTipNaloga,
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

    public static boolean provjeraObjektaZaUnos(TextField tfGrad, TextField tfAdresa,
                                                TextField tfCijenaRezervacije, TextField tfBrojMjesta, TextField tfBrojStolova) {
        boolean detektorGreske = false;
        if (!validacijaIme(tfGrad.getText())) {
            Main.ocistiPolje(tfGrad);
            detektorGreske = true;
        }
        if (!validacijaAdresa(tfAdresa.getText())) {
            Main.ocistiPolje(tfAdresa);
            detektorGreske = true;
        }
        if (!validacijaDoubleBroj(tfCijenaRezervacije))
            detektorGreske = true;
        if (!validacijaIntBroj(tfBrojMjesta))
            detektorGreske = true;
        if (!validacijaIntBroj(tfBrojStolova))
            detektorGreske = true;
        return !detektorGreske;
    }
}