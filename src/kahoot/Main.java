package kahoot;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application {

    String[] descriptions = {"first question", "second question", "third question"};
    private int ind = 0;

    public Button kahootButton(String btnText, String btnColor) {
        Font font = Font.font("Times New Roman", FontWeight.BOLD,
                FontPosture.ITALIC, 18);
        Button btn = new Button(btnText);
        btn.setMinWidth(200);
        btn.setMinHeight(100);
        btn.setStyle("-fx-background-color: " + btnColor);
        btn.setTextFill(Color.WHITE);
        btn.setFont(font);
        return btn;
    }

    public BorderPane currentQuestion() {

        Label txtLabel = new Label(descriptions[ind]);
        Font font = Font.font("Times New Roman", FontWeight.BOLD,
                FontPosture.ITALIC, 18);
        txtLabel.setFont(font);
        txtLabel.setMinWidth(500);
        txtLabel.setAlignment(Pos.CENTER);

        Button redBtn = kahootButton("RED!", "red");
        redBtn.setOnAction(event -> {
            txtLabel.setText(descriptions[--ind]);
        });
        Button orangeBtn = kahootButton("ORANGE!", "orange");
        orangeBtn.setOnAction(event -> {
            System.out.println("ORANGE!");
        });
        VBox vBox1 = new VBox();
        vBox1.getChildren().addAll(redBtn, orangeBtn);
        Button blueBtn = kahootButton("BLUE", "blue");
        blueBtn.setOnAction(event -> {
            txtLabel.setText(descriptions[++ind]);
        });
        Button greenBtn = kahootButton("GREEN", "green");
        greenBtn.setOnAction(event -> {
            System.out.println("GREEN!");
        });
        VBox vBox2 = new VBox();
        vBox2.getChildren().addAll(blueBtn, greenBtn);
        VBox.setMargin(redBtn, new Insets(3));
        VBox.setMargin(orangeBtn, new Insets(3));
        VBox.setMargin(blueBtn, new Insets(3));
        VBox.setMargin(greenBtn, new Insets(3));
        HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox1, vBox2);
        StackPane buttons = new StackPane();
        buttons.getChildren().addAll(hBox);
        hBox.setMaxHeight(200);
        hBox.setMaxWidth(200);
        BorderPane mainPane = new BorderPane();
        mainPane.setTop(txtLabel);
//        mainPane.setCenter();
        mainPane.setBottom(buttons);
        BorderPane.setMargin(txtLabel, new Insets(10));
        BorderPane.setMargin(buttons, new Insets(10));
        return mainPane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(currentQuestion()));
        primaryStage.setTitle("JavaFX");
        primaryStage.show();
    }


}

