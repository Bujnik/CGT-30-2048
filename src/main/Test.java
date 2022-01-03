package main;

import main.Tile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Test {
    static int score = 0;
    static int maxTile = 0;
    public static void main(String[] args) {
        int a = 0;
        int b = 0;
        int c = 2;
        int d = 16;
        Tile[] tiles = new Tile[]{new Tile(a),new Tile(b), new Tile(c),new Tile(d)};
        consolidateTiles(tiles);
        System.out.println(score);
        System.out.println(maxTile);
        System.out.println(Arrays.toString(tiles));


    }

    private static void consolidateTiles(Tile[] tiles) {
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

        mergeTiles(tiles);
    }

    private static void mergeTiles(Tile[] tiles) {
        for (int i = 0; i < tiles.length - 1; i++){
            if(tiles[i].value == tiles[i + 1].value && !tiles[i].isEmpty()){
                //if we have not empty tile with same value as neighbouring one,
                //we check if maxTile needs to be checked and score increased by double of value of checked tile
                //for example, in row {4 4 0 0} we add 8 points

                //Double value of checked tile, set value of next one to zero, add points to the score

                int points = tiles[i].value * 2;
                tiles[i].setValue(points);
                tiles[i + 1].setValue(0);
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
    }
}