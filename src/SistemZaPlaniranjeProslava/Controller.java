package SistemZaPlaniranjeProslava;

import SistemZaPlaniranjeProslava.Model.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {
    private static Admin admin = null;
    private static Obavjestenje obavjestenje = null;
    private static Map<String, BankovniRacun> bankovniRacuni = new HashMap<>();
    private static Map<String, Vlasnik> vlasnici = new HashMap<>();
    private static Map<String, Klijent> klijenti = new HashMap<>();

    public static void connectDB() {
        Database.connectWithDB();
    }

    public static void disconnectDB() {
        Database.disconnectDB();
    }

    public static void ucitavanjeIzBaze() {
        try {
            admin = Database.ucitajAdmina();
            bankovniRacuni = Database.ucitajBankovneRacune();
            vlasnici = Database.ucitajVlasnike();
            klijenti = Database.ucitajKlijente();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ispisBaze() {
        System.out.println(admin);
        vlasnici.forEach((k, v) -> System.out.println(k + ": " + v));
        klijenti.forEach((k, v) -> System.out.println(k + ": " + v));
        bankovniRacuni.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    public static void prijava(TextField korisnickoIme, PasswordField lozinka) {
        boolean detektorGreske = false;
        if (korisnickoIme.getText().isEmpty() || lozinka.getText().isEmpty()) {
            Main.upozorenje("Niste popunili sva polja! Pokusajte ponovo");
            detektorGreske = true;
        } else if (admin.getKorisnicko_ime().equals(korisnickoIme.getText())) {
            if (admin.getLozinka().equals(lozinka.getText())) {
                //admin se ulogovao
            } else {
                Main.upozorenje("Pogresna lozinka! Pokusajte ponovo");
                detektorGreske = true;
            }
        } else if (klijenti.containsKey(korisnickoIme.getText())) {
            if (klijenti.get(korisnickoIme.getText()).getLozinka().equals(lozinka.getText())) {
                //klijent se ulogovao
            } else {
                Main.upozorenje("Pogresna lozinka! Pokusajte ponovo");
                detektorGreske = true;
            }
        } else if (vlasnici.containsKey(korisnickoIme.getText())) {
            if (vlasnici.get(korisnickoIme.getText()).getLozinka().equals(lozinka.getText())) {
                //vlasnik se ulogovao
            } else {
                Main.upozorenje("Pogresna lozinka! Pokusajte ponovo");
                detektorGreske = true;
            }
        } else {
            Main.upozorenje("Uneseno korisnicko ime ne postoji! Pokusajte ponovo ili kreirajte novi nalog");
            detektorGreske = true;
        }
        if (detektorGreske) {
            Main.ocistiPolje(korisnickoIme);
            Main.ocistiPolje(lozinka);
        }
    }

    private static boolean verifikujIme(String ime) {
        Pattern pattern = Pattern.compile("^[A-Z][a-zA-Z]+(\\s?[a-zA-Z]+)*");
        Matcher matcher = pattern.matcher(ime);
        return matcher.matches();
    }

    private static boolean verifikujJMBG(String jmbg) {
        Pattern pattern = Pattern.compile("[0-9]{13}");
        Matcher matcher = pattern.matcher(jmbg);
        return matcher.matches();
    }

    private static boolean verifikujRacun(String brojRacuna, String jmbg) {
        Pattern pattern = Pattern.compile("[0-9]{16}");
        Matcher matcher = pattern.matcher(brojRacuna);
        return matcher.matches() && bankovniRacuni.containsKey(brojRacuna) && bankovniRacuni.get(brojRacuna).getJmbg().equals(jmbg);
    }

    private static <T> boolean verifikujKorisnickoIme(String korisnickoIme, Map<String, T> korisnici) {
        Pattern pattern = Pattern.compile("^[a-zA-Z][a-zA-Z0-9_\\.]+");
        Matcher matcher = pattern.matcher(korisnickoIme);
        return matcher.matches() && !korisnici.containsKey(korisnickoIme);
    }

    private static boolean verifikujLozinku(String lozinka, String potvrdaLozinke) {
        return lozinka.equals(potvrdaLozinke);
    }

    public static void kreirajNoviNalog(TextField tfIme, TextField tfPrezime, TextField tfJMBG, TextField tfBrojUBanci, TextField tfKorisnickoIme, PasswordField pfLozinka, PasswordField pfPotvrdaLozinke, ComboBox cbTipNaloga) {
        boolean detektorGreske = false;
        if (!verifikujIme(tfIme.getText())) {
            Main.ocistiPolje(tfIme);
            detektorGreske = true;
        }
        if (!verifikujIme(tfPrezime.getText())) {
            Main.ocistiPolje(tfPrezime);
            detektorGreske = true;
        }
        if (!verifikujJMBG(tfJMBG.getText())) {
            Main.ocistiPolje(tfJMBG);
            detektorGreske = true;
        }
        if (!verifikujRacun(tfBrojUBanci.getText(), tfJMBG.getText())) {
            Main.ocistiPolje(tfBrojUBanci);
            detektorGreske = true;
        }
        if (cbTipNaloga.getValue().equals("Klijent") && !verifikujKorisnickoIme(tfKorisnickoIme.getText(), klijenti)) {
            Main.ocistiPolje(tfKorisnickoIme);
            detektorGreske = true;
        }
        if (cbTipNaloga.getValue().equals("Vlasnik") && !verifikujKorisnickoIme(tfKorisnickoIme.getText(), vlasnici)) {
            Main.ocistiPolje(tfKorisnickoIme);
            detektorGreske = true;
        }
        if (!verifikujLozinku(pfLozinka.getText(), pfPotvrdaLozinke.getText())) {
            Main.ocistiPolje(pfLozinka);
            Main.ocistiPolje(pfPotvrdaLozinke);
            detektorGreske = true;
        }
        if (detektorGreske)
            Main.upozorenje("Neka od polja nisu korektno popunjena ili je korisnicko ime vec zauzeto! Pokusajte ponovo");
    }
}