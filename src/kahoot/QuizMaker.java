package kahoot;

// animation
// audio test
// img test

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuizMaker  {

    public static Stage stage;
    private static final double W = 850, H = 650;

    public static List<Question> questions = new ArrayList<>();
    private int sec, min;
//    Media media = new Media(new File("/home/yerbol/IdeaProjects/CSS108/src/Project2/kahoot_music.wav").toURI().toString());


    private boolean isOrderShuffled;

    private static int N, correct;

    public Server server;
    public  QuizMaker(Server server)
    {
        this.server = server;
    }



    public BorderPane resultPane() {
        BorderPane borderPane = new BorderPane();
//        ImageView iv = new ImageView(new Image("Project2/img/result.png"));
//        iv.setFitWidth(W - 300);
//        iv.setFitHeight(H - 300);

//        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16);

        Label label = new Label("Your Result:");
        label.setFont(Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 20));
        Label lblCorrect = new Label("Number of correct answers: " + correct + "/" + N);
        lblCorrect.setFont(Font.font("Times New Roman", FontPosture.ITALIC, 16));

        Label lblPercentage = new Label(String.format("%.2f%s", correct * 100. / N, "%"));
        lblPercentage.setFont(Font.font("Times New Roman", FontPosture.ITALIC, 18));
        Label lblTime = new Label(String.format("Finished in %s", timeFormat(sec)));
        lblTime.setFont(Font.font("Times New Roman", FontPosture.ITALIC, 16));

        Button btnShow = new Button("Show answers");
        btnShow.setMinWidth(400);
        btnShow.setMinHeight(50);
        btnShow.setStyle("-fx-background-color: blue");
        VBox.setMargin(btnShow, new Insets(10));

        Button btnClose = new Button("Close Test");
        btnClose.setTextFill(Color.WHITE);
        btnClose.setMinWidth(400);
        btnClose.setMinHeight(50);
        btnClose.setStyle("-fx-background-color: red");


        btnShow.setTextFill(Color.WHITE);
        btnShow.setWrapText(true);
        btnShow.setPadding(new Insets(10));

        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 15);
        btnShow.setFont(font);
        btnClose.setFont(font);

        VBox vBox = new VBox(label, lblPercentage, lblCorrect, lblTime, btnShow, btnClose);
        vBox.setMinWidth(W);
        vBox.setAlignment(Pos.CENTER);
        VBox.setMargin(label, new Insets(20));
        VBox.setMargin(lblCorrect, new Insets(10));
        VBox.setMargin(lblPercentage, new Insets(10));

        borderPane.setTop(vBox);

//        borderPane.setBottom(new StackPane(iv));
        btnClose.setOnAction(e -> {
            System.exit(1);
        });
//        btnShow.setOnMouseEntered(e -> stage.getScene().setCursor(Cursor.HAND));
//        btnShow.setOnMouseExited(e -> stage.getScene().setCursor(Cursor.DEFAULT));

        btnClose.setOnMouseEntered(e -> stage.getScene().setCursor(Cursor.HAND));
        btnClose.setOnMouseExited(e -> stage.getScene().setCursor(Cursor.DEFAULT));
        return borderPane;
    }

    public StackPane go(Server server) {
        this.server = server;
//        timeline = new Timeline();
//        lblTimer = new Label();


        StackPane stackPane = new StackPane();
//        ImageView iv = new ImageView(new Image("Project2/img/background.jpg"));
//        iv.setFitWidth(W);
//        iv.setFitHeight(H);

        FileChooser fc = new FileChooser();
        Button btn = new Button("Choose a file");

        btn.setOnAction(e -> {

            File selectedFile = fc.showOpenDialog(stage);
            Quiz quiz = new Quiz();

            questions = quiz.loadFromFile(selectedFile.getPath());
            System.out.println(this.server.getClientHandlers());

//            System.out.println(questions);
            if (isOrderShuffled)
                Collections.shuffle(questions);
            N = questions.size();
            quiz.setName(selectedFile.getName().replace(".txt", ""));

            if (questions.get(0) instanceof Test)
                stage.setScene(new Scene(currentTest(0), W, H));
            else
                stage.setScene(new Scene(currentFillIn(0), W, H));

//            MediaPlayer mediaPlayer = new MediaPlayer(media);
//            mediaPlayer.play();

        });

//        stackPane.getChildren().addAll(iv, btn);
        stackPane.getChildren().addAll(btn);
        return stackPane;
    }

    public BorderPane currentTest(int ind) {

//        questions = new Quiz().loadFromFile("General.txt");
        Timeline timeline = new Timeline();
        Label lblTimer = new Label();

        Test test = (Test) questions.get(ind);

        Label txtLabel = new Label((ind + 1) + ") " + test.getDescription());
        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16);
        txtLabel.setFont(font);
        txtLabel.setWrapText(true);
        txtLabel.setMinWidth(W);
        txtLabel.setAlignment(Pos.CENTER);
//        txtLabel.setMinWidth(W);

        ToggleGroup tg = new ToggleGroup();
        RadioButton redBtn = kahootButton(test.getOptionAt(0), "red");
        RadioButton orangeBtn = kahootButton(test.getOptionAt(1), "orange");
        RadioButton blueBtn = kahootButton(test.getOptionAt(2), "blue");
        RadioButton greenBtn = kahootButton(test.getOptionAt(3), "green");

        redBtn.setToggleGroup(tg);
        orangeBtn.setToggleGroup(tg);
        blueBtn.setToggleGroup(tg);
        greenBtn.setToggleGroup(tg);

        RadioButton[] btns = {redBtn, orangeBtn, blueBtn, greenBtn};

        if (test.getSelectedInd() != -1) {
            int j = test.getSelectedInd();
            for (int i = 0; i < 4; i++) {
                btns[i].setSelected(i == j);
            }
        }

        redBtn.setOnAction(e -> {
            test.setSelectedInd(0);
            int idx = test.getSelectedInd();
            String userChoice = test.getOptionAt(idx);
            checkTest(userChoice, test);
        });

        orangeBtn.setOnAction(e -> {
            test.setSelectedInd(1);
            int idx = test.getSelectedInd();
            String userChoice = test.getOptionAt(idx);
            checkTest(userChoice, test);
        });

        blueBtn.setOnAction(e -> {
            test.setSelectedInd(2);
            int idx = test.getSelectedInd();
            String userChoice = test.getOptionAt(idx);
            checkTest(userChoice, test);
        });

        greenBtn.setOnAction(e -> {
            test.setSelectedInd(3);
            int idx = test.getSelectedInd();
            String userChoice = test.getOptionAt(idx);
            checkTest(userChoice, test);
        });
        VBox vBox1 = new VBox();
        vBox1.getChildren().addAll(redBtn, orangeBtn);
        VBox vBox2 = new VBox();
        vBox2.getChildren().addAll(blueBtn, greenBtn);

        setMargin(redBtn, orangeBtn, blueBtn, greenBtn);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox1, vBox2);
        hBox.setMaxHeight(200);
        hBox.setMaxWidth(200);


//        redBtn.setOnAction(e -> {
//            redBtn.setStyle("-fx-border-color: black; -fx-border-width: 0 3 3 0; -fx-background-color: red;");
//        });


//        Image img = new Image("Project2/img/logo.png");
//        ImageView iv = new ImageView(img);
//        iv.setFitWidth(450.);
//        iv.setFitHeight(300.);

        VBox holder = new VBox(10);
        holder.setAlignment(Pos.CENTER);
        holder.setMaxWidth(500);
        holder.setMaxHeight(500);
        lblTimer.setFont(Font.font("Times New Roman", FontWeight.MEDIUM, FontPosture.ITALIC, 16));
//        holder.getChildren().addAll(lblTimer, iv);
        holder.getChildren().addAll(lblTimer);
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(txtLabel);
        borderPane.setCenter(holder);
        borderPane.setBottom(hBox);

        Button btnNext = controlButton("❯");
        Button btnPrev = controlButton("❮");
        if (ind == 0) btnPrev.setVisible(false);

        if (ind + 1 < questions.size()) {
            btnNext.setOnAction(e -> {
                timeline.stop();
                if (questions.get(ind + 1) instanceof FillIn) {
                    stage.setScene(new Scene(currentFillIn(ind + 1), W, H));
                } else {
                    stage.setScene(new Scene(currentTest(ind + 1), W, H));
                }

            });
        } else {
            if (ind == questions.size() - 1) btnNext.setText("✔");
            btnNext.setOnAction(e -> {
                stage.setScene(new Scene(resultPane(), W, H));
            });
        }

        btnPrev.setOnAction(e -> {
            if (ind > 0) {
                if (questions.get(ind - 1) instanceof FillIn) {
                    stage.setScene(new Scene(currentFillIn(ind - 1), W, H));
                } else {
                    stage.setScene(new Scene(currentTest(ind - 1), W, H));
                }
            }
        });
        StackPane next = new StackPane(btnNext);
        next.setMinWidth(80);
        StackPane prev = new StackPane(btnPrev);
        prev.setMinWidth(50);
        borderPane.setRight(next);
        borderPane.setLeft(prev);

        BorderPane.setMargin(txtLabel, new Insets(10));

        redBtn.setOnMouseEntered(e -> stage.getScene().setCursor(Cursor.HAND));
        redBtn.setOnMouseExited(e -> stage.getScene().setCursor(Cursor.DEFAULT));

        orangeBtn.setOnMouseEntered(e -> stage.getScene().setCursor(Cursor.HAND));
        orangeBtn.setOnMouseExited(e -> stage.getScene().setCursor(Cursor.DEFAULT));

        blueBtn.setOnMouseEntered(e -> stage.getScene().setCursor(Cursor.HAND));
        blueBtn.setOnMouseExited(e -> stage.getScene().setCursor(Cursor.DEFAULT));

        greenBtn.setOnMouseEntered(e -> stage.getScene().setCursor(Cursor.HAND));
        greenBtn.setOnMouseExited(e -> stage.getScene().setCursor(Cursor.DEFAULT));


        // timer

        lblTimer.setText(timeFormat(sec));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        event ->
                                lblTimer.setText(timeFormat(++sec))));

        timeline.play();

        return borderPane;
    }

    private String timeFormat(int sec) {
        min = sec / 60;
        sec = sec % 60;
        String strMin = min + "";
        if (min < 10) strMin = "0" + strMin;
        String strSec = sec + "";
        if (sec < 10) strSec = "0" + strSec;

        return strMin + ":" + strSec;
    }

    private void checkTest(String userChoice, Test test) {
        if (userChoice.equals(test.getAnswer())) {
            if (!test.isFlag()) {
                correct++;
                test.setFlag(true);
            }
        } else {
            if (test.isFlag()) {
                correct--;
                test.setFlag(false);
            }
        }
    }

    public BorderPane currentFillIn(int ind) {
        Timeline timeline = new Timeline();
        Label lblTimer = new Label();

        FillIn fillIn = (FillIn) questions.get(ind);
//        ImageView btnIv = new ImageView(new Image("Project2/img/k.png"));
//        btnIv.setFitWidth(50);
//        btnIv.setFitHeight(30);
//        Label txtLabel = new Label((ind + 1) + ") " + fillIn.toString(), btnIv);
        Label txtLabel = new Label((ind + 1) + ") " + fillIn.toString());
        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 16);
        txtLabel.setFont(font);
        txtLabel.setWrapText(true);
        txtLabel.setMinWidth(W);
        txtLabel.setAlignment(Pos.CENTER);

        BorderPane borderPane = new BorderPane();

        RadioButton redBtn = kahootButton("red", "red");
        RadioButton orangeBtn = kahootButton("orange", "orange");
        RadioButton blueBtn = kahootButton("blue", "blue");
        RadioButton greenBtn = kahootButton("green", "green");

        redBtn.setVisible(false);
        orangeBtn.setVisible(false);
        blueBtn.setVisible(false);
        greenBtn.setVisible(false);

        VBox vBox1 = new VBox();
        vBox1.getChildren().addAll(redBtn, orangeBtn);
        VBox vBox2 = new VBox();
        vBox2.getChildren().addAll(blueBtn, greenBtn);

        setMargin(redBtn, orangeBtn, blueBtn, greenBtn);

        HBox hBox = new HBox();
        hBox.getChildren().addAll(vBox1, vBox2);
        hBox.setMaxHeight(200);
        hBox.setMaxWidth(200);

        Button btnNext = controlButton("❯");
        Button btnPrev = controlButton("❮");
        if (ind == 0) btnPrev.setVisible(false);
//        borderPane.requestFocus();
        StackPane next = new StackPane(btnNext);
        next.setMinWidth(80);
        StackPane prev = new StackPane(btnPrev);
        prev.setMinWidth(50);

        lblTimer.setText(timeFormat(sec));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1),
                        event ->
                                lblTimer.setText(timeFormat(++sec))));

        timeline.play();

        VBox ansLayout = new VBox(10);
//        Image img = new Image("Project2/img/fillin.png");
//        ImageView iv = new ImageView(img);
        VBox holder = new VBox();
        holder.setAlignment(Pos.CENTER);
        holder.setMaxWidth(500);
        holder.setMaxHeight(500);
        lblTimer.setFont(Font.font("Times New Roman", FontWeight.MEDIUM, FontPosture.ITALIC, 16));
//        holder.getChildren().addAll(lblTimer, iv);
        holder.getChildren().addAll(lblTimer);
        holder.setAlignment(Pos.CENTER);
        VBox.setMargin(lblTimer, new Insets(20, 0, 0, 0));

//        iv.setFitWidth(450.);
//        iv.setFitHeight(300.);
        Label hint = new Label("Type your answer here:");
        TextField tf = new TextField();

        tf.setText(fillIn.getTypedAns());
        tf.setMaxWidth(450);
        tf.setMaxHeight(500);
        tf.setPromptText("Type your answer here..");

//        tf1.setVisible(false);
        ansLayout.getChildren().addAll(holder, hint, tf);
        ansLayout.setAlignment(Pos.CENTER);
//        borderPane.setTop(iv);
        borderPane.setTop(txtLabel);
        borderPane.setCenter(ansLayout);
        borderPane.setRight(next);
        borderPane.setLeft(prev);
        borderPane.setBottom(hBox);


        BorderPane.setMargin(txtLabel, new Insets(10));

        if (ind + 1 < questions.size()) {

            btnNext.setOnAction(e -> {
                checkFillIn(fillIn, tf);
                if (questions.get(ind + 1) instanceof FillIn) {
                    stage.setScene(new Scene(currentFillIn(ind + 1), W, H));
                } else {
                    stage.setScene(new Scene(currentTest(ind + 1), W, H));
                }


            });
        } else {
            btnNext.setText("✔");
            btnNext.setOnAction(e -> {
                checkFillIn(fillIn, tf);
                stage.setScene(new Scene(resultPane(), W, H));
            });
        }
        btnPrev.setOnAction(e -> {
            checkFillIn(fillIn, tf);
            if (ind > 0) {
                if (questions.get(ind - 1) instanceof FillIn) {
                    stage.setScene(new Scene(currentFillIn(ind - 1), W, H));
                } else {
                    stage.setScene(new Scene(currentTest(ind - 1), W, H));
                }
            }
        });

        return borderPane;
    }

    private void checkFillIn(FillIn fillIn, TextField tf) {
        fillIn.setTypedAns(tf.getText());
        if (fillIn.getAnswer().equals(tf.getText())) {
            if (!fillIn.isFlag()) {
                correct++;
                fillIn.setFlag(true);
            }
        } else {
            if (fillIn.isFlag()) {
                correct--;
                fillIn.setFlag(false);
            }
        }
    }

    private void setMargin(RadioButton redBtn, RadioButton orangeBtn, RadioButton blueBtn, RadioButton greenBtn) {
        VBox.setMargin(redBtn, new Insets(3));
        VBox.setMargin(orangeBtn, new Insets(3));
        VBox.setMargin(blueBtn, new Insets(3));
        VBox.setMargin(greenBtn, new Insets(3));
    }

    public Button controlButton(String btnText) {
        Button btn = new Button(btnText);
        btn.setMinWidth(50);
        btn.setMinHeight(50);

        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 18);
        btn.setFont(font);
        return btn;
    }

    public RadioButton kahootButton(String btnText, String btnColor) {
        RadioButton btn = new RadioButton(btnText);
        btn.setMinWidth(W / 2. - 5);
        btn.setMinHeight(100);
        btn.setStyle("-fx-background-color: " + btnColor);

        btn.setTextFill(Color.WHITE);
        btn.setWrapText(true);
        btn.setPadding(new Insets(10));

        Font font = Font.font("Times New Roman", FontWeight.BOLD, FontPosture.ITALIC, 15);
        btn.setFont(font);
        return btn;
    }

    public List<Question> getQuestions() {
        return questions;
    }

//    @Override
//    public void start(Stage primaryStage) throws Exception {
//        stage = primaryStage;
//        primaryStage.setScene(new Scene(go(), W, H));
//        primaryStage.setTitle("Project 2");
//        isOrderShuffled = true;
//        primaryStage.show();
//    }

    public Stage setStage(Stage serverStage)
    {
        stage = serverStage;
        isOrderShuffled = true;
        stage.setTitle("Project 2");
        return stage;
    }
}
