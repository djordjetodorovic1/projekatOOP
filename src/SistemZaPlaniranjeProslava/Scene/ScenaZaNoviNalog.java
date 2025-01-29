package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class ScenaZaNoviNalog {
    public static void scenaNoviNalog(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Kreirajte novi nalog");
        Label lblIme = new Label("Unesite ime");
        Label lblPrezime = new Label("Unesite prezime");
        Label lblJBMG = new Label("Unesite JMBG");
        Label lblBrojUBanci = new Label("Unesite broj računa u banci");
        Label lblKorisnickoIme = new Label("Unesite korisničko ime");
        Label lblLozinka = new Label("Unesite lozinku");
        Label lblPotvrdaLozinke = new Label("Potvrdite lozinku");
        Label lblTipNaloga = new Label("Izaberite tip naloga");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        TextField tfIme = new TextField();
        TextField tfPrezime = new TextField();
        TextField tfJMBG = new TextField();
        TextField tfBrojUBanci = new TextField();
        TextField tfKorisnickoIme = new TextField();
        PasswordField pfLozinka = new PasswordField();
        PasswordField pfPotvrdaLozinke = new PasswordField();
        tfIme.setPromptText("Novak");
        tfPrezime.setPromptText("Djokovic");
        tfJMBG.setPromptText("2205987123456");
        tfBrojUBanci.setPromptText("1111222233334444");
        tfKorisnickoIme.setPromptText("novak_djokovic");
        pfLozinka.setPromptText("nole_24");
        pfPotvrdaLozinke.setPromptText("nole_24");

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNoviNalog = new Button("Kreiraj novi nalog");
        Button btnNazad = new Button("", prikazStrelice);

        ChoiceBox<String> cbTipNaloga = new ChoiceBox<>();
        cbTipNaloga.getItems().addAll("Klijent", "Vlasnik");
        cbTipNaloga.setValue("Klijent");
        cbTipNaloga.setPadding(new Insets(0, 53, 0, 53));

        btnNazad.setOnAction(actionEvent -> ScenaZaPrijavu.scenaPrijava(primaryStage));
        btnNoviNalog.setOnAction(event -> Controller.kreirajNoviNalog(primaryStage, tfIme, tfPrezime, tfJMBG, tfBrojUBanci, tfKorisnickoIme, pfLozinka, pfPotvrdaLozinke, cbTipNaloga));
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnNoviNalog.fire();
        });

        VBox vBoxLijevi = new VBox(10);
        vBoxLijevi.getChildren().addAll(lblIme, tfIme, lblPrezime, tfPrezime, lblJBMG, tfJMBG, lblBrojUBanci, tfBrojUBanci);
        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(lblKorisnickoIme, tfKorisnickoIme, lblLozinka, pfLozinka, lblPotvrdaLozinke, pfPotvrdaLozinke, lblTipNaloga, cbTipNaloga);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vBoxLijevi, vBoxDesni);
        hBox.setPadding(new Insets(40, 0, 40, 0));

        VBox vBoxUnos = new VBox(10);
        vBoxUnos.getChildren().addAll(lblNaslov, hBox, btnNoviNalog);
        vBoxUnos.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBoxUnos);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scenaNoviNalog = new Scene(root, 750, 600);
        primaryStage.setScene(scenaNoviNalog);
        primaryStage.show();
    }
}