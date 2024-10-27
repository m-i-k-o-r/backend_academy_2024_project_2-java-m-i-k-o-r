package backend.academy;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

public class Console {
    public static final String ERROR_NUMBER_OUT_OF_RANGE = " ! Некорректный ввод: число не в диапазоне";
    public static final String ERROR_NOT_A_NUMBER = " ! Некорректный ввод: ожидается число";
    public static final String ERROR_CELL_IS_WALL = " ! Некорректный ввод: выбранная ячейка является стеной";
    public static final String ERROR_START_EQUALS_END = " ! Некорректный ввод: начальная и конечная точки совпадают";
    public static final String ERROR_NO_WAY = " ! Путь не найден";

    public static final char CELL_WALL = '\u2B1B';
    public static final char CELL_PASSAGE = '\u2B1C';
    public static final String CELL_START = "\uD83C\uDD70\uFE0F";
    public static final String CELL_END = "\uD83C\uDD71\uFE0F";
    public static final String CELL_PATH = "\uD83D\uDFE8";

    private final Scanner scanner;
    private final PrintWriter writer;

    public Console() {
        this(
            new Scanner(System.in, StandardCharsets.UTF_8),
            new OutputStreamWriter(System.out, StandardCharsets.UTF_8)
        );
    }

    public Console(Scanner scanner, OutputStreamWriter writer) {
        this.scanner = scanner;
        this.writer = new PrintWriter(writer, true);
    }

    public int getValidInput(String prompt, int minValue, int maxValue) {
        while (true) {
            print(prompt);
            if (scanner.hasNextInt()) {
                int input = scanner.nextInt();

                if (input >= minValue && input <= maxValue) {
                    return input;
                } else {
                    println(ERROR_NUMBER_OUT_OF_RANGE);
                }
            } else {
                println(ERROR_NOT_A_NUMBER);
            }
            scanner.nextLine();
        }
    }

    public int getValidInput(String prompt, int maxValue) {
        return getValidInput(prompt, 0, maxValue);
    }

    private String formatPrompt(String type, int maxValue) {
        return String.format("Введите %s (0 - %d) > ", type, maxValue);
    }

    public Coordinate setStartPoint(Maze maze) {
        while (true) {
            int row = getValidInput(
                formatPrompt("начальную строку", maze.height() - 1),
                maze.height() - 1
            );

            int col = getValidInput(
                formatPrompt("начальный столбец", maze.width() - 1),
                maze.width() - 1
            );

            if (maze.grid()[row][col].type() == Cell.Type.PASSAGE) {
                return new Coordinate(row, col);
            } else {
                println(ERROR_CELL_IS_WALL);
            }
        }
    }

    public Coordinate setEndPoint(Maze maze, Coordinate start) {
        while (true) {
            int row = getValidInput(
                formatPrompt("конечную строку", maze.height() - 1),
                maze.height() - 1
            );

            int col = getValidInput(
                formatPrompt("конечный столбец", maze.width() - 1),
                maze.width() - 1
            );

            if (maze.grid()[row][col].type() == Cell.Type.PASSAGE) {
                if (row != start.row() || col != start.col()) {
                    return new Coordinate(row, col);
                } else {
                    println(ERROR_START_EQUALS_END);
                }
            } else {
                println(ERROR_CELL_IS_WALL);
            }
        }
    }

    public void println() {
        writer.println();
    }

    public void println(String message) {
        writer.println(message);
    }

    public void print(String message) {
        writer.print(message);
        writer.flush();
    }

    public void render(Maze maze) {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < maze.height(); row++) {
            for (int col = 0; col < maze.width(); col++) {
                Cell cell = maze.grid()[row][col];
                if (cell.type() == Cell.Type.WALL) {
                    sb.append(CELL_WALL);
                } else {
                    sb.append(CELL_PASSAGE);
                }
            }
            sb.append('\n');
        }

        println(sb.toString());
    }

    public void render(Maze maze, Coordinate start, Coordinate end) {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < maze.height(); row++) {
            for (int col = 0; col < maze.width(); col++) {
                if (row == start.row() && col == start.col()) {
                    sb.append(CELL_START);
                } else if (row == end.row() && col == end.col()) {
                    sb.append(CELL_END);
                } else {
                    Cell cell = maze.grid()[row][col];
                    if (cell.type() == Cell.Type.WALL) {
                        sb.append(CELL_WALL);
                    } else {
                        sb.append(CELL_PASSAGE);
                    }
                }
            }
            sb.append('\n');
        }

        println(sb.toString());
    }

    public void render(Maze maze, Coordinate start, Coordinate end, List<Coordinate> path) {
        StringBuilder sb = new StringBuilder();

        for (int row = 0; row < maze.height(); row++) {
            for (int col = 0; col < maze.width(); col++) {
                if (row == start.row() && col == start.col()) {
                    sb.append(CELL_START);
                } else if (row == end.row() && col == end.col()) {
                    sb.append(CELL_END);
                } else if (path.contains(new Coordinate(row, col))) {
                    sb.append(CELL_PATH);
                } else {
                    Cell cell = maze.grid()[row][col];
                    if (cell.type() == Cell.Type.WALL) {
                        sb.append(CELL_WALL);
                    } else {
                        sb.append(CELL_PASSAGE);
                    }
                }
            }
            sb.append('\n');
        }

        println(sb.toString());
    }

    public void close() {
        scanner.close();
        writer.close();
    }
}
