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
    private static final String DATA_DIR_PATH = "./data";
    private static final String MAIN_WINDOW_FXML_PATH = "/view/MainWindow.fxml";

    private final Eclipse chatbot = new Eclipse(DATA_DIR_PATH);

    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(MAIN_WINDOW_FXML_PATH));
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
