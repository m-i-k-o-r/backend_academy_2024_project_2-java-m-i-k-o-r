package backend.academy.generators;

import backend.academy.models.Maze;

public interface Generator {
    Maze generate(int height, int width);

    String name();
}
