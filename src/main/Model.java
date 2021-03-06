package main;

import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    public int score = 0;
    public int maxTile = 0;

    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public Model(){
        resetGameTiles();
    }

    public void resetGameTiles(){
        //Game Start: Reset board to empty state and add 2 random tiles
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    private void addTile(){
        List<Tile> tiles = getEmptyTiles();
        //We choose random empty tile to spawn new one
        if (!tiles.isEmpty())
        {
            int chosenIndex = (int)(Math.random() * (tiles.size()));
            //We have 10% chance of spawning tile with value of 4.
            tiles.get(chosenIndex).setValue(Math.random() < 0.9 ? 2 : 4);
        }

    }

    private List<Tile> getEmptyTiles(){
        List<Tile> tiles = new ArrayList<>();
        for (int i = 0; i < FIELD_WIDTH; i++){
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].isEmpty()) tiles.add(gameTiles[i][j]);
            }
        }
        return tiles;
    }

    private boolean consolidateTiles(Tile[] tiles) {
        //In order to check if changes were made to the array, we create deep copy of tiles array - inserting new Tile objects with the same value
        Tile[] tempCheck = new Tile[tiles.length];
        int k = 0;
        for (Tile tile : tiles) {
            tempCheck[k] = new Tile(tile.value);
            k++;
        }
        List<Integer> temp = new ArrayList<>();
        for (Tile tile : tiles) {
            //we iterate through tiles, if tile is not empty, we add its value to temporary list
            if (tile.value != 0) {
                temp.add(tile.value);
            }
        }

        //Replacing values in array with values from temp list, remaining places will be set to 0
        for (int i = 0; i < temp.size(); i++) tiles[i].setValue(temp.get(i));
        for (int i = temp.size(); i < tiles.length; i++) tiles[i].setValue(0);

        //if value of a tile is not matching value from tempCheck array, we return true, as we changed the array
        for (int i = 0; i < tempCheck.length; i++) {
            if (tempCheck[i].value != tiles[i].value) return true;
        }
        return false;
    }

    private boolean mergeTiles(Tile[] tiles) {
        //If merge operation is performed, flag isChanged is set to true
        boolean isChanged = false;
        for (int i = 0; i < tiles.length - 1; i++){
            if(tiles[i].value == tiles[i + 1].value && !tiles[i].isEmpty()){
                //if we have not empty tile with same value as neighbouring one,
                //we check if maxTile needs to be checked and score increased by double of value of checked tile
                //for example, in row {4 4 0 0} we add 8 points

                //Double value of checked tile, set value of next one to zero, add points to the score

                int points = tiles[i].value * 2;
                tiles[i].setValue(points);
                tiles[i + 1].setValue(0);
                isChanged = true;
                score += points;
                //Update max value if needed
                maxTile = Math.max(maxTile, tiles[i].value);
                //Move remaining tiles to the left
                for (int j = i + 1; j < tiles.length - 1; j++){
                    tiles[j].setValue(tiles[j + 1].value);
                    tiles[j + 1].setValue(0);
                }
            }
        }
        return isChanged;
    }

    public void left(){
        if (isSaveNeeded) saveState(gameTiles);
        //It's default setting, no rotation is necessary
        processMove();
        isSaveNeeded = true;
    }
    public void up(){
        if (isSaveNeeded) saveState(gameTiles);
        //If we rotate counterclockwise, and then consolidate to the left, we got movement upwards
        rotateCounterCW();
        processMove();
        //We need to return to initial state
        rotateCW();
        isSaveNeeded = true;
    }
    public void right(){
        if (isSaveNeeded) saveState(gameTiles);
        //In order to get right movement, we have to flip board by 180 degrees, and then again to return to initial state
        rotateCW();
        rotateCW();
        processMove();
        rotateCW();
        rotateCW();
        isSaveNeeded = true;
    }
    public void down(){
        if (isSaveNeeded) saveState(gameTiles);
        //We need to rotate CW and then CCW afterwards to process this move correctly
        rotateCW();
        processMove();
        rotateCounterCW();
        isSaveNeeded = true;
    }
    private void processMove() {
        //If consolidate/merge methods change game state, we add new random tile
        boolean isChanged = false;
        for (int i = 0; i < gameTiles.length; i++) {
            isChanged |= consolidateTiles(gameTiles[i]);
            isChanged |= mergeTiles(gameTiles[i]);
        }
        if (isChanged) addTile();
    }

    private void rotateCW(){
        //This method will help us rotate our matrix.
        //In order to rotate 90 degrees clockwise, we need to populate element at coordinates [i][j]
        //with element[size - j - 1][i]. We'll do that through temp matrix and clone it into gameTiles afterwards
        Tile[][] temp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++){
            for (int j = 0; j < FIELD_WIDTH; j++) temp[i][j] = gameTiles[FIELD_WIDTH - j - 1][i];
        }
        gameTiles = temp.clone();
    }
    private Tile[][] rotateCW(Tile[][] board){
        //This method will help us rotate our matrix.
        //In order to rotate 90 degrees clockwise, we need to populate element at coordinates [i][j]
        //with element[size - j - 1][i]. We'll do that through temp matrix and clone it into gameTiles afterwards
        Tile[][] temp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++){
            for (int j = 0; j < FIELD_WIDTH; j++) temp[i][j] = board[FIELD_WIDTH - j - 1][i];
        }
        return temp.clone();
    }

    private void rotateCounterCW(){
        //This method will help us rotate our matrix.
        //In order to rotate 90 degrees counter-clockwise, we need to populate element at coordinates [i][j]
        //with element[j][size - i - 1]. We'll do that through temp matrix and clone it into gameTiles afterwards
        Tile[][] temp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++){
            for (int j = 0; j < FIELD_WIDTH; j++) temp[i][j] = gameTiles[j][FIELD_WIDTH - i - 1];
        }
        gameTiles = temp.clone();
    }

    public boolean canMove(){
        Tile[][] temp = copyBoard(gameTiles);
        //Let's check if we can consolidate or merge tiles on all directions,
        //Perform this operation 4 times, logic will be as in processMove() method
        //After each operation we will rotate the board clockwise
        //Once result of consolidate/merge is true, we will return that value
        for (int j = 0; j < 4; j++) {
            for (Tile[] tiles : temp) {
                if (consolidateTiles(tiles)) return true;
                if (mergeTiles(tiles)) return true;
            }
            temp = rotateCW(temp);
        }
        return false;
    }

    private void saveState(Tile[][] board) {
        Tile[][] temp = copyBoard(board);
        previousStates.push(temp);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    private Tile[][] copyBoard(Tile[][] board) {
        //Create deep copy of passed board
        Tile[][] temp = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) temp[i][j] = new Tile(board[i][j].value);
        }
        return temp;
    }

    public void rollback(){
        boolean canChange = !previousScores.isEmpty() && !previousStates.isEmpty();
        if (canChange) {
            score = previousScores.pop();
            gameTiles = previousStates.pop();
        }
    }
    public void randomMove(){
        int n = (int)(Math.random()*4) + 1;
        switch (n){
            case 1:
                left();
                break;
            case 2:
                right();
                break;
            case 3:
                up();
                break;
            case 4:
                down();
                break;
        }
    }
    public boolean hasBoardChanged(){
        Tile[][] backup = previousStates.peek();
        for (int i = 0; i < FIELD_WIDTH; i++){
            for (int j = 0; j < FIELD_WIDTH; j++){
                if (backup[i][j].value != gameTiles[i][j].value) return true;
            }
        }
        return false;
    }
    public MoveFitness getMoveFitness(Move move){
        move.move();
        int emptyTiles = -1;
        int sc = 0;
        if (hasBoardChanged()){
            emptyTiles = getEmptyTiles().size();
            sc = score;
            rollback();
        }
        return new MoveFitness(emptyTiles,sc,move);
    }
    public void autoMove(){
        PriorityQueue<MoveFitness> moveQueue = new PriorityQueue<>(4, Collections.reverseOrder());
        moveQueue.add(getMoveFitness(this::left));
        moveQueue.add(getMoveFitness(this::right));
        moveQueue.add(getMoveFitness(this::up));
        moveQueue.add(getMoveFitness(this::down));
        if (moveQueue.peek() != null) {
            moveQueue.peek().getMove().move();
        }
    }
}