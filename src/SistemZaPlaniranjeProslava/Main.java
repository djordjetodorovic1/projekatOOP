package SistemZaPlaniranjeProslava;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Main extends Application {
    private Controller controller = new Controller();
    public static void main(String[] args) {
        launch(args);
    }
    public void start(Stage primaryStage) {
        //Scena LogIn
        VBox root = new VBox(10);
        root.setPadding(new Insets(10, 10, 10, 10));

        Label lblTekst = new Label("Dobrodosli u nasu aplikaciju");
        lblTekst.setTextFill(Color.BLUE);


        Button btn = new Button("KLikni");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                controller.logIn();
            }
        });

        root.getChildren().addAll(lblTekst, btn);
        Scene scene = new Scene(root, 400, 200);

        primaryStage.setScene(scene);
        primaryStage.setTitle("Aplikacija");
        primaryStage.show();
    }
}