package backend.academy.solvers;

import backend.academy.models.Cell;
import backend.academy.models.Coordinate;
import backend.academy.models.Maze;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraSolver implements Solver {

    @Override
    public List<Coordinate> solve(Maze maze, Coordinate startCoordinate, Coordinate endCoordinate) {
        Map<Cell, List<Cell>> graph = maze.toGraph();

        Map<Cell, Integer> distances = new HashMap<>();
        Map<Cell, Cell> cameFrom = new HashMap<>();
        PriorityQueue<Cell> priorityQueue = new PriorityQueue<>(Comparator.comparingInt(distances::get));

        graph.keySet().forEach(node -> {
            distances.put(node, Integer.MAX_VALUE);
            cameFrom.put(node, null);
        });

        Cell start = maze.grid()[startCoordinate.row()][startCoordinate.col()];
        Cell end = maze.grid()[endCoordinate.row()][endCoordinate.col()];

        distances.put(start, 0);
        priorityQueue.add(start);

        while (!priorityQueue.isEmpty()) {
            Cell current = priorityQueue.poll();

            if (current.equals(end)) {
                return reconstructPath(cameFrom, end);
            }

            for (Cell neighbor : graph.getOrDefault(current, new ArrayList<>())) {
                int newDist = distances.get(current) + neighbor.type().ordinal();

                if (newDist < distances.get(neighbor)) {
                    distances.put(neighbor, newDist);
                    cameFrom.put(neighbor, current);
                    priorityQueue.add(neighbor);
                }
            }
        }

        return Collections.emptyList();
    }

    /*
    private int getWeight(Cell cell) {
        return switch (cell.type()) {
            case WALL -> Integer.MAX_VALUE;
            case PASSAGE -> 1;
            default -> 1;
        };
    }
    */

    @Override
    public String name() {
        return "Dijkstra's Algorithm";
    }
}
