package utils;

import app.foot.repository.entity.TeamEntity;

public class TestUtils {
  public static final int PLAYER1_ID = 1;
  public static final int PLAYER7_ID = 7;
  public static final int MATCH1_ID = 1;
  public static TeamEntity teamGhana() {
    return TeamEntity.builder()
        .id(2)
        .name("Ghana")
        .build();
  }

  public static TeamEntity teamBarea() {
    return TeamEntity.builder()
        .id(1)
        .name("Barea")
        .build();
  }
}
