package backend.academy.maze;

import backend.academy.Console;
import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConsoleTest {
    private Console console;
    private Scanner scanner;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() {
        scanner = mock(Scanner.class);
        outputStream = new ByteArrayOutputStream();
        console = new Console(scanner, new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
    }

    @Test
    void testGetValidInput() {
        when(scanner.hasNextInt()).thenReturn(true);
        when(scanner.nextInt()).thenReturn(5);

        int result = console.getValidInput("Введите число > ", 10);

        assertEquals(5, result);
    }

    @Test
    void testGetValidInputNotInteger() {
        when(scanner.hasNextInt())
            .thenReturn(false)
            .thenReturn(true);

        when(scanner.nextInt()).thenReturn(5);

        int result = console.getValidInput("Введите число > ", 10);

        String output = outputStream.toString();
        assertTrue(output.contains(Console.ERROR_NOT_A_NUMBER));

        assertEquals(5, result);
    }

    @Test
    void testGetValidInputOutOfRangeMore() {
        when(scanner.hasNextInt()).thenReturn(true);
        when(scanner.nextInt())
            .thenReturn(100)
            .thenReturn(5);

        int result = console.getValidInput("Введите число > ", 10);

        String output = outputStream.toString();
        assertTrue(output.contains(Console.ERROR_NUMBER_OUT_OF_RANGE));

        assertEquals(5, result);
    }

    @Test
    void testGetValidInputOutOfRangeLess() {
        when(scanner.hasNextInt()).thenReturn(true);
        when(scanner.nextInt())
            .thenReturn(0)
            .thenReturn(5);

        int result = console.getValidInput("Введите число > ", 3, 10);

        String output = outputStream.toString();
        assertTrue(output.contains(Console.ERROR_NUMBER_OUT_OF_RANGE));

        assertEquals(5, result);
    }

    private Maze createTestMaze() {
        int height = 3;
        int width = 5;
        Cell[][] grid = new Cell[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL);
            }
        }

        grid[1][1] = new Cell(1, 1, Cell.Type.PASSAGE);
        grid[1][3] = new Cell(1, 1, Cell.Type.PASSAGE);

        return new Maze(height, width, grid);
    }

    @Test
    void testSetStartPointValidInput() {
        Maze maze = createTestMaze();
        when(scanner.hasNextInt()).thenReturn(true);
        when(scanner.nextInt()).thenReturn(1).thenReturn(1);

        Coordinate start = console.setStartPoint(maze);

        assertEquals(new Coordinate(1, 1), start);
    }

    @Test
    void testSetStartPointInvalidInputWall() {
        Maze maze = createTestMaze();
        when(scanner.hasNextInt()).thenReturn(true);
        when(scanner.nextInt())
            .thenReturn(2).thenReturn(2)
            .thenReturn(1).thenReturn(1);

        Coordinate start = console.setStartPoint(maze);

        String output = outputStream.toString();
        assertTrue(output.contains(Console.ERROR_CELL_IS_WALL));

        assertEquals(new Coordinate(1, 1), start);
    }

    @Test
    void testSetEndPointValidInput() {
        Maze maze = createTestMaze();
        Coordinate start = new Coordinate(0, 0);
        when(scanner.hasNextInt()).thenReturn(true);
        when(scanner.nextInt()).thenReturn(1).thenReturn(3);

        Coordinate end = console.setEndPoint(maze, start);

        assertEquals(new Coordinate(1, 3), end);
    }

    @Test
    void testSetEndPointInvalidInputWall() {
        Maze maze = createTestMaze();
        Coordinate start = new Coordinate(0, 0);
        when(scanner.hasNextInt()).thenReturn(true);
        when(scanner.nextInt())
            .thenReturn(2).thenReturn(2)
            .thenReturn(1).thenReturn(3);

        Coordinate end = console.setEndPoint(maze, start);

        String output = outputStream.toString();
        assertTrue(output.contains(Console.ERROR_CELL_IS_WALL));

        assertEquals(new Coordinate(1, 3), end);
    }

    @Test
    void testSetEndPointInvalidInputSameAsStart() {
        Maze maze = createTestMaze();
        Coordinate start = new Coordinate(1, 1);
        when(scanner.hasNextInt()).thenReturn(true);
        when(scanner.nextInt())
            .thenReturn(1).thenReturn(1)
            .thenReturn(1).thenReturn(3);

        Coordinate end = console.setEndPoint(maze, start);

        String output = outputStream.toString();
        assertTrue(output.contains(Console.ERROR_START_EQUALS_END));

        assertEquals(new Coordinate(1, 3), end);
    }

    @Test
    void testRender() {
        Maze maze = createTestMaze();

        console.render(maze);

        String expectedOutput = """
            ⬛⬛⬛⬛⬛
            ⬛⬜⬛⬜⬛
            ⬛⬛⬛⬛⬛
            """;
        assertEquals(expectedOutput.trim(), outputStream.toString().trim());
    }

    @Test
    void testRenderWithPoint() {
        Maze maze = createTestMaze();
        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(1, 3);

        console.render(maze, start, end);

        String expectedOutput = """
            ⬛⬛⬛⬛⬛
            ⬛🅰️⬛🅱️⬛
            ⬛⬛⬛⬛⬛
            """;
        assertEquals(expectedOutput.trim(), outputStream.toString().trim());
    }

    @Test
    void testRenderWithPointAndPath() {
        Maze maze = createTestMaze();
        Coordinate start = new Coordinate(1, 1);
        Coordinate end = new Coordinate(1, 3);

        List<Coordinate> path = List.of(
            new Coordinate(1, 2)
        );

        console.render(maze, start, end, path);

        String expectedOutput = """
            ⬛⬛⬛⬛⬛
            ⬛🅰️🟨🅱️⬛
            ⬛⬛⬛⬛⬛
            """;
        assertEquals(expectedOutput.trim(), outputStream.toString().trim());
    }
}
