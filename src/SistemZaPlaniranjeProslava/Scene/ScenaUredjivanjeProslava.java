package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Database;
import SistemZaPlaniranjeProslava.Main;
import SistemZaPlaniranjeProslava.Model.*;
import SistemZaPlaniranjeProslava.Validator;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.time.LocalDate;
import java.util.*;

public class ScenaUredjivanjeProslava {
    private static ArrayList<String> imenaGostiju = new ArrayList<>();
    public static boolean scenaStampanjeAktivna = false;

    private static void scenaZaLozinku(Klijent klijent, Proslava proslava) {
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
                if (Controller.getStanjeRacuna(klijent.getBrojRacuna()) >= proslava.getUkupnaCijena()) {
                    Controller.transakcija(klijent, proslava.getObjekat(), proslava.getUkupnaCijena());
                    Controller.uredjenaProslava(proslava, proslava.getUkupnaCijena());
                    Main.informacija("Trenutno stanje na računu: " + Controller.getStanjeRacuna(klijent.getBrojRacuna()) + " Proslava je uspješno uređena!");
                } else
                    Main.upozorenje("Rezervacija proslave nije moguća zbog nedovoljno novca na racunu!");
                stagePotvrdaLozinke.close();
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

    public static void scenaUredjivanjeProslava(Stage primaryStage, Klijent klijent, Proslava proslava) {
        boolean proslavaZaUredjivanje = proslava.getStatus().equals(StatusProslave.AKTIVNA) && proslava.provjeraDatumZaUredjivanje();

        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Uredite vašu proslavu");
        Label lblNaziv = new Label("Naziv objekta: \"" + proslava.getObjekat().getNaziv() + "\"");
        Label lblMeni = new Label("Izaberite meni");
        Label lblRaspored = new Label("Uredite raspored sjedenja");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        ArrayList<Meni> meniji = Controller.menijiZaObjekat(proslava.getObjekat().getId());
        ChoiceBox<Meni> cbMeni = new ChoiceBox<>();
        cbMeni.setValue(proslava.getMeni());
        for (Meni meni : meniji)
            cbMeni.getItems().add(meni);
        cbMeni.setPadding(new Insets(5));
        cbMeni.setPrefWidth(300);

        ArrayList<Sto> stolovi = Controller.stoloviZaObjekat(proslava.getObjekat().getId());
        ChoiceBox<Sto> cbSto = new ChoiceBox<>();
        for (Sto sto : stolovi)
            cbSto.getItems().add(sto);
        cbSto.setPadding(new Insets(5, 50, 5, 50));

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        Button btnZamjena = new Button("Zamjeni mjesta sjedenja");
        Button btnSacuvajIzmjene = new Button("Izracunaj ukupnu sumu");
        Button btnStampanje = new Button("Štampanje rasporeda");
        Button btnOtkazi = new Button("Otkaži proslavu");
        Button btnZavrsi = new Button("Završi uređivanje proslave");

        btnZamjena.setPadding(new Insets(10));
        btnSacuvajIzmjene.setPadding(new Insets(10));
        btnStampanje.setPadding(new Insets(10));
        btnOtkazi.setPadding(new Insets(10));
        btnZavrsi.setPadding(new Insets(10));
        btnZamjena.setPrefWidth(300);
        btnSacuvajIzmjene.setPrefWidth(300);
        btnStampanje.setPrefWidth(300);
        btnOtkazi.setPrefWidth(300);
        btnZavrsi.setPrefWidth(300);

        if (!proslavaZaUredjivanje) {
            btnZamjena.setDisable(true);
            btnSacuvajIzmjene.setDisable(true);
            btnOtkazi.setDisable(true);
            btnZavrsi.setDisable(true);
            cbMeni.setDisable(true);
        }
        if (proslava.getStatus().equals(StatusProslave.AKTIVNA))
            btnZavrsi.setDisable(false);
        if(proslava.getPotpunaUplata()){
            btnSacuvajIzmjene.setDisable(true);
            btnZavrsi.setDisable(true);
            cbMeni.setDisable(true);
        }

        btnNazad.setOnAction(actionEvent -> ScenaKlijent.scenaKlijent(primaryStage, klijent));
        btnZamjena.setOnAction(actionEvent -> ScenaZamjenaGostiju.scenaZamjenaGostiju(primaryStage, stolovi, klijent, proslava));
        btnSacuvajIzmjene.setOnAction(actionEvent -> {
            int brojGostijuNaProslavi = 0;
            for (Sto sto : stolovi) {
                Raspored raspored = Controller.getRasporedi().get(sto.getId() + "-" + proslava.getId());
                if (raspored != null)
                    brojGostijuNaProslavi += raspored.getGosti().stream().filter(s -> (!s.isEmpty())).toList().size();
            }
            proslava.setBrojGostiju(brojGostijuNaProslavi);
            if (cbMeni.getValue() != null) {
                proslava.setMeni(cbMeni.getValue());
                proslava.setUkupnaCijena(brojGostijuNaProslavi * proslava.getMeni().getCijenaPoOsobi());
                Database.izmjeniProslavu(proslava);
                Main.informacija("Trenutna suma: " + proslava.getUkupnaCijena());
            } else
                Main.informacija("Niste izabrali meni! Pokušajte ponovo");
        });
        btnStampanje.setOnAction(actionEvent -> {
            if (!scenaStampanjeAktivna) {
                scenaStampanjeAktivna = true;
                ScenaStampanje.stampanje(proslava, stolovi);
            }
        });
        btnOtkazi.setOnAction(actionEvent -> {
            proslava.setStatus(StatusProslave.OTKAZANA);
            proslava.setDatum(LocalDate.of(1980, 1, 1));
            Database.otkazanaProslava(proslava.getId(), LocalDate.of(1980, 1, 1));
            if (proslava.getUplacenIznos() == proslava.getUkupnaCijena()) {
                Controller.transakcija(klijent, proslava.getObjekat(), proslava.getUkupnaCijena() * -1);
                Controller.uredjenaProslava(proslava, 0);
            }
            Main.informacija("Trenutno stanje na računu: " + Controller.getStanjeRacuna(klijent.getBrojRacuna()) + " Proslava je uspješno otkazana!");
        });
        btnZavrsi.setOnAction(actionEvent -> {
            btnSacuvajIzmjene.fire();
            if (proslava.getBrojGostiju() > 0)
                scenaZaLozinku(klijent, proslava);
            else
                Main.informacija("Niste unijeli ni jednog gosta! Pokušajte ponovo");
        });

        Pane paneSto = new Pane();
        paneSto.setMinSize(800, 650);
        paneSto.setMaxSize(800, 650);
        paneSto.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        Circle krug = new Circle(400, 350, 150, Color.ORANGE);

        cbSto.setOnAction(event -> {
            Sto izabraniSto = cbSto.getValue();
            paneSto.getChildren().clear();
            paneSto.getChildren().add(krug);

            Raspored raspored = Controller.getRasporedi().get(izabraniSto.getId() + "-" + proslava.getId());
            if (raspored != null)
                imenaGostiju = raspored.getGosti();
            else {
                ArrayList<String> lista = new ArrayList<>();
                for (int j = 0; j < izabraniSto.getBrojMjesta(); j++)
                    lista.add("");
                imenaGostiju = lista;
            }
            double pomjerajUgla = 360.0 / izabraniSto.getBrojMjesta();
            for (int i = 0; i < izabraniSto.getBrojMjesta(); i++) {
                double x = 400 + Math.cos(Math.toRadians(pomjerajUgla * i)) * 280 - 50;
                double y = 350 + Math.sin(Math.toRadians(pomjerajUgla * i)) * 280 - 20;
                TextField tfGost = new TextField();
                tfGost.setLayoutX(x);
                tfGost.setLayoutY(y);
                tfGost.setPrefWidth(110);
                if (!proslavaZaUredjivanje || proslava.getPotpunaUplata())
                    tfGost.setEditable(false);
                tfGost.setText(imenaGostiju.get(i));

                final int index = i;
                tfGost.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (tfGost.getText().isEmpty() || Validator.provjeraRegIzraza(tfGost.getText(), "^[a-zA-Z\\s]*$")) {
                            imenaGostiju.set(index, newValue);
                            Controller.dodajURaspored(izabraniSto, proslava, new ArrayList<>(imenaGostiju));
                        } else {
                            tfGost.setText(imenaGostiju.get(index));
                            Main.upozorenje("Nekorektan unos! Pokušajte ponovo");
                        }
                    }
                });
                paneSto.getChildren().add(tfGost);
            }
        });

        VBox vLijevi = new VBox(20);
        vLijevi.setPrefWidth(450);
        vLijevi.getChildren().addAll(lblNaziv, lblRaspored, cbSto, btnZamjena, lblMeni, cbMeni, btnSacuvajIzmjene, btnStampanje, btnOtkazi, btnZavrsi);
        vLijevi.setAlignment(Pos.CENTER);

        VBox vDesni = new VBox(10);
        vDesni.setPrefWidth(800);
        vDesni.getChildren().addAll(paneSto);
        vDesni.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vLijevi, vDesni);

        VBox vBox = new VBox(10);
        vBox.getChildren().addAll(lblNaslov, hBox);
        vBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnNazad, vBox);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scenaProslava = new Scene(root, 1300, 780);
        primaryStage.setScene(scenaProslava);
        primaryStage.show();
    }
}