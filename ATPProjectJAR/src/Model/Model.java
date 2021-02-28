package Model;

//import algorithms.mazeGenerators.*;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.util.Observable;

public class Model extends Observable implements IModel {

    private int characterPositionRow ;
    private int characterPositionColumn;
    private algorithms.mazeGenerators.MyMazeGenerator generator=new algorithms.mazeGenerators.MyMazeGenerator();
    private algorithms.mazeGenerators.Maze G_maze;
    private int[][] MatrixMaze;
    private  int numOfRow;
    private  int numOfCol;
    Server_Operation Server_generator;
    private Solution solve;

    public Model() {

    }
    /***
     *  the function loading a existing maze and notify the observers on the model
     * @param f the maze we should to open
     */
    public void loadMaze(File f)  {

        FileInputStream loadFile = null;
        try {
            loadFile = new FileInputStream(f);
            ObjectInputStream tmp=new ObjectInputStream(new FileInputStream(f));
            this.G_maze=(Maze)tmp.readObject();
            this.MatrixMaze = G_maze.getTheMaze();
            this.characterPositionColumn = G_maze.getStart_position().getColumnIndex();
            characterPositionRow = G_maze.getStart_position().getRowIndex();
            loadFile.close();
            tmp.close();
            setChanged();
            notifyObservers();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * this function save a new maze
     * @param f the maze we want to save
     */
    @Override
    public void SaveMaze(File f) {
        if(f==null)
            return;
        try {

            new FileWriter(f.getName(), true);
            ObjectOutputStream buf = new ObjectOutputStream(new FileOutputStream(f.getPath()));
            buf.writeObject(this.G_maze);
            buf.flush();
            buf.close();
        } catch (IOException var11) {
            var11.printStackTrace();
        }
    }
    /**
     * this function generating a new maze with the server
     * @param Rows the number of the rows in the maze
     * @param Column the number of the columns in the maze
     */
    @Override
    public void GenerateMaze(int Rows, int Column) {
        this.Server_generator=new Server_Operation(Rows,Column);
        this.G_maze=this.Server_generator.Generate_Operate();
        //this.G_maze=this.generator.generate(Rows,Column);
        this.MatrixMaze=this.G_maze.getTheMaze();
        characterPositionRow=this.G_maze.getStart_position().getRowIndex();
        characterPositionColumn=this.G_maze.getStart_position().getColumnIndex();
        this.numOfRow=this.MatrixMaze.length;
        this.numOfCol=this.MatrixMaze[0].length;
        setChanged(); //Raise a flag that I have changed
        notifyObservers();
    }

    @Override
    public int[][] getMaze() {
        return MatrixMaze;
    }

    @Override
    public void MoveCharacter(KeyCode Movement) {

    }
    /**
     * this function generating a solution with the server
     * @return the solve of the maze
     */
    public Solution GenerateSolution(){
        this.solve=this.Server_generator.Solve_operate();
        return this.solve;
    }

    @Override
    public Solution getSolution() {
        return this.solve;
    }


    public algorithms.mazeGenerators.Maze getMazeObj(){return this.G_maze;}

    /**
     * this function in charge of the movement of the character ,chack if the next move is legal.
     * @param movement the button pressed by user
     */
    @Override
    public void moveCharacter(KeyCode movement) {

        Position next_pos=new algorithms.mazeGenerators.Position(0,0) ;
        switch (movement) {
            case NUMPAD8:
                next_pos.setRowPosition(characterPositionRow-1);
                next_pos.setColumnPosition(characterPositionColumn);
                if (characterPositionRow > 0 && this.G_maze.validPosition(next_pos))
                    characterPositionRow--;
                break;


            case NUMPAD2:
                next_pos.setRowPosition(characterPositionRow+1);
                next_pos.setColumnPosition(characterPositionColumn);
                if (characterPositionRow < this.numOfRow - 1 && this.G_maze.validPosition(next_pos) )
                    characterPositionRow++;
                break;

            case NUMPAD6:
                next_pos.setRowPosition(characterPositionRow);
                next_pos.setColumnPosition(characterPositionColumn+1);
                if (characterPositionColumn < this.numOfCol - 1 && this.G_maze.validPosition(next_pos) )
                    characterPositionColumn++;
                break;

            case NUMPAD4:
                next_pos.setRowPosition(characterPositionRow);
                next_pos.setColumnPosition(characterPositionColumn-1);
                if (characterPositionColumn > 0 && this.G_maze.validPosition(next_pos) )
                    characterPositionColumn--;
                break;
            case NUMPAD7:
                next_pos.setRowPosition(characterPositionRow-1);
                next_pos.setColumnPosition(characterPositionColumn-1);
                if(characterPositionRow>0 && characterPositionColumn>0 && this.G_maze.validPosition(next_pos)  )
                {
                    characterPositionRow--;
                    characterPositionColumn--;
                }
                break;
            case  NUMPAD9:
                next_pos.setRowPosition(characterPositionRow-1);
                next_pos.setColumnPosition(characterPositionColumn+1);
                if(characterPositionRow>0 && characterPositionColumn<this.numOfCol  &&  this.G_maze.validPosition(next_pos) ){
                    characterPositionRow--;
                    characterPositionColumn++;
                }
                break;
            case NUMPAD1:
                next_pos.setRowPosition(characterPositionRow+1);
                next_pos.setColumnPosition(characterPositionColumn-1);
                if(characterPositionRow<this.numOfRow && characterPositionColumn>0 &&  this.G_maze.validPosition(next_pos)){
                    characterPositionRow++;
                    characterPositionColumn--;
                }
                break;
            case NUMPAD3:
                next_pos.setRowPosition(characterPositionRow+1);
                next_pos.setColumnPosition(characterPositionColumn+1);
                if(characterPositionRow<this.numOfRow && characterPositionColumn<this.numOfCol && this.G_maze.validPosition(next_pos)){
                    characterPositionRow++;
                    characterPositionColumn++;
                }
                break;

            case HOME:
                characterPositionRow=this.G_maze.getStart_position().getRowIndex();
                characterPositionColumn=this.G_maze.getStart_position().getColumnIndex();
                break;
        }
        setChanged();
        notifyObservers();
    }

    @Override
    public int get_Start_Row_Pos() {
        return this.G_maze.getStart_position().getRowIndex();
    }

    @Override
    public int get_End_Row_Pos() {
        return this.G_maze.getEnd_position().getRowIndex();
    }

    @Override
    public int get_Start_Col_Pos() {
        return this.G_maze.getStart_position().getColumnIndex();
    }

    @Override
    public int get_End_Col_Pos() {
        return this.G_maze.getEnd_position().getColumnIndex();
    }

    @Override
    public int getCharacterPositionRow() {
        return this.characterPositionRow;
    }

    @Override
    public int getCharacterPositionCol() {
        return this.characterPositionColumn;
    }

    @Override
    public void Close() {

    }
}
