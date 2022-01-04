package main;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    public int score = 0;
    public int maxTile = 0;

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
        boolean isChanged = false;

        for (int i = 0; i < gameTiles.length; i++) {
            isChanged |= consolidateTiles(gameTiles[i]);
            isChanged |= mergeTiles(gameTiles[i]);
        }

        if (isChanged) addTile();
    }
}