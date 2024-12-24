package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Main;
import SistemZaPlaniranjeProslava.Model.Objekat;
import SistemZaPlaniranjeProslava.Validator;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ScenaZaNoviObjekat {
    private static List<Spinner<Integer>> spinnerBrojMijestaPoStolovima = new ArrayList<>();

    private static void scenaZaUnosStolova(int brojStolova) {
        Stage stage = new Stage();
        stage.setTitle("Kreiraj novi objekat");
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        Label lblNaslov = new Label("Unesite broj mijesta za svaki od stolova");
        lblNaslov.setStyle("-fx-font: 24 'Comic Sans MS';");
        root.getChildren().add(lblNaslov);

        VBox vBoxtf = new VBox(10);
        vBoxtf.setAlignment(Pos.CENTER);
        vBoxtf.setPadding(new Insets(10, 40, 10, 40));

        spinnerBrojMijestaPoStolovima.clear();
        for (int i = 0; i < brojStolova; i++) {
            Label lblSto = new Label("Sto broj " + (i + 1));
            Spinner<Integer> newSpinner = new Spinner<>(1, 100, 5);
            newSpinner.setPromptText("Sto broj " + (i + 1));

            //Ukloniti
            newSpinner.setMaxWidth(250);

            newSpinner.setEditable(true);
            vBoxtf.getChildren().addAll(lblSto, newSpinner);
            ScenaZaNoviObjekat.spinnerBrojMijestaPoStolovima.add(newSpinner);
        }

        Button btnSacuvaj = new Button("Sacuvaj izmjene");
        btnSacuvaj.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stage.close();
            }
        });

        ScrollPane scrollPane = new ScrollPane(vBoxtf);
        scrollPane.setMaxWidth(350);
        root.getChildren().addAll(scrollPane, btnSacuvaj);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scena = new Scene(root, 750, 600);
        stage.setScene(scena);
        stage.show();
    }

    public static void scenaNoviObjekat(Stage stageNoviObjekat, Vlasnik vlasnik, Map<Integer, Objekat> objekti) {
        stageNoviObjekat.setTitle("Kreiraj novi objekat");
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Dodaj novi objekat");
        Label lblNaziv = new Label("Unesite naziv");
        Label lblCijenaRezervacije = new Label("Unesite cijenu rezervacije");
        Label lblGrad = new Label("Unesite grad");
        Label lblAdresa = new Label("Unesite adresu");
        Label lblBrojMijesta = new Label("Unesite broj mijesta");
        Label lblBrojStolova = new Label("Unesite broj stolova");
        Label lblMijestaZaStolom = new Label("Unesite broj mijesta za svaki sto");
        Label lblMeni = new Label("Unesite meni i cijenu menija");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        TextField tfNaziv = new TextField();
        TextField tfCijenaRezervacije = new TextField();
        TextField tfGrad = new TextField();
        TextField tfAdresa = new TextField();
        TextField tfBrojMijesta = new TextField();
        TextField tfBrojStolova = new TextField();
        TextField tfMeni = new TextField();
        TextField tfCijenaMenija = new TextField();
        tfNaziv.setPromptText("Caffe Renas");
        tfCijenaRezervacije.setPromptText("200.00");
        tfGrad.setPromptText("Banja Luka");
        tfAdresa.setPromptText("Mladena Stojanovica 2");
        tfBrojMijesta.setPromptText("50");
        tfMeni.setPromptText("Predjelo - Glavno jelo - Dezert");
        tfCijenaMenija.setPromptText("50.00");
        tfMeni.setMaxWidth(150);
        tfCijenaMenija.setMaxWidth(150);

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNoviObjekat = new Button("Kreiraj novi nalog");
        Button btnBrojStolica = new Button("Unesi");
        Button btnNazad = new Button("", prikazStrelice);
        btnBrojStolica.setPadding(new Insets(5,50,5,50));

        btnNazad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScenaVlasnik.scenaVlasnik(stageNoviObjekat, vlasnik, objekti);
            }
        });

        btnBrojStolica.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (Validator.validacijaBroj(tfBrojStolova)) {
                    scenaZaUnosStolova(Integer.parseInt(tfBrojStolova.getText()));
                } else {
                    Main.upozorenje("Polje za broj stolova nije korektno popunjeno! Pokusajte ponovo");
                }
            }
        });

        btnNoviObjekat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ArrayList<Integer> brojMijestaPoStolovima = spinnerBrojMijestaPoStolovima.stream()
                        .map(spinner -> Integer.parseInt(spinner.getValue().toString()))
                        .collect(Collectors.toCollection(ArrayList::new));
                System.out.println("broj Mijesta:  " + brojMijestaPoStolovima);
                Controller.kreirajNoviObjekat(stageNoviObjekat, tfNaziv, tfGrad, tfAdresa, tfCijenaRezervacije, tfBrojMijesta, tfBrojStolova, tfMeni, tfCijenaMenija, vlasnik, brojMijestaPoStolovima);
            }
        });
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnNoviObjekat.fire();
            }
        });

        VBox vBoxLijevi = new VBox(10);
        vBoxLijevi.getChildren().addAll(lblNaziv, tfNaziv, lblGrad, tfGrad, lblAdresa, tfAdresa, lblCijenaRezervacije, tfCijenaRezervacije);
        HBox hBoxMeni = new HBox(10);
        hBoxMeni.getChildren().addAll(tfMeni, tfCijenaMenija);
        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(lblBrojMijesta, tfBrojMijesta, lblBrojStolova, tfBrojStolova, lblMijestaZaStolom, btnBrojStolica, lblMeni, hBoxMeni);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vBoxLijevi, vBoxDesni);
        hBox.setPadding(new Insets(40, 0, 40, 0));

        VBox vBoxUnos = new VBox(10);
        vBoxUnos.getChildren().addAll(lblNaslov, hBox, btnNoviObjekat);
        vBoxUnos.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBoxUnos);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scena = new Scene(root, 750, 600);
        stageNoviObjekat.setScene(scena);
        stageNoviObjekat.show();
    }
}