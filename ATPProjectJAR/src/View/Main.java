package View;

import Model.Model;
import ViewModel.ViewModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Font.loadFont(Main.class.getResource("resources/Font/Game_of_Thrones.ttf").toExternalForm(), 12);

        //ViewModel -> Model
        Model model = new Model();
        ViewModel viewModel = new ViewModel(model);
        model.addObserver(viewModel);

        //Loading Main Windows
        primaryStage.setTitle("My Application!");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("MyView.fxml").openStream());
        Scene scene = new Scene(root, 900, 700);
        scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
        primaryStage.setScene(scene);


        //View -> ViewModel
        MyViewController view = fxmlLoader.getController();
        view.initialize(viewModel,primaryStage,scene);
        viewModel.addObserver(view);
        //--------------

        view.setResizeEvent(scene);
        view.addMouseScrolling(view.mazeDisplay);



        setStageCloseEvent(view,primaryStage);
        //
        //Show the Main Window
        primaryStage.show();
    }

    private void setStageCloseEvent(MyViewController view,Stage primaryStage) {
        primaryStage.setOnCloseRequest(event -> view.exit());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
