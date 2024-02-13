package com.pdemuinck.app;

import com.pdemuinck.domain.Athlete;
import java.util.List;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class AthleteList {

  private List<Athlete> athletes;
  private ScrollPane scrollPane;


  public AthleteList(List<Athlete> athletes) {
    VBox vBox = new VBox();
    vBox.getChildren().addAll(athletes.stream().map(a -> new Text(a.getLastName())).toList());
    this.scrollPane = new ScrollPane();
    this.scrollPane.setContent(vBox);
  }

  public ScrollPane getScrollPane() {
    return scrollPane;
  }
}
