package com.insertokname.dum.playfield;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.insertokname.dum.GameConfig;

public class Playfield {
    public Player player;
    private final int width, height;
    private final Tile[][] tiles;

    public Playfield(int width, int height) {
        player = new Player(new Vector2(width, height).scl(0.5f), new Vector2(0, 1));
        this.width = width;
        this.height = height;
        tiles = new Tile[height][width];
        for (int i = 0; i < this.height; i++) {
            for (int j = 0; j < this.width; j++) {
                tiles[i][j] = Tile.AIR;
            }
        }
    }

    public void setTile(int x, int y, Tile tile) {
        this.tiles[y][x] = tile;
    }

    public void update(float delta) {
        player.displaceRotation(delta * player.getDirectionDisplacement());
        Vector2 position = player.getPosition();
        Vector2 displacedPosition = player.getPositionDisplacement().scl(delta).add(position);
        int centerTileX = (int) displacedPosition.x;
        int centerTileY = (int) displacedPosition.y;

        for (int y = centerTileY - 1; y <= centerTileY + 1; y++) {
            for (int x = centerTileX - 1; x <= centerTileX + 1; x++) {
                if (0 <= y && y < height &&
                    0 <= x && x < width &&
                    tiles[y][x] == Tile.WALL) {

                    displacedPosition.x += getCollisionRepulseForce(x, y, displacedPosition.x, position.y, true);
                    displacedPosition.y += getCollisionRepulseForce(x, y, position.x, displacedPosition.y, false);
                }
            }
        }

        player.displacePosition(displacedPosition.sub(position));
    }

    private static float getCollisionRepulseForce(int squareX, int squareY, float playerX, float playerY, boolean onXAxis) {
        float reach = 0.5f + GameConfig.PLAYER_HITBOX_RADIUS;
        float distX = playerX - (squareX + 0.5f);
        float distY = playerY - (squareY + 0.5f);

        if (Math.abs(distX) >= reach || Math.abs(distY) >= reach) return 0;

        boolean isXBigger = Math.abs(distX) >= Math.abs(distY);
        if (onXAxis != isXBigger) return 0;

        float dist = onXAxis ? distX : distY;
        return Math.signum(dist) * reach - dist;
    }

    public void drawTopDown(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color;
                switch (tiles[i][j]) {
                    case AIR:
                        color = GameConfig.AIR_COLOR;
                        break;
                    case WALL:
                        color = GameConfig.WALL_COLOR;
                        break;
                    default:
                        color = Color.CLEAR;
                        break;
                }

                shapeRenderer.setColor(color);
                shapeRenderer.rect(j, i, 1, 1);
            }
        }
        shapeRenderer.end();

        player.drawTopDown(shapeRenderer);
    }
}
