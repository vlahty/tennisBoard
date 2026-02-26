package services;

import model.Match;
import model.Player;

public class EntitiesValidator{
    private final Player player;

    public EntitiesValidator(Player player, Match match) {
        this.player = player;
    }

    public static boolean isPlayersNameUniqueAndNotNull(String firstPlayerName, String secondPlayerName){

        if (firstPlayerName == null || secondPlayerName == null)
            return false;

        if (firstPlayerName.trim().isEmpty() || secondPlayerName.trim().isEmpty())
            return false;

        return firstPlayerName.equals(secondPlayerName);
    }

}
