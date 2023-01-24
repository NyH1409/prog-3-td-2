import app.foot.model.Player;
import app.foot.model.PlayerScorer;
import app.foot.repository.MatchRepository;
import app.foot.repository.PlayerRepository;
import app.foot.repository.entity.MatchEntity;
import app.foot.repository.entity.PlayerEntity;
import app.foot.repository.entity.PlayerScoreEntity;
import app.foot.repository.mapper.PlayerMapper;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static utils.TestUtils.MATCH1_ID;
import static utils.TestUtils.PLAYER1_ID;
import static utils.TestUtils.teamBarea;
import static utils.TestUtils.teamGhana;

//TODO-2: complete these tests
public class PlayerMapperTest {
    PlayerMapper subject;
    MatchRepository matchRepository;
    PlayerRepository playerRepository;

    @BeforeEach
    void setUp(){
        matchRepository = mock(MatchRepository.class);
        playerRepository = mock(PlayerRepository.class);
        subject = new PlayerMapper(matchRepository, playerRepository);

        when(playerRepository.findById(any(Integer.class))).thenReturn(
            Optional.ofNullable(playerToMap()));
        when(matchRepository.findById(any(Integer.class))).thenReturn(
            Optional.ofNullable(matchEntity()));
    }

    @Test
    void player_to_domain_ok() {
        Player actual = subject.toDomain(playerToMap());

        assertEquals(expectedDomainPlayer(), actual);
    }

    @Test
    void player_scorer_to_domain_ok() {
        PlayerScorer actual = subject.toDomain(playerScoreEntity());

        assertEquals(domainPlayerScorer(), actual);
    }

    @Test
    void player_scorer_to_entity_ok() {
        PlayerScoreEntity actual = subject.toEntity(MATCH1_ID, domainPlayerScorer());
        actual.setId(playerScoreEntity().getId());
        actual.getMatch().setDatetime(playerScoreEntity().getMatch().getDatetime());

        assertEquals(playerScoreEntity(), actual);
    }

    PlayerEntity playerToMap() {
        return PlayerEntity.builder()
            .id(PLAYER1_ID)
            .name("J1")
            .team(teamBarea())
            .guardian(false)
            .build();
    }

    Player expectedDomainPlayer() {
        return Player.builder()
            .id(playerToMap().getId())
            .name(playerToMap().getName())
            .isGuardian(playerToMap().isGuardian())
            .teamName(playerToMap().getTeam().getName())
            .build();
    }

    MatchEntity matchEntity(){
        return MatchEntity.builder()
            .id(MATCH1_ID)
            .scorers(List.of())
            .teamA(teamBarea())
            .teamB(teamGhana())
            .datetime(Instant.parse("2022-08-26T06:33:50.595Z"))
            .stadium("MAHAMASINA")
            .build();
    }

    PlayerScoreEntity playerScoreEntity(){
        return PlayerScoreEntity.builder()
            .id(2)
            .player(playerToMap())
            .match(matchEntity())
            .ownGoal(false)
            .minute(70)
            .build();
    }

    PlayerScorer domainPlayerScorer(){
        return PlayerScorer.builder()
            .player(expectedDomainPlayer())
            .isOwnGoal(playerScoreEntity().isOwnGoal())
            .minute(playerScoreEntity().getMinute())
            .build();
    }

}
