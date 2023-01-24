import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.validator.GoalValidator;
import app.foot.model.exception.BadRequestException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static utils.TestUtils.PLAYER1_ID;
import static utils.TestUtils.PLAYER7_ID;

//TODO-1: complete these tests
public class GoalValidatorTest {
    GoalValidator subject = new GoalValidator();

    @Test
    void accept_ok() {
        assertDoesNotThrow(() -> subject.accept(validPlayerScorer()));
    }

    //Mandatory attributes not provided : scoreTime
    @Test
    void accept_ko() {
        assertThrows(NullPointerException.class,
            () -> subject.accept(playerScorerWithNullScoreTime()));
    }

    @Test
    void when_guardian_throws_exception() {
        assertThrows(BadRequestException.class, () -> subject.accept(goalKeeper()));
    }

    @Test
    void when_score_time_greater_than_90_throws_exception() {
        assertThrows(BadRequestException.class,
            () -> subject.accept(playerScorerWithGreaterScoreTime()));
    }

    @Test
    void when_score_time_less_than_0_throws_exception() {
        assertThrows(BadRequestException.class,
            () -> subject.accept(playerScorerWithLessScoreTime()));
    }

    PlayerScorer validPlayerScorer() {
        return PlayerScorer.builder()
            .player(player())
            .isOG(false)
            .scoreTime(82)
            .build();
    }

    PlayerScorer playerScorerWithNullScoreTime() {
        return PlayerScorer.builder()
            .player(player())
            .scoreTime(null)
            .isOG(true)
            .build();
    }

    PlayerScorer playerScorerWithGreaterScoreTime() {
        return PlayerScorer.builder()
            .player(player())
            .scoreTime(100)
            .isOG(true)
            .build();
    }

    PlayerScorer playerScorerWithLessScoreTime() {
        return PlayerScorer.builder()
            .player(player())
            .scoreTime(-1)
            .isOG(true)
            .build();
    }

    PlayerScorer goalKeeper() {
        return PlayerScorer.builder()
            .player(Player.builder()
                .id(PLAYER7_ID)
                .name("G1")
                .isGuardian(true)
                .build())
            .isOG(false)
            .scoreTime(82)
            .build();
    }

    Player player(){
        return Player.builder()
            .id(PLAYER1_ID)
            .name("J1")
            .isGuardian(false)
            .build();
    }
}
