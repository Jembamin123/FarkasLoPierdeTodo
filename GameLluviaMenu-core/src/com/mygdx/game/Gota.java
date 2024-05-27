package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Gota implements Actualizable {
    protected float x, y, width, height;
    protected Texture texture;

    public Gota(Texture texture, float x, float y, float width, float height) {
        this.texture = texture;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public abstract void actualizar();

    public void dibujar(SpriteBatch batch) {
        batch.draw(texture, x, y, width, height);
    }

    public boolean colisionaCon(Rectangle rect) {
        Rectangle gotaRect = new Rectangle(x, y, width, height);
        return gotaRect.overlaps(rect);
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

	protected abstract Texture getTexture();
}
