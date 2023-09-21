package invaders.engine;

import java.util.ArrayList;
import java.util.List;

import invaders.GameObject;
import invaders.entities.Alien.Alien;
import invaders.entities.Bunker.Bunker;
import invaders.entities.Player;
import invaders.entities.Projectile.PlayerProjectile;
import invaders.entities.Projectile.Projectile;
import invaders.entities.Projectile.ProjectileBehavior;
import invaders.entities.Projectile.SlowStraightBehavior;
import invaders.physics.Moveable;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine {

	private List<GameObject> gameobjects;
	private List<Renderable> renderables;
	private Player player;

	private boolean left;
	private boolean right;

	private boolean playerProjectileActive = false;
	private boolean aliensMovingRight = true; // Initial direction
	private static final int PLAYER_Y = 750; // Player's y-coordinate

	private int alienShootCounter = 0;
	private static final int ALIEN_SHOOT_INTERVAL = 200;

	public List<Projectile> projectiles = new ArrayList<>();

	public GameEngine(String configPath) {
		gameobjects = new ArrayList<GameObject>();
		renderables = new ArrayList<Renderable>();

		player = new Player(new Vector2D(200, 380));
		renderables.add(player);

		// Load game configuration from JSON
		GameConfig gameConfig = new GameConfig(configPath);

		// Create and add bunkers from the configuration
		for (GameConfig.Bunker bunkerConfig : gameConfig.Bunkers) {
			Vector2D bunkerPosition = new Vector2D(bunkerConfig.position.x, bunkerConfig.position.y);
			Bunker bunker = new Bunker(bunkerPosition);
			renderables.add(bunker);
		}

		for (GameConfig.Alien alienConfig : gameConfig.Aliens) {
			Vector2D alienPosition = new Vector2D(alienConfig.position.x, alienConfig.position.y);
			Alien alien = new Alien(alienPosition, "src/main/resources/enemy.png", this);
			renderables.add(alien);
		}

//		if(ProjectileActive){
//			renderables.add(projectiles);
//			ProjectileActive = false;
//		}




	}

	/**
	 * Updates the game/simulation
	 */
	public void update(){
		movePlayer();
		for(GameObject go: gameobjects){
			go.update();
		}

		// ensure that renderable foreground objects don't go off-screen
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= 640) {
				ro.getPosition().setX(639-ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(1);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= 400) {
				ro.getPosition().setY(399-ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(1);
			}
		}

		alienShootCounter++;
		if (alienShootCounter >= ALIEN_SHOOT_INTERVAL) {
			alienShootCounter = 0;
			shootFromRandomAlien();
		}

		moveAliens();
		if (checkGameOver()) {
			endGame();
		}

		List<Projectile> projectilesToRemove = new ArrayList<>();
		for (Projectile projectile : projectiles) {
			projectile.move();
			if (projectile.getPosition().getY() <= 1) {
				projectile.getPosition().setY(-100);
				playerProjectileActive = false; // Reset the flag if a projectile is removed
			}
		}
		projectiles.removeAll(projectilesToRemove);
		checkProjectileCollisions();
	}

	private void checkProjectileCollisions() {
		List<Projectile> projectilesToRemove = new ArrayList<>();
		List<Alien> aliensToRemove = new ArrayList<>();
		for (Projectile projectile : projectiles) {
			if (projectileCollidesWithPlayer(projectile)) {
				// Handle player hit logic
				endGame();  // or reduce player's life, etc.
				projectile.setImage("src/main/resources/black_projectile.png"); // Set the image to black_projectile
				projectile.getPosition().setX(1200);
			} else if (projectileCollidesWithBunker(projectile)) {
				// Handle bunker hit logic
				// Reduce bunker's health, change its state, etc.
				projectile.setImage("src/main/resources/black_projectile.png"); // Set the image to black_projectile
				projectile.getPosition().setX(1200);
			} else if (projectile.getPosition().getY() >= 380) { // Assuming 800 is the height of the screen

				projectile.setImage("src/main/resources/black_projectile.png"); // Set the image to black_projectile
				projectile.getPosition().setX(1200);
			}

			for (Renderable ro : renderables) {
				if (ro instanceof Alien) {
					Alien alien = (Alien) ro;
					if (projectileCollidesWithAlien(projectile, alien)) {
						System.out.println("Alien hit detected!");
						alien.setImage("src/main/resources/enemy_black.png"); // Replace 'path_to_your_directory' with the actual path
						alien.getPosition().setY(-100);
						projectile.getPosition().setX(1200);
						aliensToRemove.add(alien);

						increaseAlienSpeed(); // Increase the speed of all aliens
						break;
					}
				}
			}

		}
		projectiles.removeAll(projectilesToRemove);
		renderables.removeAll(projectilesToRemove);
		renderables.removeAll(aliensToRemove);
	}

	private void increaseAlienSpeed() {
		final double SPEED_INCREMENT = 0.01;
		for (Renderable ro : renderables) {
			if (ro instanceof Alien) {
				Alien alien = (Alien) ro;
				alien.increaseSpeed(SPEED_INCREMENT);
			}
		}
	}

	private boolean projectileCollidesWithPlayer(Projectile projectile) {
		// Implement collision detection logic between projectile and player
		// Return true if they collide, false otherwise
		return projectile.getPosition().getY() + projectile.getHeight() >= player.getPosition().getY() &&
				projectile.getPosition().getX() < player.getPosition().getX() + player.getWidth() &&
				projectile.getPosition().getX() + projectile.getWidth() > player.getPosition().getX();
	}

	private boolean projectileCollidesWithBunker(Projectile projectile) {
		// Implement collision detection logic between projectile and bunkers
		for (Renderable ro : renderables) {
			if (ro instanceof Bunker) {
				Bunker bunker = (Bunker) ro;
				if (projectile.getPosition().getY() + projectile.getHeight() >= bunker.getPosition().getY() &&
						projectile.getPosition().getX() < bunker.getPosition().getX() + bunker.getWidth() &&
						projectile.getPosition().getX() + projectile.getWidth() > bunker.getPosition().getX()) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean projectileCollidesWithAlien(Projectile projectile, Alien alien) {
		return projectile.getPosition().getY() <= alien.getPosition().getY() + alien.getHeight() &&
				projectile.getPosition().getX() + projectile.getWidth() >= alien.getPosition().getX() &&
				projectile.getPosition().getX() <= alien.getPosition().getX() + alien.getWidth();
	}

	private void moveAliens() {
		boolean edgeReached = false;

		// Check if any alien has reached the edge
		for (Renderable ro : renderables) {
			if (ro instanceof Alien) {
				Alien alien = (Alien) ro;
				if (aliensMovingRight && alien.getPosition().getX() + alien.getWidth() >= 620) {
					edgeReached = true;
					break;
				} else if (!aliensMovingRight && alien.getPosition().getX() <= 20) {
					edgeReached = true;
					break;
				}
			}
		}

		// If edge reached, descend and reverse direction
		if (edgeReached) {
			for (Renderable ro : renderables) {
				if (ro instanceof Alien) {
					Alien alien = (Alien) ro;
					alien.descend();
				}
			}
			aliensMovingRight = !aliensMovingRight;
		} else {
			// Otherwise, move horizontally
			for (Renderable ro : renderables) {
				if (ro instanceof Alien) {
					Alien alien = (Alien) ro;
					if (aliensMovingRight) {
						alien.right();
					} else {
						alien.left();
					}
				}
			}
		}
	}

	public void addProjectile(Projectile projectile) {
		projectiles.add(projectile);
		renderables.add(projectile); // Assuming you have a list of renderables to draw on the screen
	}

	private boolean checkGameOver() {
		for (Renderable ro : renderables) {
			if (ro instanceof Alien) {
				Alien alien = (Alien) ro;
				if (alien.getPosition().getY() + alien.getHeight() >= 790 ||  // Assuming 800 is the height of the screen
						alien.getPosition().getY() + alien.getHeight() >= PLAYER_Y) {
					return true;
				}
			}
		}
		return false;
	}


	private void shootFromRandomAlien() {
		List<Alien> aliens = getAliens();
		if (!aliens.isEmpty()) {
			int randomIndex = (int) (Math.random() * aliens.size());
			Alien randomAlien = aliens.get(randomIndex);
			randomAlien.shoot();
		}
	}

	private List<Alien> getAliens() {
		List<Alien> aliens = new ArrayList<>();
		for (Renderable ro : renderables) {
			if (ro instanceof Alien) {
				aliens.add((Alien) ro);
			}
		}
		return aliens;
	}

	private void endGame() {
		// Handle game over logic here
		// For now, we'll just print a message
		System.out.println("Game Over!");
		// You can also stop the game loop, display a game over screen, etc.
	}



	public List<Renderable> getRenderables(){
		return renderables;
	}


	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public void shootPressed() {
		if (playerProjectileActive) {
			return; // Don't shoot if a player's projectile is already active
		}
		// Create a projectile at the player's position
		ProjectileBehavior behavior = new PlayerProjectile();  // or whichever behavior you want for the player's projectile
		Vector2D startPosition = new Vector2D(player.getPosition().getX() + player.getWidth() / 2 - 4, player.getPosition().getY() - 8);
		Projectile projectile = new Projectile(behavior, startPosition);
		projectile.getPosition().setX(player.getPosition().getX() + player.getWidth() / 2 - projectile.getWidth() / 2);
		projectile.getPosition().setY(player.getPosition().getY() - projectile.getHeight());

		// Add the projectile to the game world
		projectiles.add(projectile);
		renderables.add(projectile);
		playerProjectileActive = true;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}
}
