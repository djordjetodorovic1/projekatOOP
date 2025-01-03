package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Main;
import SistemZaPlaniranjeProslava.Model.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class ScenaZamjenaGostiju {
    public static void scenaZamjenaGostiju(Stage primaryStage, ArrayList<Sto> stolovi, Klijent klijent, Proslava proslava) {
        Label lblNaslov = new Label("Izaberite stolove za zamjenu mjesta");
        lblNaslov.setStyle("-fx-font: 24 'Comic Sans MS';");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        ChoiceBox<Sto> cbSto1 = new ChoiceBox<>();
        for (Sto sto : stolovi)
            cbSto1.getItems().add(sto);
        cbSto1.setPadding(new Insets(5, 50, 5, 50));

        ChoiceBox<Sto> cbSto2 = new ChoiceBox<>();
        for (Sto sto : stolovi)
            cbSto2.getItems().add(sto);
        cbSto2.setPadding(new Insets(5, 50, 5, 50));

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        Button btnZamjena = new Button("Zamjeni mjesta sjedenja");
        btnZamjena.setPadding(new Insets(10));
        btnNazad.setOnAction(actionEvent -> ScenaUredjivanjeProslava.scenaUredjivanjeProslava(primaryStage, klijent, proslava));

        ListView<String> lvRaspored1 = new ListView<>();
        Runnable izmjeniRaspored1 = () -> {
            lvRaspored1.getItems().clear();
            Raspored raspored = Controller.getRasporedi().get(cbSto1.getValue().getId() + "-" + proslava.getId());
            if (raspored != null)
                for (String s : raspored.getGosti())
                    if (!s.isEmpty())
                        lvRaspored1.getItems().add(s);
        };

        ListView<String> lvRaspored2 = new ListView<>();
        Runnable izmjeniRaspored2 = () -> {
            lvRaspored2.getItems().clear();
            Raspored raspored = Controller.getRasporedi().get(cbSto2.getValue().getId() + "-" + proslava.getId());
            if (raspored != null)
                for (String s : raspored.getGosti())
                    if (!s.isEmpty())
                        lvRaspored2.getItems().add(s);
        };

        cbSto1.setOnAction(event -> izmjeniRaspored1.run());
        cbSto2.setOnAction(event -> izmjeniRaspored2.run());

        btnZamjena.setOnAction(actionEvent -> {
            String gost1 = lvRaspored1.getSelectionModel().getSelectedItem();
            String gost2 = lvRaspored2.getSelectionModel().getSelectedItem();

            if (cbSto1.getValue() == null || cbSto2.getValue() == null) {
                Main.upozorenje("Molimo odaberite oba stola za zamenu.");
                return;
            }

            if (gost1 == null && gost2 == null) {
                Main.upozorenje("Molimo selektujte bar jednog gosta za zamenu.");
                return;
            }

            Raspored raspored1 = Controller.getRasporedi().get(cbSto1.getValue().getId() + "-" + proslava.getId());
            Raspored raspored2 = Controller.getRasporedi().get(cbSto2.getValue().getId() + "-" + proslava.getId());

            ArrayList<String> imenaGostiju1 = new ArrayList<>();
            if (raspored1 != null)
                imenaGostiju1 = new ArrayList<>(raspored1.getGosti());
            if (imenaGostiju1.isEmpty()) {
                for (int j = 0; j < cbSto1.getValue().getBrojMjesta(); j++)
                    imenaGostiju1.add("");
                Controller.dodajURaspored(cbSto1.getValue(), proslava, new ArrayList<>(imenaGostiju1));
                raspored1 = Controller.getRasporedi().get(cbSto1.getValue().getId() + "-" + proslava.getId());
            }
            ArrayList<String> imenaGostiju2 = new ArrayList<>();
            if (raspored2 != null)
                imenaGostiju2 = new ArrayList<>(raspored2.getGosti());
            if (imenaGostiju2.isEmpty()) {
                for (int j = 0; j < cbSto2.getValue().getBrojMjesta(); j++)
                    imenaGostiju2.add("");
                Controller.dodajURaspored(cbSto2.getValue(), proslava, new ArrayList<>(imenaGostiju2));
                raspored2 = Controller.getRasporedi().get(cbSto2.getValue().getId() + "-" + proslava.getId());
            }

            if (gost1 == null) {
                if (raspored1.getSto().getBrojMjesta() > raspored1.getGosti().stream().filter(s -> (!s.isEmpty())).toList().size()) {
                    for (String s : raspored1.getGosti())
                        if (s.isEmpty()) {
                            raspored1.getGosti().set(raspored1.getGosti().indexOf(s), gost2);
                            break;
                        }
                    raspored2.getGosti().set(raspored2.getGosti().indexOf(gost2), "");
                    Controller.dodajURaspored(cbSto1.getValue(), proslava, raspored1.getGosti());
                    Controller.dodajURaspored(cbSto2.getValue(), proslava, raspored2.getGosti());
                } else
                    Main.upozorenje("Kapacitet stola je već popunjen!");
            } else if (gost2 == null) {
                if (raspored2.getSto().getBrojMjesta() > raspored2.getGosti().stream().filter(s -> (!s.isEmpty())).toList().size()) {
                    for (String s : raspored2.getGosti())
                        if (s.isEmpty()) {
                            raspored2.getGosti().set(raspored2.getGosti().indexOf(s), gost1);
                            break;
                        }
                    raspored1.getGosti().set(raspored1.getGosti().indexOf(gost1), "");
                    Controller.dodajURaspored(cbSto1.getValue(), proslava, raspored1.getGosti());
                    Controller.dodajURaspored(cbSto2.getValue(), proslava, raspored2.getGosti());
                } else
                    Main.upozorenje("Kapacitet stola je već popunjen!");
            } else {
                raspored1.getGosti().set(raspored1.getGosti().indexOf(gost1), gost2);
                raspored2.getGosti().set(raspored2.getGosti().indexOf(gost2), gost1);
                Controller.dodajURaspored(cbSto1.getValue(), proslava, raspored1.getGosti());
                Controller.dodajURaspored(cbSto2.getValue(), proslava, raspored2.getGosti());
            }
            System.out.println("1\n" + raspored1);
            System.out.println("2\n" + raspored2);
            izmjeniRaspored1.run();
            izmjeniRaspored2.run();
        });

        VBox vLijevi = new VBox(10);
        vLijevi.getChildren().addAll(cbSto1, lvRaspored1);
        vLijevi.setAlignment(Pos.CENTER);

        VBox vDesni = new VBox(10);
        vDesni.getChildren().addAll(cbSto2, lvRaspored2);
        vDesni.setAlignment(Pos.CENTER);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vLijevi, btnZamjena, vDesni);

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(lblNaslov, hBox);

        root.getChildren().addAll(btnNazad, vBox);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Platform.runLater(root::requestFocus);
        Scene scenaZamjena = new Scene(root, 1000, 600);
        primaryStage.setScene(scenaZamjena);
        primaryStage.show();
    }
}