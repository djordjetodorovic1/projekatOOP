package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Model.Klijent;
import SistemZaPlaniranjeProslava.Model.Proslava;
import SistemZaPlaniranjeProslava.Model.StatusProslave;
import javafx.application.Platform;
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

public class ScenaKlijent {
    public static boolean scenaZaLozinkuAktivna = false;

    public static void scenaKlijent(Stage primaryStage, Klijent klijent) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Vasi podaci");
        Label lblPodnaslov1 = new Label("Izaberite objekat za novu proslavu");
        Label lblPodnaslov2 = new Label("Uredite vec rezervisane proslave");
        Label lblIme = new Label("Ime:");
        Label lblPrezime = new Label("Prezime:");
        Label lblKorisnickoIme = new Label("Korisnicko ime:");
        Label lblStanjeUBanci = new Label("Stanje na racunu:");
        Label lblNovaLozinka = new Label("Promijeni lozinku");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        TextField tfIme = new TextField(klijent.getIme());
        TextField tfPrezime = new TextField(klijent.getPrezime());
        TextField tfKorisnickoIme = new TextField(klijent.getKorisnickoIme());
        TextField tfStanjeUBanci = new TextField("" + Controller.getStanjeRacuna(klijent.getBrojRacuna()));
        tfIme.setEditable(false);
        tfPrezime.setEditable(false);
        tfKorisnickoIme.setEditable(false);
        tfStanjeUBanci.setEditable(false);

        CheckBox cbProtekle = new CheckBox("Protekle");
        CheckBox cbAktivne = new CheckBox("Aktivne");
        CheckBox cbOtkazane = new CheckBox("Otkazane");
        cbAktivne.setSelected(true);

        ListView<Proslava> lvProslave = new ListView<>();
        lvProslave.setOnMouseClicked(event -> {
            Proslava izabranaProslava = lvProslave.getSelectionModel().getSelectedItem();
            if (izabranaProslava != null) {
                //scenaInformacijeOProslavi(izabranaProslava);
            }
        });
        Runnable izmjeniListu = () -> {
            lvProslave.getItems().clear();
            for (Proslava pr : Controller.getProslave().values()) {
                if (pr.getKlijent().getId() == klijent.getId()) {
                    if (cbAktivne.isSelected() && pr.getStatus() == StatusProslave.AKTIVNA) {
                        lvProslave.getItems().add(pr);
                    } else if (cbProtekle.isSelected() && pr.getStatus() == StatusProslave.PROTEKLA) {
                        lvProslave.getItems().add(pr);
                    } else if (cbOtkazane.isSelected() && pr.getStatus() == StatusProslave.OTKAZANA) {
                        lvProslave.getItems().add(pr);
                    }
                }
            }
        };

        cbProtekle.setOnAction(event -> izmjeniListu.run());
        cbAktivne.setOnAction(event -> izmjeniListu.run());
        cbOtkazane.setOnAction(event -> izmjeniListu.run());
        izmjeniListu.run();

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        Button btnPromjeniLozinku = new Button("Nova lozinka");
        Button btnNovaProslavaObjekat = new Button("Izaberi objekat");
        btnPromjeniLozinku.setPadding(new Insets(10, 53, 10, 53));
        btnNovaProslavaObjekat.setPadding(new Insets(10, 53, 10, 53));

        btnNazad.setOnAction(actionEvent -> ScenaZaPrijavu.scenaPrijava(primaryStage));

        btnPromjeniLozinku.setOnAction(actionEvent -> {
            if (!scenaZaLozinkuAktivna) {
                scenaZaLozinkuAktivna = true;
                ScenaZaPromjenuLozinke.scenaZaPromjenuLozinke(klijent);
            }
        });

        btnNovaProslavaObjekat.setOnAction(event -> Controller.scenaBiranjeObjekta(primaryStage, klijent));

        VBox vLijeviLijevi = new VBox(22);
        vLijeviLijevi.getChildren().addAll(lblIme, lblPrezime, lblKorisnickoIme, lblStanjeUBanci);
        vLijeviLijevi.setAlignment(Pos.CENTER_RIGHT);
        VBox vLijeviDesni = new VBox(10);
        vLijeviDesni.getChildren().addAll(tfIme, tfPrezime, tfKorisnickoIme, tfStanjeUBanci);

        HBox hBoxLijevi = new HBox(10);
        hBoxLijevi.getChildren().addAll(vLijeviLijevi, vLijeviDesni);
        HBox hBoxDesni = new HBox(10);
        hBoxDesni.getChildren().addAll(cbProtekle, cbAktivne, cbOtkazane);

        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(lblPodnaslov2, lvProslave, hBoxDesni);
        vBoxDesni.setAlignment(Pos.CENTER);

        VBox vBoxLijevi = new VBox(10);
        vBoxLijevi.setAlignment(Pos.CENTER);
        vBoxLijevi.getChildren().addAll(hBoxLijevi, lblNovaLozinka, btnPromjeniLozinku, lblPodnaslov1, btnNovaProslavaObjekat);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vBoxLijevi, vBoxDesni);
        hBox.setPadding(new Insets(30, 0, 20, 0));

        VBox vBox = new VBox(1);
        vBox.getChildren().addAll(lblNaslov, hBox);
        vBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBox);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scenaNoviNalog = new Scene(root, 750, 600);
        primaryStage.setScene(scenaNoviNalog);
        primaryStage.show();
    }
}