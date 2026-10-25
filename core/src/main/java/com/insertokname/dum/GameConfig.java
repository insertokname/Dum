package com.insertokname.dum;

import com.badlogic.gdx.graphics.Color;

public final class GameConfig {
    public static final class Playfield {
        public static final String[] LAYOUT = {
            "########################",
            "#......#.......#.......#",
            "#.####.#.#####.#.#####.#",
            "#.#....#.#.....#.#.....#",
            "#.#.####.#.#####.#.###.#",
            "#.#......#.............#",
            "#.######.#..........##.#",
            "#........#...#..#......#",
            "####.###.....#..#..#####",
            "#....#.#...........#...#",
            "#.##.#.#..........##.#.#",
            "#.#..#.###.####...#..#.#",
            "#.#.##.....#......#.##.#",
            "#.#....###.#.######....#",
            "#...##.....#...........#",
            "########################",
        };
        public static final int WIDTH = LAYOUT[0].length();
        public static final int HEIGHT = LAYOUT.length;
    }

    public static final class Player {
        public static final float MOVEMENT_SPEED = 2.4f;
        public static final float ROTATION_SPEED = 180f;
        public static final float HITBOX_RADIUS = 0.2f;
        public static final Color COLOR = Color.YELLOW;
        public static final float HEAD_SIZE = 0.125f;
        public static final Color HEAD_COLOR = Color.BLUE;
    }

    public static final class Renderer {
        public static final float FOV = 70;
        public static final int RAY_COUNT = 1000;
        public static final float RAY_STEP = 0.002f;
        public static final float RENDER_DISTANCE = 6.0f;
        public static final float FOG_START = RENDER_DISTANCE - 1.5f;

        public static final Color RAY_COLOR = Color.GREEN;
        public static final float RAY_WIDTH = 0.02f;
    }

    public static final float MAP_SCREEN_PERCENT = 0.4f;
    public static final int MAP_PADDING_X = 20;
    public static final int MAP_PADDING_Y = 20;

    public static final Color WALL_COLOR = Color.RED;
    public static final Color AIR_COLOR = Color.CLEAR;
}
