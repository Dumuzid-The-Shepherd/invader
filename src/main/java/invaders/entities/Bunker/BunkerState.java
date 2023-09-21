package invaders.entities.Bunker;

import javafx.scene.paint.Color;

public interface BunkerState {
    void handleHit(Bunker bunker);
    String getImageFilename();
}

