package main;

import java.awt.event.KeyAdapter;

public class Controller extends KeyAdapter {
    private Model model;
    private View view;

    public Tile[][] getGameTiles(){
        return model.getGameTiles();
    }
    public int getScore(){
        return model.score;
    }
}
