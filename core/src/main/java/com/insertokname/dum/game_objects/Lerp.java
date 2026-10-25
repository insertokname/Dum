package com.insertokname.dum.game_objects;

public final class Lerp {
    public static float lerp(float a, float b, float t) {
        return a * (1 - t) + b * t;
    }
}
