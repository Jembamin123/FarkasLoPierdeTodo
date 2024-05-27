package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class GameOverScreen implements Screen {
    private final GameLluviaMenu game;
    private SpriteBatch batch;
    private BitmapFont font;
    private OrthographicCamera camera;
    Texture textureFondo;

    public GameOverScreen(final GameLluviaMenu game) {
        this.game = game;
        this.batch = game.getBatch();
        this.font = game.getFont();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        textureFondo = new Texture(Gdx.files.internal("perdiste.png")); // Asegúrate de cargar la textura aquí
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);
        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(textureFondo, 0, 0, camera.viewportWidth, camera.viewportHeight); // Dibujar la imagen de fondo
        font.draw(batch, "GAME OVER", camera.viewportWidth / 2 - 50, camera.viewportHeight / 2 + 50);
        font.draw(batch, "Toca en cualquier lado para reiniciar.", camera.viewportWidth / 2 - 100, camera.viewportHeight / 2 - 50);
        font.draw(batch, "Puntuación: " + game.getHigherScore(), camera.viewportWidth / 2 - 50, camera.viewportHeight / 2 - 100);
        batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void show() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        textureFondo.dispose(); // Liberar la textura al finalizar
    }
}
