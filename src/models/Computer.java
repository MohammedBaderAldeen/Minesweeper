package models;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Computer extends Player implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = -2528260191376535498L;
	private transient Cell[][] grid;

    public Computer(Cell[][] grid) {
        super("Computer");
        this.grid = grid;
    }

    @Override
    public void makeMove(List<Pair<Integer, Integer>> shuffledCoordinates) {
        boolean openAction = true;
        Pair<Integer, Integer> coordinate;
        Cell cell;
        for (int i = shuffledCoordinates.size() - 1; i >= 0; i--) {
            coordinate = shuffledCoordinates.get(i);
            cell = this.grid[coordinate.getKey()][coordinate.getValue()];
            if (!cell.getIsOpened() && !cell.getIsFlagged()) {
                if (openAction) {
                    cell.onLeftClick();
                } else {
                    cell.onRightClick();
                }
                return;
            }
        }
    }
}
