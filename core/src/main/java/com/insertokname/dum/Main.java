package com.insertokname.dum;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.insertokname.dum.playfield.Playfield;
import com.insertokname.dum.playfield.Tile;

/**
 * {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms.
 */
public class Main extends ApplicationAdapter {
    private Camera camera;
    private Viewport viewport;
    private ShapeRenderer shapeRenderer;

    private Viewport hudViewport;
    private SpriteBatch batch;
    private BitmapFont font;

    Playfield playfield;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        viewport = new FitViewport(GameConfig.PLAYFIELD_WIDTH, GameConfig.PLAYFIELD_HEIGHT, camera);
        shapeRenderer = new ShapeRenderer();
        hudViewport = new ScreenViewport();
        batch = new SpriteBatch();
        font = new BitmapFont();
        playfield = new Playfield(GameConfig.PLAYFIELD_WIDTH, GameConfig.PLAYFIELD_HEIGHT);

        playfield.setTile(3, 4, Tile.WALL);
        for (int i = 0; i < GameConfig.PLAYFIELD_WIDTH; i++) {
            playfield.setTile(i, 0, Tile.WALL);
            playfield.setTile(i, GameConfig.PLAYFIELD_HEIGHT - 1, Tile.WALL);
        }
        for (int i = 0; i < GameConfig.PLAYFIELD_HEIGHT; i++) {
            playfield.setTile(0, i, Tile.WALL);
            playfield.setTile(GameConfig.PLAYFIELD_WIDTH - 1, i, Tile.WALL);
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        viewport.update(width, height, true);
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
        playfield.update(delta);
    }

    private void draw() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        shapeRenderer.setProjectionMatrix(camera.combined);
        playfield.drawTopDown(shapeRenderer);

        hudViewport.apply();
        batch.setProjectionMatrix(hudViewport.getCamera().combined);
        batch.begin();
        font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 5, hudViewport.getWorldHeight() - 5);
        batch.end();
    }
}
