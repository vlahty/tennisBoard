package models.secondary;

import lombok.*;
import models.Player;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MatchDTO {
    private UUID id;
    private Player player1;
    private Player player2;
    private Player winner;
    private Score score;
}
