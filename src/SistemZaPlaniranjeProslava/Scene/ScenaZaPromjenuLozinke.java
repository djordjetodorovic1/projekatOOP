package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Model.Korisnik;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class ScenaZaPromjenuLozinke {
    public static void scenaZaPromjenuLozinke(Korisnik korisnik) {
        Stage stageLozinka = new Stage();
        stageLozinka.setTitle("Promjena lozinke");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Promjena lozinke");
        Label lblKorisnickoIme = new Label("Korisnicko ime");
        Label lblStaraLozinka = new Label("Unesite novu lozinku");
        Label lblNovaLozinka = new Label("Potvrdite novu lozinku");
        Label lblPotvrdaLozinke = new Label("Ime:");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        TextField tfKorisnickoIme = new TextField(korisnik.getKorisnicko_ime());
        tfKorisnickoIme.setEditable(false);
        TextField tfStaraLozinka = new TextField();
        PasswordField pfNovaLozinka = new PasswordField();
        PasswordField pfPotvrdaLozinke = new PasswordField();

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        Button btnPromjeniLozinku = new Button("Potvrdi");

        btnNazad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stageLozinka.close();
            }
        });

        btnPromjeniLozinku.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (Controller.promjenaLozinke(korisnik, tfStaraLozinka, pfNovaLozinka, pfPotvrdaLozinke))
                    stageLozinka.close();
            }
        });
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnPromjeniLozinku.fire();
            }
        });

        VBox vBoxLijevi = new VBox(10);
        vBoxLijevi.getChildren().addAll(lblKorisnickoIme, tfKorisnickoIme, lblStaraLozinka, tfStaraLozinka, lblNovaLozinka, pfNovaLozinka, lblPotvrdaLozinke, pfPotvrdaLozinke);

        HBox hBox = new HBox(50);
        hBox.getChildren().addAll(vBoxLijevi, btnPromjeniLozinku);
        hBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(lblNaslov, hBox);
        vBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBox);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scenaNoviNalog = new Scene(root, 750, 600);
        stageLozinka.setScene(scenaNoviNalog);
        stageLozinka.show();
    }
}