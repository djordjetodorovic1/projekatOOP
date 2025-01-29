package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;

public class ScenaZaPrijavu {
    public static void scenaPrijava(Stage primaryStage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Image logo = new Image((new File("resursi/logo.jpg")).toURI().toString());
        ImageView logoPrikaz = new ImageView(logo);
        logoPrikaz.setFitWidth(350);
        logoPrikaz.setPreserveRatio(true);

        Label lblNaziv = new Label("Slobodan sto");
        Label lblKorisnickoIme = new Label("Unesite korisničko ime");
        Label lblLozinka = new Label("Unesite lozinku");
        Label lblIli = new Label("ili");
        lblNaziv.setStyle("-fx-text-fill: #4d6a88; -fx-font: 40 'Comic Sans MS';");

        TextField tfKorisnickoIme = new TextField();
        PasswordField pfLozinka = new PasswordField();
        tfKorisnickoIme.setPromptText("novak_djokovic");
        pfLozinka.setPromptText("nole_24");

        Button btnPrijava = new Button("Prijavi me");
        Button btnNoviNalog = new Button("Kreiraj novi nalog");

        btnPrijava.setOnAction(event -> Controller.prijava(primaryStage, tfKorisnickoIme, pfLozinka));
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnPrijava.fire();
        });
        btnNoviNalog.setOnAction(event -> ScenaZaNoviNalog.scenaNoviNalog(primaryStage));

        VBox vBoxLogIn = new VBox();
        vBoxLogIn.getChildren().addAll(lblKorisnickoIme, tfKorisnickoIme, lblLozinka, pfLozinka);

        HBox hBoxLogIn = new HBox(40);
        hBoxLogIn.getChildren().addAll(vBoxLogIn, btnPrijava);
        hBoxLogIn.setAlignment(Pos.CENTER);
        hBoxLogIn.setPadding(new Insets(10, 0, 10, 0));

        root.getChildren().addAll(lblNaziv, logoPrikaz, hBoxLogIn, lblIli, btnNoviNalog);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scenaPrijava = new Scene(root, 750, 600);
        primaryStage.setScene(scenaPrijava);
        primaryStage.show();
    }
}