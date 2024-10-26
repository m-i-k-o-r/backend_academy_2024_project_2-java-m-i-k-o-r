package backend.academy.generators;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.security.SecureRandom;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

public class RecursiveBacktrackerMazeGenerator implements Generator {
    private static final Random RANDOM = new SecureRandom();

    @Override
    public Maze generate(int height, int width) {
        int normalizedHeight = (height % 2 == 0) ? height + 1 : height;
        int normalizedWidth = (width % 2 == 0) ? width + 1 : width;

        Cell[][] grid = new Cell[normalizedHeight][normalizedWidth];

        for (int row = 0; row < normalizedHeight; row++) {
            for (int col = 0; col < normalizedWidth; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL);
            }
        }

        Deque<Cell> arrayCells = new ArrayDeque<>();
        int startRow = 1;
        int startCol = 1;
        Cell startCell = new Cell(startRow, startCol, Cell.Type.PASSAGE);
        grid[startRow][startCol] = startCell;
        arrayCells.push(startCell);

        while (!arrayCells.isEmpty()) {
            Cell current = arrayCells.peek();
            List<Cell> neighbors = getUnvisitedNeighbors(grid, current, normalizedHeight, normalizedWidth);

            if (!neighbors.isEmpty()) {

                Cell next = neighbors.get(RANDOM.nextInt(neighbors.size()));

                int midRow = (int) ((1. * current.row() + next.row()) / 2);
                int midCol = (int) ((1. * current.col() + next.col()) / 2);
                grid[midRow][midCol] = new Cell(midRow, midCol, Cell.Type.PASSAGE);
                grid[next.row()][next.col()] = next;

                arrayCells.push(next);
            } else {
                arrayCells.pop();
            }
        }

        return new Maze(normalizedHeight, normalizedWidth, grid);
    }

    private static final int STEP_SIZE = 2;

    private List<Cell> getUnvisitedNeighbors(Cell[][] grid, Cell cell, int height, int width) {
        List<Cell> neighbors = new ArrayList<>();
        int row = cell.row();
        int col = cell.col();

        List<Coordinate> directions = Arrays.asList(
            new Coordinate(0, STEP_SIZE),
            new Coordinate(STEP_SIZE, 0),
            new Coordinate(0, -STEP_SIZE),
            new Coordinate(-STEP_SIZE, 0)
        );

        for (Coordinate dir : directions) {
            int newRow = row + dir.row();
            int newCol = col + dir.col();

            if (newRow > 0 && newRow < height - 1 && newCol > 0 && newCol < width - 1
                && grid[newRow][newCol].type() == Cell.Type.WALL) {
                neighbors.add(new Cell(newRow, newCol, Cell.Type.PASSAGE));
            }
        }

        return neighbors;
    }

    @Override
    public String name() {
        return "Recursive Backtracker";
    }
}
