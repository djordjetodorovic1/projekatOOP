package SistemZaPlaniranjeProslava;

import SistemZaPlaniranjeProslava.Model.*;
import SistemZaPlaniranjeProslava.Scene.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Controller {
    private static Admin admin = null;
    private static ArrayList<Obavjestenje> obavjestenja = new ArrayList<>();
    private static Map<String, BankovniRacun> bankovniRacuni = new HashMap<>();
    private static Map<String, Vlasnik> vlasnici = new HashMap<>();
    private static Map<String, Klijent> klijenti = new HashMap<>();
    private static Map<Integer, Objekat> objekti = new HashMap<>();
    private static Map<Integer, Sto> stolovi = new HashMap<>();
    private static Map<Integer, Meni> meniji = new HashMap<>();

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
            stolovi = Database.ucitajStolove();
            meniji = Database.ucitajMenije();
            Obavjestenje ob = Database.ucitajObavjestenje();
            if (ob != null)
                obavjestenja.add(ob);
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
        stolovi.forEach((k, v) -> System.out.println(k + ": " + v));
        meniji.forEach((k, v) -> System.out.println(k + ": " + v));
        System.out.println(obavjestenja);
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
                    ScenaVlasnik.scenaVlasnik(primaryStage, vlasnici.get(tfKorisnickoIme.getText()), objekti);
                    // Dodati za obavjestenje
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

    public static void kreirajNoviObjekat(Stage stage, TextField tfNaziv, TextField tfGrad, TextField tfAdresa, TextField tfCijenaRezervacije, TextField tfBrojMijesta, TextField tfBrojStolova, ArrayList<String> meniOpis, ArrayList<Double> meniCijene, Vlasnik vlasnik, ArrayList<Integer> brojMijestaPoStolovima) {
        if (meniOpis.isEmpty()) {
            Main.upozorenje("Niste popunili meni! Pokusajte ponovo");
        } else if (brojMijestaPoStolovima.isEmpty()) {
            Main.upozorenje("Niste popunili podatke o stolovima! Pokusajte ponovo");
        } else if (!Validator.provjeraObjektaZaUnos(tfNaziv, tfGrad, tfAdresa, tfCijenaRezervacije, tfBrojMijesta, tfBrojStolova)) {
            Main.upozorenje("Neka od polja nisu pravilno popunjena! Pokusajte ponovo");
        } else {
            int idObjekat, idSto, idMeni, idObavjestenje;
            try {
                Database.dodajUBazu("INSERT INTO `objekat` (`Vlasnik_id`, `naziv`, `cijena_rezervacije`, `grad`, `adresa`, `broj_mjesta`, `broj_stolova`, `datumi`, `zarada`) VALUES ("
                        + vlasnik.getId() + ", '" + tfNaziv.getText() + "', " + Double.parseDouble(tfCijenaRezervacije.getText()) + ", '" + tfGrad.getText() + "', '" + tfAdresa.getText() + "', "
                        + Integer.parseInt(tfBrojMijesta.getText()) + ", " + Integer.parseInt(tfBrojStolova.getText()) + ", '', " + 0.0 + ")");
                idObjekat = Database.procitajID("SELECT id FROM objekat ORDER BY id DESC LIMIT 1");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            objekti.put(idObjekat, new Objekat(idObjekat, vlasnik, tfNaziv.getText(), Double.parseDouble(tfCijenaRezervacije.getText()), tfGrad.getText(), tfAdresa.getText(), Integer.parseInt(tfBrojMijesta.getText()), Integer.parseInt(tfBrojStolova.getText()), StatusObjekta.NA_CEKANJU));

            try {
                for (int i = 0; i < meniOpis.size(); i++) {
                    Database.dodajUBazu("INSERT INTO `meni`(`Objekat_id`, `opis`, `cijena_po_osobi`) VALUES (" + idObjekat + ",'" + meniOpis.get(i) + "'," + meniCijene.get(i) + ")");
                    idMeni = Database.procitajID("SELECT id FROM meni ORDER BY id DESC LIMIT 1");
                    meniji.put(idMeni, new Meni(idMeni, idObjekat, meniOpis.get(i), meniCijene.get(i)));

                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            try {
                for (int i = 0; i < brojMijestaPoStolovima.size(); i++) {
                    Database.dodajUBazu("INSERT INTO `sto`(`Objekat_id`, `broj_mjesta`) VALUES (" + idObjekat + "," + brojMijestaPoStolovima.get(i) + ")");
                    idSto = Database.procitajID("SELECT id FROM sto ORDER BY id DESC LIMIT 1");
                    stolovi.put(idSto, new Sto(idSto, idObjekat, brojMijestaPoStolovima.get(i)));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            try {
                Database.dodajUBazu("INSERT INTO `obavjestenje`(`Objekat_id`, `tekst`) VALUES (" + idObjekat + ",'Novi objekat ceka na odobrenje!')");
                idObavjestenje = Database.procitajID("SELECT id FROM obavjestenje ORDER BY id DESC LIMIT 1");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            obavjestenja.add(new Obavjestenje(idObavjestenje, idObjekat, "Novi objekat ceka na odobreneje!"));
            Main.informacija("Objekat uspjesno kreiran!");
            ScenaVlasnik.scenaVlasnik(stage, vlasnik, objekti);
        }
    }

    public static boolean dodavanjeMenija(TextField tfMeni, TextField tfCijenaMenija) {
        if (Validator.validacijaMeni(tfMeni, tfCijenaMenija)) {
            Main.upozorenje("Nekorektan unos! Pokusajte ponovo");
            return false;
        }
        return true;
    }

    public static Vlasnik getVlasnik(int id) {
        for (Vlasnik vlasnik : vlasnici.values())
            if (vlasnik.getId() == id)
                return vlasnik;
        return null;
    }

    public static Objekat getObjekat(int id) {
        for (Objekat objekat : objekti.values())
            if (objekat.getId() == id)
                return objekat;
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