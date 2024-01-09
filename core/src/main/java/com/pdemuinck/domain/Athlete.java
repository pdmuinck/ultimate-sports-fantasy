package com.pdemuinck.domain;

import java.util.Objects;

public class Athlete {

  private String systemId;
  private String lastName;
  private String firstName;
  private String imageUrl;
  private int ranking;
  private String category;
  private String eventId;

  public Athlete(String systemId, String lastName, String firstName, int ranking, String imageUrl, String eventId) {
    this.systemId = systemId;
    this.lastName = lastName;
    this.firstName = firstName;
    this.ranking = ranking;
    this.imageUrl = imageUrl;
    this.eventId = eventId;
    if(ranking <= 7){
      this.category = "A";
    } else if(ranking <= 15){
      this.category = "B";
    } else if(ranking <= 32){
      this.category = "C";
    } else {
      this.category = "D";
    }
  }

  public Athlete(String systemId, String lastName, String firstName){
    this(systemId, lastName, firstName, 0, null, null);
  }

  public String getSystemId() {
    return systemId;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFirstName() {
    return firstName;
  }

  public int getRanking() {
    return ranking;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public String getCategory() {
    return category;
  }

  public String getEventId() {
    return eventId;
  }

    @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Athlete athlete = (Athlete) o;
    return Objects.equals(systemId, athlete.systemId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(systemId);
  }

  @Override
  public String toString() {
    return "Athlete{" +
        "lastName='" + lastName + '\'' +
        ", firstName='" + firstName + '\'' +
        '}';
  }
}
