package models;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
@Table(name = "matches")
public class Match {
    @Id
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player1")
    private Player player1;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "player2")
    private Player player2;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "winner")
    private Player winner;
}