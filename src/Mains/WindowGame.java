package Mains;

import Model.Game.CarAI;
import Model.Game.Player;
import Model.Game.RenderableObject;
import Model.KeyPressedListener;
import Model.Network.InputFactory;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

import java.util.ArrayList;
import java.util.List;

public class WindowGame extends BasicGame {
    private GameContainer container;
    private TiledMap map;

    private List<Player> players = new ArrayList<Player>();
    private List<KeyPressedListener> keyPressedListeners = new ArrayList<KeyPressedListener>();

    public static void launch() throws SlickException {
        AppGameContainer app = new AppGameContainer(new WindowGame(), 1300, 960, false);
        app.setTargetFrameRate(100);
        app.start();
    }

    public WindowGame() {
        super("GeneticDriver");
    }

    @Override
    public void init(GameContainer container) throws SlickException {
        this.container = container;
        this.map = new TiledMap("maps/Map.tmx");

        players.add(new Player("Matt", map, false));
        players.add(new Player("AI", map, "save.txt"));
        for (Player player : players) {
            keyPressedListeners.add(player.getCar());
        }
    }

    @Override
    public void render(GameContainer container, Graphics g) throws SlickException {
        this.map.render(0, 0);

        for (Player player : players) {
            RenderableObject car = player.getCar();
            car.render(container, g);
        }
    }

    @Override
    public void update(GameContainer container, int delta) throws SlickException {
        for (Player player : players) {
            if(player.IsAI()){
                RenderableObject car = player.getCar();
                ((CarAI)car).processNet();
                car.processInput(InputFactory.generateInputFromAI((CarAI)car), delta);
            }else{
                RenderableObject car = player.getCar();
                car.processInput(InputFactory.generateInput(container), delta);
            }

        }
    }

    @Override
    public void keyReleased(int key, char c) {
        if (Input.KEY_ESCAPE == key) {
            this.container.exit();
        }
    }

    @Override
    public void keyPressed(int key, char c) {
        for(KeyPressedListener object : keyPressedListeners) {
            object.keyPressed(key, c);
        }
    }
}