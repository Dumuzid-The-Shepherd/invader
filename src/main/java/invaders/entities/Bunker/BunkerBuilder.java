package invaders.entities.Bunker;

import invaders.engine.GameConfig;

public interface BunkerBuilder {
    BunkerBuilder setPosition(GameConfig.Position position);
    BunkerBuilder setSize(GameConfig.Size size);
//    Bunker build();
}
