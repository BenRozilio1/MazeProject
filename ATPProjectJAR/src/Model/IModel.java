package Model;

import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {

    void GenerateMaze(int Rows,int Column);
    int[][] getMaze();
    void MoveCharacter(KeyCode Movement);
    int getCharacterPositionRow();
    int  getCharacterPositionCol();
    void Close();
    algorithms.mazeGenerators.Maze getMazeObj();
    Solution GenerateSolution();
    Solution getSolution();
    void moveCharacter(KeyCode movement);
    int get_Start_Row_Pos();
    int get_End_Row_Pos();
    int get_Start_Col_Pos();
    int get_End_Col_Pos();
    void loadMaze(File chosen);
    void SaveMaze(File f);
}
