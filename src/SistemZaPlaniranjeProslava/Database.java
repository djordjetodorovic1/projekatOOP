package SistemZaPlaniranjeProslava;

import SistemZaPlaniranjeProslava.Model.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Connection connection;

    private static Admin admin = null;
    private static Map<String, Vlasnik> vlasnici = new HashMap<>();
    private static Map<String, Klijent> klijenti = new HashMap<>();
    private static Map<Integer, Objekat> objekti = new HashMap<>();
    private static Map<Integer, Sto> stolovi = new HashMap<>();
    private static Map<Integer, Meni> meniji = new HashMap<>();
    private static Map<Integer, Proslava> proslave = new HashMap<>();
    private static Map<String, Raspored> rasporedi = new HashMap<>();

    public static void connectWithDB() {
        try {
            int port = 3306;
            String DB_name = "sistemzaplaniranjeproslava";
            String connectionUrl = "jdbc:mysql://localhost" + ":" + port + "/" + DB_name;
            String DB_user = "root";
            String DB_password = "";
            connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
            System.out.println("Uspjesno ste se konektovali na bazu:" + connectionUrl);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void disconnectDB() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Admin ucitajAdmina() throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM admin";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            admin = new Admin(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5));
        statement.close();
        return admin;
    }

    public static Map<String, BankovniRacun> ucitajBankovneRacune() throws SQLException {
        Map<String, BankovniRacun> bankovniRacuni = new HashMap<>();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM `bankovni racun`";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            bankovniRacuni.put(resultSet.getString(2), new BankovniRacun(resultSet.getInt(1), resultSet.getString(2),
                    resultSet.getString(3), resultSet.getDouble(4)));
        statement.close();
        return bankovniRacuni;
    }

    public static Map<String, Klijent> ucitajKlijente() throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM klijent";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            klijenti.put(resultSet.getString(6), new Klijent(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7)));
        statement.close();
        return klijenti;
    }

    public static Map<String, Vlasnik> ucitajVlasnike() throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM vlasnik";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            vlasnici.put(resultSet.getString(6), new Vlasnik(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3),
                    resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7)));
        statement.close();
        return vlasnici;
    }

    private static Vlasnik getVlasnik(int id) {
        for (Vlasnik vlasnik : vlasnici.values())
            if (vlasnik.getId() == id)
                return vlasnik;
        return null;
    }

    private static Klijent getKlijent(int id) {
        for (Klijent klijent : klijenti.values())
            if (klijent.getId() == id)
                return klijent;
        return null;
    }

    public static Map<Integer, Objekat> ucitajObjekte() throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM objekat";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            objekti.put(resultSet.getInt(1), new Objekat(resultSet.getInt(1), getVlasnik(resultSet.getInt(2)), resultSet.getString(3),
                    resultSet.getDouble(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7),
                    resultSet.getInt(8), resultSet.getDouble(10), StatusObjekta.valueOf(resultSet.getString(11))));
        statement.close();
        return objekti;
    }

    public static Map<Integer, Sto> ucitajStolove() throws SQLException {
        stolovi.clear();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM sto";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            stolovi.put(resultSet.getInt(1), new Sto(resultSet.getInt(1), objekti.get(resultSet.getInt(2)), resultSet.getInt(3)));
        statement.close();
        return stolovi;
    }

    public static Map<Integer, Meni> ucitajMenije() throws SQLException {
        meniji.clear();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM meni";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            meniji.put(resultSet.getInt(1), new Meni(resultSet.getInt(1), objekti.get(resultSet.getInt(2)), resultSet.getString(3),
                    resultSet.getDouble(4)));
        statement.close();
        return meniji;
    }

    public static ArrayList<Obavjestenje> ucitajObavjestenje() throws SQLException {
        ArrayList<Obavjestenje> obavjestenja = new ArrayList<>();
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM obavjestenje";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            obavjestenja.add(new Obavjestenje(resultSet.getInt(1), objekti.get(resultSet.getInt(2)), resultSet.getString(3)));
        statement.close();
        return obavjestenja;
    }

    public static Map<Integer, Proslava> ucitajProslave() throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM proslava";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            proslave.put(resultSet.getInt(1), new Proslava(resultSet.getInt(1), objekti.get(resultSet.getInt(2)),
                    getKlijent(resultSet.getInt(3)), meniji.get(resultSet.getInt(4)), resultSet.getDate(5).toLocalDate(), resultSet.getInt(6),
                    resultSet.getDouble(7), resultSet.getDouble(8)));
        statement.close();
        return proslave;
    }

    public static Map<String, Raspored> ucitajRasporede() throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM raspored";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            rasporedi.put(resultSet.getInt(1) + "-" + resultSet.getInt(2), new Raspored(stolovi.get(resultSet.getInt(1)), proslave.get(resultSet.getInt(2)), resultSet.getString(3)));
        statement.close();
        return rasporedi;
    }

    private static int procitajID(String SQLQuery) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        int id = 0;
        if (resultSet.next())
            id = resultSet.getInt(1);
        statement.close();
        return id;

    }

    public static int dodajNalogUBazu(String nalog, String ime, String prezime, String jmbg, String brojUBanci, String korisnickoIme, String lozinka) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "INSERT INTO `" + nalog + "`(`ime`, `prezime`, `jmbg`, `broj_racuna`, `korisnicko_ime`, `lozinka`) VALUES ('" + ime + "','"
                    + prezime + "','" + jmbg + "','" + brojUBanci + "','" + korisnickoIme + "','" + lozinka + "')";
            statement.executeUpdate(SQLQuery);
            statement.close();

            return procitajID("SELECT id FROM " + nalog + " WHERE korisnicko_ime = '" + korisnickoIme + "'");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int dodajObjekatUBazu(int vlasnikID, String naziv, double cijenaRezervacije, String grad, String adresa, int brojMjesta, int brojStolova) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "INSERT INTO `objekat` (`Vlasnik_id`, `naziv`, `cijena_rezervacije`, `grad`, `adresa`, `broj_mjesta`, `broj_stolova`, `datumi`, `zarada`) VALUES ("
                    + vlasnikID + ", '" + naziv + "', " + cijenaRezervacije + ", '" + grad + "', '" + adresa + "', "
                    + brojMjesta + ", " + brojStolova + ", '', " + 0.0 + ")";
            statement.executeUpdate(SQLQuery);
            statement.close();

            return procitajID("SELECT id FROM objekat ORDER BY id DESC LIMIT 1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int dodajMeniUBazu(int idObjekat, String meniOpis, double meniCijena) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "INSERT INTO `meni`(`Objekat_id`, `opis`, `cijena_po_osobi`) VALUES (" + idObjekat + ",'" + meniOpis + "'," + meniCijena + ")";

            statement.executeUpdate(SQLQuery);
            statement.close();

            return procitajID("SELECT id FROM meni ORDER BY id DESC LIMIT 1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int dodajStoUBazu(int idObjekat, int brojMjesta) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "INSERT INTO `sto`(`Objekat_id`, `broj_mjesta`) VALUES (" + idObjekat + "," + brojMjesta + ")";
            statement.executeUpdate(SQLQuery);
            statement.close();

            return procitajID("SELECT id FROM sto ORDER BY id DESC LIMIT 1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int dodajObavjestenjeUBazu(int idObjekat, String poruka) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "INSERT INTO `obavjestenje`(`Objekat_id`, `tekst`) VALUES (" + idObjekat + ",'" + poruka + "')";
            statement.executeUpdate(SQLQuery);
            statement.close();

            return procitajID("SELECT id FROM obavjestenje ORDER BY id DESC LIMIT 1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int dodajProslavuUBazu(int idObjekta, int idKlijent, LocalDate datum) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "INSERT INTO `proslava`(`Objekat_id`, `Klijent_id`, `datum`) VALUES (" + idObjekta + "," + idKlijent + ",'" + Date.valueOf(datum) + "')";
            statement.executeUpdate(SQLQuery);
            statement.close();

            return procitajID("SELECT id FROM proslava ORDER BY id DESC LIMIT 1");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void dodajRasporedUBazu(int idSto, int idProslava, String gosti) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "INSERT INTO `raspored`(`idSto`, `idProslava`, `gosti`) VALUES (" + idSto + "," + idProslava + ",'" + gosti + "')";
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void izmjeniLozinku(String nalog, String lozinka, String korisnickoIme) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "UPDATE `" + nalog + "` SET `lozinka`='" + lozinka + "' WHERE korisnicko_ime = '" + korisnickoIme + "'";
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void izmjeniObjekatUBazi(Double cijenaRezervacije, int brojMjesta, int brojStolova, int idObjekat) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "UPDATE `objekat` SET `cijena_rezervacije`=" + cijenaRezervacije + ",`broj_mjesta`=" + brojMjesta
                    + ",`broj_stolova`=" + brojStolova + ", `status`='" + StatusObjekta.NA_CEKANJU + "' WHERE id=" + idObjekat;
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void objekatObradjen(int idObjekat, StatusObjekta statusObjekta) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "UPDATE `objekat` SET `status`='" + statusObjekta + "' WHERE id=" + idObjekat;
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void izbrisiIzBazeZaObjekatID(String tabela, int objekatID) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "DELETE FROM `" + tabela + "` WHERE Objekat_id =" + objekatID;
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void izbrisiObavjestenjeIzBaze(int obavjestenjeID) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "DELETE FROM `obavjestenje` WHERE id =" + obavjestenjeID;
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void izmjeniStanjeRacuna(int idRacuna, double stanje) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "UPDATE `bankovni racun` SET `stanje`=" + stanje + " WHERE id=" + idRacuna;
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void izmjeniProslavu(Proslava proslava) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "UPDATE `proslava` SET `Meni_id`=" + proslava.getMeni().getId() + ",`broj_gostiju`=" + proslava.getBrojGostiju() + ",`ukupna_cijena`= " + proslava.getUkupnaCijena() + " WHERE id = " + proslava.getId();
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void uredjenaProslava(int proslavaID, double uplacenIznos) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "UPDATE `proslava` SET `uplacen_iznos`=" + uplacenIznos + " WHERE id = " + proslavaID;
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void otkazanaProslava(int proslavaID, LocalDate datum) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "UPDATE `proslava` SET `datum`='" + Date.valueOf(datum) + "' WHERE id = " + proslavaID;
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void izmjeniRasporedUBazi(Raspored raspored) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "UPDATE `raspored` SET `gosti`='" + String.join(", ", raspored.getGosti()) + "' WHERE idSto = " + raspored.getSto().getId() + " && idProslava = " + raspored.getProslava().getId();
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void izmjeniZaraduObjekta(int idObjekat, double zarada) {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "UPDATE `objekat` SET `zarada`=" + zarada + " WHERE id=" + idObjekat;
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}