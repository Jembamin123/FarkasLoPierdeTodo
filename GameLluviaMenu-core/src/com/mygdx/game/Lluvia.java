package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class Lluvia {
    private Array<Rectangle> rainDropsPos;
    private Array<Integer> rainDropsType;
    private long lastDropTime;
    private long multiplicadorStartTime; // Tiempo de inicio del multiplicador
    private boolean multiplicadorActivo; // Indicador de si el multiplicador está activo
    private Texture gotaBuena;
    private Texture gotaMala;
    private Texture gotaMultiplicadora; // Nueva textura para la gota multiplicadora
    private Sound dropSound;
    private Music rainMusic;
    private Music multiplicadorMusic; // Nueva música para el multiplicador
    private float dropSpeed; // Variable para la velocidad de caída
    private int puntosPorGota; // Variable para el valor de puntos por gota

    public Lluvia(Texture gotaBuena, Texture gotaMala, Texture gotaMultiplicadora, Sound ss, Music mm, Music multiplicadorMusic) {
        rainMusic = mm;
        dropSound = ss;
        this.gotaBuena = gotaBuena;
        this.gotaMala = gotaMala;
        this.gotaMultiplicadora = gotaMultiplicadora; // Asignamos la nueva textura
        this.multiplicadorMusic = multiplicadorMusic; // Asignamos la música del multiplicador
        this.dropSpeed = 200; // Velocidad inicial de las gotas
        this.puntosPorGota = 10; // Valor inicial de puntos por gota
        this.multiplicadorActivo = false; // Inicialmente el multiplicador no está activo
    }

    public void crear() {
        rainDropsPos = new Array<Rectangle>();
        rainDropsType = new Array<Integer>();
        crearGotaDeLluvia();
        // start the playback of the background music immediately
        rainMusic.setLooping(true);
        rainMusic.play();
    }

    private void crearGotaDeLluvia() {
        int numDrops = MathUtils.random(1, 5); // Número aleatorio de gotas a crear
        for (int i = 0; i < numDrops; i++) {
            Rectangle raindrop = new Rectangle();
            raindrop.x = MathUtils.random(0, 800 - 64);
            raindrop.y = 480;
            raindrop.width = 64;
            raindrop.height = 64;
            rainDropsPos.add(raindrop);
            // ver el tipo de gota
            int dropType;
            if (multiplicadorActivo) {
                dropType = 2; // Solo gotas buenas cuando el multiplicador está activo
            } else {
                dropType = MathUtils.random(1, 10);
                if (dropType < 4)
                    dropType = 1; // Gota dañina
                else if (dropType < 8)
                    dropType = 2; // Gota a recolectar
                else
                    dropType = 3; // Gota multiplicadora
            }
            rainDropsType.add(dropType);
        }
        lastDropTime = TimeUtils.nanoTime();
    }

    public boolean actualizarMovimiento(Tarro tarro) {
        // Verificar si el multiplicador está activo y si han pasado 10 segundos
        if (multiplicadorActivo && TimeUtils.nanoTime() - multiplicadorStartTime > 10_000_000_000L) {
            puntosPorGota = 10; // Resetear el valor de puntos por gota
            multiplicadorActivo = false; // Desactivar el multiplicador
            multiplicadorMusic.stop(); // Detener la música del multiplicador
            rainMusic.play(); // Reanudar la música normal
        }

        // Generar gotas de lluvia 
        if (TimeUtils.nanoTime() - lastDropTime > MathUtils.random(500000000, 1500000000)) {
            crearGotaDeLluvia();
        }

        // Revisar si las gotas cayeron al suelo o chocaron con el tarro
        for (int i = 0; i < rainDropsPos.size; i++) {
            Rectangle raindrop = rainDropsPos.get(i);
            raindrop.y -= dropSpeed * Gdx.graphics.getDeltaTime();
            // Cae al suelo y se elimina
            if (raindrop.y + 64 < 0) {
                rainDropsPos.removeIndex(i);
                rainDropsType.removeIndex(i);
                i--; // Ajustar el índice después de eliminar una gota
            }
            if (raindrop.overlaps(tarro.getArea())) { // La gota choca con el tarro
                if (rainDropsType.get(i) == 1) { // Gota dañina
                    tarro.dañar();
                    if (tarro.getVidas() <= 0)
                        return false; // Si se queda sin vidas retorna falso /game over
                    rainDropsPos.removeIndex(i);
                    rainDropsType.removeIndex(i);
                    i--; // Ajustar el índice después de eliminar una gota
                } else if (rainDropsType.get(i) == 2) { // Gota a recolectar
                    tarro.sumarPuntos(puntosPorGota); // Usamos la variable de puntos por gota
                    dropSound.play();
                    rainDropsPos.removeIndex(i);
                    rainDropsType.removeIndex(i);
                    i--; // Ajustar el índice después de eliminar una gota
                    incrementarVelocidad(); // Incrementar la velocidad de caída
                } else if (rainDropsType.get(i) == 3) { // Gota multiplicadora
                    puntosPorGota *= 2; // Duplicar el valor de puntos por gota
                    multiplicadorStartTime = TimeUtils.nanoTime(); // Guardar el tiempo de inicio del multiplicador
                    multiplicadorActivo = true; // Activar el multiplicador
                    dropSound.play();
                    rainMusic.stop(); // Detener la música normal
                    multiplicadorMusic.setLooping(true);
                    multiplicadorMusic.play(); // Iniciar la música del multiplicador
                    rainDropsPos.removeIndex(i);
                    rainDropsType.removeIndex(i);
                    i--; // Ajustar el índice después de eliminar una gota
                    incrementarVelocidad(); // Incrementar la velocidad de caída
                }
            }
        }
        return true;
    }

    public void actualizarDibujoLluvia(SpriteBatch batch) {
        for (int i = 0; i < rainDropsPos.size; i++) {
            Rectangle raindrop = rainDropsPos.get(i);
            if (rainDropsType.get(i) == 1) // Gota dañina
                batch.draw(gotaMala, raindrop.x, raindrop.y);
            else if (rainDropsType.get(i) == 2) // Gota a recolectar
                batch.draw(gotaBuena, raindrop.x, raindrop.y);
            else if (rainDropsType.get(i) == 3) // Gota multiplicadora
                batch.draw(gotaMultiplicadora, raindrop.x, raindrop.y); // Imagen de la gota multiplicadora
        }
    }

    public void destruir() {
        dropSound.dispose();
        rainMusic.dispose();
        multiplicadorMusic.dispose(); // Liberar la música del multiplicador
    }

    public void pausar() {
        rainMusic.stop();
        multiplicadorMusic.stop(); // Detener la música del multiplicador en caso de estar activa
    }

    public void continuar() {
        if (multiplicadorActivo) {
            multiplicadorMusic.play();
        } else {
            rainMusic.play();
        }
    }

    private void incrementarVelocidad() {
        dropSpeed += 3; // Incrementar la velocidad en cada gota recolectada
    }
}
