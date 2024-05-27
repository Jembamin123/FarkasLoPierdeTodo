package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class GotaMala extends Gota {
    private int daño;

    public GotaMala(Texture texture, float x, float y, float width, float height, int daño) {
        super(texture, x, y, width, height);
        this.daño = daño;
    }

    @Override
    public void actualizar() {
        y -= 300 * Gdx.graphics.getDeltaTime();
    }

    public int getDaño() {
        return daño;
    }

	@Override
	protected Texture getTexture() {
		// TODO Auto-generated method stub
		return null;
	}
}