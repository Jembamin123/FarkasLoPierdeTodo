package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class GotaBuena extends Gota {
    private int puntos;

    public GotaBuena(Texture texture, float x, float y, float width, float height, int puntos) {
        super(texture, x, y, width, height);
        this.puntos = puntos;
    }

    @Override
    public void actualizar() {
        y -= 300 * Gdx.graphics.getDeltaTime();
    }

    public int getPuntos() {
        return puntos;
    }

	@Override
	protected Texture getTexture() {
		// TODO Auto-generated method stub
		return null;
	}
}