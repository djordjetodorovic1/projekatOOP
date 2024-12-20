package SistemZaPlaniranjeProslava;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

public class Database{
    private static String DB_user = "root";
    private static String DB_password = "";
    private static String connectionUrl;
    private static int port = 3306;
    private static String DB_name = "sistemzaplaniranjeproslava";
    private static Connection connection;

    private static Admin admin = null;
    private static Obavjestenje obavjestenje = null;
    private static HashMap<Integer, BankovniRacun> bankovniRacuni = new HashMap<>();
    private static HashMap<Integer, Vlasnik> vlasnici = new HashMap<>();
    private static HashMap<Integer, Klijent> klijenti = new HashMap<>();

    private static void DBConnect() throws SQLException {
        connectionUrl = "jdbc:mysql://localhost" + ":" + port + "/" + DB_name;
        connection = DriverManager.getConnection(connectionUrl, DB_user, DB_password);
    }

    private static void loadFromDatabase() throws SQLException {
        ResultSet resultSet = null;
        Statement statement = connection.createStatement();
        String SQLQuery = "SELECT * FROM admin";
        resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            admin = new Admin(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5));

        SQLQuery = "SELECT * FROM `bankovni racun`";
        resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            bankovniRacuni.put(resultSet.getInt(1), new BankovniRacun(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getDouble(4)));
        bankovniRacuni.forEach((k, v) -> System.out.println(k + ": " + v));

        SQLQuery = "SELECT * FROM klijent";
        resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            klijenti.put(resultSet.getInt(1), new Klijent(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7)));
        klijenti.forEach((k, v) -> System.out.println(k + ": " + v));

        SQLQuery = "SELECT * FROM vlasnik";
        resultSet = statement.executeQuery(SQLQuery);
        while (resultSet.next())
            vlasnici.put(resultSet.getInt(1), new Vlasnik(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getString(4), resultSet.getString(5), resultSet.getString(6), resultSet.getString(7)));
        vlasnici.forEach((k, v) -> System.out.println(k + ": " + v));
        statement.close();
    }

    public static void connectWithDB() {
        try {
            DBConnect();
            System.out.println("Uspjesno ste se konektovali na bazu:" + connectionUrl);
            loadFromDatabase();
            connection.close();
        } catch (SQLException e){
            e.printStackTrace();
        }

        System.out.println(admin);
    }
}