package com.pdemuinck.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;

class PlayerTest {

  @Test
  public void player_gets_points_when_picks_contain_winner(){
    Athlete winner = new Athlete("1", "Djokovic", "Novak");
    List<Athlete> picks = List.of(
        winner,
        new Athlete("2", "Nadal", "Rafa"),
        new Athlete("3", "Goffin", "David"));

    Player player = new Player("PJ", picks);
    Game game = new Game(winner, 1);
    assertThat(player.calculateScore(List.of(game), Integer::sum)).isEqualTo(1);
  }

  @Test
  public void player_gets_no_points_when_picks_dont_contain_winner(){
    Athlete winner = new Athlete("1", "Djokovic", "Novak");
    List<Athlete> picks = List.of(
        new Athlete("2", "Nadal", "Rafa"),
        new Athlete("3", "Goffin", "David"));

    Player player = new Player("PJ", picks);
    Game game = new Game(winner, 1);
    assertThat(player.calculateScore(List.of(game), Integer::sum)).isEqualTo(0);
  }

  @Test
  public void invalidates_picks_when_not_enough_players(){
    List<Athlete> picks = List.of(
        new Athlete("2", "Nadal", "Rafa", 1, "imageUrl", "MS"),
        new Athlete("3", "Goffin", "David", 123, "imageUrl", "MS"));

    Player player = new Player("PJ", picks);
    assertThrows(RuntimeException.class, player::validatePicks);
  }
}
