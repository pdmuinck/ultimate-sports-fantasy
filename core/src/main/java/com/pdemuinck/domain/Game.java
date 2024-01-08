package com.pdemuinck.domain;

public class Game {
  private Athlete winner;
  private int round;

  public Game(Athlete winner, int round) {
    this.winner = winner;
    this.round = round;
  }

  public Athlete getWinner() {
    return winner;
  }

  public int getRound() {
    return round;
  }
}
