package SistemZaPlaniranjeProslava;

import SistemZaPlaniranjeProslava.Model.*;
import SistemZaPlaniranjeProslava.Scene.*;
import javafx.application.Platform;
import javafx.scene.control.ComboBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.util.*;

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
            obavjestenja = Database.ucitajObavjestenje();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void ispisBaze() {
        System.out.println(admin);
        vlasnici.values().forEach(System.out::println);
        klijenti.values().forEach(System.out::println);
        bankovniRacuni.values().forEach(System.out::println);
        objekti.values().forEach(System.out::println);
        stolovi.values().forEach(System.out::println);
        meniji.values().forEach(System.out::println);
        for (Obavjestenje ob : obavjestenja)
            System.out.println(ob);
    }

    private static void provjeriNovaObavjestenja(Stage sstage, Vlasnik vlasnik, ArrayList<Obavjestenje> obavjestenja, String korisnickoIme) {
        ArrayList<Obavjestenje> obavjestenjaZaVlansika = new ArrayList<>(obavjestenja.stream()
                .filter(ob -> (ob.getObjekat().getVlasnik().getKorisnicko_ime().equals(korisnickoIme)))
                .toList());
        processObavjestenja(obavjestenjaZaVlansika.iterator(), sstage, vlasnik);
    }
//Popraviti Dodati za brisanje iz baze
//Dodati i kod poziva metode da se izvrsi nakon njenog zavrsetka
    private static void processObavjestenja(Iterator<Obavjestenje> iterator, Stage stage, Vlasnik vlasnik) {
        if (iterator.hasNext()) {
            Obavjestenje ob = iterator.next();
            if (ob.getObjekat().getStatus() == StatusObjekta.ODOBREN) {
                Main.informacija(ob.getTekst());
                processObavjestenja(iterator, stage, vlasnik);

            } else if (ob.getObjekat().getStatus() == StatusObjekta.ODBIJEN) {
                if (Main.potvrda(ob.getTekst())) {
                    ScenaZaNoviObjekat.scenaNoviObjekatPonovo(stage, vlasnik, objekti, ob.getObjekat().getId(), () -> {
                        Platform.runLater(() -> {
                            stage.close();
                            processObavjestenja(iterator, stage, vlasnik);
                        });
                    });
                }
            }
        }
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
                    if (!obavjestenja.isEmpty())
                        provjeriNovaObavjestenja(primaryStage, vlasnici.get(tfKorisnickoIme.getText()), obavjestenja, tfKorisnickoIme.getText());
                    ScenaVlasnik.scenaVlasnik(primaryStage, vlasnici.get(tfKorisnickoIme.getText()), objekti);
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

    public static void kreirajNoviNalog(Stage primaryStage, TextField tfIme, TextField tfPrezime, TextField tfJMBG, TextField tfBrojUBanci,
                                        TextField tfKorisnickoIme, PasswordField pfLozinka, PasswordField pfPotvrdaLozinke, ComboBox<String> cbTipNaloga) {
        if (!Validator.provjeriNoviNalog(tfIme, tfPrezime, tfJMBG, tfBrojUBanci, tfKorisnickoIme, pfLozinka, pfPotvrdaLozinke, cbTipNaloga, bankovniRacuni, klijenti, vlasnici)) {
            Main.upozorenje("Neka od polja nisu pravilno popunjena ili je korisnicko ime vec zauzeto! Pokusajte ponovo");
        } else {
            int id;
            String nalog = "klijent";
            if (cbTipNaloga.getValue().equals("Vlasnik"))
                nalog = "vlasnik";

            Database.dodajUBazu("INSERT INTO `" + nalog + "`(`ime`, `prezime`, `jmbg`, `broj_racuna`, `korisnicko_ime`, `lozinka`) VALUES ('" + tfIme.getText() + "','"
                    + tfPrezime.getText() + "','" + tfJMBG.getText() + "','" + tfBrojUBanci.getText() + "','" + tfKorisnickoIme.getText() + "','" + pfLozinka.getText() + "')");
            id = Database.procitajID("SELECT id FROM " + nalog + " WHERE korisnicko_ime = '" + tfKorisnickoIme.getText() + "'");

            if (nalog.equals("klijent"))
                klijenti.put(tfKorisnickoIme.getText(), new Klijent(id, tfIme.getText(), tfPrezime.getText(), tfJMBG.getText(), tfBrojUBanci.getText(), tfKorisnickoIme.getText(), pfLozinka.getText()));
            else
                vlasnici.put(tfKorisnickoIme.getText(), new Vlasnik(id, tfIme.getText(), tfPrezime.getText(), tfJMBG.getText(), tfBrojUBanci.getText(), tfKorisnickoIme.getText(), pfLozinka.getText()));
            ScenaZaPrijavu.scenaPrijava(primaryStage);
        }
    }

    public static boolean promjenaLozinke(Korisnik korisnik, TextField tfStaraLozinka, PasswordField pfLozinka, PasswordField pfPotvrdaLozinke) {
        if (korisnik.getLozinka().equals(tfStaraLozinka.getText())) {
            if (Validator.verifikujLozinku(pfLozinka.getText(), pfPotvrdaLozinke.getText())) {
                korisnik.setLozinka(pfLozinka.getText());
                String nalog = "vlasnik";
                if (korisnik instanceof Klijent)
                    nalog = "klijent";
                Database.izmjeniUBazi("UPDATE `" + nalog + "` SET `lozinka`='" + pfLozinka.getText() + "' WHERE korisnicko_ime = '" + korisnik.getKorisnicko_ime() + "'");
                Main.informacija("Uspjesno promjenjena lozinka!");
                return true;

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

    public static boolean kreirajNoviObjekat(TextField tfNaziv, TextField tfGrad, TextField tfAdresa, TextField tfCijenaRezervacije, TextField tfBrojMijesta,
                                             TextField tfBrojStolova, ArrayList<String> meniOpis, ArrayList<Double> meniCijene, Vlasnik vlasnik, ArrayList<Integer> brojMijestaPoStolovima, int idObjekat) {
        if (meniOpis.isEmpty()) {
            Main.upozorenje("Niste popunili meni! Pokusajte ponovo");
        } else if (brojMijestaPoStolovima.isEmpty()) {
            Main.upozorenje("Niste popunili podatke o stolovima! Pokusajte ponovo");
        } else if (!Validator.provjeraObjektaZaUnos(tfNaziv, tfGrad, tfAdresa, tfCijenaRezervacije, tfBrojMijesta, tfBrojStolova)) {
            Main.upozorenje("Neka od polja nisu pravilno popunjena! Pokusajte ponovo");
        } else {
            if (idObjekat > 0) {
                Database.izmjeniUBazi("UPDATE `objekat` SET `cijena_rezervacije`=" + Double.parseDouble(tfCijenaRezervacije.getText()) + ",`broj_mjesta`=" + Integer.parseInt(tfBrojMijesta.getText())
                        + ",`broj_stolova`=" + Integer.parseInt(tfBrojStolova.getText()) + ", `status`='" + StatusObjekta.NA_CEKANJU + "' WHERE id=" + idObjekat);
                Database.izbrisiIzBaze("DELETE FROM `meni` WHERE Objekat_id =" + idObjekat);
                Database.izbrisiIzBaze("DELETE FROM `sto` WHERE Objekat_id =" + idObjekat);
                try {
                    meniji = Database.ucitajMenije();
                    stolovi = Database.ucitajStolove();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Objekat objekatZaIzmjenu = new Objekat(idObjekat, vlasnik, tfNaziv.getText(), Double.parseDouble(tfCijenaRezervacije.getText()), tfGrad.getText(), tfAdresa.getText(),
                        Integer.parseInt(tfBrojMijesta.getText()), Integer.parseInt(tfBrojStolova.getText()), "", 0.0, StatusObjekta.NA_CEKANJU);
                objekti.put(idObjekat, objekatZaIzmjenu);
            } else if (idObjekat == 0) {
                Database.dodajUBazu("INSERT INTO `objekat` (`Vlasnik_id`, `naziv`, `cijena_rezervacije`, `grad`, `adresa`, `broj_mjesta`, `broj_stolova`, `datumi`, `zarada`) VALUES ("
                        + vlasnik.getId() + ", '" + tfNaziv.getText() + "', " + Double.parseDouble(tfCijenaRezervacije.getText()) + ", '" + tfGrad.getText() + "', '" + tfAdresa.getText() + "', "
                        + Integer.parseInt(tfBrojMijesta.getText()) + ", " + Integer.parseInt(tfBrojStolova.getText()) + ", '', " + 0.0 + ")");
                idObjekat = Database.procitajID("SELECT id FROM objekat ORDER BY id DESC LIMIT 1");

                objekti.put(idObjekat, new Objekat(idObjekat, vlasnik, tfNaziv.getText(), Double.parseDouble(tfCijenaRezervacije.getText()), tfGrad.getText(), tfAdresa.getText(),
                        Integer.parseInt(tfBrojMijesta.getText()), Integer.parseInt(tfBrojStolova.getText()), "", 0.0, StatusObjekta.NA_CEKANJU));
            }

            int idMeni, idSto, idObavjestenje;

            for (int i = 0; i < meniOpis.size(); i++) {
                Database.dodajUBazu("INSERT INTO `meni`(`Objekat_id`, `opis`, `cijena_po_osobi`) VALUES (" + idObjekat + ",'" + meniOpis.get(i) + "'," + meniCijene.get(i) + ")");
                idMeni = Database.procitajID("SELECT id FROM meni ORDER BY id DESC LIMIT 1");
                meniji.put(idMeni, new Meni(idMeni, objekti.get(idObjekat), meniOpis.get(i), meniCijene.get(i)));
            }

            for (Integer integer : brojMijestaPoStolovima) {
                Database.dodajUBazu("INSERT INTO `sto`(`Objekat_id`, `broj_mjesta`) VALUES (" + idObjekat + "," + integer + ")");
                idSto = Database.procitajID("SELECT id FROM sto ORDER BY id DESC LIMIT 1");
                stolovi.put(idSto, new Sto(idSto, objekti.get(idObjekat), integer));
            }

            Database.dodajUBazu("INSERT INTO `obavjestenje`(`Objekat_id`, `tekst`) VALUES (" + idObjekat + ",'Novi objekat ceka na odobrenje!')");
            idObavjestenje = Database.procitajID("SELECT id FROM obavjestenje ORDER BY id DESC LIMIT 1");
            obavjestenja.add(new Obavjestenje(idObavjestenje, objekti.get(idObjekat), "Novi objekat ceka na odobreneje!"));

            Main.informacija("Objekat uspjesno kreiran!");
            return true;
        }
        return false;
    }

    public static boolean dodavanjeMenija(TextField tfMeni, TextField tfCijenaMenija) {
        return Validator.validacijaMeni(tfMeni, tfCijenaMenija);
    }

    public static double getStanjeRacuna(String brojRacuna) {
        return bankovniRacuni.get(brojRacuna).getStanje();
    }
}