package com.pdemuinck.domain;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;

public class TotoPool {
  private List<Player> players;
  private List<Game> games;
  private BinaryOperator<Integer> rules;

  public TotoPool(List<Player> players, List<Game> games, BinaryOperator<Integer> rules) {
    this.players = players;
    this.games = games;
    this.rules = rules;
  }

  public List<Player> rankByScore() {
    return players.stream().sorted((p1, p2) -> p2.calculateScore(games, rules)
        .compareTo(p1.calculateScore(games, rules))).collect(Collectors.toList());
  }

  public double averageScore() {
    return players.stream().map(player -> player.calculateScore(games, rules))
        .mapToDouble(x -> (double) x).average().getAsDouble();
  }

  public double highestScore(){
    return players.stream().map(player -> player.calculateScore(games, rules))
        .mapToDouble(x -> (double) x).max().getAsDouble();
  }
}
