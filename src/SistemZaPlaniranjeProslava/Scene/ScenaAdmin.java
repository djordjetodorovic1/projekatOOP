package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Main;
import SistemZaPlaniranjeProslava.Model.*;
import javafx.application.Platform;
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
import javafx.scene.input.KeyCode;
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
    private static boolean scenaZaPorukuAktivna = false;
    private static String poruka;

    private static void scenaUnosPoruke(Runnable nakonUnosaPoruke) {
        Stage stagePoruka = new Stage();
        stagePoruka.setTitle("Pregled objekta");
        stagePoruka.setOnCloseRequest(e -> scenaZaPorukuAktivna = false);

        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Unesite razlog odbijanja objekta");
        lblNaslov.setStyle("-fx-font: 20 'Comic Sans MS';");

        TextField tfPoruka = new TextField();

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        Button btnProvjera = new Button("Pošalji poruku");

        btnNazad.setOnAction(actionEvent -> {
            scenaZaPorukuAktivna = false;
            stagePoruka.close();
        });
        btnProvjera.setOnAction(event -> {
            scenaZaPorukuAktivna = false;
            if (!tfPoruka.getText().isEmpty()) {
                poruka = tfPoruka.getText();
                stagePoruka.close();
                nakonUnosaPoruke.run();
            } else
                Main.upozorenje("Polje za unos poruke je prazno");
        });
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnProvjera.fire();
        });

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(lblNaslov, tfPoruka, btnProvjera);
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(100));

        root.getChildren().addAll(btnNazad, vBox);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Scene scena = new Scene(root, 750, 600);
        stagePoruka.setScene(scena);
        stagePoruka.show();
    }

    private static void scenaObjekatAdmin(Obavjestenje obavjestenje, Runnable nakonObrade) {
        Map<Integer, Sto> stolovi = Controller.getStolovi();
        Map<Integer, Meni> meniji = Controller.getMeni();

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
        for (Sto sto : stolovi.values())
            if (sto.getObjekat().equals(obavjestenje.getObjekat()))
                taStolovi.appendText("Sto " + brojStola++ + ": " + sto.getBrojMjesta() + " mjesta\n");

        TextArea taMeni = new TextArea();
        taMeni.setEditable(false);
        int brojMenija = 1;
        for (Meni meni : meniji.values())
            if (meni.getObjekat().equals(obavjestenje.getObjekat()))
                taMeni.appendText("Meni " + brojMenija++ + ": " + meni.getOpis() + " - " + meni.getCijenaPoOsobi() + "\n");

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

        btnNazad.setOnAction(actionEvent -> stageObjekatAdmin.close());
        btnProvjera.setOnAction(event -> Objekat.provjeriObjekatZaOdobrenje(obavjestenje.getObjekat()));
        btnOdobri.setOnMouseEntered(mouseEvent -> btnOdobri.setBackground(Background.fill(Color.LIGHTGREEN)));
        btnOdobri.setOnMouseExited(mouseEvent -> btnOdobri.setBackground(Background.fill(Color.GREEN)));
        btnOdobri.setOnAction(event -> {
            poruka = "\"" + obavjestenje.getObjekat().getNaziv() + "\" - Objekat zadovoljava sve uslove!";
            Controller.promjenaStatusaObjekta(obavjestenje.getObjekat(), StatusObjekta.ODOBREN, poruka, obavjestenje);
            stageObjekatAdmin.close();
            nakonObrade.run();
        });
        btnOdbij.setOnMouseEntered(mouseEvent -> btnOdbij.setBackground(Background.fill(Color.ORANGERED)));
        btnOdbij.setOnMouseExited(mouseEvent -> btnOdbij.setBackground(Background.fill(Color.RED)));
        btnOdbij.setOnAction(event -> {
            if (!scenaZaPorukuAktivna) {
                scenaZaPorukuAktivna = true;
                scenaUnosPoruke(() -> Platform.runLater(() -> {
                    String potpupnaPoruka = "\"" + obavjestenje.getObjekat().getNaziv() + "\" - " + poruka;
                    Controller.promjenaStatusaObjekta(obavjestenje.getObjekat(), StatusObjekta.ODBIJEN, potpupnaPoruka, obavjestenje);
                    stageObjekatAdmin.close();
                    nakonObrade.run();
                }));
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

    public static void scenaAdmin(Stage primaryStage, Admin admin, ArrayList<Obavjestenje> obavjestenja) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Vaši podaci");
        Label lblIme = new Label("Ime:");
        Label lblPrezime = new Label("Prezime:");
        Label lblKorisnickoIme = new Label("Korisničko ime:");
        Label lblNovaLozinka = new Label("Promijeni lozinku");
        Label lblObavjestenja = new Label("Obavještenja");
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

        btnNazad.setOnAction(actionEvent -> ScenaZaPrijavu.scenaPrijava(primaryStage));

        btnPromjeniLozinku.setOnAction(actionEvent -> {
            if (!scenaZaLozinkuAktivna) {
                scenaZaLozinkuAktivna = true;
                ScenaZaPromjenuLozinke.scenaZaPromjenuLozinke(admin);
            }
        });

        ListView<Obavjestenje> lvObavjestenja = new ListView<>();
        lvObavjestenja.setPrefWidth(400);
        for (Obavjestenje ob : obavjestenja)
            lvObavjestenja.getItems().add(ob);

        lvObavjestenja.setOnMouseClicked(event -> {
            Obavjestenje izabranoObavjestenje = lvObavjestenja.getSelectionModel().getSelectedItem();
            if (izabranoObavjestenje != null)
                scenaObjekatAdmin(izabranoObavjestenje, () -> Platform.runLater(() -> lvObavjestenja.getItems().remove(izabranoObavjestenje)));
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