package com.insertokname.dum.game_objects.raycasting;

import com.badlogic.gdx.math.Vector2;
import com.insertokname.dum.game_objects.Tile;

public class Ray {
    public Vector2 ray;
    public Tile collidedTile;
    public float distToPlayerViewPlane;

    public Ray(Vector2 ray, Tile collidedTile, float distToPlayerViewPlane) {
        this.ray = ray;
        this.collidedTile = collidedTile;
        this.distToPlayerViewPlane = distToPlayerViewPlane;
    }
}
