package com.insertokname.dum.game_objects.raycasting;

import com.badlogic.gdx.math.Vector2;
import com.insertokname.dum.game_objects.Direction;
import com.insertokname.dum.game_objects.Tile;

public class Ray {
    public Vector2 ray;
    public Tile collidedTile;
    public Direction collisionDirection;
    public float distToPlayerViewPlane;
    public float textureSamplePos;

    public Ray(Vector2 ray, Tile collidedTile, Direction collisionDirection, float distToPlayerViewPlane, float textureSamplePos) {
        this.ray = ray;
        this.collidedTile = collidedTile;
        this.collisionDirection = collisionDirection;
        this.distToPlayerViewPlane = distToPlayerViewPlane;
        this.textureSamplePos = textureSamplePos;
    }
}
