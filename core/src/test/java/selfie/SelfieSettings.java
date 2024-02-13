package selfie;

import com.diffplug.selfie.Camera;
import com.diffplug.selfie.Selfie;
import com.diffplug.selfie.Snapshot;
import com.diffplug.selfie.junit5.SelfieSettingsAPI;
import com.pdemuinck.domain.Athlete;
import java.util.List;
import java.util.stream.Collectors;

public class SelfieSettings extends SelfieSettingsAPI {
  private static final Camera<List<Athlete>> CAMERA_ATHLETES = athletes ->
    Snapshot.of(String.join("\n", athletes.stream().map(Athlete::getLastName).collect(Collectors.toList())));

  public static Selfie.DiskSelfie expectSelfie(List<Athlete> athletes){
    return Selfie.expectSelfie(athletes, CAMERA_ATHLETES);
  }
}
