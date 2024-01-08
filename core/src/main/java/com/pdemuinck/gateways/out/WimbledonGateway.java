package com.pdemuinck.gateways.out;

import static java.net.http.HttpClient.newHttpClient;

import com.pdemuinck.domain.Athlete;
import com.pdemuinck.domain.Game;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.JSONArray;
import org.json.JSONObject;

public class WimbledonGateway {

  public static List<Athlete> parseAthletes(String hostUrl) {
    HttpClient httpClient = newHttpClient();
    URI uri = URI.create(String.format("%s/en_GB/scores/feeds/2023/players/players.json", hostUrl));
    HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
    try {
      List<Athlete> athletes = new ArrayList<>();
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      JSONArray players = new JSONObject(response.body()).getJSONArray("players");
      for (int i = 0; i < players.length(); i++) {
        JSONObject player = players.getJSONObject(i);
        JSONArray eventsEntered = player.getJSONArray("events_entered");
        if (eventsEntered.isEmpty()) {
          continue;
        }
        for (int j = 0; j < eventsEntered.length(); j++) {
          String eventId = eventsEntered.getJSONObject(j).getString("event_id");
          if (eventId.equals("LS") || eventId.equals("MS")) {
            if (player.getString("id").contains("wta") || player.getString("id").contains("atp")) {
              athletes.add(new Athlete(player.getString("id"), player.getString("last_name"),
                  player.getString("first_name"),
                  Integer.parseInt(player.getString("singles_rank")),
                  "https://images.wimbledon.com/square/" + player.getString("id") + ".jpg", eventId));
            }
          }
        }
      }
      return athletes;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

  }


  public static List<Game> parseGames(String hostUrl) {
    List<String> eventUrls = parseEventDays(hostUrl);

    return eventUrls.stream().map(url -> parseGameResults(url)).flatMap(List::stream)
        .collect(Collectors.toList());
  }

  private static List<Game> parseGameResults(String eventUrl) {
    HttpClient httpClient = newHttpClient();
    URI uri = URI.create(eventUrl);
    HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      JSONObject jsonObject = new JSONObject(response.body());
      JSONArray matches = jsonObject.getJSONArray("matches");
      List<Game> games = new ArrayList<>();
      for (int i = 0; i < matches.length(); i++) {
        JSONObject match = matches.getJSONObject(i);
        String eventCode = match.getString("eventCode");
        if (eventCode.equals("LS") || eventCode.equals("MS")) {
          String roundCode = match.getString("roundCode");
          int round = switch (roundCode) {
            case "F" -> 7;
            case "S" -> 6;
            case "Q" -> 5;
            default -> Integer.parseInt(roundCode);
          };
          Athlete winner =
              Stream.of(match.getJSONObject("team1"), match.getJSONObject("team2"))
                  .filter(p -> p.getBoolean("won")).map(
                      p -> new Athlete(p.getString("idA"), p.getString("lastNameA"),
                          p.getString("firstNameA"))).findFirst()
                  .orElseThrow(() -> new RuntimeException("No winner found!!!"));

          games.add(new Game(winner, round));
        }
      }
      return games;

    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static List<String> parseEventDays(String hostUrl) {
    HttpClient httpClient = newHttpClient();
    URI uri = URI.create(
        String.format("%s/en_GB/scores/feeds/2023/completed_matches/eventDays.json", hostUrl));
    HttpRequest request = HttpRequest.newBuilder().uri(uri).build();
    try {
      HttpResponse<String> response =
          httpClient.send(request, HttpResponse.BodyHandlers.ofString());
      JSONObject jsonObject = new JSONObject(response.body());
      JSONArray eventDays = jsonObject.getJSONArray("eventDays");
      List<String> eventDayLinks = new ArrayList<>();
      for (int i = 0; i < eventDays.length(); i++) {
        JSONObject eventDay = eventDays.getJSONObject(i);
        List<Object> events = eventDay.getJSONArray("events").toList();
        boolean hasMatches = events.stream().map(Object::toString)
            .anyMatch(event -> event.equals("MS") || event.equals("LS"));
        if (hasMatches) {
          eventDayLinks.add(eventDay.getString("url"));
        }
      }
      return eventDayLinks;
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}
