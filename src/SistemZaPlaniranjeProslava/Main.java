package SistemZaPlaniranjeProslava;

import SistemZaPlaniranjeProslava.Scene.ScenaZaPrijavu;
import javafx.application.Application;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        Controller.connectDB();
        Controller.ucitavanjeIzBaze();
        launch(args);
    }

    public static void upozorenje(String poruka) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setContentText(poruka);
        a.showAndWait();
    }

    public static void informacija(String poruka) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setContentText(poruka);
        a.showAndWait();
    }

    public static boolean potvrda(String poruka) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setContentText(poruka);
        ButtonType izbor = a.showAndWait().orElse(ButtonType.CANCEL);
        return izbor == ButtonType.OK;
    }

    public static <T extends javafx.scene.control.TextInputControl> void ocistiPolje(T polje) {
        polje.clear();
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Slobodan sto");
        ScenaZaPrijavu.scenaPrijava(primaryStage);
    }

    @Override
    public void stop() {
        //Controller.ispisBaze();
        Controller.disconnectDB();
    }
}