import java.lang.reflect.Array;
import java.util.Random;
import java.util.ArrayList;

public class Board {

    int[][] blocks;
    int[][] manhattanDistances;
    int blankPosition;

    public Board(int[][] blocks) {
        this.blocks = blocks;
        this.manhattanDistances = new int[blocks.length][blocks.length];
        this.initManhattanDistances();
        this.findBlankPosition();
    }

    public int dimension() {
        return this.blocks.length;
    }

    public int hamming() {
        int blocksOutOfPlace = 0;
        for (int i = 0; i < this.dimension(); i++)
            for (int j = 0; j < this.dimension(); j++)
                if (isBlockOutOfPlace(this.blocks[i][j], i, j)) blocksOutOfPlace++;

        return blocksOutOfPlace;
    }

    public int manhattan() {
        int sum = 0;
        for (int i = 0; i < this.dimension(); i++)
            for (int j = 0; j < this.dimension(); j++)
                sum += this.manhattanDistances[i][j];

        return sum;
    }

    public boolean isGoal() {
        for (int i = 0; i < this.dimension(); i++)
            for (int j = 0; j < this.dimension(); j++)
                if (isBlockOutOfPlace(this.blocks[i][j], i, j)) return false;

        return true;
    }

    public Board twin() {

        Random rand = new Random();
        int n = this.dimension() * this.dimension();
        int randomPosition = rand.nextInt(n);
        if (randomPosition == this.blankPosition) randomPosition = (randomPosition + 1) % n;
        int randomPosition2 = rand.nextInt(n);
        if (randomPosition2 == this.blankPosition) randomPosition2 = (randomPosition2 + 1) % n;
        if (randomPosition2 == randomPosition) randomPosition2 = (randomPosition2 + 1) % n;

        int[][] twinBlocks = this.copyBlocks();

        int number1 = twinBlocks[getRowFromPosition(randomPosition)][getColFromPosition(randomPosition)];
        int number2 = twinBlocks[getRowFromPosition(randomPosition2)][getColFromPosition(randomPosition2)];
        twinBlocks[getRowFromPosition(randomPosition)][getColFromPosition(randomPosition)] = number2;
        twinBlocks[getRowFromPosition(randomPosition2)][getColFromPosition(randomPosition2)] = number1;

        return new Board(twinBlocks);

    }

    public boolean equals(Object y) {

        Board board = (Board) y;
        if (this.dimension() != board.dimension()) return false;
        for (int i = 0; i < this.dimension(); i++)
            for (int j = 0; j < this.dimension(); j++)
                if (this.blocks[i][j] != board.blocks[i][j]) return false;

        return true;
    }


    public Iterable<Board> neighbors() {

        ArrayList<Board> neighboringBoards = new ArrayList<>();
        for (int position : neighborsOfBlankPosition()) {
            neighboringBoards.add(newBoardFromMove(position));
        }

        return neighboringBoards;
    }

    public String toString() {

        String res = "" + this.dimension() + "\n";

        for (int i = 0; i < this.dimension(); i++) {
            for (int j = 0; j < this.dimension(); j++) {
                res += " " + this.blocks[i][j] + " ";
            }
            res += "\n";
        }

        return res;
    }

    private ArrayList<Integer> neighborsOfBlankPosition() {

        ArrayList<Integer> neighbors = new ArrayList<>();
        int row = this.blankPosition / this.dimension();
        int col = this.blankPosition % this.dimension();
        if (row + 1 < this.dimension()) neighbors.add(getPositionFromRowAndCol(row + 1, col));
        if (row - 1 >= 0) neighbors.add(getPositionFromRowAndCol(row - 1, col));
        if (col + 1 < this.dimension()) neighbors.add(getPositionFromRowAndCol(row, col + 1));
        if (col - 1 >= 0) neighbors.add(getPositionFromRowAndCol(row, col - 1));

        return neighbors;
    }

    private int manhattanDistance(int number, int currentRow, int currentCol) {
        int n = this.dimension();
        number--;
        int goalRow = number / n;
        int goalCol = number % n;
        return Math.abs(currentCol - goalCol) + Math.abs(currentRow - goalRow);
    }

    private void initManhattanDistances() {
        for (int i = 0; i < this.dimension(); i++)
            for (int j = 0; j < this.dimension(); j++)
                this.manhattanDistances[i][j] = manhattanDistance(this.blocks[i][j], i, j);
    }

    private void findBlankPosition() {
        for (int i = 0; i < this.dimension(); i++)
            for (int j = 0; j < this.dimension(); j++)
                if (this.blocks[i][j] == 0) this.blankPosition = i + j;
    }

    private boolean isBlockOutOfPlace(int number, int row, int column) {
        return getPositionFromRowAndCol(row, column) == --number;
    }

    private int getPositionFromRowAndCol(int row, int col) {
        return row * this.dimension() + col;
    }

    private int getRowFromPosition(int position) {
        return position / this.dimension();
    }

    private int getColFromPosition(int position) {
        return position % this.dimension();
    }

    private int[][] copyBlocks() {
        int[][] newBlocks = new int[this.dimension()][this.dimension()];
        for (int i = 0; i < this.dimension(); i++) {
            System.arraycopy(this.blocks[i], 0, newBlocks[i], 0, this.dimension());
        }
        return newBlocks;
    }

    private Board newBoardFromMove(int position) {
        int[][] newBlocks = copyBlocks();
        int row = getRowFromPosition(position);
        int col = getColFromPosition(position);
        int value = newBlocks[row][col];
        newBlocks[row][col] = 0;
        newBlocks[getRowFromPosition(this.blankPosition)][getColFromPosition(this.blankPosition)] = value;

        return new Board(newBlocks);
    }

    public static void main(String[] args) {

    }

}
