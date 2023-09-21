package invaders.entities.Bunker;

import javafx.scene.paint.Color;

public class YellowState implements BunkerState {
    @Override
    public void handleHit(Bunker bunker) {
        bunker.setState(new RedState());
    }

    @Override
    public String getImageFilename() {
        return "bunker_yellow.png";
    }
}