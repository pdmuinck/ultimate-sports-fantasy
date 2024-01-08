package com.pdemuinck.gateways;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.common.io.Files;
import com.pdemuinck.domain.Athlete;
import com.pdemuinck.domain.Game;
import com.pdemuinck.gateways.out.WimbledonGateway;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.client.MockServerClient;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
class WimbledonGatewayTest {

  @Container
  private static final MockServerContainer mockServerContainer =
      new MockServerContainer(DockerImageName.parse("mockserver/mockserver:5.14.0"));


  private static MockServerClient mockServerClient;

  @BeforeAll
  public static void beforeAll() {
    mockServerClient =
        new MockServerClient(mockServerContainer.getHost(), mockServerContainer.getServerPort());
  }

  @BeforeEach
  public void setUp() {
    mockServerClient.reset();
  }

  @Test
  public void wimbledon_players_are_parsed() throws IOException {
    mockPlayersResponse();

    String hostUrl = "http://" + mockServerContainer.getHost() + ":" + mockServerClient.getPort();

    List<Athlete> athletes = WimbledonGateway.parseAthletes(hostUrl);
    assertThat(athletes).allMatch(athlete -> athlete.getLastName() != null);
    assertThat(athletes).allMatch(athlete -> athlete.getFirstName() != null);
    assertThat(athletes).allMatch(athlete -> athlete.getSystemId() != null);
    assertThat(athletes).allMatch(athlete -> athlete.getRanking() != 0);
    assertThat(athletes).allMatch(athlete -> athlete.getImageUrl() != null);
    assertThat(athletes).allMatch(athlete -> athlete.getCategory() != null);
    assertThat(athletes).allMatch(athlete -> athlete.getEventId() != null);
  }

  @Test
  public void wimbledon_games_are_parsed() throws IOException {
    mockEventDayResponse();
    mockGameResultResponse();

    String hostUrl = "http://" + mockServerContainer.getHost() + ":" + mockServerClient.getPort();

    List<Game> games = WimbledonGateway.parseGames(hostUrl);

    assertThat(games).as("We should parse two final winners (men and women)")
        .filteredOn(game -> game.getRound() == 7).hasSize(2);
    assertThat(games).as("We should parse four semifinal winners (men and women)")
        .filteredOn(game -> game.getRound() == 6).hasSize(4);
    assertThat(games).as("We should parse eight quarterfinal winners (men and women)")
        .filteredOn(game -> game.getRound() == 5).hasSize(8);
    assertThat(games).as("We should parse sixteen 4th round winners (men and women)")
        .filteredOn(game -> game.getRound() == 4).hasSize(16);
    assertThat(games).as("We should parse thirty-two 3rd round winners (men and women)")
        .filteredOn(game -> game.getRound() == 3).hasSize(32);
    assertThat(games).as("We should parse sixty-four 2nd round winners (men and women)")
        .filteredOn(game -> game.getRound() == 2).hasSize(64);
    assertThat(games).as(
            "We should parse one hundred twenty-eight 1st round winners (men and women)")
        .filteredOn(game -> game.getRound() == 1).hasSize(128);

  }

  private void mockEventDayResponse() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("wimbledon_2023_days.json").getFile());
    String wimbledonDays = String.join("\n", Files.readLines(file, Charset.defaultCharset()));

    mockServerClient
        .when(
            request()
                .withMethod("GET")
                .withPath("/en_GB/scores/feeds/2023/completed_matches/eventDays.json")
        )
        .respond(
            response()
                .withBody(wimbledonDays)
        );

  }

  private void mockPlayersResponse() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("wimbledon_2023_players.json").getFile());
    String players = String.join("\n", Files.readLines(file, Charset.defaultCharset()));

    mockServerClient
        .when(
            request()
                .withMethod("GET")
                .withPath("/en_GB/scores/feeds/2023/players/players.json")
        )
        .respond(
            response()
                .withBody(players)
        );

  }

  private void mockGameResultResponse() {
    List<Integer> days = IntStream.range(8, 22).boxed().collect(Collectors.toList());

    ClassLoader classLoader = getClass().getClassLoader();
    days.forEach(day -> {
      String fileName = String.format("wimbledon_2023_day_%d.json", day);
      File file = new File(classLoader.getResource(fileName).getFile());
      String results;
      try {
        results = String.join("\n", Files.readLines(file, Charset.defaultCharset()));
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

      mockServerClient
          .when(
              request()
                  .withMethod("GET")
                  .withPath(String.format(
                      "https://www.wimbledon.com/en_GB/scores/feeds/2023/completed_matches/days/day_%d.json",
                      day))
          )
          .respond(
              response()
                  .withBody(results)
          );
    });

  }

}
