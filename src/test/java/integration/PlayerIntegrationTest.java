package integration;

import app.foot.FootApi;
import app.foot.controller.rest.Player;
import app.foot.exception.NotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.TestUtils.convertHttpResponse;

@SpringBootTest(classes = FootApi.class)
@AutoConfigureMockMvc
class PlayerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    Player player1() {
        return Player.builder()
                .id(1)
                .name("J1")
                .isGuardian(false)
                .build();
    }

    Player player2() {
        return Player.builder()
                .id(2)
                .name("J2")
                .isGuardian(false)
                .build();
    }

    Player player3() {
        return Player.builder()
                .id(3)
                .name("J3")
                .isGuardian(false)
                .build();
    }

    @Test
    void read_players_ok() throws Exception {
        MockHttpServletResponse response = mockMvc
            .perform(get("/players"))
            .andReturn()
            .getResponse();
        List<Player> actual = convertHttpResponse(List.class, response);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(9, actual.size());
        assertTrue(actual.containsAll(List.of(
            player1(),
            player2(),
            player3())));
    }

    @Test
    void create_players_ok() throws Exception {
        Player toCreate = Player.builder()
                .name("Joe Doe")
                .isGuardian(false)
                .teamName("E1")
                .build();
        MockHttpServletResponse response = mockMvc
                .perform(post("/players")
                        .content(objectMapper.writeValueAsString(List.of(toCreate)))
                    .contentType("application/json")
                    .accept("application/json"))
            .andReturn()
            .getResponse();
        List<Player> actual = convertHttpResponse(List.class, response);

        assertEquals(1, actual.size());
        assertEquals(toCreate, actual.get(0).toBuilder().id(null).build());
    }

    @Test
    void update_players_ok() throws Exception {
        Player toCreate = Player.builder()
            .id(1)
            .name("Joe Doe")
            .isGuardian(false)
            .teamName("E1")
            .build();
        MockHttpServletResponse response = mockMvc
            .perform(put("/players")
                .content(objectMapper.writeValueAsString(List.of(toCreate)))
                .contentType("application/json")
                .accept("application/json"))
            .andReturn()
            .getResponse();
        List<Player> actual = convertHttpResponse(List.class, response);

        assertEquals(1, actual.size());
        assertEquals(toCreate, actual.get(0).toBuilder().id(null).build());
    }

    @Test
    void update_players_ko() throws Exception {
        Player toCreate = Player.builder()
            .id(100)
            .name("Joe Doe")
            .isGuardian(false)
            .teamName("E1")
            .build();
        mockMvc
            .perform(put("/players")
                .content(objectMapper.writeValueAsString(List.of(toCreate)))
                .contentType("application/json")
                .accept("application/json"))
            .andExpect(status().isNotFound())
            .andExpect(result ->
                assertTrue(result.getResolvedException() instanceof NotFoundException))
            .andExpect(result ->
                assertEquals("Player.100 not found.",
                    result.getResolvedException().getMessage()));

    }

}
