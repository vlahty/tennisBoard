package models.additional;

import lombok.*;
import java.util.UUID;

@Getter
@Setter
public class MatchScore {

    private UUID matchId;
    private int player1Sets;
    private int player2Sets;
    private int player1Points;
    private int player2Points;
    private int player1Games;
    private int player2Games;
    private boolean isTieBreak;
    private String player1Name;
    private String player2Name;
    private boolean matchFinished;
    private int winner;

    public MatchScore(UUID matchId, String player1Name, String player2Name) {
        this.matchId = matchId;
        this.player1Name = player1Name;
        this.player2Name = player2Name;
    }
}
