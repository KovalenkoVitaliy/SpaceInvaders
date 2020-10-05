package gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {

    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    public EnemyFleet(){
        createShips();
    }

    private double getSpeed(){
        if (ships.size() >= 2 ) {
            return 3.0d / ships.size();
        } else {
            return 2.0d;
        }
    }

    public double getBottomBorder() {
        double bottom = 0;
        if (ships.size() == 0) {
            return bottom;
        }
        bottom = ships.get(0).y + ships.get(0).height;
        for (int i = 0; i < ships.size(); i++) {
            if (bottom < ships.get(i).y + ships.get(i).height) {
                bottom = ships.get(i).y + ships.get(i).height;
            }
        }
        return bottom;
    }

    public int getShipsCount() {
        return ships.size();
    }

    public int verifyHit(List<Bullet> bullets) {
        int score = 0;
        if (bullets.size()==0) {
            return 0;
        }
        for (EnemyShip el: ships) {
            for (Bullet bul: bullets) {
                if (el.isCollision(bul) && el.isAlive && bul.isAlive) {
                    el.kill();
                    bul.kill();
                    score = score + el.score;
                }
            }
        }
        return score;
    }

    public void deleteHiddenShips(){
        for (EnemyShip ship : new ArrayList<>(ships)) {
            if (!ship.isVisible()) {
                ships.remove(ship);
            }
        }
    }

    public Bullet fire(Game game){
        if (ships.size() == 0) {
            return null;
        }
        if (game.getRandomNumber(100/SpaceInvadersGame.COMPLEXITY) > 0) {
            return null;
        }
        int number = game.getRandomNumber(ships.size());
        return ships.get(number).fire();
    }

    public void move() {
        if (ships.size() == 0) {
            return;
        }
        Direction current = direction;
        if (direction == Direction.LEFT && getLeftBorder() < 0) {
            direction = Direction.RIGHT;
            current = Direction.DOWN;

        } else if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
            direction = Direction.LEFT;
            current = Direction.DOWN;
        }
        double speed = getSpeed();
        for (EnemyShip elem : ships) {
            elem.move(current, speed);
        }
    }

    private double getLeftBorder() {
        double min = ships.get(0).x;
        for (EnemyShip element : ships) {
            if (min > element.x) {
                min = element.x;
            }
        }
        return min;
    }

    private double getRightBorder() {
        double max = ships.get(0).x + ships.get(0).width;
        for (EnemyShip element : ships) {
            if (max < element.x + element.width) {
                max = element.x + element.width;
            }
        }
        return max;
    }

    private void createShips(){
        ships = new ArrayList<>();
        for (int y = 0; y < ROWS_COUNT; y++) {
            for (int x = 0; x < COLUMNS_COUNT; x++) {
                ships.add(new EnemyShip(x * STEP, y*STEP + 12));
            }
        }
        ships.add(new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5 ));
    }

    public void draw(Game game) {
        for (EnemyShip x: ships) {
            x.draw(game);
        }
    }

}
