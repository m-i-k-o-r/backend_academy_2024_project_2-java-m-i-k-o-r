package backend.academy;

import backend.academy.generators.BinaryTreeMazeGenerator;
import backend.academy.generators.Generator;
import backend.academy.generators.RecursiveBacktrackerMazeGenerator;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import backend.academy.solvers.BfsSolver;
import backend.academy.solvers.DijkstraSolver;
import backend.academy.solvers.Solver;
import java.util.List;

public class MazeApp {
    private final Console console;
    private final List<Generator> generators;
    private final List<Solver> solvers;

    public MazeApp() {
        this(
            new Console(),
            List.of(
                new BinaryTreeMazeGenerator(),
                new RecursiveBacktrackerMazeGenerator()
            ),
            List.of(
                new BfsSolver(),
                new DijkstraSolver()
            )
        );
    }

    public MazeApp(Console console, List<Generator> generators, List<Solver> solvers) {
        this.console = console;
        this.generators = generators;
        this.solvers = solvers;
    }

    private static final int MIN_SIZE_MAZE = 5;

    public void run() {
        int height = console.getValidInput("Введите высоту лабиринта (5 - ...) > ", MIN_SIZE_MAZE, Integer.MAX_VALUE);
        int width = console.getValidInput("Введите ширину лабиринта (5 - ...) > ", MIN_SIZE_MAZE, Integer.MAX_VALUE);

        Generator selectedGenerator = selectGenerator();
        Maze maze = selectedGenerator.generate(height, width);
        console.render(maze);

        Coordinate start = console.setStartPoint(maze);
        Coordinate end = console.setEndPoint(maze, start);
        console.render(maze, start, end);

        Solver selectedSolver = selectSolver();
        List<Coordinate> path = selectedSolver.solve(maze, start, end);
        if (path.isEmpty()) {
            console.println(Console.ERROR_NO_WAY);
        } else {
            console.render(maze, start, end, path);
        }

        console.close();
    }

    private Generator selectGenerator() {
        console.println("Выберите генератор лабиринта:");
        for (int i = 0; i < generators.size(); i++) {
            console.println(i + ": " + generators.get(i).name());
        }

        return generators.get(
            console.getValidInput(
                "Введите номер генератора из списка > ",
                generators.size()
            ));
    }

    private Solver selectSolver() {
        console.println("Выберите алгоритм поиска пути:");
        for (int i = 0; i < solvers.size(); i++) {
            console.println(i + ": " + solvers.get(i).name());
        }

        return solvers.get(
            console.getValidInput(
                "Введите номер алгоритма поиска пути из списка > ",
                solvers.size()
            ));
    }
}
