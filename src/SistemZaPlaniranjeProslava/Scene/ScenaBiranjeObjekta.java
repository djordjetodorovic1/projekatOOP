package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Model.Klijent;
import SistemZaPlaniranjeProslava.Model.Objekat;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ScenaBiranjeObjekta {
    private static LocalDate datum;

    public static void scenaBiranjeObjekta(Stage primaryStage, ArrayList<Objekat> objekti, Klijent klijent) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Izaberite objekat za novu proslavu");
        Label lblBrMjesta = new Label("Izaberite potreban broj mjesta");
        Label lblKalendar = new Label("Izaberite datum proslave");
        Label lblGrad = new Label("Izaberite grad");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        TextField tfBrMjesta = new TextField();
        tfBrMjesta.setPromptText("100");

        DatePicker kalendar = new DatePicker();

        ChoiceBox<String> cbGradovi = new ChoiceBox<>();
        Set<String> skupGradova = new HashSet<>();
        skupGradova.add("");
        for (Objekat objekat : objekti)
            skupGradova.add(objekat.getGrad().toUpperCase());
        cbGradovi.getItems().addAll(skupGradova);
        cbGradovi.setValue("");
        cbGradovi.setPadding(new Insets(5, 50, 5, 50));

        ListView<Objekat> lvObjekti = new ListView<>();
        lvObjekti.setOnMouseClicked(event -> {
            Objekat izabraniObjekat = lvObjekti.getSelectionModel().getSelectedItem();
            if (izabraniObjekat != null)
                ScenaRezervacijaObjekta.scenaRezervacijaObjekta(primaryStage, izabraniObjekat, klijent);
        });
        Runnable izmjeniListu = () -> {
            lvObjekti.getItems().clear();
            boolean zadovoljava;
            for (Objekat objekat : objekti) {
                zadovoljava = true;
                if (!tfBrMjesta.getText().isEmpty()) {
                    try {
                        if (objekat.getBrojMjesta() < Integer.parseInt(tfBrMjesta.getText()))
                            zadovoljava = false;
                    } catch (NumberFormatException e) {
                        zadovoljava = false;
                    }
                }
                if (datum != null && Controller.zauzetObjekatZaDatum(datum, objekat))
                    zadovoljava = false;
                if (!cbGradovi.getValue().isEmpty() && !objekat.getGrad().toUpperCase().equals(cbGradovi.getValue()))
                    zadovoljava = false;
                if (zadovoljava)
                    lvObjekti.getItems().add(objekat);
            }
        };
        izmjeniListu.run();
        tfBrMjesta.textProperty().addListener((observable, oldValue, newValue) -> izmjeniListu.run());
        kalendar.setOnAction(event -> {
            datum = kalendar.getValue();
            izmjeniListu.run();
        });
        cbGradovi.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> izmjeniListu.run());

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        Button btnReset = new Button("Obriši filtere");
        btnReset.setPadding(new Insets(10, 30, 10, 30));

        btnNazad.setOnAction(actionEvent -> {
            btnReset.fire();
            ScenaKlijent.scenaKlijent(primaryStage, klijent);
        });
        btnReset.setOnAction(actionEvent -> {
            tfBrMjesta.clear();
            kalendar.setValue(null);
            cbGradovi.setValue("");
        });

        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(lblBrMjesta, tfBrMjesta, lblKalendar, kalendar, lblGrad, cbGradovi, btnReset);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(lvObjekti, vBoxDesni);

        VBox vBoxInfo = new VBox(10);
        vBoxInfo.getChildren().addAll(lblNaslov, hBox);
        vBoxInfo.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBoxInfo);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Scene scena = new Scene(root, 750, 600);
        primaryStage.setScene(scena);
        primaryStage.show();
    }
}