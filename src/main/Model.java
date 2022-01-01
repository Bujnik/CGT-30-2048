package main;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];

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
}