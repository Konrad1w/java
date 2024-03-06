package uj.wmii.pwj.collections;

public interface BattleshipGenerator {

    static BattleshipGenerator defaultInstance() {
        return null;
    }

    String generateMap();

}
