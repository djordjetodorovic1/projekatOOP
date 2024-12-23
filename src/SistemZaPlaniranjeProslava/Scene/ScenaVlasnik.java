package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Model.Vlasnik;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class ScenaVlasnik {
    public static void scenaVlasnik(Stage primaryStage, Vlasnik vlasnik) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Vasi podaci");
        Label lblPodnaslov1 = new Label("Izaberite objekat za prikaz");
        Label lblPodnaslov2 = new Label("Dodajte novi objekat");
        Label lblIme = new Label("Ime:");
        Label lblPrezime = new Label("Prezime:");
        Label lblKorisnickoIme = new Label("Korisnicko ime:");
        Label lblStanjeUBanci = new Label("Stanje na racunu:");
        Label lblNovaLozinka = new Label("Promijeni lozinku");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");
        lblPodnaslov1.setStyle("-fx-font: 24 'Comic Sans MS';");
        lblPodnaslov2.setStyle("-fx-font: 24 'Comic Sans MS';");

        TextField tfIme = new TextField(vlasnik.getIme());
        TextField tfPrezime = new TextField(vlasnik.getPrezime());
        TextField tfKorisnickoIme = new TextField(vlasnik.getKorisnicko_ime());
        TextField tfStanjeUBanci = new TextField("" + Controller.getStanjeRacuna(vlasnik.getBroj_racuna()));
        tfIme.setEditable(false);
        tfPrezime.setEditable(false);
        tfKorisnickoIme.setEditable(false);
        tfStanjeUBanci.setEditable(false);

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        Button btnPromjeniLozinku = new Button("Nova lozinka");
        Button btnNoviObjekat = new Button("Kreiraj zahtjev");
        btnNoviObjekat.setPadding(new Insets(10, 53, 10, 53));

        ComboBox<String> cbObjekti = new ComboBox<>();
        cbObjekti.getItems().addAll();
        cbObjekti.setValue("Klijent");
        cbObjekti.setPadding(new Insets(5, 53, 5, 53));

        btnNazad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScenaZaPrijavu.scenaPrijava(primaryStage);
            }
        });

        btnPromjeniLozinku.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScenaZaPromjenuLozinke.scenaZaPromjenuLozinke(vlasnik);
            }
        });

        btnNoviObjekat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ScenaZaNoviObjekat.scenaNoviObjekat(primaryStage, vlasnik);
            }
        });

        VBox vLijeviLijevi = new VBox(22);
        vLijeviLijevi.getChildren().addAll(lblIme, lblPrezime, lblKorisnickoIme, lblStanjeUBanci);
        vLijeviLijevi.setAlignment(Pos.CENTER_RIGHT);
        VBox vLijeviDesni = new VBox(10);
        vLijeviDesni.getChildren().addAll(tfIme, tfPrezime, tfKorisnickoIme, tfStanjeUBanci);

        HBox hBoxLijevi = new HBox(10);
        hBoxLijevi.getChildren().addAll(vLijeviLijevi, vLijeviDesni);
        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(lblNovaLozinka, btnPromjeniLozinku);
        vBoxDesni.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(hBoxLijevi, vBoxDesni);
        hBox.setPadding(new Insets(30, 0, 20, 0));

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(lblNaslov, hBox, lblPodnaslov1, cbObjekti, lblPodnaslov2, btnNoviObjekat);
        vBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBox);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scenaNoviNalog = new Scene(root, 750, 600);
        primaryStage.setScene(scenaNoviNalog);
        primaryStage.show();
    }
}