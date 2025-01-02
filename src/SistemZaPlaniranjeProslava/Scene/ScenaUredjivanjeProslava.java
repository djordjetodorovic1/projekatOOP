package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Model.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class ScenaUredjivanjeProslava {
    private static ArrayList<TextField> textFields = new ArrayList<>();
    private static ArrayList<String> imenaGostiju = new ArrayList<>();

    public static void scenaUredjivanjeProslava(Stage primaryStage, Klijent klijent, Proslava proslava) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Uredite vašu proslavu");
        Label lblNaziv = new Label("Naziv objekta: \"" + proslava.getObjekat().getNaziv() + "\"");
        Label lblMeni = new Label("Izaberite meni");
        Label lblRaspored = new Label("Uredite raspored sjedenja");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        ArrayList<Meni> meniji = Controller.menijiZaObjekat(proslava.getObjekat().getId());
        ChoiceBox<Meni> cbMeni = new ChoiceBox<>();
        for (Meni meni : meniji)
            cbMeni.getItems().add(meni);
        cbMeni.setPadding(new Insets(5, 50, 5, 50));
        cbMeni.setPrefWidth(450);

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
        Button btnSacuvajIzmjene = new Button("Sacuvaj izmjene");
        Button btnStampanje = new Button("Štampanje rasporeda");
        Button btnZavrsi = new Button("Završi uređivanje proslave");

        btnZamjena.setPadding(new Insets(10));
        btnSacuvajIzmjene.setPadding(new Insets(10));
        btnStampanje.setPadding(new Insets(10));
        btnZavrsi.setPadding(new Insets(10));
        btnZamjena.setPrefWidth(300);
        btnSacuvajIzmjene.setPrefWidth(300);
        btnStampanje.setPrefWidth(300);
        btnZavrsi.setPrefWidth(300);

        btnNazad.setOnAction(actionEvent -> Controller.scenaKlijent(primaryStage, klijent));

        btnSacuvajIzmjene.setOnAction(actionEvent -> {
            /*proslava.setMeni(cbMeni.getValue());
            int brojGostijuNaProslavi = 0;
            for (Sto sto : stolovi)
                brojGostijuNaProslavi += Controller.getRasporedi().get(sto.getId() + "-" + proslava.getId()).getGosti().size();
            proslava.setBrojGostiju(brojGostijuNaProslavi);
            proslava.setUkupnaCijena(brojGostijuNaProslavi * proslava.getMeni().getCijenaPoOsobi());*/
        });

        btnZamjena.setOnAction(actionEvent -> {/*Zamjena gostiju*/});
        btnStampanje.setOnAction(actionEvent -> {/*Prikaz sortirano*/});
        btnZavrsi.setOnAction(actionEvent -> {/*Placanje Kraj*/});

        Pane paneSto = new Pane();
        paneSto.setMinSize(800, 550);
        paneSto.setMaxSize(800, 550);
        paneSto.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));

        Circle krug = new Circle(400, 270, 150, Color.ORANGE);
        paneSto.getChildren().add(krug);

        cbSto.setOnAction(event -> {
            Sto izabraniSto = cbSto.getValue();
            imenaGostiju.clear();
            textFields.forEach(paneSto.getChildren()::remove);
            textFields.clear();

            Raspored raspored = Controller.getRasporedi().get(izabraniSto.getId() + "-" + proslava.getId());
            if (raspored != null)
                imenaGostiju = new ArrayList<>(raspored.getGosti());

            double pomjerajUgla = 360.0 / izabraniSto.getBrojMjesta();
            for (int i = 0; i < izabraniSto.getBrojMjesta(); i++) {
                double x = 400 + Math.cos(Math.toRadians(pomjerajUgla * i)) * 220 - 40;
                double y = 270 + Math.sin(Math.toRadians(pomjerajUgla * i)) * 220 - 20;

                TextField tfGost = new TextField();
                tfGost.setLayoutX(x);
                tfGost.setLayoutY(y);
                tfGost.setPrefWidth(80);

                if (i < imenaGostiju.size())
                    tfGost.setText(imenaGostiju.get(i));
                else
                    tfGost.setText("");

                final int index = i;
                tfGost.textProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        if (imenaGostiju.isEmpty())
                            for (int j = 0; j < textFields.size(); j++)
                                imenaGostiju.add("");
                        imenaGostiju.set(index, newValue);

                        System.out.println(imenaGostiju);
                        Controller.dodajURaspored(izabraniSto, proslava, new ArrayList<>(imenaGostiju));
                    }
                });
                textFields.add(tfGost);
                paneSto.getChildren().add(tfGost);
            }
        });

        VBox vLijevi = new VBox(20);
        vLijevi.setPrefWidth(450);
        vLijevi.getChildren().addAll(lblNaziv, lblMeni, cbMeni, btnSacuvajIzmjene, btnZamjena, btnStampanje, btnZavrsi);
        vLijevi.setAlignment(Pos.CENTER);

        VBox vDesni = new VBox(10);
        vDesni.setPrefWidth(800);
        vDesni.getChildren().addAll(lblRaspored, cbSto, paneSto);
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