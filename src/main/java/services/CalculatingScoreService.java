package services;

import models.additional.MatchScore;
import java.util.HashMap;
import java.util.Map;

public class CalculatingScoreService {

    private MatchScore ms;

    public CalculatingScoreService(MatchScore ms) {
        this.ms = ms;
    }

    public void pointWon(int playerNumber) { // Игрок (1 или 2)
        // 1. Если идёт тай-брейк
        if (ms.isTieBreak()) {
            handleTieBreakPoint(playerNumber);
        } else {
            // 2. Обычный гейм
            handleRegularGamePoint(playerNumber);
        }
    }

    public String convertPointsToScore(int playerPoints, int opponentPoints){

        if (playerPoints == 0) return "0";
        if (playerPoints == 1) return "15";
        if (playerPoints == 2) return "30";
        if (playerPoints == 3) return "40";

        if (playerPoints >3){
            if (playerPoints - opponentPoints >=1){
                return "AD";
            } else  return "40";
        }

        return String.valueOf(playerPoints);
    }

    public Map<String, Object> getScoreDisplay() {
        Map<String, Object> display = new HashMap<>();

        // Счёт в текущем гейме в теннисной нотации
        display.put("player1Points",
                convertPointsToScore(ms.getPlayer1Points(), ms.getPlayer2Points()));
        display.put("player2Points",
                convertPointsToScore(ms.getPlayer2Points(), ms.getPlayer1Points()));

        // Счёт по геймам (обычные цифры)
        display.put("player1Games", ms.getPlayer1Games());
        display.put("player2Games", ms.getPlayer2Games());

        // Счёт по сетам
        display.put("player1Sets", ms.getPlayer1Sets());
        display.put("player2Sets", ms.getPlayer2Sets());

        // Информация о тай-брейке
        display.put("isTieBreak", ms.isTieBreak());

        return display;
    }

    private void handleRegularGamePoint(int playerNumber) {
        // Увеличиваем счёт игрока
        if (playerNumber == 1) {
            ms.setPlayer1Points(ms.getPlayer1Points() + 1);
        } else ms.setPlayer2Points(ms.getPlayer2Points() + 1);


        // Проверяем, не выиграл ли игрок гейм
        if (ms.getPlayer1Points() >= 4 && ms.getPlayer1Points() - ms.getPlayer2Points() >= 2) {
            // Игрок 1 выиграл гейм
            ms.setPlayer1Games(ms.getPlayer1Games() + 1);
            resetGamePoints();
            checkSetWon(1);
        } else if (ms.getPlayer2Points() >= 4 && ms.getPlayer2Points() - ms.getPlayer1Points() >= 2) {
            // Игрок 2 выиграл гейм
            ms.setPlayer2Games(ms.getPlayer2Games() + 1);
            resetGamePoints();
            checkSetWon(2);
        }
        // Если условия не выполнены, играем дальше в гейме.
        // При счёте 3-3 (40-40) следующим очком будет "больше", но разница в 1 очко ещё не выигрывает гейм.
    }

    private void resetGamePoints() {
        ms.setPlayer1Points(0);
        ms.setPlayer2Points(0);
    }

    private void checkSetWon(int playerNumber) {
        int opponentGames = (playerNumber == 1) ? ms.getPlayer2Games() : ms.getPlayer1Games();
        int playerGames = (playerNumber == 1) ? ms.getPlayer1Games() : ms.getPlayer2Games();

        // Проверка на победу в сете: 6-0..6-4 или 7-5
        if (playerGames >= 6 && playerGames - opponentGames >= 2) {
            // Игрок выиграл сет
            if (playerNumber == 1) ms.setPlayer1Sets(ms.getPlayer1Sets() + 1);
            else ms.setPlayer2Sets(ms.getPlayer2Sets() + 1);
            resetSetScores();
            checkMatchWon();
        }
        // Проверка на счёт 6-6, чтобы начать тай-брейк
        else if (playerGames == 6 && opponentGames == 6) {
            ms.setTieBreak(true);
        }
    }

    private void resetSetScores() {
        ms.setPlayer1Games(0);
        ms.setPlayer2Games(0);
        resetGamePoints();
        ms.setTieBreak(false);  // Тай-брейк закончен вместе с сетом
    }

    private void handleTieBreakPoint(int playerNumber) {
        // В тай-брейке очки считаются просто 1, 2, 3...
        if (playerNumber == 1) ms.setPlayer1Points(ms.getPlayer1Points() + 1);
        else ms.setPlayer2Points(ms.getPlayer2Points() + 1);

        // Проверяем, выигран ли тай-брейк
        if (ms.getPlayer1Points() >= 7 && ms.getPlayer1Points() - ms.getPlayer2Points() >= 2) {
            // Игрок 1 выиграл сет на тай-брейке
            ms.setPlayer1Sets(ms.getPlayer1Sets() + 1);
            resetSetScores(); // Сбросит и флаг isTieBreak
            checkMatchWon();
        } else if (ms.getPlayer2Points() >= 7 && ms.getPlayer2Points() - ms.getPlayer1Points() >= 2) {
            // Игрок 2 выиграл сет на тай-брейке
            ms.setPlayer2Sets(ms.getPlayer2Sets() + 1);
            resetSetScores();
            checkMatchWon();
        }
        // Если счёт 6-6, играем дальше.
    }

    private void checkMatchWon() {
        if (ms.getPlayer1Sets() == 2) {
            ms.setMatchFinished(true);
            ms.setWinner(1);
        } else if (ms.getPlayer2Sets() == 2) {
            ms.setMatchFinished(true);
            ms.setWinner(2);
        }
    }
}
