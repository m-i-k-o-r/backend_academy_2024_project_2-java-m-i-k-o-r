package backend.academy.solvers;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

public class BfsSolver implements Solver {

    @Override public List<Coordinate> solve(Maze maze, Coordinate startCoordinate, Coordinate endCoordinate) {

        Map<Cell, List<Cell>> graph = maze.toGraph();
        Queue<Cell> queue = new LinkedList<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();

        Cell start = maze.grid()[startCoordinate.row()][startCoordinate.col()];
        Cell end = maze.grid()[endCoordinate.row()][endCoordinate.col()];

        queue.add(start);
        cameFrom.put(start, null);

        while (!queue.isEmpty()) {
            Cell current = queue.poll();

            if (current.equals(end)) {
                return reconstructPath(cameFrom, end);
            }

            for (Cell neighbor : graph.getOrDefault(current, new ArrayList<>())) {
                if (!cameFrom.containsKey(neighbor)) {
                    queue.add(neighbor);
                    cameFrom.put(neighbor, current);
                }
            }
        }

        return Collections.emptyList();
    }

    @Override public String name() {
        return "Breadth-First Search (BFS)";
    }
}
