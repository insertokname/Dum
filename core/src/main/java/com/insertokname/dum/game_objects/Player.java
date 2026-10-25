package com.insertokname.dum.game_objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.insertokname.dum.GameConfig;

public class Player {
    private final Vector2 position;
    private final Vector2 direction;

    public Player(Vector2 position, Vector2 direction) {
        this.position = position;
        this.direction = direction;
    }

    public Vector2 getDirection() {
        return new Vector2(direction);
    }

    public void displaceRotation(float displacementDeg) {
        this.direction.rotateDeg(displacementDeg);
    }

    public float getDirectionDisplacement() {
        float rotation = 0;
        if (Gdx.input.isKeyPressed(Input.Keys.L)) {
            rotation -= 1;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.J)) {
            rotation += 1;
        }
        return rotation * GameConfig.Player.ROTATION_SPEED;
    }


    public Vector2 getPosition() {
        return new Vector2(position);
    }

    public void displacePosition(Vector2 displacement) {
        this.position.add(displacement);
    }

    static final Vector2 UP = new Vector2(0, 1);

    public Vector2 getPositionDisplacement() {
        Vector2 movement = new Vector2(0, 0);
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            movement.add(-1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            movement.add(1, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            movement.add(0, 1);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            movement.add(0, -1);
        }
        return movement
            .nor()
            .scl(GameConfig.Player.MOVEMENT_SPEED)
            .rotateDeg(direction.angleDeg(UP));
    }

    public void drawTopDown(ShapeRenderer shapeRenderer) {

        Vector2 head = new Vector2(direction)
            .scl(GameConfig.Player.HITBOX_RADIUS)
            .add(position);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(GameConfig.Player.COLOR);
        shapeRenderer.circle(position.x, position.y, GameConfig.Player.HITBOX_RADIUS, 24);

        shapeRenderer.setColor(GameConfig.Player.HEAD_COLOR);
        shapeRenderer.circle(head.x, head.y, GameConfig.Player.HEAD_SIZE, 24);

        shapeRenderer.end();
    }
}
