package backend.academy.solvers;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public interface Solver {
    List<Coordinate> solve(Maze maze, Coordinate start, Coordinate end);

    String name();

    default List<Coordinate> reconstructPath(Map<Cell, Cell> cameFrom, Cell end) {
        List<Coordinate> path = new LinkedList<>();
        Cell current = end;

        while (current != null) {
            path.addFirst(new Coordinate(current.row(), current.col()));
            current = cameFrom.get(current);
        }

        return path;
    }
}
