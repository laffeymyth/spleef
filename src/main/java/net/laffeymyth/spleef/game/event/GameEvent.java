package net.laffeymyth.spleef.game.event;

public interface GameEvent {
    default void start() {
    }

    default void tick(int leftTime) {
    }

    default void endGame() {
    }

    default void end() {
    }

    int time();
}
