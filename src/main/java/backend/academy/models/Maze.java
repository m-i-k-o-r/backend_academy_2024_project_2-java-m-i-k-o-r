package backend.academy.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public record Maze(
    int height,
    int width,
    Cell[][] grid
) {
    public Map<Cell, List<Cell>> toGraph() {
        Map<Cell, List<Cell>> graph = new HashMap<>();

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Cell currentCell = grid[row][col];
                if (currentCell.type() == Cell.Type.PASSAGE) {
                    List<Cell> neighbors = getPassageNeighbors(currentCell);
                    graph.put(currentCell, neighbors);
                }
            }
        }

        return graph;
    }

    private static final int STEP_SIZE = 1;

    private List<Cell> getPassageNeighbors(Cell cell) {
        List<Cell> neighbors = new ArrayList<>();
        List<Coordinate> directions = Arrays.asList(
            new Coordinate(0, STEP_SIZE),
            new Coordinate(STEP_SIZE, 0),
            new Coordinate(0, -STEP_SIZE),
            new Coordinate(-STEP_SIZE, 0)
        );

        for (Coordinate dir : directions) {
            int newRow = cell.row() + dir.row();
            int newCol = cell.col() + dir.col();

            if (newRow >= 0 && newRow < height && newCol >= 0 && newCol < width
                && grid[newRow][newCol].type() != Cell.Type.WALL) {
                neighbors.add(grid[newRow][newCol]);
            }
        }

        return neighbors;
    }
}
