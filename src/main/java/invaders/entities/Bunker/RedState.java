package invaders.entities.Bunker;

import javafx.scene.paint.Color;

public class RedState implements BunkerState {
    @Override
    public void handleHit(Bunker bunker) {
        // The bunker is destroyed after turning red and getting hit again.
        // You can implement the logic to remove the bunker from the game here.
        // For now, we'll just set its state to null.
        bunker.setState(null);
    }

    @Override
    public String getImageFilename() {
        return "bunker_red.png";
    }
}
