package com.pdemuinck.app;

import com.pdemuinck.domain.Athlete;
import com.pdemuinck.gateways.out.WimbledonGateway;
import java.util.List;
import java.util.Random;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class HelloController {
    List<Athlete> athletes = WimbledonGateway.parseAthletes("https://www.wimbledon.com");
    Random rand = new Random();
    int upperBound = athletes.size() - 1;

    @FXML
    private Label welcomeText;
    @FXML
    private ImageView myImageView;


    @FXML
    protected void onHelloButtonClick() {
        int i = rand.nextInt(upperBound);
        Image image = new Image(athletes.get(i).getImageUrl());
        myImageView.setImage(image);
        welcomeText.setText(String.format("Hello from athlete %s %s", athletes.get(i).getFirstName(), athletes.get(i).getLastName()));
    }

}
