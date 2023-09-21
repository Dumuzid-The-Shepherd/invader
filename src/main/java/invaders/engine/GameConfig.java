package invaders.engine;

import javafx.scene.image.Image;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GameConfig {
    public Game Game;
    public Player Player;
    public List<Bunker> Bunkers;
    public List<Alien> Aliens;

    public GameConfig(String configPath) {
        try {
            String content = new String(Files.readAllBytes(Paths.get("src/main/resources/config.json")));
            JSONObject jsonObject = new JSONObject(content);

            // Parse Game
            this.Game = parseGame(jsonObject.getJSONObject("Game"));

            // Parse Player
            this.Player = parsePlayer(jsonObject.getJSONObject("Player"));

            // Parse Bunkers
            this.Bunkers = parseBunkers(jsonObject.getJSONArray("Bunkers"));

            // Parse Enemies
            this.Aliens = parseAliens(jsonObject.getJSONArray("Enemies"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Game parseGame(JSONObject gameJson) {
        Game game = new Game();
        game.size = new Size();
        game.size.x = gameJson.getJSONObject("size").getInt("x");
        game.size.y = gameJson.getJSONObject("size").getInt("y");
        return game;
    }

    private Player parsePlayer(JSONObject playerJson) {
        Player player = new Player();
        player.colour = playerJson.getString("colour");
        player.speed = playerJson.getInt("speed");
        player.lives = playerJson.getInt("lives");
        player.position = new Position();
        player.position.x = playerJson.getJSONObject("position").getInt("x");
        player.position.y = playerJson.getJSONObject("position").getInt("y");
        return player;
    }

    private List<Bunker> parseBunkers(JSONArray bunkersJson) {
        List<Bunker> bunkers = new ArrayList<>();
        for (int i = 0; i < bunkersJson.length(); i++) {
            JSONObject bunkerJson = bunkersJson.getJSONObject(i);
            Bunker bunker = new Bunker();
            bunker.position = new Position();
            bunker.position.x = bunkerJson.getJSONObject("position").getInt("x");
            bunker.position.y = bunkerJson.getJSONObject("position").getInt("y");
            bunker.size = new Size();
            bunker.size.x = bunkerJson.getJSONObject("size").getInt("x");
            bunker.size.y = bunkerJson.getJSONObject("size").getInt("y");
            bunkers.add(bunker);
        }
        return bunkers;
    }

    private List<Alien> parseAliens(JSONArray aliensJson) {
        List<Alien> aliens = new ArrayList<>();
        for (int i = 0; i < aliensJson.length(); i++) {
            JSONObject alienJson = aliensJson.getJSONObject(i);
            Alien alien = new Alien();
            alien.position = new Position();
            alien.position.x = alienJson.getJSONObject("position").getInt("x");
            alien.position.y = alienJson.getJSONObject("position").getInt("y");
            alien.projectile = alienJson.getString("projectile");
            aliens.add(alien);
        }
        return aliens;
    }

    public static class Game {
        public Size size;
    }

    public static class Player {
        public String colour;
        public int speed;
        public int lives;
        public Position position;
    }

    public static class Bunker {
        public Position position;
        public Size size;
    }


    public static class Position {
        public int x;
        public int y;
    }

    public static class Alien {
        public Position position;
        public String projectile;
        public String imageFilename; // Add this to specify the image for each alien type in the JSON
    }

    public static class Size {
        public int x;
        public int y;
    }
}

