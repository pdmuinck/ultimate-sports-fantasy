package com.pdemuinck.app;

import com.pdemuinck.domain.Athlete;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AthleteList {


  private ScrollPane scrollPane;

  public AthleteList(List<Athlete> athletes) {
    VBox vBox = new VBox();
    List<HBox> athleteBoxes = athletes.parallelStream().map(a -> {
      ImageView imageView = new ImageView(new Image(a.getImageUrl()));
      imageView.setFitWidth(75.0);
      imageView.setFitHeight(75.0);
      return new HBox(imageView, new Text(a.getFirstName()),
          new Text(a.getLastName()));
    }).toList();
    athleteBoxes.forEach(athlete -> {
      athlete.setSpacing(5.0);
      Tooltip tooltip = new Tooltip("test");
      Tooltip.install(athlete, tooltip);
    });
    vBox.getChildren().addAll(athleteBoxes);
    this.scrollPane = new ScrollPane();
    this.scrollPane.setContent(vBox);
  }

  public ScrollPane getScrollPane() {
    return scrollPane;
  }
}
