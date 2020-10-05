import com.javarush.engine.cell.*;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {
    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    private List<Star> stars;
    private EnemyFleet enemyFleet;
    public static final int COMPLEXITY = 5;
    private List<Bullet> enemyBullets;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private  List<Bullet> playerBullets;
    private static final int PLAYER_BULLETS_MAX = 1;
    private int score = 0;

    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        score = 0;
        createStars();
        enemyFleet = new EnemyFleet();
        setTurnTimer(40);
        enemyBullets = new ArrayList<>();
        playerShip = new PlayerShip();
        isGameStopped = false;
        animationsCount = 0;
        playerBullets = new ArrayList<Bullet>();
        drawScene();
    }

    public void onKeyPress(Key key) {
        if (key == Key.SPACE && isGameStopped == true) {
            createGame();
        }
        if (key == Key.LEFT) {
            playerShip.setDirection(Direction.LEFT);
        } else if (key == Key.RIGHT) {
            playerShip.setDirection(Direction.RIGHT);
        }
        if (key == Key.SPACE) {
            Bullet current = playerShip.fire();
            if (current != null && playerBullets.size() < PLAYER_BULLETS_MAX) {
                playerBullets.add(current);
            }
        }
    }

    public void onKeyReleased(Key key) {
        if (key == Key.LEFT && playerShip.getDirection() == Direction.LEFT) {
            playerShip.setDirection(Direction.UP);
        } else if (key == Key.RIGHT && playerShip.getDirection() == Direction.RIGHT) {
            playerShip.setDirection(Direction.UP);
        }
    }

    private  void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) {
            showMessageDialog(Color.BLACK, "YOU ARE WIN", Color.GREEN, 15);
        } else {
            showMessageDialog(Color.BLACK, "YOU ARE LOSE", Color.RED, 15);
        }
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount >=10)  stopGame(playerShip.isAlive);
    }

    private void moveSpaceObjects(){
        enemyFleet.move();
        for (Bullet element: enemyBullets) {
            element.move();
        }
        for (Bullet element: playerBullets) {
            element.move();
        }
        playerShip.move();
    }

    private void check() {
        playerShip.verifyHit(enemyBullets);
        enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();
        if (!playerShip.isAlive) {
            stopGameWithDelay();
        }
        double koord = enemyFleet.getBottomBorder();
        if (koord >= playerShip.y) {
            playerShip.kill();
        }
        int count = enemyFleet.getShipsCount();
        if (count == 0) {
            playerShip.win();
            stopGameWithDelay();
        }
        score = score + enemyFleet.verifyHit(playerBullets);

    }

    public void setCellValueEx(int x, int y, Color col, String str) {
        if (x < 0 || x > WIDTH-1 || y < 0 || y > HEIGHT-1) {
            return;
        }
        super.setCellValueEx(x, y, col, str);
    }

    private void removeDeadBullets(){
        List<Bullet> spisok = new ArrayList<>(enemyBullets);
        for (Bullet list: spisok ) {
            if (!list.isAlive || list.y >= HEIGHT-1) {
                enemyBullets.remove(list);
            }
        }
        List<Bullet> spisok1 = new ArrayList<>(playerBullets);
        for (Bullet list: spisok1 ) {
            if (!list.isAlive || list.y + list.height < 0) {
                playerBullets.remove(list);
            }
        }
    }

    private void drawScene() {
        drawField();
        playerShip.draw(this);
        for (Bullet element: enemyBullets) {
            element.draw(this);
        }
        for (Bullet element: playerBullets) {
            element.draw(this);
        }
        enemyFleet.draw(this);
    }

    public void onTurn(int x){
        setScore(score);
        moveSpaceObjects();
        check();
        Bullet bul = enemyFleet.fire(this);
        if (bul != null) {
            enemyBullets.add(bul);
        }
        drawScene();
    }

    private void  createStars() {
        stars = new ArrayList<>();
        stars.add(new Star(2,10));
        stars.add(new Star(10,15));
        stars.add(new Star(10,20));
        stars.add(new Star(45,10));
        stars.add(new Star(50,20));
        stars.add(new Star(55,20));
        stars.add(new Star(35,5));
        stars.add(new Star(26,10));

    }

    private void drawField() {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }
        for (Star x: stars) {
            x.draw(this);
        }

    }

}
