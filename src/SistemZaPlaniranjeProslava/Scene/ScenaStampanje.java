package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Model.Proslava;
import SistemZaPlaniranjeProslava.Model.Raspored;
import SistemZaPlaniranjeProslava.Model.Sto;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ScenaStampanje {
    public static void stampanje(Proslava proslava, ArrayList<Sto> stolovi) {
        Map<Sto, String> sviGostiPoStolovima = new TreeMap<>(Raspored.porediPoStolovima);
        Map<String, Sto> sviGostiLeksikografski = new TreeMap<>(Raspored.porediPoImenu);
        for (Sto sto : stolovi) {
            Raspored raspored = Controller.getRasporedi().get(sto.getId() + "-" + proslava.getId());
            if (raspored != null) {
                ArrayList<String> gostiZaSto = raspored.getGosti().stream().filter(s -> (!s.isEmpty())).collect(Collectors.toCollection(ArrayList::new));
                if (!gostiZaSto.isEmpty())
                    for (String gost : gostiZaSto) {
                        sviGostiLeksikografski.put(gost, sto);
                        String zaSto = sviGostiPoStolovima.get(sto);
                        if (zaSto == null)
                            sviGostiPoStolovima.put(sto, gost);
                        else
                            sviGostiPoStolovima.put(sto, zaSto + ", " + gost);
                    }
            }
        }
        Stage stageStampanje = new Stage();
        stageStampanje.setTitle("Prikaz stampanja");
        stageStampanje.setOnCloseRequest(e -> ScenaUredjivanjeProslava.scenaStampanjeAktivna = false);
        VBox root = new VBox(10);
        root.setPadding(new Insets(20));

        Label lblNaslov = new Label("Štampanje");
        Label lblSto = new Label("Sortirano po stolovima");
        Label lblIme = new Label("Sortirano leksikografski");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        TextArea taPoStolovima = new TextArea();
        taPoStolovima.setEditable(false);
        taPoStolovima.setMaxWidth(850);
        for (Sto sto : sviGostiPoStolovima.keySet())
            taPoStolovima.appendText(sto + ": " + sviGostiPoStolovima.get(sto) + "\n");

        TextArea taPoImenu = new TextArea();
        taPoImenu.setEditable(false);
        taPoImenu.setMaxWidth(850);
        for (String gost : sviGostiLeksikografski.keySet())
            taPoImenu.appendText(gost + " - " + sviGostiLeksikografski.get(gost) + "\n");

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        btnNazad.setOnAction(actionEvent -> {
            ScenaUredjivanjeProslava.scenaStampanjeAktivna = false;
            stageStampanje.close();
        });

        VBox vBox = new VBox(10);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(lblNaslov, lblSto, taPoStolovima, lblIme, taPoImenu);

        root.getChildren().addAll(btnNazad, vBox);
        root.setStyle("-fx-font: 16 'Comic Sans MS';");
        Scene scenaNoviNalog = new Scene(root, 900, 700);
        Platform.runLater(root::requestFocus);
        stageStampanje.setScene(scenaNoviNalog);
        stageStampanje.show();
    }
}