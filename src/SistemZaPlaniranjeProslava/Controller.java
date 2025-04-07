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
    private static Map<String, Raspored> rasporedi = new HashMap<>();

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
            rasporedi = Database.ucitajRasporede();
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
        rasporedi.values().forEach(System.out::println);
    }

    private static void prijavaVlasnik(Stage primaryStage, String korisnickoIme) {
        ArrayList<Obavjestenje> obavjestenjaZaVlansika = new ArrayList<>(obavjestenja.stream()
                .filter(ob -> (ob.getObjekat().getVlasnik().getKorisnickoIme().equals(korisnickoIme) && ob.getObjekat().getStatus() != StatusObjekta.NA_CEKANJU))
                .toList());
        if (!obavjestenjaZaVlansika.isEmpty()) {
            Collections.sort(obavjestenjaZaVlansika);
            for (Obavjestenje ob : obavjestenjaZaVlansika) {
                if (ob.getObjekat().getStatus() == StatusObjekta.ODOBREN)
                    Main.informacija(ob.getTekst());
                else if (Main.potvrda(ob.getTekst())) {
                    Stage stagePonoviObjekat = new Stage();
                    ScenaZaNoviObjekat.scenaIzmjenaObjekta(stagePonoviObjekat, vlasnici.get(korisnickoIme), ob);
                }
                Database.izbrisiObavjestenjeIzBaze(ob.getId());
                obavjestenja.remove(ob);
            }
        }
        ScenaVlasnik.scenaVlasnik(primaryStage, vlasnici.get(korisnickoIme));
    }

    private static void prijavaAdmin(Stage primaryStage) {
        ArrayList<Obavjestenje> obavjestenjaZaAdmina = new ArrayList<>(obavjestenja.stream()
                .filter(ob -> (ob.getObjekat().getStatus() == StatusObjekta.NA_CEKANJU))
                .toList());
        ScenaAdmin.scenaAdmin(primaryStage, admin, obavjestenjaZaAdmina);
    }

    public static void scenaBiranjeObjekta(Stage primaryStage, Klijent klijent) {
        ArrayList<Objekat> objektiZaKlijenta = new ArrayList<>();
        for (Objekat objekat : objekti.values())
            if (objekat.getStatus() == StatusObjekta.ODOBREN)
                objektiZaKlijenta.add(objekat);
        objektiZaKlijenta.sort(Objekat::compareTo);
        ScenaBiranjeObjekta.scenaBiranjeObjekta(primaryStage, objektiZaKlijenta, klijent);
    }

    public static void prijava(Stage primaryStage, TextField tfKorisnickoIme, PasswordField pfLozinka) {
        if (tfKorisnickoIme.getText().isEmpty() || pfLozinka.getText().isEmpty()) {
            Main.upozorenje("Niste popunili sva polja! Pokušajte ponovo");
        } else if (Validator.provjeriPrijavu(tfKorisnickoIme.getText(), pfLozinka.getText())) {
            if (admin.getKorisnickoIme().equals(tfKorisnickoIme.getText())) {
                if (admin.getLozinka().equals(pfLozinka.getText())) {
                    prijavaAdmin(primaryStage);
                } else {
                    Main.upozorenje("Pogrešna lozinka! Pokušajte ponovo");
                    Main.ocistiPolje(pfLozinka);
                }
            } else if (klijenti.containsKey(tfKorisnickoIme.getText())) {
                if (klijenti.get(tfKorisnickoIme.getText()).getLozinka().equals(pfLozinka.getText())) {
                    ScenaKlijent.scenaKlijent(primaryStage, klijenti.get(tfKorisnickoIme.getText()));
                } else {
                    Main.upozorenje("Pogrešna lozinka! Pokušajte ponovo");
                    Main.ocistiPolje(pfLozinka);
                }
            } else if (vlasnici.containsKey(tfKorisnickoIme.getText())) {
                if (vlasnici.get(tfKorisnickoIme.getText()).getLozinka().equals(pfLozinka.getText())) {
                    prijavaVlasnik(primaryStage, tfKorisnickoIme.getText());
                } else {
                    Main.upozorenje("Pogrešna lozinka! Pokušajte ponovo");
                    Main.ocistiPolje(pfLozinka);
                }
            } else {
                Main.upozorenje("Nalog ne postoji! Pokušajte ponovo ili kreirajte novi nalog");
                Main.ocistiPolje(tfKorisnickoIme);
                Main.ocistiPolje(pfLozinka);
            }
        } else {
            Main.upozorenje("Nekorektan unos! Pokušajte ponovo");
            Main.ocistiPolje(tfKorisnickoIme);
            Main.ocistiPolje(pfLozinka);
        }
    }

    public static void kreirajNoviNalog(Stage primaryStage, TextField tfIme, TextField tfPrezime, TextField tfJMBG, TextField tfBrojUBanci,
                                        TextField tfKorisnickoIme, PasswordField pfLozinka, PasswordField pfPotvrdaLozinke, ChoiceBox<String> cbTipNaloga) {
        if (!Validator.provjeriNoviNalog(tfIme, tfPrezime, tfJMBG, tfBrojUBanci, tfKorisnickoIme, pfLozinka, pfPotvrdaLozinke, cbTipNaloga, bankovniRacuni, klijenti, vlasnici)) {
            Main.upozorenje("Neka od polja nisu pravilno popunjena ili je korisničko ime već zauzeto! Pokušajte ponovo");
        } else {
            String nalog = "klijent";
            if (cbTipNaloga.getValue().equals("Vlasnik"))
                nalog = "vlasnik";
            int id = Database.dodajNalogUBazu(nalog, tfIme.getText(), tfPrezime.getText(), tfJMBG.getText(), tfBrojUBanci.getText(), tfKorisnickoIme.getText(), pfLozinka.getText());
            if (nalog.equals("klijent"))
                klijenti.put(tfKorisnickoIme.getText(), new Klijent(id, tfIme.getText(), tfPrezime.getText(), tfJMBG.getText(),
                        tfBrojUBanci.getText(), tfKorisnickoIme.getText(), pfLozinka.getText()));
            else
                vlasnici.put(tfKorisnickoIme.getText(), new Vlasnik(id, tfIme.getText(), tfPrezime.getText(), tfJMBG.getText(),
                        tfBrojUBanci.getText(), tfKorisnickoIme.getText(), pfLozinka.getText()));
            Main.informacija("Nalog uspješno kreiran!");
            ScenaZaPrijavu.scenaPrijava(primaryStage);
        }
    }

    public static boolean promjenaLozinke(Osoba osoba, TextField tfStaraLozinka, PasswordField pfLozinka, PasswordField pfPotvrdaLozinke) {
        if (osoba.getLozinka().equals(tfStaraLozinka.getText())) {
            if (Validator.verifikujLozinku(pfLozinka.getText(), pfPotvrdaLozinke.getText())) {
                String nalog = "klijent";
                if (osoba instanceof Vlasnik)
                    nalog = "vlasnik";
                else if (osoba instanceof Admin)
                    nalog = "admin";

                Database.izmjeniLozinku(nalog, pfLozinka.getText(), osoba.getKorisnickoIme());
                osoba.setLozinka(pfLozinka.getText());
                return true;
            } else {
                Main.ocistiPolje(pfLozinka);
                Main.ocistiPolje(pfPotvrdaLozinke);
                Main.upozorenje("Nova lozinka nije pravilno unesena! Pokušajte ponovo");
            }
        } else {
            Main.ocistiPolje(tfStaraLozinka);
            Main.upozorenje("Stara lozinka nije pravilno unesena! Pokušajte ponovo");
        }
        return false;
    }

    public static boolean kreirajNoviObjekat(TextField tfNaziv, TextField tfGrad, TextField tfAdresa, TextField tfCijenaRezervacije,
                                             TextField tfBrojMjesta, TextField tfBrojStolova, ArrayList<String> meniOpis, ArrayList<Double> meniCijene,
                                             Vlasnik vlasnik, ArrayList<Integer> brojMjestaPoStolovima, int idObjekat) {
        if (!Validator.provjeraObjektaZaUnos(tfGrad, tfAdresa, tfCijenaRezervacije, tfBrojMjesta, tfBrojStolova))
            Main.upozorenje("Neka od polja nisu pravilno popunjena! Pokušajte ponovo");
        else if (idObjekat == 0 && meniOpis.isEmpty())
            Main.upozorenje("Niste popunili meni! Pokušajte ponovo");
        else if (idObjekat == 0 && (brojMjestaPoStolovima.isEmpty() || brojMjestaPoStolovima.size() != Integer.parseInt(tfBrojStolova.getText())))
            Main.upozorenje("Niste popunili podatke o stolovima! Pokušajte ponovo");
        else if (idObjekat > 0 && (stoloviZaObjekat(idObjekat).size() != Integer.parseInt(tfBrojStolova.getText())) && brojMjestaPoStolovima.size() != Integer.parseInt(tfBrojStolova.getText()))
            Main.upozorenje("Niste popunili podatke o stolovima! Pokušajte ponovo");
        else {
            if (idObjekat > 0) {
                Database.izmjeniObjekatUBazi(Double.parseDouble(tfCijenaRezervacije.getText()), Integer.parseInt(tfBrojMjesta.getText()), Integer.parseInt(tfBrojStolova.getText()), idObjekat);
                Objekat objekatZaIzmjenu = new Objekat(idObjekat, vlasnik, tfNaziv.getText(), Double.parseDouble(tfCijenaRezervacije.getText()), tfGrad.getText(), tfAdresa.getText(),
                        Integer.parseInt(tfBrojMjesta.getText()), Integer.parseInt(tfBrojStolova.getText()), 0.0, StatusObjekta.NA_CEKANJU);
                objekti.put(idObjekat, objekatZaIzmjenu);
            } else if (idObjekat == 0) {
                idObjekat = Database.dodajObjekatUBazu(vlasnik.getId(), tfNaziv.getText(), Double.parseDouble(tfCijenaRezervacije.getText()), tfGrad.getText(),
                        tfAdresa.getText(), Integer.parseInt(tfBrojMjesta.getText()), Integer.parseInt(tfBrojStolova.getText()));
                objekti.put(idObjekat, new Objekat(idObjekat, vlasnik, tfNaziv.getText(), Double.parseDouble(tfCijenaRezervacije.getText()), tfGrad.getText(), tfAdresa.getText(),
                        Integer.parseInt(tfBrojMjesta.getText()), Integer.parseInt(tfBrojStolova.getText()), 0.0, StatusObjekta.NA_CEKANJU));
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

            idObavjestenje = Database.dodajObavjestenjeUBazu(idObjekat, "Novi objekat čeka na odobrenje!");
            obavjestenja.add(new Obavjestenje(idObavjestenje, objekti.get(idObjekat), "Novi objekat čeka na odobrenje!"));

            Main.informacija("Objekat uspješno kreiran!");
            return true;
        }
        return false;
    }

    public static void promjenaStatusaObjekta(Objekat objekat, StatusObjekta statusObjekta, String poruka, Obavjestenje obavjestenje) {
        objekti.get(objekat.getId()).setStatus(statusObjekta);
        Database.objekatObradjen(objekat.getId(), statusObjekta);

        obavjestenja.remove(obavjestenje);
        Database.izbrisiObavjestenjeIzBaze(obavjestenje.getId());
        int idObavjestenje = Database.dodajObavjestenjeUBazu(objekat.getId(), poruka);
        obavjestenja.add(new Obavjestenje(idObavjestenje, objekat, poruka));
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

    public static boolean zauzetObjekatZaDatum(LocalDate datum, Objekat objekat) {
        for (Proslava proslava : proslave.values())
            if (proslava.getObjekat().equals(objekat) && proslava.getDatum().equals(datum))
                return true;
        return false;
    }

    public static Set<LocalDate> zauzetiDatumi(Objekat objekat) {
        Set<LocalDate> datumi = new HashSet<>();
        for (Proslava proslava : proslave.values())
            if (proslava.getObjekat().equals(objekat))
                datumi.add(proslava.getDatum());
        return datumi;
    }

    public static ArrayList<Sto> stoloviZaObjekat(int idObjekat) {
        ArrayList<Sto> stoloviZaObjekat = new ArrayList<>();
        for (Sto sto : stolovi.values())
            if (sto.getObjekat().getId() == idObjekat)
                stoloviZaObjekat.add(sto);
        stoloviZaObjekat.sort(Sto.porediPoStolovima);
        return stoloviZaObjekat;
    }

    public static ArrayList<Meni> menijiZaObjekat(int idObjekat) {
        ArrayList<Meni> menijiZaObjekat = new ArrayList<>();
        for (Meni meni : meniji.values())
            if (meni.getObjekat().getId() == idObjekat)
                menijiZaObjekat.add(meni);
        return menijiZaObjekat;
    }

    public static void brisanjeStolovaIzBaze(int idObjekat) {
        Database.izbrisiIzBazeZaObjekatID("sto", idObjekat);
        try {
            stolovi = Database.ucitajStolove();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void brisanjeMenijaIzBaze(int idObjekat) {
        Database.izbrisiIzBazeZaObjekatID("meni", idObjekat);
        try {
            meniji = Database.ucitajMenije();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void transakcija(Klijent klijent, Objekat objekat, double iznosTransakcije) {
        BankovniRacun racunaKlijenta = bankovniRacuni.get(klijent.getBrojRacuna());
        BankovniRacun racunVlasnika = bankovniRacuni.get(objekat.getVlasnik().getBrojRacuna());

        racunaKlijenta.setStanje(racunaKlijenta.getStanje() - iznosTransakcije);
        racunVlasnika.setStanje(racunVlasnika.getStanje() + iznosTransakcije);
        objekat.setZarada(objekat.getZarada() + iznosTransakcije);

        Database.izmjeniZaraduObjekta(objekat.getId(), objekat.getZarada());
        Database.izmjeniStanjeRacuna(racunaKlijenta.getId(), racunaKlijenta.getStanje());
        Database.izmjeniStanjeRacuna(racunVlasnika.getId(), racunVlasnika.getStanje());
    }

    public static void dodajProslavu(Objekat objekat, Klijent klijent, LocalDate datum) {
        int id = Database.dodajProslavuUBazu(objekat.getId(), klijent.getId(), datum);
        proslave.put(id, new Proslava(id, objekat, klijent, datum));
    }

    public static void uredjenaProslava(Proslava proslava, double uplacenIznos) {
        proslava.setUplacenIznos(uplacenIznos);
        Database.uredjenaProslava(proslava.getId(), proslava.getUplacenIznos());
    }

    public static void dodajURaspored(Sto sto, Proslava proslava, ArrayList<String> gosti) {
        Raspored raspored = rasporedi.get(sto.getId() + "-" + proslava.getId());
        String gostiUpis = String.join(", ", gosti);
        if (raspored == null) {
            rasporedi.put(sto.getId() + "-" + proslava.getId(), new Raspored(sto, proslava, gosti));
            Database.dodajRasporedUBazu(sto.getId(), proslava.getId(), gostiUpis);
        } else {
            raspored.setGosti(gosti);
            Database.izmjeniRasporedUBazi(raspored);
        }
    }

    public static Map<Integer, Meni> getMeni() {
        return meniji;
    }

    public static Map<Integer, Objekat> getObjekti() {
        return objekti;
    }

    public static Map<Integer, Sto> getStolovi() {
        return stolovi;
    }

    public static Map<Integer, Proslava> getProslave() {
        return proslave;
    }

    public static Map<String, Raspored> getRasporedi() {
        return rasporedi;
    }
}