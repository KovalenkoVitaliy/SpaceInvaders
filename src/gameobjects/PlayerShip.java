package gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.List;

public class PlayerShip extends Ship {

    private Direction direction = Direction.UP;

    public void setDirection(Direction newDirection) {
        if (newDirection != Direction.DOWN) {
            direction = newDirection;
        }
    }

    public void win() {
        setStaticView(ShapeMatrix.WIN_PLAYER);
    }

    public Direction getDirection() {
        return direction;
    }

    public PlayerShip(double x, double y) {
        super(x, y);
    }

    public PlayerShip() {
        super(SpaceInvadersGame.WIDTH/2, SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length-1);
        setStaticView(ShapeMatrix.PLAYER);
    }

    public Bullet fire(){
        if (!isAlive) {
            return null;
        }
        return new Bullet(x + 2, y - ShapeMatrix.BULLET.length, Direction.UP);
    }

    public void move(){
        if (!isAlive) {
            return;
        }
        if (direction == Direction.LEFT) {
            x = x - 1;
        } else if (direction == Direction.RIGHT) {
            x = x + 1;
        }
        if ( x < 0) {
            x = 0;
        } else if (x + width > SpaceInvadersGame.WIDTH) {
            x = SpaceInvadersGame.WIDTH - width;
        }

    }

    public void kill() {
        if (!isAlive) {
            return;
        }
        isAlive = false;
        setAnimatedView(false, ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST, ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND, ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD, ShapeMatrix.DEAD_PLAYER);
    }

    public void verifyHit(List<Bullet> bullets) {
        if (bullets.size() == 0) {
            return;
        }
        boolean flag = false;
        if (isAlive) {
            for (Bullet x : bullets) {
                if (x.isAlive) {
                    flag = isCollision(x);
                    if (flag) {
                        kill();
                        x.kill();
                    }
                }
            }
        }
    }

}
