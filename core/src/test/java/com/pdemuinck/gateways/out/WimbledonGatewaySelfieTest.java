package com.pdemuinck.gateways.out;


import com.pdemuinck.domain.Athlete;
import java.util.List;
import org.junit.jupiter.api.Test;

import static selfie.SelfieSettings.expectSelfie;


class WimbledonGatewaySelfieTest {
  @Test
  public void fetch_athletes() {
    List<Athlete> athletes = WimbledonGateway.parseAthletes();
    expectSelfie(athletes).toMatchDisk("players");
  }

}
