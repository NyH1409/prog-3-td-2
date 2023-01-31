package integration;

import app.foot.FootApi;
import app.foot.controller.rest.Match;
import app.foot.controller.rest.Player;
import app.foot.controller.rest.PlayerScorer;
import app.foot.controller.rest.Team;
import app.foot.controller.rest.TeamMatch;
import app.foot.exception.BadRequestException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtils.MATCH3_ID;
import static utils.TestUtils.convertHttpResponse;
import static utils.TestUtils.goalKeeper;
import static utils.TestUtils.scorer1;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
class MatchIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    private static Player player6() {
        return Player.builder()
            .id(6)
            .name("J6")
            .isGuardian(false)
            .teamName("E3")
            .build();
    }

    private static Player player3() {
        return Player.builder()
            .id(3)
            .name("J3")
            .isGuardian(false)
            .teamName("E2")
            .build();
    }

    private static Match expectedMatch2() {
        return Match.builder()
            .id(2)
            .teamA(teamMatchA())
            .teamB(teamMatchB())
            .stadium("S2")
            .datetime(Instant.parse("2023-01-01T14:00:00Z"))
            .build();
    }

    private static TeamMatch teamMatchB() {
        return TeamMatch.builder()
                .team(team3())
                .score(0)
                .scorers(List.of())
                .build();
    }

    private static TeamMatch teamMatchA() {
        return TeamMatch.builder()
            .team(team2())
            .score(2)
            .scorers(List.of(PlayerScorer.builder()
                    .player(player3())
                    .scoreTime(70)
                    .isOG(false)
                    .build(),
                PlayerScorer.builder()
                    .player(player6())
                    .scoreTime(80)
                    .isOG(true)
                    .build()))
            .build();
    }

    private static Team team3() {
        return Team.builder()
            .id(3)
            .name("E3")
            .build();
    }

    private static Team team2() {
        return Team.builder()
            .id(2)
            .name("E2")
            .build();
    }

    private static Team team1() {
        return Team.builder()
            .id(1)
            .name("E1")
            .build();
    }

    @Test
    void read_matches_ok() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get("/matches"))
            .andReturn()
            .getResponse();

        List<Match> actual = convertHttpResponse(List.class, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(3, actual.size());
    }

    @Test
    void add_goals_ok() throws Exception {
        Match expected = Match.builder()
            .id(MATCH3_ID)
            .teamA(TeamMatch.builder()
                .team(team1())
                .scorers(List.of(scorer1().toBuilder()
                    .player(Player.builder()
                        .id(1)
                        .name("J1")
                        .isGuardian(false)
                        .teamName("E1")
                        .build()).build()))
                .build())
            .teamB(TeamMatch.builder()
                .team(team3())
                .score(0)
                .scorers(List.of())
                .build())
            .datetime(Instant.parse("2023-01-01T18:00:00Z"))
            .stadium("S3")
            .build();

        MockHttpServletResponse response = mockMvc
            .perform(post("/matches/{matchId}/goals", MATCH3_ID)
                .content(objectMapper.writeValueAsString(List.of(scorer1())))
                .contentType("application/json"))
            .andReturn()
            .getResponse();

        Match actual = convertHttpResponse(Match.class, response);
        expected.getTeamA().setScore(actual.getTeamA().getScore());
        expected.getTeamB().setScore(actual.getTeamB().getScore());
        expected.getTeamA().setScorers(actual.getTeamA().getScorers());
        expected.getTeamB().setScorers(actual.getTeamB().getScorers());

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expected, actual);
    }

    @Test
    void add_goals_ko() throws Exception {
        mockMvc
            .perform(post("/matches/{matchId}/goals", MATCH3_ID)
                .content(objectMapper.writeValueAsString(List.of(goalKeeper())))
                .contentType("application/json"))
            .andExpect(status().isBadRequest())
            .andExpect(
                result -> assertTrue(result.getResolvedException() instanceof BadRequestException));
    }

    @Test
    void read_match_by_id_ok() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(get("/matches/2"))
            .andExpect(status().isOk())
            .andReturn()
            .getResponse();
        Match actual = convertHttpResponse(Match.class, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(expectedMatch2(), actual);
    }
}
