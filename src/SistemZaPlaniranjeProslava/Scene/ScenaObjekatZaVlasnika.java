package SistemZaPlaniranjeProslava.Scene;

import SistemZaPlaniranjeProslava.Controller;
import SistemZaPlaniranjeProslava.Model.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.skin.DatePickerSkin;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.File;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class ScenaObjekatZaVlasnika {
    private static void podesiTF(TextField tf) {
        tf.setPadding(new Insets(10));
        tf.setPrefWidth(320);
        tf.setEditable(false);
    }

    private static void scenaInformacijeOProslavi(Proslava proslava) {
        Stage stageProslava = new Stage();
        stageProslava.setTitle("Informacije o proslavi");

        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        TextField tfBrojGostiju = new TextField("Broj gostiju: " + proslava.getBrojGostiju());
        TextField tfMeni = new TextField();
        TextField tfUkupnaCijena = new TextField();
        TextField tfPotvrdaUplate = new TextField("Uplaćen ukupan iznos: " + (proslava.getPotpunaUplata() ? "DA" : "NE"));
        Label lblStolovi = new Label("Broj gostiju po stolovima");

        if (proslava.getMeni() == null) {
            tfMeni.setText("Meni nije izabran");
            tfUkupnaCijena.setText("Meni nije izabran");
        } else {
            tfMeni.setText("Meni: " + proslava.getMeni());
            tfUkupnaCijena.setText("Ukupna cijena proslave: " + (proslava.getBrojGostiju() * proslava.getMeni().getCijenaPoOsobi()));
        }

        podesiTF(tfBrojGostiju);
        podesiTF(tfMeni);
        podesiTF(tfUkupnaCijena);
        podesiTF(tfPotvrdaUplate);

        Map<String, Raspored> rasporedi = Controller.getRasporedi();
        TextArea taStolovi = new TextArea();
        taStolovi.setEditable(false);
        int brojStola = 1, brojMjesta;
        for (Raspored raspored : rasporedi.values())
            if (raspored.getProslava().getId() == proslava.getId() && (brojMjesta = raspored.getGosti().stream().filter(s -> (!s.isEmpty())).toList().size()) > 0)
                taStolovi.appendText("Sto " + brojStola++ + ": " + brojMjesta + " mjesta\n");

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        btnNazad.setOnAction(actionEvent -> stageProslava.close());
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                btnNazad.fire();
        });

        VBox vBoxLijevi = new VBox(30);
        vBoxLijevi.getChildren().addAll(tfBrojGostiju, tfMeni, tfUkupnaCijena, tfPotvrdaUplate);
        vBoxLijevi.setPadding(new Insets(40, 5, 5, 5));
        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(lblStolovi, taStolovi);
        vBoxDesni.setMaxWidth(280);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vBoxLijevi, vBoxDesni);
        hBox.setPadding(new Insets(50, 10, 10, 10));

        root.getChildren().addAll(btnNazad, hBox);
        root.setStyle("-fx-font: 18 'Comic Sans MS';");
        Scene scena = new Scene(root, 750, 600);
        stageProslava.setScene(scena);
        stageProslava.show();
    }

    public static void scenaObjekatZaVlasnika(Stage primaryStage, Objekat objekat) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(20, 20, 20, 20));

        Label lblNaslov = new Label("Informacije o objektu \"" + objekat.getNaziv() + "\"");
        Label lblZarada = new Label("Zarada: " + objekat.getZarada());
        Label lblKalendar = new Label("Raspored rezervacija");
        Label lblProslave = new Label("Lista proslava");
        lblNaslov.setStyle("-fx-font: 32 'Comic Sans MS';");

        Set<LocalDate> zauzetiDatumi = Controller.zauzetiDatumi(objekat);
        DatePicker kalendar = new DatePicker();
        Callback<DatePicker, DateCell> izmjenaPolja = dp -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (zauzetiDatumi.contains(item)) {
                    if (item.isBefore(LocalDate.now()))
                        setStyle("-fx-background-color: #ff0000; -fx-text-fill: #000000;");
                    else
                        setStyle("-fx-background-color: #00a3ff; -fx-text-fill: #ffffff;");
                }
            }
        };
        kalendar.setDayCellFactory(izmjenaPolja);
        DatePickerSkin kalendarSkin = new DatePickerSkin(kalendar);

        CheckBox cbProtekle = new CheckBox("Protekle");
        CheckBox cbAktivne = new CheckBox("Aktivne");
        CheckBox cbOtkazane = new CheckBox("Otkazane");
        cbAktivne.setSelected(true);

        Map<Integer, Proslava> proslave = Controller.getProslave();
        ListView<Proslava> lvProslave = new ListView<>();
        lvProslave.setOnMouseClicked(event -> {
            Proslava izabranaProslava = lvProslave.getSelectionModel().getSelectedItem();
            if (izabranaProslava != null) {
                scenaInformacijeOProslavi(izabranaProslava);
            }
        });
        Runnable izmjeniListu = () -> {
            lvProslave.getItems().clear();
            for (Proslava pr : proslave.values()) {
                if (pr.getObjekat().equals(objekat)) {
                    if (cbAktivne.isSelected() && pr.getStatus() == StatusProslave.AKTIVNA)
                        lvProslave.getItems().add(pr);
                    else if (cbProtekle.isSelected() && pr.getStatus() == StatusProslave.PROTEKLA)
                        lvProslave.getItems().add(pr);
                    else if (cbOtkazane.isSelected() && pr.getStatus() == StatusProslave.OTKAZANA)
                        lvProslave.getItems().add(pr);
                }
            }
        };

        cbProtekle.setOnAction(event -> izmjeniListu.run());
        cbAktivne.setOnAction(event -> izmjeniListu.run());
        cbOtkazane.setOnAction(event -> izmjeniListu.run());
        izmjeniListu.run();

        Image strelica = new Image((new File("resursi/backArrow.png")).toURI().toString());
        ImageView prikazStrelice = new ImageView(strelica);
        prikazStrelice.setFitWidth(20);
        prikazStrelice.setFitHeight(20);

        Button btnNazad = new Button("", prikazStrelice);
        btnNazad.setOnAction(actionEvent -> ScenaVlasnik.scenaVlasnik(primaryStage, objekat.getVlasnik()));
        root.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                btnNazad.fire();
            }
        });

        HBox hBoxCheckBox = new HBox(40);
        hBoxCheckBox.getChildren().addAll(cbProtekle, cbAktivne, cbOtkazane);

        VBox vBoxLijevi = new VBox(10);
        vBoxLijevi.getChildren().addAll(lblZarada, lblKalendar, kalendarSkin.getPopupContent());
        VBox vBoxDesni = new VBox(10);
        vBoxDesni.getChildren().addAll(lblProslave, lvProslave, hBoxCheckBox);

        HBox hBox = new HBox(40);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(vBoxLijevi, vBoxDesni);

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