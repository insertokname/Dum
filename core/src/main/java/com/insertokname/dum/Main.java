package com.insertokname.dum;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.insertokname.dum.game_objects.Playfield;
import com.insertokname.dum.game_objects.raycasting.RayCastResult;
import com.insertokname.dum.game_objects.Tile;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private ShapeRenderer shapeRenderer;

    private Viewport viewport;
    private Playfield playfield;
    private Viewport povViewport;

    private Viewport hudViewport;
    private SpriteBatch batch;
    private BitmapFont font;

    private static boolean showMap = true;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();

        viewport = new FitViewport(GameConfig.Playfield.WIDTH, GameConfig.Playfield.HEIGHT);
        playfield = new Playfield(GameConfig.Playfield.WIDTH, GameConfig.Playfield.HEIGHT);
        povViewport = new FitViewport(1, 1);

        hudViewport = new ScreenViewport();
        batch = new SpriteBatch();
        font = new BitmapFont();

        for (int row = 0; row < GameConfig.Playfield.HEIGHT; row++) {
            String line = GameConfig.Playfield.LAYOUT[row];
            for (int x = 0; x < GameConfig.Playfield.WIDTH; x++) {
                if (line.charAt(x) == '#') {
                    playfield.setTile(x, GameConfig.Playfield.HEIGHT - 1 - row, Tile.WALL);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
        povViewport.update(width, height, true);
        hudViewport.update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Math.min(Gdx.graphics.getDeltaTime(), 1 / 30.0f);
        update(delta);
        draw();
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        font.dispose();
    }

    private void update(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.M)) {
            showMap = !showMap;
        }
        playfield.update(delta);
    }

    private void draw() {
        RayCastResult rayCastResult = playfield.computeRayCast();

        ScreenUtils.clear(Color.BLACK);

        povViewport.apply();
        shapeRenderer.setProjectionMatrix(povViewport.getCamera().combined);
        playfield.drawPov(shapeRenderer, rayCastResult);

        float size =
            GameConfig.MAP_SCREEN_PERCENT * Math.min(
                (float) Gdx.graphics.getWidth() / GameConfig.Playfield.WIDTH,
                (float) Gdx.graphics.getHeight() / GameConfig.Playfield.HEIGHT);
        int mapHeight = (int) (GameConfig.Playfield.HEIGHT * size);
        int mapWidth = (int) (GameConfig.Playfield.WIDTH * size);
        viewport.setScreenBounds(
            Gdx.graphics.getWidth() - mapWidth - GameConfig.MAP_PADDING_X,
            GameConfig.MAP_PADDING_Y,
            mapWidth,
            mapHeight);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        if (showMap) {
            playfield.drawTopDown(shapeRenderer, rayCastResult);
        }

        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, hudViewport.getWorldHeight() - 5);
        batch.end();
    }
}
