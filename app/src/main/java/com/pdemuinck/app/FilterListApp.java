package com.pdemuinck.app;

import com.pdemuinck.domain.Athlete;
import com.pdemuinck.gateways.out.WimbledonGateway;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

public class FilterListApp extends Application {

  @Override
  public void start(Stage primaryStage) throws Exception {
    ReadOnlyObjectProperty<ObservableList<Athlete>> playersProperty =
        new SimpleObjectProperty<>(FXCollections.observableArrayList());

    ReadOnlyObjectProperty<FilteredList<Athlete>> viewablePlayersProperty =
        new SimpleObjectProperty<FilteredList<Athlete>>(
            new FilteredList<>(playersProperty.get()
            ));

    ObjectProperty<Predicate<? super Athlete>> filterProperty =
        viewablePlayersProperty.get().predicateProperty();


    VBox vbox = new VBox();
    vbox.setSpacing(4);

    HBox hbox = new HBox();
    hbox.setSpacing( 2 );

    ToggleGroup filterTG = new ToggleGroup();

    @SuppressWarnings("unchecked")
    EventHandler<ActionEvent> toggleHandler = (event) -> {
      ToggleButton tb = (ToggleButton)event.getSource();
      Predicate<Athlete> filter = (Predicate<Athlete>)tb.getUserData();
      filterProperty.set( filter );
    };

    ToggleButton tbShowAll = new ToggleButton("Show All");
    tbShowAll.setSelected(true);
    tbShowAll.setToggleGroup( filterTG );
    tbShowAll.setOnAction(toggleHandler);
    tbShowAll.setUserData( (Predicate<Athlete>) (Athlete p) -> true);

    List<ToggleButton> tbs = Arrays.asList("A", "B", "C", "D")
        .stream()
        .distinct()
        .map( (team) -> {
          ToggleButton tb = new ToggleButton( team );
          tb.setToggleGroup( filterTG );
          tb.setOnAction( toggleHandler );
          tb.setUserData( (Predicate<Athlete>) (Athlete p) -> team.equals(p.getCategory()) );
          return tb;
        })
        .collect(Collectors.toList());

    hbox.getChildren().add( tbShowAll );
    hbox.getChildren().addAll( tbs );

    ListView<Athlete> lv = new ListView<>();
    lv.itemsProperty().bind( viewablePlayersProperty );
    lv.setCellFactory(new Callback<ListView<Athlete>, ListCell<Athlete>>() {
      @Override
      public ListCell<Athlete> call(ListView<Athlete> param) {
        return new AthleteCell();
      }
    });
    lv.setMinHeight(900.0);

    vbox.getChildren().addAll( hbox, lv );

    Scene scene = new Scene(vbox);

    primaryStage.setScene( scene );
    primaryStage.setOnShown((evt) -> {
      playersProperty.get().addAll(WimbledonGateway.parseAthletes().subList(0, 6));
    });

    primaryStage.show();

  }

  public static void main(String args[]) {
    launch(args);
  }

  static class AthleteCell extends ListCell<Athlete> {
    HBox hbox = new HBox();
    Label category = new Label("");
    Label label = new Label("(empty)");
    ImageView imageView = new ImageView();
    Pane pane = new Pane();
    Button button = new Button("(>)");
    Athlete lastItem;

    public AthleteCell() {
      super();
      hbox.getChildren().addAll(category, imageView, label, pane, button);
      HBox.setHgrow(pane, Priority.ALWAYS);
      button.setOnAction(new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent event) {
          System.out.println(lastItem + " : " + event);
        }
      });
    }

    @Override
    protected void updateItem(Athlete athlete, boolean empty) {
      super.updateItem(athlete, empty);
      setText(null);  // No text in label of super class
      if (empty) {
        lastItem = null;
        setGraphic(null);
      } else {
        category.setText(athlete.getCategory());
        lastItem = athlete;
        label.setText(athlete!=null ? athlete.toString() : "<null>");
        imageView.setImage(new Image(athlete.getImageUrl()));
        imageView.setFitHeight(100.0);
        imageView.setFitWidth(100.0);
        setGraphic(hbox);
      }
    }
  }
}
