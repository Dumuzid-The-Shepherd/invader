package invaders.entities.Bunker;

import javafx.scene.paint.Color;

public class GreenState implements BunkerState {
    @Override
    public void handleHit(Bunker bunker) {
        bunker.setState(new YellowState());
    }

    @Override
    public String getImageFilename() {
        return "bunker_green.png";
    }
}
