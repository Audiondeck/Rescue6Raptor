package org.tensorflow.lite.examples.classification.rover;

public class FieldActivity {
    double blockWidth = 2.5;
    double blockLength = 5;

    private int blocksPerLane;
    private int lanes;

    static int notSearched = 0;
    static int searched = 1;
    static int found = 2;

    public int[][] grid = new int[lanes][blocksPerLane];


    //Sets blocksPerLane, lanes
    public void calculateFieldParams(double fieldWidth, double fieldLength) {
        lanes = (int) (fieldWidth / blockWidth);
        blocksPerLane = (int) (fieldLength / blockLength);
    }

    //Creates the grid similar to battleship
    public void createGrid() {
        for (int i = 1; i < grid.length; i++) {
            for (int j = 1; j < grid[i].length; j++) {
                grid[i][j] = notSearched;
            }
        }
    }

    //TODO: updateGrid()
    //Take in other grids from other rovers (possibly pulls from SN) and update this one with updated information

    //Changes value next to key from notSearched to searched
    void searchedBlock(int lane, int block) {
        grid[lane][block] = searched;
    }

    //Changes value next to key from notSearched to found
    void foundInBlock(int lane, int block) {
        grid[lane][block] = found;
    }

    public int getBlocksPerLane() {
        return blocksPerLane;
    }

    public int getLanes() {
        return lanes;
    }
}