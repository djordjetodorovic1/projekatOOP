package SistemZaPlaniranjeProslava;

import SistemZaPlaniranjeProslava.Model.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Database {
    private static Connection connection;

    private static Admin admin = null;
    private static ArrayList<Obavjestenje> obavjestenja = new ArrayList<>();
    private static Map<String, BankovniRacun> bankovniRacuni = new HashMap<>();
    private static Map<String, Vlasnik> vlasnici = new HashMap<>();
    private static Map<String, Klijent> klijenti = new HashMap<>();
    private static Map<Integer, Objekat> objekti = new HashMap<>();
    private static Map<Integer, Sto> stolovi = new HashMap<>();
    private static Map<Integer, Meni> meniji = new HashMap<>();

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

    public static Map<Integer, Objekat> ucitajObjekte() throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM objekat";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            objekti.put(resultSet.getInt(1), new Objekat(resultSet.getInt(1), getVlasnik(resultSet.getInt(2)), resultSet.getString(3),
                    resultSet.getDouble(4), resultSet.getString(5), resultSet.getString(6), resultSet.getInt(7),
                    resultSet.getInt(8), resultSet.getString(9), resultSet.getDouble(10), StatusObjekta.valueOf(resultSet.getString(11))));
        statement.close();
        return objekti;
    }

    public static Map<Integer, Sto> ucitajStolove() throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM sto";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            stolovi.put(resultSet.getInt(1), new Sto(resultSet.getInt(1), objekti.get(resultSet.getInt(2)), resultSet.getInt(3)));
        statement.close();
        return stolovi;
    }

    public static Map<Integer, Meni> ucitajMenije() throws SQLException {
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM meni";
        ResultSet resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            meniji.put(resultSet.getInt(1), new Meni(resultSet.getInt(1), objekti.get(resultSet.getInt(2)), resultSet.getString(3),
                    resultSet.getDouble(4)));
        statement.close();
        return meniji;
    }

    public static ArrayList<Obavjestenje> ucitajObavjestenje() {
        try {
            Statement statement = connection.createStatement();
            String SQLQuery = "SELECT * FROM obavjestenje";
            ResultSet resultSet = statement.executeQuery(SQLQuery);
            while (resultSet.next())
                obavjestenja.add(new Obavjestenje(resultSet.getInt(1), objekti.get(resultSet.getInt(2)), resultSet.getString(3)));
            statement.close();
            return obavjestenja;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void izmjeniUBazi(String SQLQuery) {
        /*try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(SQLQuery);
            statement.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }*/
    }

    public static void dodajUBazu(String SQLQuery) {
        izmjeniUBazi(SQLQuery);
    }


    public static void izbrisiIzBaze(String SQLQuery) {
        izmjeniUBazi(SQLQuery);
    }

    public static int procitajID(String SQLQuery) {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(SQLQuery);
            int id = 0;
            if (resultSet.next())
                id = resultSet.getInt(1);
            statement.close();
            return id;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}