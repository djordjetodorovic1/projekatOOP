package SistemZaPlaniranjeProslava;

import SistemZaPlaniranjeProslava.Model.*;
import SistemZaPlaniranjeProslava.Scene.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private static Admin admin = null;
    private static Obavjestenje obavjestenje = null;
    private static Map<String, BankovniRacun> bankovniRacuni = new HashMap<>();
    private static Map<String, Vlasnik> vlasnici = new HashMap<>();
    private static Map<String, Klijent> klijenti = new HashMap<>();
    private static Map<String, Objekat> objekti = new HashMap<>();

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
            objekti = Database.ucitajObjekte();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ispisBaze() {
        System.out.println(admin);
        vlasnici.forEach((k, v) -> System.out.println(k + ": " + v));
        klijenti.forEach((k, v) -> System.out.println(k + ": " + v));
        bankovniRacuni.forEach((k, v) -> System.out.println(k + ": " + v));
        objekti.forEach((k, v) -> System.out.println(k + ": " + v));
    }

    public static void prijava(Stage primaryStage, TextField tfKorisnickoIme, PasswordField pfLozinka) {
        if (tfKorisnickoIme.getText().isEmpty() || pfLozinka.getText().isEmpty()) {
            Main.upozorenje("Niste popunili sva polja! Pokusajte ponovo");
        } else if (Validator.provjeriPrijavu(tfKorisnickoIme, pfLozinka)) {
            if (admin.getKorisnicko_ime().equals(tfKorisnickoIme.getText())) {
                if (admin.getLozinka().equals(pfLozinka.getText())) {
                    //admin se ulogovao
                } else {
                    Main.upozorenje("Pogresna lozinka! Pokusajte ponovo");
                    Main.ocistiPolje(pfLozinka);
                }
            } else if (klijenti.containsKey(tfKorisnickoIme.getText())) {
                if (klijenti.get(tfKorisnickoIme.getText()).getLozinka().equals(pfLozinka.getText())) {
                    //Klijent se ulogovao
                } else {
                    Main.upozorenje("Pogresna lozinka! Pokusajte ponovo");
                    Main.ocistiPolje(pfLozinka);
                }
            } else if (vlasnici.containsKey(tfKorisnickoIme.getText())) {
                if (vlasnici.get(tfKorisnickoIme.getText()).getLozinka().equals(pfLozinka.getText())) {
                    ScenaVlasnik.scenaVlasnik(primaryStage, vlasnici.get(tfKorisnickoIme.getText()));
                } else {
                    Main.upozorenje("Pogresna lozinka! Pokusajte ponovo");
                    Main.ocistiPolje(pfLozinka);
                }
            } else {
                Main.upozorenje("Nalog ne postoji! Pokusajte ponovo ili kreirajte novi nalog");
                Main.ocistiPolje(tfKorisnickoIme);
                Main.ocistiPolje(pfLozinka);
            }
        } else {
            Main.upozorenje("Nekorektan unos! Pokusajte ponovo");
            Main.ocistiPolje(tfKorisnickoIme);
            Main.ocistiPolje(pfLozinka);
        }
    }

    public static void kreirajNoviNalog(Stage primaryStage, TextField tfIme, TextField tfPrezime, TextField tfJMBG, TextField tfBrojUBanci, TextField tfKorisnickoIme,
                                        PasswordField pfLozinka, PasswordField pfPotvrdaLozinke, ComboBox<String> cbTipNaloga) {
        if (!Validator.provjeriNoviNalog(tfIme, tfPrezime, tfJMBG, tfBrojUBanci, tfKorisnickoIme, pfLozinka, pfPotvrdaLozinke, cbTipNaloga, bankovniRacuni, klijenti, vlasnici)) {
            Main.upozorenje("Neka od polja nisu pravilno popunjena ili je korisnicko ime vec zauzeto! Pokusajte ponovo");
        } else {
            int id;
            String nalog = "klijent";
            if (cbTipNaloga.getValue().equals("Vlasnik"))
                nalog = "vlasnik";
            try {
                Database.dodajUBazu("INSERT INTO `" + nalog + "`(`ime`, `prezime`, `jmbg`, `broj_racuna`, `korisnicko_ime`, `lozinka`) VALUES ('" + tfIme.getText() + "','" +
                        tfPrezime.getText() + "','" + tfJMBG.getText() + "','" + tfBrojUBanci.getText() + "','" + tfKorisnickoIme.getText() + "','" + pfLozinka.getText() + "')");
                id = Database.procitajID("SELECT id FROM " + nalog + " WHERE korisnicko_ime = '" + tfKorisnickoIme.getText() + "'");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (nalog.equals("klijent"))
                klijenti.put(tfKorisnickoIme.getText(), new Klijent(id, tfIme.getText(), tfPrezime.getText(), tfJMBG.getText(), tfBrojUBanci.getText(), tfKorisnickoIme.getText(), pfLozinka.getText()));
            else
                vlasnici.put(tfKorisnickoIme.getText(), new Vlasnik(id, tfIme.getText(), tfPrezime.getText(), tfJMBG.getText(), tfBrojUBanci.getText(), tfKorisnickoIme.getText(), pfLozinka.getText()));
            ScenaZaPrijavu.scenaPrijava(primaryStage);
        }
    }

    public static Vlasnik getVlasnik(int id) {
        for (Vlasnik vlasnik : vlasnici.values())
            if (vlasnik.getId() == id)
                return vlasnik;
        return null;
    }

    public static double getStanjeRacuna(String brojRacuna) {
        if (bankovniRacuni.containsKey(brojRacuna)) {
            return bankovniRacuni.get(brojRacuna).getStanje();
        }
        return 0;
    }

    public static boolean promjenaLozinke(Korisnik korisnik, TextField tfStaraLozinka, PasswordField pfLozinka, PasswordField pfPotvrdaLozinke) {
        if (korisnik.getLozinka().equals(tfStaraLozinka.getText())) {
            if (Validator.verifikujLozinku(pfLozinka.getText(), pfPotvrdaLozinke.getText())) {
                korisnik.setLozinka(pfLozinka.getText());
                try {
                    String nalog = "vlasnik";
                    if (korisnik instanceof Klijent)
                        nalog = "klijent";
                    Database.izmjeniUBazi("UPDATE `" + nalog + "` SET `lozinka`='" + pfLozinka.getText() + "' WHERE korisnicko_ime = '" + korisnik.getKorisnicko_ime() + "'");
                    Main.informacija("Uspjesno promjenjena lozinka!");
                    return true;
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Main.ocistiPolje(pfLozinka);
                Main.ocistiPolje(pfPotvrdaLozinke);
                Main.upozorenje("Nova lozinka nije pravilno unesena! Pokusajte ponovo");
            }
        } else {
            Main.ocistiPolje(tfStaraLozinka);
            Main.upozorenje("Stara lozinka nije pravilno unesena! Pokusajte ponovo");
        }
        return false;
    }
}