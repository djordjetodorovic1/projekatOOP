package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Main;
import SistemZaPlaniranjeProslava.Model.Klijent;
import SistemZaPlaniranjeProslava.Model.Meni;
import SistemZaPlaniranjeProslava.Model.Objekat;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Set;

public class ScenaRezervacijaObjekta {
    private static void scenaZaLozinku(Stage primaryStage, Klijent klijent, Objekat objekat, LocalDate datum) {
        Stage stagePotvrdaLozinke = new Stage();
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        Label lblLozinka = new Label("Unesite lozinku");
        PasswordField pfLozinka = new PasswordField();
        pfLozinka.setMaxWidth(200);
        Button btnPotvrda = new Button("Potvrda lozinke");

        btnPotvrda.setOnAction(actionEvent -> {
            if (pfLozinka.getText().equals(klijent.getLozinka())) {
                if (Controller.getStanjeRacuna(klijent.getBrojRacuna()) >= objekat.getCijenaRezervacije()) {
                    Controller.transakcija(klijent, objekat, objekat.getCijenaRezervacije());
                    Controller.dodajProslavu(objekat, klijent, datum);
                    Main.informacija("Trenutno stanje na računu: " + Controller.getStanjeRacuna(klijent.getBrojRacuna()) + " Proslava je uspješno rezervisana!");
                } else
                    Main.upozorenje("Rezervacija proslave nije moguća zbog nedovoljno novca na racunu!");
                stagePotvrdaLozinke.close();
                ScenaKlijent.scenaKlijent(primaryStage, klijent);
            } else {
                Main.upozorenje("Pogrešna lozinka! Pokušajte ponovo");
                Main.ocistiPolje(pfLozinka);
            }
        });
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnPotvrda.fire();
        });

        root.getChildren().addAll(lblLozinka, pfLozinka, btnPotvrda);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Scene scena = new Scene(root, 400, 300);
        stagePotvrdaLozinke.setScene(scena);
        stagePotvrdaLozinke.show();
    }

    public static void scenaRezervacijaObjekta(Stage primaryStage, Objekat objekat, Klijent klijent) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Rezervišite objekat");
        Label lblNaziv = new Label("Naziv: \"" + objekat.getNaziv() + "\"");
        Label lblAdresa = new Label("Adresa: " + objekat.getAdresa() + ", " + objekat.getGrad());
        Label lblCijenaRezervacije = new Label("Cijena rezervacije: " + objekat.getCijenaRezervacije());
        Label lblBrojMjesta = new Label("Broj mjesta: " + objekat.getBrojMjesta());
        Label lblBrojStolova = new Label("Broj stolova: " + objekat.getBrojStolova());
        Label lblMeni = new Label("Meniji");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        TextArea taMeni = new TextArea();
        taMeni.setEditable(false);
        taMeni.setPrefWidth(350);
        int brojMenija = 1;
        ArrayList<Meni> meniji = Controller.menijiZaObjekat(objekat.getId());
        for (Meni meni : meniji)
            if (meni.getObjekat().equals(objekat))
                taMeni.appendText("Meni " + brojMenija++ + ": " + meni + "\n");

        Set<LocalDate> zauzetiDatumi = Controller.zauzetiDatumi(objekat);
        DatePicker kalendar = new DatePicker();
        Callback<DatePicker, DateCell> izmjenaPolja = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (zauzetiDatumi.contains(item)) {
                    setStyle("-fx-background-color: #ff0000; -fx-text-fill: #ffffff;");
                    setDisable(true);
                } else if (item.isBefore(LocalDate.now()))
                    setDisable(true);
            }
        };
        kalendar.setDayCellFactory(izmjenaPolja);
        DatePickerSkin kalendarSkin = new DatePickerSkin(kalendar);

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnPotvrdi = new Button("Potvrdi rezervaciju");
        Button btnNazad = new Button("", prikazStrelice);

        btnNazad.setOnAction(actionEvent -> Controller.scenaBiranjeObjekta(primaryStage, klijent));
        btnPotvrdi.setOnAction(event -> {
            if (kalendar.getValue() != null)
                scenaZaLozinku(primaryStage, klijent, objekat, kalendar.getValue());
            else
                Main.upozorenje("Niste odabrali datum! Pokušajte ponovo");
        });
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnPotvrdi.fire();
        });

        VBox vBoxLijevi = new VBox(10);
        vBoxLijevi.getChildren().addAll(lblNaziv, lblAdresa, lblCijenaRezervacije, lblBrojMjesta, lblBrojStolova, lblMeni, taMeni);
        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(kalendarSkin.getPopupContent(), btnPotvrdi);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vBoxLijevi, vBoxDesni);
        hBox.setPadding(new Insets(40, 0, 40, 0));

        VBox vBoxUnos = new VBox(10);
        vBoxUnos.getChildren().addAll(lblNaslov, hBox);
        vBoxUnos.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBoxUnos);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Scene scena = new Scene(root, 750, 600);
        primaryStage.setScene(scena);
        primaryStage.show();
    }
}