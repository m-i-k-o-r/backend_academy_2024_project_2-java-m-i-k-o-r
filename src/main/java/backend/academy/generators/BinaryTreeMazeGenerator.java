package backend.academy.generators;

import backend.academy.models.Cell;
import backend.academy.models.Maze;
import java.security.SecureRandom;
import java.util.Random;

public class BinaryTreeMazeGenerator implements Generator {
    private static final Random RANDOM = new SecureRandom();

    @Override
    public Maze generate(int height, int width) {
        int correctedHeight = (height % 2 == 0) ? height + 1 : height;
        int correctedWidth = (width % 2 == 0) ? width + 1 : width;

        Cell[][] grid = new Cell[correctedHeight][correctedWidth];

        for (int row = 0; row < correctedHeight; row++) {
            for (int col = 0; col < correctedWidth; col++) {
                grid[row][col] = new Cell(row, col, Cell.Type.WALL);
            }
        }

        for (int row = 1; row < correctedHeight; row += 2) {
            for (int col = 1; col < correctedWidth; col += 2) {
                grid[row][col] = new Cell(row, col, Cell.Type.PASSAGE);

                if (row == 1 && col == 1) {
                    continue;
                }

                if (RANDOM.nextBoolean()) {
                    if (row > 1) {
                        grid[row - 1][col] = new Cell(row - 1, col, Cell.Type.PASSAGE);
                    } else if (col > 1) {
                        grid[row][col - 1] = new Cell(row, col - 1, Cell.Type.PASSAGE);
                    }
                } else {
                    if (col > 1) {
                        grid[row][col - 1] = new Cell(row, col - 1, Cell.Type.PASSAGE);
                    } else if (row > 1) {
                        grid[row - 1][col] = new Cell(row - 1, col, Cell.Type.PASSAGE);
                    }
                }
            }
        }

        return new Maze(correctedHeight, correctedWidth, grid);
    }

    @Override
    public String name() {
        return "Binary Tree";
    }
}
