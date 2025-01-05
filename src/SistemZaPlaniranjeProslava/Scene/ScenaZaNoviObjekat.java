package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Main;
import SistemZaPlaniranjeProslava.Model.Obavjestenje;
import SistemZaPlaniranjeProslava.Model.Sto;
import SistemZaPlaniranjeProslava.Validator;
import SistemZaPlaniranjeProslava.Model.Vlasnik;
import javafx.application.Platform;
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

public class ScenaZaNoviObjekat {
    private static boolean scenaZaMeniAktivna = false;
    private static boolean scenaZaStoAktivna = false;
    private static List<Spinner<Integer>> spinnerBrojMjestaPoStolovima = new ArrayList<>();
    private static ArrayList<String> meniOpis = new ArrayList<>();
    private static ArrayList<Double> meniCijene = new ArrayList<>();

    private static void scenaZaUnosStolova(int brojStolova, Obavjestenje obavjestenje) {
        Stage stageSto = new Stage();
        stageSto.setTitle("Slobodan sto");
        stageSto.setOnCloseRequest(e -> scenaZaStoAktivna = false);

        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        Label lblNaslov = new Label("Unesite broj mjesta za svaki od stolova");
        lblNaslov.setStyle("-fx-font: 24 'Comic Sans MS';");
        root.getChildren().add(lblNaslov);

        VBox vBoxtf = new VBox(10);
        vBoxtf.setAlignment(Pos.CENTER);
        vBoxtf.setPadding(new Insets(10, 40, 10, 40));

        ArrayList<Sto> stoloviZaObjekat = new ArrayList<>();
        if (obavjestenje != null)
            stoloviZaObjekat = Controller.stoloviZaObjekat(obavjestenje.getObjekat().getId());
        spinnerBrojMjestaPoStolovima.clear();
        for (int i = 0; i < brojStolova; i++) {
            Label lblSto = new Label("Sto broj " + (i + 1));

            Spinner<Integer> newSpinner = new Spinner<>();
            SpinnerValueFactory<Integer> valueFactory;
            if (stoloviZaObjekat.isEmpty())
                valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 16, 4);
            else
                valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 16, stoloviZaObjekat.get(i).getBrojMjesta());
            newSpinner.setEditable(true);
            newSpinner.setMaxWidth(250);
            newSpinner.setValueFactory(valueFactory);
            newSpinner.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d+"))
                    newSpinner.getEditor().setText(oldValue);
            });
            vBoxtf.getChildren().addAll(lblSto, newSpinner);
            spinnerBrojMjestaPoStolovima.add(newSpinner);
        }

        Button btnSacuvaj = new Button("Sačuvaj izmjene");
        btnSacuvaj.setOnAction(actionEvent -> {
            if (obavjestenje != null)
                Controller.brisanjeStolovaIzBaze(obavjestenje.getObjekat().getId());
            scenaZaStoAktivna = false;
            stageSto.close();
        });

        ScrollPane scrollPane = new ScrollPane(vBoxtf);
        scrollPane.setMaxWidth(350);
        root.getChildren().addAll(scrollPane, btnSacuvaj);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scena = new Scene(root, 750, 600);
        stageSto.setScene(scena);
        stageSto.show();
    }

    private static void scenaZaUnosMenija() {
        Stage stageMeni = new Stage();
        stageMeni.setTitle("Slobodan sto");
        stageMeni.setOnCloseRequest(e -> scenaZaMeniAktivna = false);

        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));
        root.setAlignment(Pos.CENTER);

        Label lblNaslov = new Label("Unesite novi meni i njegovu cijenu");
        lblNaslov.setStyle("-fx-font: 24 'Comic Sans MS';");

        TextField tfMeni = new TextField();
        TextField tfCijenaMenija = new TextField();
        tfMeni.setPromptText("Predjelo Glavno jelo Dezert");
        tfCijenaMenija.setPromptText("50.00");
        tfMeni.setMaxWidth(300);
        tfCijenaMenija.setMaxWidth(300);

        Button btnDodajMeni = new Button("Dodaj meni");
        btnDodajMeni.setPadding(new Insets(5, 50, 5, 50));

        btnDodajMeni.setOnAction(actionEvent -> {
            if (Validator.validacijaMeni(tfMeni, tfCijenaMenija)) {
                meniOpis.add(tfMeni.getText());
                meniCijene.add(Double.parseDouble(tfCijenaMenija.getText()));
                Main.ocistiPolje(tfMeni);
                Main.ocistiPolje(tfCijenaMenija);
            }
        });
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnDodajMeni.fire();
        });

        root.getChildren().addAll(lblNaslov, tfMeni, tfCijenaMenija, btnDodajMeni);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scena = new Scene(root, 550, 400);
        stageMeni.setScene(scena);
        stageMeni.show();
    }

    public static void scenaNoviObjekat(Stage stageNoviObjekat, Vlasnik vlasnik) {
        scenaIzmjenaObjekta(stageNoviObjekat, vlasnik, null);
    }

    public static void scenaIzmjenaObjekta(Stage stageNoviObjekat, Vlasnik vlasnik, Obavjestenje obavjestenje) {
        meniOpis.clear();
        meniCijene.clear();

        stageNoviObjekat.setTitle("Slobodan sto");
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Dodaj novi objekat");
        Label lblNaziv = new Label("Unesite naziv");
        Label lblCijenaRezervacije = new Label("Unesite cijenu rezervacije");
        Label lblGrad = new Label("Unesite grad");
        Label lblAdresa = new Label("Unesite adresu");
        Label lblBrojMjesta = new Label("Unesite broj mjesta");
        Label lblBrojStolova = new Label("Unesite broj stolova");
        Label lblMjestaZaStolom = new Label("Unesite broj mjesta za svaki sto");
        Label lblMeni = new Label("Unesite menije");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        TextField tfNaziv = new TextField();
        TextField tfCijenaRezervacije = new TextField();
        TextField tfGrad = new TextField();
        TextField tfAdresa = new TextField();
        TextField tfBrojMjesta = new TextField();
        TextField tfBrojStolova = new TextField();

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNoviObjekat = new Button("Kreiraj novi nalog");
        Button btnBrojStolica = new Button();
        Button btnMeni = new Button();
        Button btnNazad = new Button("", prikazStrelice);
        btnBrojStolica.setPadding(new Insets(5, 50, 5, 50));
        btnMeni.setPadding(new Insets(5, 50, 5, 50));

        if (obavjestenje != null) {
            tfNaziv.setText(obavjestenje.getObjekat().getNaziv());
            tfCijenaRezervacije.setText("" + obavjestenje.getObjekat().getCijenaRezervacije());
            tfGrad.setText(obavjestenje.getObjekat().getGrad());
            tfAdresa.setText(obavjestenje.getObjekat().getAdresa());
            tfBrojMjesta.setText("" + obavjestenje.getObjekat().getBrojMjesta());
            tfBrojStolova.setText("" + obavjestenje.getObjekat().getBrojStolova());
            tfNaziv.setEditable(false);
            tfGrad.setEditable(false);
            tfAdresa.setEditable(false);
            btnBrojStolica.setText("Izmjeni");
            btnMeni.setText("Izmjeni");
        } else {
            tfNaziv.setPromptText("Caffe Renas");
            tfCijenaRezervacije.setPromptText("200.00");
            tfGrad.setPromptText("Banja Luka");
            tfAdresa.setPromptText("Mladena Stojanovica 2");
            tfBrojMjesta.setPromptText("50");
            tfBrojStolova.setPromptText("10");
            btnBrojStolica.setText("Unesi");
            btnMeni.setText("Unesi");
        }

        btnNazad.setOnAction(actionEvent -> {
            if (obavjestenje != null)
                stageNoviObjekat.close();
            else
                ScenaVlasnik.scenaVlasnik(stageNoviObjekat, vlasnik);
        });

        btnBrojStolica.setOnAction(event -> {
            if (!scenaZaStoAktivna) {
                if (Validator.validacijaIntBroj(tfBrojStolova)) {
                    scenaZaStoAktivna = true;
                    scenaZaUnosStolova(Integer.parseInt(tfBrojStolova.getText()), obavjestenje);
                } else
                    Main.upozorenje("Polje za broj stolova nije korektno popunjeno! Pokušajte ponovo");
            }
        });

        btnMeni.setOnAction(event -> {
            if (!scenaZaMeniAktivna) {
                if (obavjestenje != null)
                    Controller.brisanjeMenijaIzBaze(obavjestenje.getObjekat().getId());
                scenaZaMeniAktivna = true;
                scenaZaUnosMenija();
            }
        });

        btnNoviObjekat.setOnAction(event -> {
            ArrayList<Integer> brojMjestaPoStolovima = new ArrayList<>();
            if (!spinnerBrojMjestaPoStolovima.isEmpty())
                spinnerBrojMjestaPoStolovima.stream()
                        .map(spinner -> Integer.parseInt(spinner.getValue().toString()))
                        .forEach(brojMjestaPoStolovima::add);
            if (obavjestenje == null) {
                if (Controller.kreirajNoviObjekat(tfNaziv, tfGrad, tfAdresa, tfCijenaRezervacije,
                        tfBrojMjesta, tfBrojStolova, meniOpis, meniCijene, vlasnik, brojMjestaPoStolovima, 0))
                    ScenaVlasnik.scenaVlasnik(stageNoviObjekat, vlasnik);
            } else {
                if (Controller.kreirajNoviObjekat(tfNaziv, tfGrad, tfAdresa, tfCijenaRezervacije,
                        tfBrojMjesta, tfBrojStolova, meniOpis, meniCijene, vlasnik, brojMjestaPoStolovima, obavjestenje.getObjekat().getId()))
                    stageNoviObjekat.close();
            }
        });
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnNoviObjekat.fire();
        });

        VBox vBoxLijevi = new VBox(10);
        vBoxLijevi.getChildren().addAll(lblNaziv, tfNaziv, lblGrad, tfGrad, lblAdresa, tfAdresa, lblCijenaRezervacije, tfCijenaRezervacije);
        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(lblBrojMjesta, tfBrojMjesta, lblBrojStolova, tfBrojStolova, lblMjestaZaStolom, btnBrojStolica, lblMeni, btnMeni);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vBoxLijevi, vBoxDesni);
        hBox.setPadding(new Insets(40, 0, 40, 0));

        VBox vBoxUnos = new VBox(10);
        vBoxUnos.getChildren().addAll(lblNaslov, hBox, btnNoviObjekat);
        vBoxUnos.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBoxUnos);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Scene scena = new Scene(root, 750, 600);
        stageNoviObjekat.setScene(scena);
        stageNoviObjekat.show();
    }
}