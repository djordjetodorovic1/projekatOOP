package SistemZaPlaniranjeProslava;

import SistemZaPlaniranjeProslava.Model.*;
import SistemZaPlaniranjeProslava.Scene.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.*;
import java.time.LocalDate;

public class Controller {
    private static Admin admin = null;
    private static ArrayList<Obavjestenje> obavjestenja = new ArrayList<>();
    private static Map<String, BankovniRacun> bankovniRacuni = new HashMap<>();
    private static Map<String, Vlasnik> vlasnici = new HashMap<>();
    private static Map<String, Klijent> klijenti = new HashMap<>();
    private static Map<Integer, Objekat> objekti = new HashMap<>();
    private static Map<Integer, Sto> stolovi = new HashMap<>();
    private static Map<Integer, Meni> meniji = new HashMap<>();
    private static Map<Integer, Proslava> proslave = new HashMap<>();

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
            proslave = Database.ucitajProslave();

            proslave.put(1, new Proslava(1, objekti.get(16),
                    Database.getKlijent(4), meniji.get(30), LocalDate.of(2025, 2, 5), 10,
                    300, 250));
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
        proslave.values().forEach(System.out::println);
    }

    public static void scenaVlasnik(Stage primaryStage, Vlasnik vlasnik) {
        ScenaVlasnik.scenaVlasnik(primaryStage, vlasnik, objekti, stolovi, proslave);
    }

    private static void prijavaVlasnika(Stage primaryStage, String korisnickoIme) {
        ArrayList<Obavjestenje> obavjestenjaZaVlansika = new ArrayList<>(obavjestenja.stream()
                .filter(ob -> (ob.getObjekat().getVlasnik().getKorisnickoIme().equals(korisnickoIme) && ob.getObjekat().getStatus() != StatusObjekta.NA_CEKANJU))
                .toList());
        if (!obavjestenjaZaVlansika.isEmpty()) {
            Collections.sort(obavjestenjaZaVlansika);
            for (Obavjestenje ob : obavjestenjaZaVlansika) {
                Stage stagePonoviObjekat = new Stage();
                if (ob.getObjekat().getStatus() == StatusObjekta.ODOBREN)
                    Main.informacija(ob.getTekst());
                else if (Main.potvrda(ob.getTekst()))
                    ScenaZaNoviObjekat.scenaIzmjenaObjekta(stagePonoviObjekat, vlasnici.get(korisnickoIme), objekti, proslave, ob);
                Database.izbrisiObavjestenjeIzBaze(ob.getId());
                obavjestenja.remove(ob);
            }
        }
        scenaVlasnik(primaryStage, vlasnici.get(korisnickoIme));
    }

    public static void prijavaAdmin(Stage primaryStage) {
        ArrayList<Obavjestenje> obavjestenjaZaAdmina = new ArrayList<>(obavjestenja.stream()
                .filter(ob -> (ob.getObjekat().getStatus() == StatusObjekta.NA_CEKANJU))
                .toList());
        ScenaAdmin.scenaAdmin(primaryStage, admin, obavjestenjaZaAdmina, stolovi, meniji);
    }

    public static void prijava(Stage primaryStage, TextField tfKorisnickoIme, PasswordField pfLozinka) {
        if (tfKorisnickoIme.getText().isEmpty() || pfLozinka.getText().isEmpty()) {
            Main.upozorenje("Niste popunili sva polja! Pokusajte ponovo");
        } else if (Validator.provjeriPrijavu(tfKorisnickoIme.getText(), pfLozinka.getText())) {
            if (admin.getKorisnickoIme().equals(tfKorisnickoIme.getText())) {
                if (admin.getLozinka().equals(pfLozinka.getText())) {
                    prijavaAdmin(primaryStage);
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
                    prijavaVlasnika(primaryStage, tfKorisnickoIme.getText());
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
                                        TextField tfKorisnickoIme, PasswordField pfLozinka, PasswordField pfPotvrdaLozinke, ChoiceBox<String> cbTipNaloga) {
        if (!Validator.provjeriNoviNalog(tfIme, tfPrezime, tfJMBG, tfBrojUBanci, tfKorisnickoIme, pfLozinka, pfPotvrdaLozinke, cbTipNaloga, bankovniRacuni, klijenti, vlasnici)) {
            Main.upozorenje("Neka od polja nisu pravilno popunjena ili je korisnicko ime vec zauzeto! Pokusajte ponovo");
        } else {
            String nalog = "klijent";
            if (cbTipNaloga.getValue().equals("Vlasnik"))
                nalog = "vlasnik";
            int id = Database.dodajNalogUBazu(nalog, tfIme.getText(), tfPrezime.getText(), tfJMBG.getText(), tfBrojUBanci.getText(), tfKorisnickoIme.getText(), pfLozinka.getText());

            if (nalog.equals("klijent"))
                klijenti.put(tfKorisnickoIme.getText(), new Klijent(id, tfIme.getText(), tfPrezime.getText(), tfJMBG.getText(), tfBrojUBanci.getText(), tfKorisnickoIme.getText(), pfLozinka.getText()));
            else
                vlasnici.put(tfKorisnickoIme.getText(), new Vlasnik(id, tfIme.getText(), tfPrezime.getText(), tfJMBG.getText(), tfBrojUBanci.getText(), tfKorisnickoIme.getText(), pfLozinka.getText()));
            Main.informacija("Nalog uspjesno kreiran!");
            ScenaZaPrijavu.scenaPrijava(primaryStage);
        }
    }

    public static boolean promjenaLozinke(Osoba osoba, TextField tfStaraLozinka, PasswordField pfLozinka, PasswordField pfPotvrdaLozinke) {
        if (osoba.getLozinka().equals(tfStaraLozinka.getText())) {
            if (Validator.verifikujLozinku(pfLozinka.getText(), pfPotvrdaLozinke.getText())) {
                osoba.setLozinka(pfLozinka.getText());
                String nalog = "klijent";
                if (osoba instanceof Vlasnik)
                    nalog = "vlasnik";
                else if (osoba instanceof Admin) {
                    nalog = "admin";
                }
                Database.izmjeniLozinku(nalog, pfLozinka.getText(), osoba.getKorisnickoIme());
                osoba.setLozinka(pfLozinka.getText());
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

    public static boolean kreirajNoviObjekat(TextField tfNaziv, TextField tfGrad, TextField tfAdresa, TextField tfCijenaRezervacije, TextField tfBrojMjesta,
                                             TextField tfBrojStolova, ArrayList<String> meniOpis, ArrayList<Double> meniCijene, Vlasnik vlasnik, ArrayList<Integer> brojMjestaPoStolovima, int idObjekat) {
        if (meniOpis.isEmpty()) {
            Main.upozorenje("Niste popunili meni! Pokusajte ponovo");
        } else if (brojMjestaPoStolovima.isEmpty()) {
            Main.upozorenje("Niste popunili podatke o stolovima! Pokusajte ponovo");
        } else if (!Validator.provjeraObjektaZaUnos(tfGrad, tfAdresa, tfCijenaRezervacije, tfBrojMjesta, tfBrojStolova)) {
            Main.upozorenje("Neka od polja nisu pravilno popunjena! Pokusajte ponovo");
        } else {
            if (idObjekat > 0) {
                Database.izmjeniObjekatUBazi(Double.parseDouble(tfCijenaRezervacije.getText()), Integer.parseInt(tfBrojMjesta.getText()), Integer.parseInt(tfBrojStolova.getText()), idObjekat);
                Database.izbrisiIzBazeZaObjekatID("meni", idObjekat);
                Database.izbrisiIzBazeZaObjekatID("sto", idObjekat);
                try {
                    meniji = Database.ucitajMenije();
                    stolovi = Database.ucitajStolove();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                Objekat objekatZaIzmjenu = new Objekat(idObjekat, vlasnik, tfNaziv.getText(), Double.parseDouble(tfCijenaRezervacije.getText()), tfGrad.getText(), tfAdresa.getText(),
                        Integer.parseInt(tfBrojMjesta.getText()), Integer.parseInt(tfBrojStolova.getText()), "", 0.0, StatusObjekta.NA_CEKANJU);
                objekti.put(idObjekat, objekatZaIzmjenu);
            } else if (idObjekat == 0) {
                idObjekat = Database.dodajObjekatUBazu(vlasnik.getId(), tfNaziv.getText(), Double.parseDouble(tfCijenaRezervacije.getText()), tfGrad.getText(), tfAdresa.getText(), Integer.parseInt(tfBrojMjesta.getText()), Integer.parseInt(tfBrojStolova.getText()));
                objekti.put(idObjekat, new Objekat(idObjekat, vlasnik, tfNaziv.getText(), Double.parseDouble(tfCijenaRezervacije.getText()), tfGrad.getText(), tfAdresa.getText(),
                        Integer.parseInt(tfBrojMjesta.getText()), Integer.parseInt(tfBrojStolova.getText()), "", 0.0, StatusObjekta.NA_CEKANJU));
            }

            int idMeni, idSto, idObavjestenje;

            for (int i = 0; i < meniOpis.size(); i++) {
                idMeni = Database.dodajMeniUBazu(idObjekat, meniOpis.get(i), meniCijene.get(i));
                meniji.put(idMeni, new Meni(idMeni, objekti.get(idObjekat), meniOpis.get(i), meniCijene.get(i)));
            }

            for (Integer brMjesta : brojMjestaPoStolovima) {
                idSto = Database.dodajStoUBazu(idObjekat, brMjesta);
                stolovi.put(idSto, new Sto(idSto, objekti.get(idObjekat), brMjesta));
            }

            idObavjestenje = Database.dodajObavjestenjeUBazu(idObjekat, "Novi objekat ceka na odobrenje!");
            obavjestenja.add(new Obavjestenje(idObavjestenje, objekti.get(idObjekat), "Novi objekat ceka na odobreneje!"));

            Main.informacija("Objekat uspjesno kreiran!");
            return true;
        }
        return false;
    }

    public static String provjeriObjekatZaOdobrenje(Objekat objekat) {
        boolean detektorGreske = false;
        StringBuilder poruka = new StringBuilder();
        poruka.append("\"" + objekat.getNaziv() + "\" - ");
        if (!Validator.provjeraCijeneMenijaZaOdobrenje(objekat, meniji)) {
            Main.upozorenje("Cijene menija nisu uskladjene sa cijenama u ostalim objektima!");
            poruka.append("Cijene menija nisu uskladjene sa cijenama u ostalim objektima!");
            detektorGreske = true;
        }
        if (!Validator.provjeraMjestaZaOdobrenje(objekat, stolovi)) {
            Main.upozorenje("Maksimalan broj mjesta u salonu se ne podudara sa ukupnim brojem mjesta svih stolova!");
            if (detektorGreske)
                poruka.append("\n");
            poruka.append("Maksimalan broj mjesta u salonu se ne podudara sa ukupnim brojem mjesta svih stolova!");
            detektorGreske = true;
        }
        if (!detektorGreske) {
            Main.informacija("Objekat zadovoljava sve uslove!");
            poruka.append("Objekat zadovoljava sve uslova");
        }
        return poruka.toString();
    }

    public static void promjenaStatusaObjekta(Objekat objekat, StatusObjekta statusObjekta, String poruka, Obavjestenje obavjestenje) {
        objekti.get(objekat.getId()).setStatus(statusObjekta);
        Database.objekatObradjen(objekat.getId(), statusObjekta);

        obavjestenja.remove(obavjestenje);
        Database.izbrisiObavjestenjeIzBaze(obavjestenje.getId());
        int idObavjestenje = Database.dodajObavjestenjeUBazu(objekat.getId(), poruka);
        obavjestenja.add(new Obavjestenje(idObavjestenje, objekat, poruka));
    }

    public static boolean dodavanjeMenija(TextField tfMeni, TextField tfCijenaMenija) {
        return Validator.validacijaMeni(tfMeni, tfCijenaMenija);
    }

    public static double getStanjeRacuna(String brojRacuna) {
        return bankovniRacuni.get(brojRacuna).getStanje();
    }

    public static Objekat getObjekat(String nazivObjekta) {
        for (Objekat obj : objekti.values())
            if (obj.getNaziv().equals(nazivObjekta))
                return obj;
        return null;
    }
}