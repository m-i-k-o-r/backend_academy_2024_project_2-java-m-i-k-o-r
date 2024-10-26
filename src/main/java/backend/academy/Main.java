package backend.academy;

import backend.academy.generators.BinaryTreeMazeGenerator;
import backend.academy.generators.Generator;
import backend.academy.generators.RecursiveBacktrackerMazeGenerator;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.List;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Main {
    public static void main(String[] args) {
        Console console = new Console();

        int height = console.getValidInput(
            "Введите высоту лабиринта > ",
            Integer.MAX_VALUE
        );
        int width = console.getValidInput(
            "Введите ширину лабиринта > ",
            Integer.MAX_VALUE
        );

        List<Generator> generators = List.of(
            new BinaryTreeMazeGenerator(),
            new RecursiveBacktrackerMazeGenerator()
        );

        console.println("Выберите генератор лабиринта:");
        for (int i = 0; i < generators.size(); i++) {
            console.println(i + ": " + generators.get(i).name());
        }
        Generator selectedGenerator = generators.get(
            console.getValidInput(
                "Введите номер генератора из списка > ",
                generators.size()
            )
        );

        Maze maze = selectedGenerator.generate(height, width);
        console.render(maze);

        Coordinate start = console.setStartPoint(maze);
        Coordinate end = console.setEndPoint(maze, start);

        console.render(maze, start, end);
    }
}
