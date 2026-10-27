package com.insertokname.dum.game_objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.insertokname.dum.GameConfig;
import com.insertokname.dum.game_objects.raycasting.Ray;
import com.insertokname.dum.game_objects.raycasting.RayCastResult;

import java.util.ArrayList;
import java.util.List;

public class Playfield {
    private Player player;
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

    public Tile getTile(int x, int y) {
        return tiles[y][x];
    }

    public void setTile(int x, int y, Tile tile) {
        tiles[y][x] = tile;
    }

    public void update(float delta) {
        player.displaceRotation(delta * player.getDirectionDisplacement());
        Vector2 position = player.getPosition();
        Vector2 displacedPosition = player.getPositionDisplacement().scl(delta).add(position);
        int centerTileX = (int) displacedPosition.x;
        int centerTileY = (int) displacedPosition.y;

        for (int y = centerTileY - 1; y <= centerTileY + 1; y++) {
            for (int x = centerTileX - 1; x <= centerTileX + 1; x++) {
                if (0 <= y && y < height && 0 <= x && x < width && getTile(x, y) == Tile.WALL) {

                    displacedPosition.x += getCollisionRepulseForce(x, y, displacedPosition.x, position.y, true);
                    displacedPosition.y += getCollisionRepulseForce(x, y, position.x, displacedPosition.y, false);
                }
            }
        }

        player.displacePosition(displacedPosition.sub(position));
    }

    private static float getCollisionRepulseForce(int squareX, int squareY, float playerX, float playerY, boolean onXAxis) {
        float reach = 0.5f + GameConfig.Player.HITBOX_RADIUS;
        float distX = playerX - (squareX + 0.5f);
        float distY = playerY - (squareY + 0.5f);

        if (Math.abs(distX) >= reach || Math.abs(distY) >= reach) return 0;

        boolean isXBigger = Math.abs(distX) >= Math.abs(distY);
        if (onXAxis != isXBigger) return 0;

        float dist = onXAxis ? distX : distY;
        return Math.signum(dist) * reach - dist;
    }

    public void drawTopDown(ShapeRenderer shapeRenderer, RayCastResult rayCastResult) {
        drawTopDown(shapeRenderer);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(GameConfig.Renderer.RAY_COLOR);
        for (int i = 0; i < rayCastResult.rays.size(); i++) {
            shapeRenderer.rectLine(rayCastResult.castStartPos, new Vector2(rayCastResult.rays.get(i).ray).add(rayCastResult.castStartPos), GameConfig.Renderer.RAY_WIDTH);
        }
        shapeRenderer.end();
    }

    public void drawTopDown(ShapeRenderer shapeRenderer) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color;
                switch (getTile(j, i)) {
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

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.WHITE);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                shapeRenderer.rect(j, i, 1, 1);
            }
        }
        shapeRenderer.end();

        player.drawTopDown(shapeRenderer);
    }

    public RayCastResult computeRayCast() {
        float eyeToScreenDist = 0.5f / (float) Math.tan(Math.toRadians(GameConfig.Renderer.FOV / 2));
        float screenPos = 0.5f;
        float screenPosStep = 1.0f / GameConfig.Renderer.RAY_COUNT;

        Vector2 playerPos = player.getPosition();

        List<Ray> rays = new ArrayList<>(GameConfig.Renderer.RAY_COUNT);

        for (int i = 0; i < GameConfig.Renderer.RAY_COUNT; i++) {
            double rayAngle = Math.atan(screenPos / eyeToScreenDist);
            float rayAngleCos = (float) Math.cos(rayAngle);


            Vector2 ray = new Vector2();
            Direction collisionDirection = null;
            Tile collidedTile = Tile.AIR;
            float textureSamplePos = 0;

            Vector2 rayDirection = player
                .getDirection()
                .rotateRad((float) rayAngle);

            int xDirection = rayDirection.x < 0 ? -1 : 1;
            int yDirection = rayDirection.y < 0 ? -1 : 1;

            Vector2 xStepVec = null;
            Vector2 yStepVec = null;

            while (ray.len() * rayAngleCos < GameConfig.Renderer.RENDER_DISTANCE) {
                float mapPosX = playerPos.x + ray.x;
                float mapPosY = playerPos.y + ray.y;

                int tilePosX = 0;
                int tilePosY = 0;

                if (rayDirection.x < 0) {
                    mapPosX = (float) Math.ceil(mapPosX);
                    tilePosX -= 1;
                } else {
                    mapPosX = (float) Math.floor(mapPosX);
                }

                if (rayDirection.y < 0) {
                    mapPosY = (float) Math.ceil(mapPosY);
                    tilePosY -= 1;
                } else {
                    mapPosY = (float) Math.floor(mapPosY);
                }

                tilePosX += (int) mapPosX;
                tilePosY += (int) mapPosY;

                if (0 <= tilePosX && tilePosX < width && 0 <= tilePosY && tilePosY < height && xStepVec != null && yStepVec != null) {
                    if (getTile(tilePosX, tilePosY) != Tile.AIR) {
                        collidedTile = getTile(tilePosX, tilePosY);
                        if (xStepVec.len() < yStepVec.len()) {
                            if (rayDirection.x < 0) {
                                collisionDirection = Direction.EAST;
                            } else {
                                collisionDirection = Direction.WEST;
                            }
                            textureSamplePos = (ray.y + playerPos.y - tilePosY);
                        } else {
                            if (rayDirection.y < 0) {
                                collisionDirection = Direction.NORTH;
                            } else {
                                collisionDirection = Direction.SOUTH;
                            }
                            textureSamplePos = (ray.x + playerPos.x - tilePosX);
                        }
                        break;
                    }
                }

                float xStep = (mapPosX + xDirection) - (playerPos.x + ray.x);
                float yStep = (mapPosY + yDirection) - (playerPos.y + ray.y);

                xStepVec = new Vector2(rayDirection).scl(xStep / rayDirection.x);
                yStepVec = new Vector2(rayDirection).scl(yStep / rayDirection.y);

                if (xStepVec.len() < yStepVec.len()) {
                    ray.add(xStepVec);
                } else {
                    ray.add(yStepVec);
                }
            }

            rays.add(new Ray(
                ray,
                collidedTile,
                collisionDirection,
                ray.len() * rayAngleCos,
                textureSamplePos
            ));
            screenPos -= screenPosStep;
        }
        return new RayCastResult(
            rays,
            playerPos,
            player.getDirection()
        );
    }

    public void drawPov(ShapeRenderer shapeRenderer, SpriteBatch spriteBatch, RayCastResult rayCastResult) {
        float step = 1.0f / rayCastResult.rays.size();
        float eyeToScreenDist = 0.5f / (float) Math.tan(Math.toRadians(GameConfig.Renderer.FOV / 2));
        float wallHeight = 1.0f;
        float minWallHeight = wallHeight * eyeToScreenDist / GameConfig.Renderer.RENDER_DISTANCE;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.49f, 0.49f, 0.49f, 1);
        shapeRenderer.rect(0, 0, 1, 0.5f - (minWallHeight) / 2);
        shapeRenderer.rect(0, 0.5f + (minWallHeight) / 2, 1, 0.5f - (minWallHeight) / 2);
        shapeRenderer.end();

        spriteBatch.begin();

        for (int i = 0; i < rayCastResult.rays.size(); i++) {
            float wallDist = rayCastResult.rays.get(i).distToPlayerViewPlane;
            float sliverHeight = wallHeight * eyeToScreenDist / wallDist;

            if (rayCastResult.rays.get(i).collidedTile != Tile.AIR) {
                spriteBatch.draw(
                    GameConfig.Wall,
                    i * step,
                    (1 - sliverHeight) / 2,
                    step,
                    sliverHeight,
                    rayCastResult.rays.get(i).textureSamplePos,
                    1,
                    rayCastResult.rays.get(i).textureSamplePos + step,
                    0);

            }
        }

        spriteBatch.end();
    }
}
