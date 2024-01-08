package com.pdemuinck.domain;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;

public class Player {

  private final String name;
  private final List<Athlete> picks;


  private Map<Integer, List<Athlete>> captainsPerRound;

  public Player(String name, List<Athlete> picks) {
    this(name, picks, new HashMap<>());
  }

  public Player(String name, List<Athlete> picks, Map<Integer, List<Athlete>> captains){
    this.name = name;
    this.picks = picks;
    this.captainsPerRound = captains;
  }

  public void validatePicks(){
    for(String eventId : List.of("MS", "LS")){
      for(String category : List.of("A", "B", "C", "D")){
        if(picks.stream().filter(athlete -> athlete.getEventId().equals(eventId) && athlete.getCategory().equals(category)).count() != 3){
          throw new IllegalArgumentException("Incorrect number of athletes selected");
        }
      }
    }
  }

  public String getName() {
    return name;
  }

  public List<Athlete> getPicks() {
    return picks;
  }

  public Map<Integer, List<Athlete>> getCaptainsPerRound() {
    return captainsPerRound;
  }

  public Integer calculateScore(List<Game> games, BinaryOperator<Integer> rules) {
    return games.stream().filter(game -> picks.contains(game.getWinner())).map(Game::getRound)
        .reduce(0, rules);
  }
}
