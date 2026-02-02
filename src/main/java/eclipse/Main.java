package eclipse;

import java.io.IOException;

import eclipse.controllers.MainWindow;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

//CHECKSTYLE.OFF: MissingJavadocType
public class Main extends Application {
    private String dirPath = "./data";
    private Eclipse chatbot = new Eclipse(dirPath);

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/view/MainWindow.fxml"));
            AnchorPane ap = fxmlLoader.load();

            Scene scene = new Scene(ap);
            stage.setScene(scene);
            fxmlLoader.<MainWindow>getController().setEclipse(chatbot);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
