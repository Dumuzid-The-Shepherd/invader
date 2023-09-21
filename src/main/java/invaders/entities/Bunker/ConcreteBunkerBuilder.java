package invaders.entities.Bunker;
import javafx.scene.paint.Color;

import invaders.engine.GameConfig;

import invaders.engine.GameConfig;

public class ConcreteBunkerBuilder implements BunkerBuilder {
    private GameConfig.Position position;
    private GameConfig.Size size;

    @Override
    public BunkerBuilder setPosition(GameConfig.Position position) {
        this.position = position;
        return this;
    }

    @Override
    public BunkerBuilder setSize(GameConfig.Size size) {
        this.size = size;
        return this;
    }

//    @Override
//    public Bunker build() {
//        return new Bunker(position);
//    }
}
