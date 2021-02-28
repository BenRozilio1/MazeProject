package View;

import ViewModel.ViewModel;
import javafx.animation.PauseTransition;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;

/***
 * class responsible for function that connect to
 * the external side of this app
 */
public class MyViewController implements Observer,IView {

    public MazeDisplay mazeDisplay;
    public javafx.scene.control.TextField txtfld_rowsNum;
    public javafx.scene.control.TextField txtfld_columnsNum;
    public javafx.scene.control.Button btn_generateMaze;
    public javafx.scene.control.Button btn_solveMaze;
    public javafx.scene.control.MenuItem btn_properties;
    public javafx.scene.image.ImageView image_adir;

    private boolean Song_flag =false;
    private boolean playing=false;
    private Media sound;
    private MediaPlayer mediaPlayer;
    public javafx.scene.layout.BorderPane myBord;

    @FXML
    private ViewModel viewModel ;
    private Scene mainScene;
    private Stage mainStage;

    public void initialize(ViewModel viewModel, Stage mainStage, Scene mainScene) {
        this.viewModel = viewModel;
        this.mainScene=mainScene;
        this.mainStage=mainStage;
        this.btn_solveMaze.setDisable(true);

    }

    @Override

    /**
     * update the changes in maze:
     * character location ,display solution....
     */
    public void update(Observable o, Object arg) {
        if (o == viewModel) {
            displayMaze(viewModel.getMaze());
            if(playing)
                btn_generateMaze.setDisable(true);

        }
    }

    /**
     * allow to load Saved maze from file.
     */
    public void loadMaze() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Load maze");
        fc.setInitialDirectory(new File("src/Saved_Maze"));
        fc.setSelectedExtensionFilter(new FileChooser.ExtensionFilter("Maze file", ".tmp"));
        File chosen = fc.showOpenDialog((Stage) mazeDisplay.getScene().getWindow());
        if (chosen != null) {
            viewModel.loadMaze(chosen);
            mediaPlayer.stop();
            PlaySong("src/View/resources/Music/game_song.mp3");
        }

    }

    /**
     * allow to save Saved maze from file.
     */
    public void saveMaze() {
        if(playing){
            FileChooser fc = new FileChooser();
            fc.setTitle("Save maze");
            FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Maze file (*.tmp)", ".tmp");
            fc.getExtensionFilters().add(extensionFilter);
            fc.setInitialDirectory(new File("src/Saved_Maze"));
            File chosen = fc.showSaveDialog((Stage)mazeDisplay.getScene().getWindow());
            if (chosen != null) {
                viewModel.SaveMaze(chosen);
            }
        }
        else {
            warning("warning", "cannot save empty maze!");

        }

    }

    /**
     * display on new windows Details about the game
     */
    public void help() {

        try {
            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.setResizable(false);
            newWindow.setTitle("Help");

            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("Help.fxml").openStream());
            Scene scene = new Scene(root, 600, 600);
            scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
            newWindow.setScene(scene);
            newWindow.initModality(Modality.APPLICATION_MODAL);
            // Set position of second window, related to primary window.
            newWindow.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * display information about algorithms , number of threads
     */
    public void ShowProperties(){
        try{
            InputStream input = new FileInputStream("config/config.properties");
            Properties prop = new Properties();
            prop.load(input);
            String s=prop.toString();
            s=s.substring(1,s.length()-1);
            System.out.println(s);
            String[] arr=s.split(",");
            ArrayList<String> strLst=new ArrayList<>();
            String[] arr1=arr[0].split("=");
            String[] arr2=arr[1].split("=");
            String s2="the searching algorithm is: "+arr1[1]+"\n"+"\n"+"the maximum activated players is: "+arr2[1]+"\n";
            System.out.println(s2);
            //PopWindowShowText(s2);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Properties");
            alert.setContentText(s2);
            alert.setHeaderText("Properties");
            alert.show();
        }

        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void displayMaze(int[][] maze) {

        mazeDisplay.setMaze(maze);
        int characterPositionRow = viewModel.getCharacterPositionRow();
        int characterPositionColumn = viewModel.getCharacterPositionColumn();
        mazeDisplay.setCharacterPosition(characterPositionRow, characterPositionColumn);
        this.playing = true;
        this.btn_solveMaze.setDisable(false);
        if (!Song_flag){
            PlaySong("src/View/resources/Music/game_song.mp3");
            this.Song_flag = true;
    }
        if(this.mazeDisplay.getFinish_flag()==true) {
            this.mediaPlayer.stop();
          //  PlaySong("src/View/resources/Music/victory_song.mp3");
            Finish();
        }

    }

    /**
     * catch when the player press on keypad
     */
    public void KeyPressed(KeyEvent keyEvent) {
        this.mazeDisplay.setDirection(keyEvent.getCode());
        viewModel.moveCharacter(keyEvent.getCode());
        keyEvent.consume();


    }

    /**
     * display the solution for the Maze
     */
    public void generateSolution(){
        if(!mazeDisplay.getSolutionFlag()){
            viewModel.generateSolution();
            mazeDisplay.setSolution(viewModel.getSolution());
        }
        mazeDisplay.switch_Solution_status();
        displayMaze(viewModel.getMaze());
    }

    /**
     * clean the information
     * turn off buttons
     */
    public void ClearMaze(){

        if(playing){
            this.txtfld_columnsNum.clear();
            this.txtfld_rowsNum.clear();
            this.mazeDisplay.clear();
            btn_generateMaze.setDisable(false);
            this.btn_solveMaze.setDisable(true);
            playing=false;
        }


    }

    public void exit(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,"", ButtonType.YES,ButtonType.NO);
        alert.setHeaderText("don't wanna stay for sure?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.YES) {
            //Server.Configurations.setValue("algorithms.mazeGenerators.MyMazeGenerator", "mazeGenerator");
            //Server.Configurations.setValue("algorithms.search.BreadthFirstSearch", "searchingAlgorithm");
            System.exit(0);
        }
    }

    /**
     * catch the size of the maze
     * generate Maze
     * check Data
     */
    public void generateMaze() {

        String S_ROW=txtfld_rowsNum.getText();
        String S_COL= txtfld_columnsNum.getText();
        if(!isNumeric(S_COL) || !isNumeric(S_ROW)) {
            warning("WRONG INPUT", "Maze dimensions invalid! choose number between 2 to 500");
            return;
        }
        int heigth = Integer.valueOf(S_ROW);
        int width = Integer.valueOf(txtfld_columnsNum.getText());
        if(heigth<2 || width<2){
            warning("WRONG INPUT", "Maze dimensions invalid! choose number between 2 to 500");
            return;
        }

        viewModel.generateMaze(heigth,width);
        this.mazeDisplay.setStartRow(this.viewModel.get_Start_Row_Pos());
        this.mazeDisplay.setStartCol(this.viewModel.get_Start_Col_Pos());
        this.mazeDisplay.setEndCol(this.viewModel.get_End_Col_Pos());
        this.mazeDisplay.setEndRow(this.viewModel.get_End_Row_Pos());
        displayMaze(viewModel.getMaze());
        btn_generateMaze.setDisable(true);
        playing=true;
    }

    public  boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }

    /**
     * rise custom alert
     */
    public void warning(String title,String contentText){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(contentText);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("Alert.css").toExternalForm());
        alert.show();
    }

    public void setResizeEvent(Scene scene) {
        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                mazeDisplay.setWidth(newSceneWidth.doubleValue()/1.4);
                mazeDisplay.draw();
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                mazeDisplay.setHeight(newSceneHeight.doubleValue()/1.3);
                mazeDisplay.draw();
            }
        });
    }

    public void PlaySong(String songName) {
        sound = new Media(new File(songName).toURI().toString());
        mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public void addMouseScrolling(Node node) {
        node.setOnScroll((ScrollEvent event) -> {

            double zoomFactor = 1.05;
            double deltaY = event.getDeltaY();
            if (deltaY < 0){
                zoomFactor = 2.0 - zoomFactor;
            }
            node.setScaleX(node.getScaleX() * zoomFactor);
            node.setScaleY(node.getScaleY() * zoomFactor);
        });
    }

    /**
     *
     */
    public void Finish() {

        Stage newWindow = new Stage();
        StackPane root = new StackPane();
        MediaPlayer player = new MediaPlayer(new Media(getClass().getResource("finish_video.mp4").toExternalForm()));
        MediaView mediaView = new MediaView(player);
        root.getChildren().add(mediaView);
        Scene scene = new Scene(root, 640, 360);
        newWindow.setScene(scene);
        newWindow.show();
        player.play();
        PauseTransition delay = new PauseTransition(Duration.seconds(13));
        newWindow.setOnCloseRequest(event ->  player.stop());
        delay.setOnFinished( event -> newWindow.close() );
        delay.play();
        ClearMaze();
        this.Song_flag=false;



    }

    /**
     * display on new windows Details about the programmers
     */
    public void about() {

        try {
            // New window (Stage)
            Stage newWindow = new Stage();
            newWindow.setResizable(false);
            newWindow.setTitle("About");

            FXMLLoader fxmlLoader = new FXMLLoader();
            Parent root = fxmlLoader.load(getClass().getResource("About.fxml").openStream());
            Scene scene = new Scene(root, 600, 300);
            scene.getStylesheets().add(getClass().getResource("AboutStyle.css").toExternalForm());

            //scene.getStylesheets().add(getClass().getResource("MainStyle.css").toExternalForm());
            newWindow.setScene(scene);
            newWindow.initModality(Modality.APPLICATION_MODAL);
            // Set position of second window, related to primary window.

            newWindow.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
