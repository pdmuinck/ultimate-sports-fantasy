package com.pdemuinck.app;

import com.pdemuinck.domain.Athlete;
import com.pdemuinck.gateways.out.WimbledonGateway;
import java.util.List;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

public class SelectAthletes extends Application {

  @Override
  public void start(Stage stage) {
    List<Athlete> athletes = WimbledonGateway.parseAthletes();
    List<Button> categoryButtons = Stream.of("A", "B", "C", "D").map(Button::new).toList();

    HBox hBox = new HBox();
    hBox.getChildren().addAll(categoryButtons);
    Scene startScene = new Scene(hBox);
    stage.setScene(startScene);

    EventHandler<ActionEvent> filterAthleteHandler = event -> {
      categoryButtons.forEach(button -> button.setBackground(Background.EMPTY));
      Button btn = ((Button) event.getSource());
      btn.setBackground(Background.fill(Paint.valueOf("yellow")));
      VBox vBox = new VBox();
      vBox.getChildren().addAll(hBox);
      vBox.getChildren().addAll(new AthleteList(athletes.stream()
          .filter(a -> a.getCategory().equals(((Button) event.getSource()).getText()))
          .toList()).getScrollPane());
      stage.setScene(new Scene(vBox));
    };

    categoryButtons.forEach(btn -> {
      btn.setOnAction(filterAthleteHandler);
      btn.setBackground(Background.EMPTY);
    });

    stage.show();
  }
}

