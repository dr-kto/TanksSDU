package kahoot;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server extends Application
{
    public Button buttonStart = new Button("Start");
    public StackPane root = new StackPane();
    private BorderPane borderPane = new BorderPane();
    public HBox users = new HBox();
    private Stage window;
    private final double W = 600, H = 600;

    private QuizMaker quizMaker;
    public static Boolean start = false;

    private int clientId = 1;
    public ServerSocket serverSocket;
    public ArrayList<ClientHandler> clientHandlers;

    public Server(ServerSocket serverSocket)
    {
        this.serverSocket = serverSocket;
    }

    public Server() {

    }


    public void startServer()
    {
        try
        {
            while (!serverSocket.isClosed())
            {
                Socket socket = serverSocket.accept();
                System.out.println(clientId + " Client is connected");
                ClientHandler clientHandler = new ClientHandler(socket,this, getPIN());
                Thread thread = new Thread(clientHandler);
                thread.start();
                AnimationTimer timer = new AnimationTimer()
                {
                    @Override
                    public void handle(long currentNanoMillis)
                    {
                        update();
                    }
                };
                timer.start();
                clientId++;
            }
        } catch (IOException e){
            closeServerSocket();
        }
    }

     public void closeServerSocket()
     {
         try
         {
             if (serverSocket != null)
             {
                 serverSocket.close();
             }
         } catch (IOException e)
         {
             e.printStackTrace();
         }
     }

    public int getPIN()
    {

        return 12345;
    }

//    public Button kahootButton(String btnText, String btnColor) {
//        Font font = Font.font("Times New Roman", FontWeight.BOLD,
//                FontPosture.ITALIC, 18);
//        Button btn = new Button(btnText);
//        btn.setMinWidth(200);
//        btn.setMinHeight(100);
//        btn.setStyle("-fx-background-color: " + btnColor);
//        btn.setTextFill(Color.WHITE);
//        btn.setFont(font);
//        return btn;
//    }

//    public BorderPane currentQuestion() {
//
//        Label txtLabel = new Label(descriptions[ind]);
//        Font font = Font.font("Times New Roman", FontWeight.BOLD,
//                FontPosture.ITALIC, 18);
//        txtLabel.setFont(font);
//        txtLabel.setMinWidth(W);
//        txtLabel.setAlignment(Pos.CENTER);
//
//        Button redBtn = kahootButton("RED!", "red");
//        redBtn.setOnAction(event -> {
//            txtLabel.setText(descriptions[--ind]);
//        });
//        Button orangeBtn = kahootButton("ORANGE!", "orange");
//        orangeBtn.setOnAction(event -> {
//            System.out.println("ORANGE!");
//        });
//        VBox vBox1 = new VBox();
//        vBox1.getChildren().addAll(redBtn, orangeBtn);
//        Button blueBtn = kahootButton("BLUE", "blue");
//        blueBtn.setOnAction(event -> {
//            txtLabel.setText(descriptions[++ind]);
//        });
//        Button greenBtn = kahootButton("GREEN", "green");
//        greenBtn.setOnAction(event -> {
//            System.out.println("GREEN!");
//        });
//        VBox vBox2 = new VBox();
//        vBox2.getChildren().addAll(blueBtn, greenBtn);
//        VBox.setMargin(redBtn, new Insets(3));
//        VBox.setMargin(orangeBtn, new Insets(3));
//        VBox.setMargin(blueBtn, new Insets(3));
//        VBox.setMargin(greenBtn, new Insets(3));
//        HBox hBox = new HBox();
//        hBox.getChildren().addAll(vBox1, vBox2);
//        StackPane buttons = new StackPane();
//        buttons.getChildren().addAll(hBox);
//        hBox.setMinHeight(H / 2- 5);
//        hBox.setMaxWidth(W / 2 - 5);
//        BorderPane mainPane = new BorderPane();
//        mainPane.setTop(txtLabel);
//
//        Button next = new Button("next");
//        next.setMinSize(100, 40);
//        next.setStyle("-fx-background-color: #3e147f");
//        next.setTextFill(Color.WHITE);
//        BorderPane nextPane = new BorderPane();
//        nextPane.setRight(next);
//        nextPane.setMinSize(W, H/3);
//
//        mainPane.setCenter(nextPane);
//        mainPane.setBottom(buttons);
//        BorderPane.setMargin(txtLabel, new Insets(10));
//        BorderPane.setMargin(buttons, new Insets(10));
//        return mainPane;
//    }




    private void update()
    {
        System.out.println(clientHandlers);
        for (ClientHandler clientHandler : clientHandlers)
        {
            users = new HBox(10);
            users.setAlignment(Pos.CENTER);
            users.setMinWidth(600);
            users.setMinHeight(450);

            Platform.runLater(() ->
                    {
                        Label nLbl = new Label(clientHandler.getClientUsername());
                        Label nLbl1 = new Label(clientHandler.getClientUsername());
                        System.out.println(nLbl);
                        nLbl.setFont(Font.font("Sans serif", FontWeight.NORMAL, FontPosture.REGULAR, 16));
                        nLbl.setTextFill(Color.WHITE);
                        nLbl1.setFont(Font.font("Sans serif", FontWeight.NORMAL, FontPosture.REGULAR, 16));
                        nLbl1.setTextFill(Color.WHITE);


                        users.getChildren().addAll(nLbl, nLbl1);

                        Label cLbl = new Label("joined: "+users.getChildren().size());
                        cLbl.setFont(Font.font("Sans serif", FontWeight.NORMAL, FontPosture.REGULAR, 16));
                        cLbl.setTextFill(Color.WHITE);
                        cLbl.setAlignment(Pos.CENTER_LEFT);
                        cLbl.setTranslateX(-100);


                        borderPane.setRight(cLbl);

                        buttonStart.setOnAction(event -> {
                            try {
                                quizMaker = new QuizMaker(this);
                                window = quizMaker.setStage(window);
                                window.setScene(new Scene(quizMaker.go(this), W, H));

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                        buttonStart.setMinWidth(150);
                        buttonStart.setMinHeight(40);
                        buttonStart.setStyle("-fx-background-color: #333333");
                        buttonStart.setTextFill(Color.WHITE);

                        HBox hBox = new HBox();
                        hBox.setMinWidth(W);
                        hBox.setAlignment(Pos.CENTER);
                        hBox.getChildren().addAll(buttonStart);

                        VBox content = new VBox();
                        content.getChildren().addAll(users, hBox);
                        borderPane.setCenter(content);
                        //                String clientChoice = fromClient.readUTF();
                    }
            );
        }
    }

    @Override
    public void start(Stage primaryStage)
    {
        new Thread(() -> {
            try
            {
                serverSocket = new ServerSocket(2022);
                Server server = new Server(serverSocket);
                server.startServer();
            } catch (IOException e)
            {
                closeServerSocket();
            }
        }).start();



        root.setStyle("-fx-background-color: #3E147F");


        primaryStage.setScene(new Scene(root, W, H));
        primaryStage.setTitle("Server");
        window = primaryStage;
        window.requestFocus();
        window.show();


        Label lbl = new Label("Game PIN:\n" + getPIN());
        lbl.setFont(Font.font("Sans serif", FontWeight.BOLD, FontPosture.REGULAR, 24));
        lbl.setMinWidth(600);
        lbl.setTextFill(Color.WHITE);
        lbl.setAlignment(Pos.CENTER);
        lbl.setTextAlignment(TextAlignment.CENTER);

        borderPane.setTop(lbl);


//        buttonStart = new Button("Start");



        users.getChildren().addAll(new Label(""));
        Label cLbl = new Label("joined: 0");
        cLbl.setFont(Font.font("Sans serif", FontWeight.NORMAL, FontPosture.REGULAR, 16));
        cLbl.setTextFill(Color.WHITE);
        cLbl.setAlignment(Pos.CENTER_LEFT);
        cLbl.setTranslateX(-100);

        borderPane.setRight(cLbl);

        root.getChildren().addAll(borderPane);
    }

    public ArrayList<ClientHandler> getClientHandlers()
    {
        return clientHandlers;
    }
}
