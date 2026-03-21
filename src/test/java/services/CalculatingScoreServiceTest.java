package services;

import models.additional.MatchScore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

class CalculatingScoreServiceTest {

    private MatchScore matchScore;
    private CalculatingScoreService service;

    @BeforeEach
    void setUp() {
        matchScore = MatchScore.builder()
                .player1Name("Игрок 1")
                .player2Name("Игрок 2")
                .build();

        service = new CalculatingScoreService(matchScore);
    }

    @Nested
    @DisplayName("Тесты отображения счёта")
    class ScoreDisplayTest {

        @Test
        @DisplayName("При счёте 0:0 отображается 0:0")
        void testScoreDisplayAtStart() {
            Map<String, Object> display = service.getScoreDisplay();

            assertThat(display.get("player1Points")).isEqualTo("0");
            assertThat(display.get("player2Points")).isEqualTo("0");
            assertThat(display.get("player1Games")).isEqualTo(0);
            assertThat(display.get("player2Games")).isEqualTo(0);
            assertThat(display.get("player1Sets")).isEqualTo(0);
            assertThat(display.get("player2Sets")).isEqualTo(0);
            assertThat(display.get("isTieBreak")).isEqualTo(false);
        }

        @Test
        @DisplayName("При счёте 15:0 отображается корректно")
        void testScoreDisplayAt15_0() {
            matchScore.setPlayer1Points(1);

            Map<String, Object> display = service.getScoreDisplay();

            assertThat(display.get("player1Points")).isEqualTo("15");
            assertThat(display.get("player2Points")).isEqualTo("0");
        }

        @Test
        @DisplayName("При счёте 30:30 отображается корректно")
        void testScoreDisplayAt30_30() {
            matchScore.setPlayer1Points(2);
            matchScore.setPlayer2Points(2);

            Map<String, Object> display = service.getScoreDisplay();

            assertThat(display.get("player1Points")).isEqualTo("30");
            assertThat(display.get("player2Points")).isEqualTo("30");
        }
    }

    @Nested
    @DisplayName("Тесты обычного гейма")
    class RegularGameTest {

        @Test
        @DisplayName("При счёте 15:0 после выигрыша очка игроком 1 становится 30:0")
        void testPointFrom15_0To30_0() {
            matchScore.setPlayer1Points(1);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Points()).isEqualTo(2);
            assertThat(matchScore.getPlayer2Points()).isEqualTo(0);
            assertThat(matchScore.getPlayer1Games()).isEqualTo(0);
        }

        @Test
        @DisplayName("При счёте 40:0 после выигрыша очка игрок 1 выигрывает гейм")
        void testGameWonAt40_0() {
            matchScore.setPlayer1Points(3);
            matchScore.setPlayer2Points(0);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Points()).isEqualTo(0);
            assertThat(matchScore.getPlayer2Points()).isEqualTo(0);
            assertThat(matchScore.getPlayer1Games()).isEqualTo(1);
        }

        @Test
        @DisplayName("При счёте 40:15 после выигрыша очка игрок 1 выигрывает гейм")
        void testGameWonAt40_15() {
            matchScore.setPlayer1Points(3);
            matchScore.setPlayer2Points(1);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Games()).isEqualTo(1);
            assertThat(matchScore.getPlayer1Points()).isEqualTo(0);
        }

        @Test
        @DisplayName("При счёте 40:40 после выигрыша очка игроком 1 гейм не заканчивается (появляется преимущество)")
        void testAdvantageAt40_40() {
            matchScore.setPlayer1Points(3);
            matchScore.setPlayer2Points(3);

            service.pointWon(1);

            // Счёт не сбросился, появилось преимущество
            assertThat(matchScore.getPlayer1Points()).isEqualTo(4);
            assertThat(matchScore.getPlayer2Points()).isEqualTo(3);
            assertThat(matchScore.getPlayer1Games()).isEqualTo(0);
        }

        @Test
        @DisplayName("При счёте AD (4:3) после выигрыша очка игроком 1 он выигрывает гейм")
        void testGameWonFromAdvantage() {
            matchScore.setPlayer1Points(4);
            matchScore.setPlayer2Points(3);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Games()).isEqualTo(1);
            assertThat(matchScore.getPlayer1Points()).isEqualTo(0);
            assertThat(matchScore.getPlayer2Points()).isEqualTo(0);
        }

        @Test
        @DisplayName("При счёте AD (4:3) после выигрыша очка игроком 2 счёт становится 40:40")
        void testBackToDeuceFromAdvantage() {
            matchScore.setPlayer1Points(4);
            matchScore.setPlayer2Points(3);

            service.pointWon(2);

            assertThat(matchScore.getPlayer1Points()).isEqualTo(4);
            assertThat(matchScore.getPlayer2Points()).isEqualTo(4);
            assertThat(matchScore.getPlayer1Games()).isEqualTo(0);
        }

        @Test
        @DisplayName("После выигрыша гейма счёт геймов увеличивается")
        void testGamesIncrement() {
            matchScore.setPlayer1Games(2);
            matchScore.setPlayer2Games(1);
            matchScore.setPlayer1Points(3);
            matchScore.setPlayer2Points(0);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Games()).isEqualTo(3);
            assertThat(matchScore.getPlayer2Games()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Тесты сетов и тай-брейка")
    class SetAndTieBreakTest {

        @Test
        @DisplayName("При счёте 5:5 и выигрыше гейма игроком 1 становится 6:5")
        void testGameAt5_5() {
            matchScore.setPlayer1Games(5);
            matchScore.setPlayer2Games(5);
            matchScore.setPlayer1Points(3);
            matchScore.setPlayer2Points(0);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Games()).isEqualTo(6);
            assertThat(matchScore.getPlayer2Games()).isEqualTo(5);
            assertThat(matchScore.getPlayer1Sets()).isEqualTo(0);
        }

        @Test
        @DisplayName("При счёте 6:5 и выигрыше гейма игроком 1 он выигрывает сет 7:5")
        void testSetWonAt7_5() {
            matchScore.setPlayer1Games(6);
            matchScore.setPlayer2Games(5);
            matchScore.setPlayer1Points(3);
            matchScore.setPlayer2Points(0);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Sets()).isEqualTo(1);
            assertThat(matchScore.getPlayer1Games()).isEqualTo(0);
            assertThat(matchScore.getPlayer2Games()).isEqualTo(0);
            assertThat(matchScore.isTieBreak()).isFalse();
        }

        @Test
        @DisplayName("При счёте 6:6 и выигрыше гейма обычным путём тай-брейк не начинается")
        void testNoTieBreakBefore6_6() {
            matchScore.setPlayer1Games(5);
            matchScore.setPlayer2Games(5);
            matchScore.setPlayer1Points(3);
            matchScore.setPlayer2Points(0);

            service.pointWon(1);

            assertThat(matchScore.isTieBreak()).isFalse();
        }

        @Test
        @DisplayName("При счёте 6:6 в сете начинается тай-брейк")
        void testTieBreakStartsAt6_6Correctly() {
            matchScore.setPlayer1Games(6);
            matchScore.setPlayer2Games(6);
            matchScore.setTieBreak(true);

            assertThat(matchScore.isTieBreak()).isTrue();
        }
    }

    @Nested
    @DisplayName("Тесты тай-брейка")
    class TieBreakTest {

        @BeforeEach
        void setupTieBreak() {
            matchScore.setPlayer1Games(6);
            matchScore.setPlayer2Games(6);
            matchScore.setTieBreak(true);
            matchScore.setPlayer1Points(0);
            matchScore.setPlayer2Points(0);
        }

        @Test
        @DisplayName("При счёте 6:6 в тай-брейке игра продолжается")
        void testTieBreakAt6_6() {
            matchScore.setPlayer1Points(6);
            matchScore.setPlayer2Points(6);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Points()).isEqualTo(7);
            assertThat(matchScore.getPlayer2Points()).isEqualTo(6);
            assertThat(matchScore.getPlayer1Sets()).isEqualTo(0);
        }

        @Test
        @DisplayName("При счёте 7:5 в тай-брейке игрок выигрывает сет")
        void testTieBreakWonAt7_5() {
            matchScore.setPlayer1Points(6);
            matchScore.setPlayer2Points(5);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Sets()).isEqualTo(1);
            assertThat(matchScore.getPlayer1Games()).isEqualTo(0);
            assertThat(matchScore.getPlayer2Games()).isEqualTo(0);
            assertThat(matchScore.isTieBreak()).isFalse();
        }

        @Test
        @DisplayName("При счёте 8:6 в тай-брейке игрок выигрывает сет")
        void testTieBreakWonAt8_6() {
            matchScore.setPlayer1Points(7);
            matchScore.setPlayer2Points(6);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Sets()).isEqualTo(1);
            assertThat(matchScore.isTieBreak()).isFalse();
        }

        @Test
        @DisplayName("После выигрыша сета в тай-брейке счёт сбрасывается")
        void testScoresResetAfterSetWin() {
            matchScore.setPlayer1Points(6);
            matchScore.setPlayer2Points(4);
            matchScore.setPlayer1Games(6);
            matchScore.setPlayer2Games(5);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Games()).isEqualTo(0);
            assertThat(matchScore.getPlayer2Games()).isEqualTo(0);
            assertThat(matchScore.getPlayer1Points()).isEqualTo(0);
            assertThat(matchScore.getPlayer2Points()).isEqualTo(0);
        }
    }

    @Nested
    @DisplayName("Тесты матча")
    class MatchTest {

        @Test
        @DisplayName("При счёте 1:0 по сетам и выигрыше следующего сета игрок побеждает в матче")
        void testMatchWonAt2Sets() {
            matchScore.setPlayer1Sets(1);
            matchScore.setPlayer2Sets(0);
            matchScore.setPlayer1Games(5);
            matchScore.setPlayer2Games(4);
            matchScore.setPlayer1Points(3);
            matchScore.setPlayer2Points(0);

            service.pointWon(1);

            assertThat(matchScore.isMatchFinished()).isTrue();
            assertThat(matchScore.getWinner()).isEqualTo(1);
        }

        @Test
        @DisplayName("При счёте 1:1 по сетам третий сет решает исход матча")
        void testDecidingSet() {
            matchScore.setPlayer1Sets(1);
            matchScore.setPlayer2Sets(1);
            matchScore.setPlayer1Games(5);
            matchScore.setPlayer2Games(4);
            matchScore.setPlayer1Points(3);
            matchScore.setPlayer2Points(0);

            service.pointWon(1);

            assertThat(matchScore.getPlayer1Sets()).isEqualTo(2);
            assertThat(matchScore.isMatchFinished()).isTrue();
            assertThat(matchScore.getWinner()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("Интеграционные тесты сценариев матча")
    class MatchScenarioTest {

        @Test
        @DisplayName("Сценарий: стандартный гейм до победы")
        void testStandardGame() {
            // 0:0 -> 15:0
            service.pointWon(1);
            assertThat(service.convertPointsToScore(matchScore.getPlayer1Points(), matchScore.getPlayer2Points())).isEqualTo("15");

            // 15:0 -> 30:0
            service.pointWon(1);
            assertThat(service.convertPointsToScore(matchScore.getPlayer1Points(), matchScore.getPlayer2Points())).isEqualTo("30");

            // 30:0 -> 40:0
            service.pointWon(1);
            assertThat(service.convertPointsToScore(matchScore.getPlayer1Points(), matchScore.getPlayer2Points())).isEqualTo("40");

            // 40:0 -> победа в гейме
            service.pointWon(1);
            assertThat(matchScore.getPlayer1Games()).isEqualTo(1);
            assertThat(matchScore.getPlayer1Points()).isEqualTo(0);
        }

        @Test
        @DisplayName("Сценарий: гейм с преимуществом")
        void testGameWithAdvantage() {
            // Доводим до 40:40
            for (int i = 0; i < 3; i++) service.pointWon(1);
            for (int i = 0; i < 3; i++) service.pointWon(2);
            assertThat(service.convertPointsToScore(matchScore.getPlayer1Points(), matchScore.getPlayer2Points())).isEqualTo("40");
            assertThat(service.convertPointsToScore(matchScore.getPlayer2Points(), matchScore.getPlayer1Points())).isEqualTo("40");

            // Преимущество игроку 1
            service.pointWon(1);
            assertThat(matchScore.getPlayer1Points()).isEqualTo(4);
            assertThat(service.convertPointsToScore(matchScore.getPlayer1Points(), matchScore.getPlayer2Points())).isEqualTo("AD");

            // Игрок 2 возвращает к 40:40
            service.pointWon(2);
            assertThat(matchScore.getPlayer1Points()).isEqualTo(4);
            assertThat(matchScore.getPlayer2Points()).isEqualTo(4);

            // Игрок 1 выигрывает гейм
            service.pointWon(1);
            service.pointWon(1);
            assertThat(matchScore.getPlayer1Games()).isEqualTo(1);
        }
    }
}