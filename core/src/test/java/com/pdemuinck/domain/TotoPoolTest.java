package com.pdemuinck.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.function.BinaryOperator;
import org.junit.jupiter.api.Test;

class TotoPoolTest {

  @Test
  public void players_can_be_ranked_by_score() {
    // Given
    Athlete winner = new Athlete("1", "Djokovic", "Novak");
    Athlete loser = new Athlete("2", "Nadal", "Rafa");

    Player player1 = new Player("player1", List.of(loser));
    Player player2 = new Player("player2", List.of(winner));

    Game game = new Game(winner, 1);
    List<Game> games = List.of(game);
    BinaryOperator<Integer> rules = Integer::sum;
    TotoPool totoPool = new TotoPool(List.of(player1, player2), List.of(game), rules);

    // When
    List<Player> result = totoPool.rankByScore();

    // Then
    assertThat(result.get(0).getName()).as("Player2 should have highest score").isEqualTo("player2");
    assertThat(result.get(1).getName()).as("Player1 should have lowest score").isEqualTo("player1");
  }

  @Test
  public void find_highest_score_in_pool() {
    // Given
    Athlete winner = new Athlete("1", "Djokovic", "Novak");
    Athlete loser = new Athlete("2", "Nadal", "Rafa");

    Player player1 = new Player("player1", List.of(loser));
    Player player2 = new Player("player2", List.of(winner));

    Game game = new Game(winner, 1);
    BinaryOperator<Integer> rules = Integer::sum;
    TotoPool totoPool = new TotoPool(List.of(player1, player2), List.of(game), rules);

    // When
    double highestScore = totoPool.highestScore();

    // Then
    assertThat(highestScore).isEqualTo(1.0);
  }

  @Test
  public void find_average_score_in_pool() {
    // Given
    Athlete winner = new Athlete("1", "Djokovic", "Novak");
    Athlete loser = new Athlete("2", "Nadal", "Rafa");

    Player player1 = new Player("player1", List.of(loser));
    Player player2 = new Player("player2", List.of(winner));

    Game game = new Game(winner, 1);
    BinaryOperator<Integer> rules = Integer::sum;
    TotoPool totoPool = new TotoPool(List.of(player1, player2), List.of(game), rules);

    // When
    double averageScore = totoPool.averageScore();

    // Then
    assertThat(averageScore).isEqualTo(0.5);
  }

}
