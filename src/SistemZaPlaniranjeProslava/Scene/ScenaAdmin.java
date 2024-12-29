package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Model.*;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

public class ScenaAdmin {
    public static boolean scenaZaLozinkuAktivna = false;
    private static String poruka;

    public static void scenaObjekatAdmin(Obavjestenje obavjestenje, Map<Integer, Sto> stolovi, Map<Integer, Meni> meniji, Runnable nakonObrade) {
        Stage stageObjekatAdmin = new Stage();
        stageObjekatAdmin.setTitle("Pregled objekta");
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Podaci o objektu");
        Label lblNaziv = new Label("Naziv");
        Label lblCijenaRezervacije = new Label("Cijena rezervacije");
        Label lblGrad = new Label("Grad");
        Label lblAdresa = new Label("Adresu");
        Label lblBrojMjesta = new Label("Broj mjesta u objektu");
        Label lblBrojStolova = new Label("Broj stolova");
        Label lblMjestaZaStolom = new Label("Broj mjesta za svaki sto");
        Label lblMeni = new Label("Meniji");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        TextField tfNaziv = new TextField();
        TextField tfCijenaRezervacije = new TextField();
        TextField tfGrad = new TextField();
        TextField tfAdresa = new TextField();
        TextField tfBrojMjesta = new TextField();
        TextField tfBrojStolova = new TextField();
        tfNaziv.setText(obavjestenje.getObjekat().getNaziv());
        tfCijenaRezervacije.setText("" + obavjestenje.getObjekat().getCijenaRezervacije());
        tfGrad.setText(obavjestenje.getObjekat().getGrad());
        tfAdresa.setText(obavjestenje.getObjekat().getAdresa());
        tfBrojMjesta.setText("" + obavjestenje.getObjekat().getBrojMjesta());
        tfBrojStolova.setText("" + obavjestenje.getObjekat().getBrojStolova());
        tfNaziv.setEditable(false);
        tfGrad.setEditable(false);
        tfAdresa.setEditable(false);
        tfBrojMjesta.setEditable(false);
        tfCijenaRezervacije.setEditable(false);
        tfBrojStolova.setEditable(false);

        TextArea taStolovi = new TextArea();
        taStolovi.setEditable(false);
        int brojStola = 1;
        for (Sto sto : stolovi.values()) {
            if (sto.getObjekat().getId() == obavjestenje.getObjekat().getId()) {
                taStolovi.appendText("Sto " + brojStola++ + ": " + sto.getBrojMjesta() + " mjesta\n");
            }
        }

        TextArea taMeni = new TextArea();
        taMeni.setEditable(false);
        int brojMenija = 1;
        for (Meni meni : meniji.values()) {
            if (meni.getObjekat().getId() == obavjestenje.getObjekat().getId()) {
                taMeni.appendText("Meni " + brojMenija++ + ": " + meni.getOpis() + " - " + meni.getCijenaPoOsobi() + "\n");
            }
        }

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        Button btnProvjera = new Button("Provjeri objekat");
        Button btnOdobri = new Button("Odobri");
        Button btnOdbij = new Button("Odbij");
        btnOdobri.setBackground(Background.fill(Color.GREEN));
        btnOdobri.setTextFill(Color.WHITE);
        btnOdobri.setPadding(new Insets(8, 10, 8, 10));
        btnOdbij.setBackground(Background.fill(Color.RED));
        btnOdbij.setTextFill(Color.WHITE);
        btnOdbij.setPadding(new Insets(8, 10, 8, 10));

        btnNazad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                stageObjekatAdmin.close();
            }
        });

        btnProvjera.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                poruka = Controller.provjeriObjekatZaOdobrenje(obavjestenje.getObjekat());
            }
        });

        btnOdobri.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btnOdobri.setBackground(Background.fill(Color.LIGHTGREEN));
            }
        });

        btnOdobri.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.promjenaStatusaObjekta(obavjestenje.getObjekat(), StatusObjekta.ODOBREN, poruka, obavjestenje);
                stageObjekatAdmin.close();
                nakonObrade.run();
            }
        });

        btnOdbij.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                btnOdbij.setBackground(Background.fill(Color.ORANGERED));
            }
        });

        btnOdbij.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Controller.promjenaStatusaObjekta(obavjestenje.getObjekat(), StatusObjekta.ODBIJEN, poruka, obavjestenje);
                stageObjekatAdmin.close();
                nakonObrade.run();
            }
        });

        VBox vBoxLijevi = new VBox(10);
        vBoxLijevi.getChildren().addAll(lblNaziv, tfNaziv, lblGrad, tfGrad, lblAdresa, tfAdresa, lblCijenaRezervacije,
                tfCijenaRezervacije, lblBrojMjesta, tfBrojMjesta, lblBrojStolova, tfBrojStolova, btnProvjera);
        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(lblMjestaZaStolom, taStolovi, lblMeni, taMeni);

        HBox hBoxGornji = new HBox(40);
        hBoxGornji.setAlignment(Pos.CENTER);
        hBoxGornji.getChildren().addAll(vBoxLijevi, vBoxDesni);

        HBox hBoxDonji = new HBox(80);
        hBoxDonji.setAlignment(Pos.CENTER);
        hBoxDonji.getChildren().addAll(btnOdobri, btnOdbij);

        VBox vBoxInfo = new VBox(10);
        vBoxInfo.getChildren().addAll(lblNaslov, hBoxGornji, hBoxDonji);
        vBoxInfo.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBoxInfo);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Scene scena = new Scene(root, 1300, 750);
        stageObjekatAdmin.setScene(scena);
        stageObjekatAdmin.show();
    }

    public static void scenaAdmin(Stage primaryStage, Admin admin, ArrayList<Obavjestenje> obavjestenja, Map<Integer, Sto> stolovi, Map<Integer, Meni> meniji) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Vasi podaci");
        Label lblIme = new Label("Ime:");
        Label lblPrezime = new Label("Prezime:");
        Label lblKorisnickoIme = new Label("Korisnicko ime:");
        Label lblNovaLozinka = new Label("Promijeni lozinku");
        Label lblObavjestenja = new Label("Obavjestenja");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        TextField tfIme = new TextField(admin.getIme());
        TextField tfPrezime = new TextField(admin.getPrezime());
        TextField tfKorisnickoIme = new TextField(admin.getKorisnickoIme());
        tfIme.setEditable(false);
        tfPrezime.setEditable(false);
        tfKorisnickoIme.setEditable(false);

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        Button btnPromjeniLozinku = new Button("Nova lozinka");

        btnNazad.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ScenaZaPrijavu.scenaPrijava(primaryStage);
            }
        });

        btnPromjeniLozinku.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!scenaZaLozinkuAktivna) {
                    scenaZaLozinkuAktivna = true;
                    ScenaZaPromjenuLozinke.scenaZaPromjenuLozinke(admin);
                }
            }
        });

        ListView<Obavjestenje> lvObavjestenja = new ListView<>();
        lvObavjestenja.setPrefWidth(400);
        for (Obavjestenje ob : obavjestenja)
            lvObavjestenja.getItems().add(ob);

        lvObavjestenja.setOnMouseClicked(event -> {
            Obavjestenje izabranoObavjestenje = lvObavjestenja.getSelectionModel().getSelectedItem();
            if (izabranoObavjestenje != null) {
                scenaObjekatAdmin(izabranoObavjestenje, stolovi, meniji, () -> Platform.runLater(() -> lvObavjestenja.getItems().remove(izabranoObavjestenje)));
            }
        });

        VBox vBoxLijevi = new VBox(10);
        vBoxLijevi.getChildren().addAll(lblIme, tfIme, lblPrezime, tfPrezime, lblKorisnickoIme, tfKorisnickoIme, lblNovaLozinka, btnPromjeniLozinku);
        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(lblObavjestenja, lvObavjestenja);

        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(vBoxLijevi, vBoxDesni);
        hBox.setAlignment(Pos.CENTER);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(lblNaslov, hBox);
        vBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBox);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scenaAdmin = new Scene(root, 750, 600);
        primaryStage.setScene(scenaAdmin);
        primaryStage.show();
    }
}