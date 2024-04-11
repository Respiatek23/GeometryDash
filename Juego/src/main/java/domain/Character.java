package domain;


import Util.imgUtils;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

import static domain.PanelJuego.getMaxY;

public class Character extends Componente {
    private Image image;
    private static int numMuertes;
    private boolean isDebajo;
    private boolean isDead=false;
    private Clip audioClip=null;
    private List<GameObject> gameObjects;

    /**
     * La clase del jugador
     */
    public Character(){
        try {
            BufferedImage image= new imgUtils().scaleImage(100,100,"resources/geometrydash.jpg");
            this.image=image;
            audioClip = AudioSystem.getClip();
            audioClip.open(AudioSystem.getAudioInputStream(new File("Resources\\jump.wav")));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        numMuertes=0;
    }
    public int getNumMuertes()
    {
        return numMuertes;
    }
    public void setIsDead(boolean s)
    {
        this.isDead=s;
    }
    public boolean getIsDead()
    {
        return this.isDead;
    }
    @Override
    public void draw(Graphics g) {
        g.drawImage(image,(int)parent.conversion.position.x,(int)parent.conversion.position.y,null);
    }
    public void setGameObjects(List<GameObject> g)
    {
        this.gameObjects=g;
    }
    public void update(double dt)
    {
        Mapea();
        parent.getComponent(Bloque.class).getVelocidad().x=0;
        if(isDebajo && parent.getComponent(Bloque.class).getVelocidad().y> 0)
                parent.getComponent(Bloque.class).getVelocidad().y=0;
        if(isDebajo == false)
            parent.getComponent(Bloque.class).getVelocidad().y+=5;
        if(parent.conversion.position.y<0)
            parent.conversion.position.y=0;
        if (Juego.getTeclasPulsadas().contains(KeyEvent.VK_SPACE)) {
            if (isDebajo)
                jump();
        }

    }
    public void setNumMuertes(int e)
    {
        numMuertes=e;
    }

    /**
     * Este metodo hace que salte y le añade un sonido
     */
    public void jump()
    {
        parent.getComponent(Bloque.class).setVelocidad(Vector.add(parent.getComponent(Bloque.class).getVelocidad(), new Vector(0, -290)));
        audioClip.setFramePosition(0);
        audioClip.start();
    }

    /**
     * Este metodo es el que me dice si hay un objeto debajo, o si ha chocado con un objeto
     */
    public void Mapea()
    {
        Rectangle R=new Rectangle((int)parent.conversion.position.x,(int)parent.conversion.position.y+4,100,100);
        Rectangle D=new Rectangle((int)parent.conversion.position.x,(int)parent.conversion.position.y,100,100);
        if(gameObjects!=null){
            for(GameObject go: gameObjects) {
                if(go.getComponent(Bloque.class).getRectangle().intersects(D) /*&& parent.geNombre()("Player")*/)
                {
                    isDead=true;
                    setNumMuertes(getNumMuertes()+1);
                    return;
                }
                if (go.getComponent(Bloque.class).getRectangle().intersects(R)) {
                    isDebajo = true;
                    return;
                }

            }}
        if(parent.conversion.position.y> getMaxY())//sacado a la fuerza
        {
            isDebajo=true;
            return;
        }
        else
            isDebajo=false;
        return;
    }

}