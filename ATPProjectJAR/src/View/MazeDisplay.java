package View;

import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MazeDisplay extends Canvas {
    private int[][] Maze;
    private int characterPositionRows;
    private int characterPositionCol;
    private StringProperty ImageFileNameWall = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter = new SimpleStringProperty();
    private StringProperty ImageFileStart = new SimpleStringProperty();
    private StringProperty ImageFileEnd = new SimpleStringProperty();
    private StringProperty ImageFileNameTrail=new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter_1 = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter_2 = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter_3 = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter_6 = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter_9= new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter_8 = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter_7 = new SimpleStringProperty();
    private StringProperty ImageFileNameCharacter_4 = new SimpleStringProperty();
    private StringProperty ImageFileSol=new SimpleStringProperty();
    private Image characterImage;
    private Boolean solution_flag=false;
    private Boolean Finish_flag=false;
    private Solution solution;
    private KeyCode  direction=KeyCode.NUMPAD6;
    private int StartRow;
    private int StartCol;
    private int EndRow;
    private int EndCol;



    public void setSolution(Solution sol){this.solution=sol;}

    public boolean getSolutionFlag(){return this.solution_flag;};

    public void setMaze(int[][] maze){
        this.Maze=maze;
    };

    /**
     * setting the character position
     * @param row row index
     * @param column column index
     */
    public void setCharacterPosition(int row, int column) {
        characterPositionRows = row;
        characterPositionCol = column;
        draw();
    }


    public void setDirection(KeyCode num){this.direction=num;}

    /**
     * spin the character on the board in accordnce the user button pressed
     */
    public void setMazeCharacter(){
        switch (direction){

            case NUMPAD1:

                try {
                    characterImage = new Image(new FileInputStream(ImageFileNameCharacter_1.get()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case NUMPAD2:
                try {
                    characterImage = new Image(new FileInputStream(ImageFileNameCharacter_2.get()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case NUMPAD3:
                try {
                    characterImage = new Image(new FileInputStream(ImageFileNameCharacter_3.get()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case NUMPAD4:
                try {
                    characterImage = new Image(new FileInputStream(ImageFileNameCharacter_4.get()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case NUMPAD6:
                try {
                    characterImage = new Image(new FileInputStream(ImageFileNameCharacter_6.get()));

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case NUMPAD7:
                try {
                   characterImage = new Image(new FileInputStream(ImageFileNameCharacter_7.get()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case NUMPAD8:
                try {
                    characterImage = new Image(new FileInputStream(ImageFileNameCharacter_8.get()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

            case NUMPAD9:
                try {
                    characterImage = new Image(new FileInputStream(ImageFileNameCharacter_9.get()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;

        }

    }

    /**
     * this function drawing the maze on the canvas
     */
    public void draw(){
        if (Maze != null) {
            double canvasHeight = getHeight();
            double canvasWidth = getWidth();
            double cellHeight = canvasHeight / Maze.length;
            double cellWidth = canvasWidth / Maze[0].length;
            setMazeCharacter();

            try {

                //Image characterImage = new Image(new FileInputStream(ImageFileNameCharacter.get()));
                Image wallImage = new Image(new FileInputStream(ImageFileNameWall.get()));
                Image StartImage = new Image(new FileInputStream(ImageFileStart.get()));
                Image EndtImage = new Image(new FileInputStream(ImageFileEnd.get()));
                Image TrailImage = new Image(new FileInputStream(this.ImageFileNameTrail.get()));
                Image SolImage = new Image(new FileInputStream(this.ImageFileSol.get()));
                GraphicsContext gc = getGraphicsContext2D();
                gc.clearRect(0, 0, getWidth(), getHeight());

                //Draw Maze
                for (int i = 0; i < Maze[0].length; i++) {
                    for (int j = 0; j < Maze.length; j++) {
                        if (Maze[j][i] == 1) {
                            gc.drawImage(wallImage, i * cellWidth, j * cellHeight, cellWidth, cellHeight);
                        }
                        if(Maze[j][i] == 0)
                            gc.drawImage(TrailImage, i * cellWidth, j * cellHeight, cellWidth, cellHeight);
                        if (this.solution_flag) {
                            for (int x = 0; x < this.solution.getSolutionPath().size(); x++) {
                                MazeState MazeStep = (MazeState) (this.solution.getSolutionPath().get(x));
                                if (MazeStep.getRowIndex() == j && MazeStep.getColIndex() == i) {
                                    gc.drawImage(SolImage, i * cellWidth, j * cellHeight, cellWidth, cellHeight);
                                    break;
                                }
                            }
                        }

                    }
                }

                        //Draw Character
                        //gc.setFill(Color.RED);
                        //gc.fillOval(characterPositionColumn * cellHeight, characterPositionRow * cellWidth, cellHeight, cellWidth);
                        if(characterPositionCol==EndCol && characterPositionRows==EndRow){ this.Finish_flag=true;}
                        else{this.Finish_flag=false;}
                        gc.drawImage(StartImage, this.StartCol* cellWidth, this.StartRow * cellHeight, cellWidth, cellHeight);
                        gc.drawImage(characterImage, characterPositionCol * cellWidth, characterPositionRows * cellHeight, cellWidth, cellHeight);
                        gc.drawImage(EndtImage, this.EndCol * cellWidth, this.EndRow * cellHeight, cellWidth, cellHeight);

                    } catch(FileNotFoundException e){
                        e.printStackTrace();
                    }
                }
            }

    public String getImageFileNameWall() {
        return ImageFileNameWall.get();
    }

    public String getImageFileStart() {
        return ImageFileStart.get();
    }

    public String getImageFileEnd() {
        return ImageFileEnd.get();
    }

    public String getImageFileTrail() {
        return this.ImageFileNameTrail.get();
    }

    public void setImageFileEnd(String imageFileEnd) {
        this.ImageFileEnd.set(imageFileEnd);
    }

    public void setImageFileStart(String imageFileStart) {
        this.ImageFileStart.set(imageFileStart);
    }

    public void setImageFileNameWall(String imageFileNameWall) {
        this.ImageFileNameWall.set(imageFileNameWall);
    }

    public void setImageFileTrail(String imageFileTrail) {
        this.ImageFileNameTrail.set(imageFileTrail);
    }

    public String getImageFileNameCharacter() {
        return ImageFileNameCharacter.get();
    }

     public String getImageFileNameCharacter_1() {
        return ImageFileNameCharacter_1.get();
    }

     public String getImageFileNameCharacter_2() {
        return ImageFileNameCharacter_2.get();
    }

     public String getImageFileNameCharacter_3() {
        return ImageFileNameCharacter_3.get();
    }

     public String getImageFileNameCharacter_6() {
        return ImageFileNameCharacter_6.get();
    }

     public String getImageFileNameCharacter_9() {
        return ImageFileNameCharacter_9.get();
    }

     public String getImageFileNameCharacter_8() {
        return ImageFileNameCharacter_8.get();
    }

     public String getImageFileNameCharacter_7() {
        return ImageFileNameCharacter_7.get();
    }

    public String getImageFileSol() {
        return ImageFileSol.get();
    }

 public String getImageFileNameCharacter_4() {
        return ImageFileNameCharacter_4.get();
    }

    /**
     *
     * setting the imafe positions of the dragon character
     */
    public void setImageFileNameCharacter(String imageFileNameCharacter) { this.ImageFileNameCharacter.set(imageFileNameCharacter); }
    public void setImageFileNameCharacter_1(String imageFileNameCharacter_1) { this.ImageFileNameCharacter_1.set(imageFileNameCharacter_1); }
    public void setImageFileNameCharacter_2(String imageFileNameCharacter_2) { this.ImageFileNameCharacter_2.set(imageFileNameCharacter_2); }
    public void setImageFileNameCharacter_3(String imageFileNameCharacter_3) { this.ImageFileNameCharacter_3.set(imageFileNameCharacter_3); }
    public void setImageFileNameCharacter_6(String imageFileNameCharacter_6) { this.ImageFileNameCharacter_6.set(imageFileNameCharacter_6); }
    public void setImageFileNameCharacter_9(String imageFileNameCharacter_9) { this.ImageFileNameCharacter_9.set(imageFileNameCharacter_9); }
    public void setImageFileNameCharacter_8(String imageFileNameCharacter_8) { this.ImageFileNameCharacter_8.set(imageFileNameCharacter_8); }
    public void setImageFileNameCharacter_7(String imageFileNameCharacter_7) { this.ImageFileNameCharacter_7.set(imageFileNameCharacter_7); }
    public void setImageFileNameCharacter_4(String imageFileNameCharacter_4) { this.ImageFileNameCharacter_4.set(imageFileNameCharacter_4); }
    public void setImageFileSol(String imageFileSol) { this.ImageFileSol.set(imageFileSol); }

    public void setStartRow(int startRow) { StartRow = startRow; }

    public void setStartCol(int startCol) { StartCol = startCol; }

    public void setEndRow(int endRow) { EndRow = endRow;
    }

    public void setEndCol(int endCol) {
        EndCol = endCol;
    }

    /**
     * this function in charge in case that the user prresed on solution button
     */
    public void switch_Solution_status(){
        if(this.solution_flag==true)
            this.solution_flag=false;
        else
            this.solution_flag=true;
    }

    /**
     * this function clear the canvas from the maze
     */
    public void clear(){
        this.Maze=null;
        this.solution=null;
        this.Finish_flag=false;
        this.solution_flag=false;
        GraphicsContext gc = getGraphicsContext2D();
        gc.clearRect(0, 0, getWidth(), getHeight());

    }

    public Boolean getFinish_flag() {
        return Finish_flag;
    }

    /**
     * this function update in case the character arrived to finish point and switch the flag.
     * @param finish_flag
     */
    public void setFinish_flag(Boolean finish_flag) {
        Finish_flag = finish_flag;
    }
}
