package domain;

import domain.Serialize;
import domain.GameObject;

import java.awt.*;

/**
 * Esta es la clase padre del bloque y del personaje
 * @param <T>
 */
public abstract class Componente<T> extends Serialize {
    public GameObject parent;

    public void draw(Graphics g) {
        return;
    }

    /**
     * actualiza el objeto
     * @param dt
     */
    public void update(double dt) {
        return;
    }

    public void start() {
        return;
    }

    @Override
    public String serialize(int tabSize) {
        return "";
    }

}
