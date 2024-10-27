package backend.academy.maze;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.solvers.BfsSolver;
import backend.academy.solvers.DijkstraSolver;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SolverTest {
    private BfsSolver bfsSolver;
    private DijkstraSolver dijkstraSolver;

    @BeforeEach
    void setUp() {
        bfsSolver = new BfsSolver();
        dijkstraSolver = new DijkstraSolver();
    }

    @Test
    void testBfsSolverFindsPath() {
        Maze maze = createMazeWithPath();
        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(1, 3);

        List<Coordinate> path = bfsSolver.solve(maze, start, end);

        assertFalse(path.isEmpty());
        assertEquals(end, path.getLast());
    }

    @Test
    void testDijkstraSolverFindsPath() {
        Maze maze = createMazeWithPath();
        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(1, 3);

        List<Coordinate> path = dijkstraSolver.solve(maze, start, end);

        assertFalse(path.isEmpty());
        assertEquals(end, path.getLast());
    }

    @Test
    void testBfsSolverNoPath() {
        Maze maze = createMazeWithoutPath();
        Coordinate start = new Coordinate(0, 1);
        Coordinate end = new Coordinate(1, 1);

        List<Coordinate> path = bfsSolver.solve(maze, start, end);

        assertTrue(path.isEmpty());
    }

    @Test
    void testDijkstraSolverNoPath() {
        Maze maze = createMazeWithoutPath();
        Coordinate start = new Coordinate(0, 1);
        Coordinate end = new Coordinate(1, 1);

        List<Coordinate> path = dijkstraSolver.solve(maze, start, end);

        assertTrue(path.isEmpty());
    }

    private Maze createMazeWithPath() {
        int height = 3;
        int width = 5;
        Cell[][] grid = new Cell[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL);
            }
        }

        grid[1][1] = new Cell(1, 1, Cell.Type.PASSAGE);
        grid[1][2] = new Cell(1, 2, Cell.Type.PASSAGE);
        grid[1][3] = new Cell(1, 3, Cell.Type.PASSAGE);

        return new Maze(height, width, grid);
    }

    private Maze createMazeWithoutPath() {
        int height = 3;
        int width = 5;
        Cell[][] grid = new Cell[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL);
            }
        }

        grid[1][1] = new Cell(1, 1, Cell.Type.PASSAGE);
        grid[1][3] = new Cell(1, 3, Cell.Type.PASSAGE);

        return new Maze(height, width, grid);
    }
}
