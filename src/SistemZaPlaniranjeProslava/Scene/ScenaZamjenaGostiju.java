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
        ChoiceBox<Sto> cbSto2 = new ChoiceBox<>();
        for (Sto sto : stolovi) {
            cbSto1.getItems().add(sto);
            cbSto2.getItems().add(sto);
        }
        cbSto1.setPadding(new Insets(5, 50, 5, 50));
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
                Main.upozorenje("Niste izabrali sto! Pokušajte ponovo");
                return;
            }
            if (gost1 == null && gost2 == null) {
                Main.upozorenje("Niste izabrali gosta za zamjenu! Pokušajte ponovo");
                return;
            }

            Raspored raspored1 = Controller.getRasporedi().get(cbSto1.getValue().getId() + "-" + proslava.getId());
            Raspored raspored2 = Controller.getRasporedi().get(cbSto2.getValue().getId() + "-" + proslava.getId());

            ArrayList<String> imenaGostiju1 = new ArrayList<>();
            if (raspored1 != null)
                imenaGostiju1 = new ArrayList<>(raspored1.getGosti());
            else {
                for (int j = 0; j < cbSto1.getValue().getBrojMjesta(); j++)
                    imenaGostiju1.add("");
                Controller.dodajURaspored(cbSto1.getValue(), proslava, new ArrayList<>(imenaGostiju1));
            }
            ArrayList<String> imenaGostiju2 = new ArrayList<>();
            if (raspored2 != null)
                imenaGostiju2 = new ArrayList<>(raspored2.getGosti());
            else {
                for (int j = 0; j < cbSto2.getValue().getBrojMjesta(); j++)
                    imenaGostiju2.add("");
                Controller.dodajURaspored(cbSto2.getValue(), proslava, new ArrayList<>(imenaGostiju2));
            }

            if (gost1 == null && cbSto1.getValue().getId() != cbSto2.getValue().getId()) {
                if (raspored1 != null) {
                    if (raspored1.getSto().getBrojMjesta() > imenaGostiju1.stream().filter(s -> (!s.isEmpty())).toList().size()) {
                        for (String s : imenaGostiju1)
                            if (s.isEmpty()) {
                                imenaGostiju1.set(imenaGostiju1.indexOf(s), gost2);
                                break;
                            }
                        imenaGostiju2.set(imenaGostiju2.indexOf(gost2), "");
                        Controller.dodajURaspored(cbSto1.getValue(), proslava, new ArrayList<>(imenaGostiju1));
                        Controller.dodajURaspored(cbSto2.getValue(), proslava, new ArrayList<>(imenaGostiju2));
                    } else
                        Main.upozorenje("Kapacitet stola je već popunjen!");
                } else {
                    imenaGostiju1.add(gost2);
                    imenaGostiju2.set(imenaGostiju2.indexOf(gost2), "");
                    Controller.dodajURaspored(cbSto1.getValue(), proslava, new ArrayList<>(imenaGostiju1));
                    Controller.dodajURaspored(cbSto2.getValue(), proslava, new ArrayList<>(imenaGostiju2));
                }
            } else if (gost2 == null && cbSto1.getValue().getId() != cbSto2.getValue().getId()) {
                if (raspored2 != null) {
                    if (raspored2.getSto().getBrojMjesta() > imenaGostiju2.stream().filter(s -> (!s.isEmpty())).toList().size()) {
                        for (String s : imenaGostiju2)
                            if (s.isEmpty()) {
                                imenaGostiju2.set(imenaGostiju2.indexOf(s), gost1);
                                break;
                            }
                        imenaGostiju1.set(imenaGostiju1.indexOf(gost1), "");
                        Controller.dodajURaspored(cbSto1.getValue(), proslava, new ArrayList<>(imenaGostiju1));
                        Controller.dodajURaspored(cbSto2.getValue(), proslava, new ArrayList<>(imenaGostiju2));
                    } else
                        Main.upozorenje("Kapacitet stola je već popunjen!");
                } else {
                    imenaGostiju2.add(gost1);
                    imenaGostiju1.set(imenaGostiju1.indexOf(gost1), "");
                    Controller.dodajURaspored(cbSto1.getValue(), proslava, new ArrayList<>(imenaGostiju1));
                    Controller.dodajURaspored(cbSto2.getValue(), proslava, new ArrayList<>(imenaGostiju2));
                }
            } else if (gost1 != null && gost2 != null) {
                if (cbSto1.getValue().getId() == cbSto2.getValue().getId()) {
                    int index1 = imenaGostiju1.indexOf(gost1);
                    int index2 = imenaGostiju1.indexOf(gost2);
                    if (index1 != index2) {
                        String temp = imenaGostiju1.get(index1);
                        imenaGostiju1.set(index1, imenaGostiju1.get(index2));
                        imenaGostiju1.set(index2, temp);
                    }
                } else {
                    imenaGostiju1.set(imenaGostiju1.indexOf(gost1), gost2);
                    imenaGostiju2.set(imenaGostiju2.indexOf(gost2), gost1);
                    Controller.dodajURaspored(cbSto2.getValue(), proslava, new ArrayList<>(imenaGostiju2));
                }
                Controller.dodajURaspored(cbSto1.getValue(), proslava, new ArrayList<>(imenaGostiju1));
            }
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
        Platform.runLater(root::requestFocus);
        primaryStage.setScene(scenaZamjena);
        primaryStage.show();
    }
}