package com.insertokname.dum.game_objects.raycasting;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class RayCastResult {
    public final List<Ray> rays;
    public final Vector2 castStartPos;
    public final Vector2 mainDirection;

    public RayCastResult(List<Ray> rays,
                         Vector2 castStartPos,
                         Vector2 mainDirection) {
        this.rays = rays;
        this.castStartPos = castStartPos;
        this.mainDirection = mainDirection;
    }
}
