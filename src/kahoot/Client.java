package kahoot;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Client extends Application
{
    private final double W = 600, H = 600;

    private Pane root;
    private Socket socket;
    private Stage window;
    private DataOutputStream toServer;
    private DataInputStream fromServer;

    //    public static void main(String args[]) throws Exception
//    {
//        Socket socket = new Socket("localhost", 2022);
//        DataOutputStream toServer = new DataOutputStream(socket.getOutputStream());
//        toServer.writeInt(1);
//        Scanner in = new Scanner(System.in);
//        System.out.println("Enter yor nickname: ");
//        String nickname = in.nextLine();
//
//    }

    public void connectToServer() throws IOException
    {
        socket = new Socket("localhost", 2022);
        toServer = new DataOutputStream(socket.getOutputStream());
        fromServer = new DataInputStream(socket.getInputStream());
    }

    public StackPane nickPane()
    {
        StackPane stackPane = new StackPane();

        TextField textField = new TextField();
        textField.setPromptText("Enter username");
        textField.setAlignment(Pos.CENTER);
        textField.setMaxWidth(W/3);
        textField.setMinHeight(40);
        Button btn = new Button("Enter");
        btn.setMaxWidth(W/3);
        btn.setMinHeight(40);
        btn.setStyle("-fx-background-color: #333333");
        btn.setTextFill(Color.WHITE);
        btn.setFont(Font.font("Sans serif", FontWeight.BOLD, FontPosture.REGULAR, 16));
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-background-color: #3E147F");
        vBox.setMaxWidth(W/2);
        vBox.setMaxHeight(H/2);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(textField, btn);

        stackPane.getChildren().addAll(vBox);
        stackPane.setStyle("-fx-background-color: #3E147F");

        btn.setOnAction(event -> {
            try
            {
                toServer.writeUTF(textField.getText());
                boolean b = fromServer.readBoolean();
                System.out.println(b);
                if(b)
                    window.setScene(new Scene(gamePin(), W, H));
            } catch (IOException e)
            {
                System.out.println("error");
            }
        });
        return stackPane;
    }

    public StackPane pinPane()
    {
        StackPane stackPane = new StackPane();

        TextField textField = new TextField();
        textField.setPromptText("Game PIN");
        textField.setAlignment(Pos.CENTER);
        textField.setMaxWidth(W/3);
        textField.setMinHeight(40);
        Button btn = new Button("Enter");
        btn.setMaxWidth(W/3);
        btn.setMinHeight(40);
        btn.setStyle("-fx-background-color: #333333");
        btn.setTextFill(Color.WHITE);
        btn.setFont(Font.font("Sans serif", FontWeight.BOLD, FontPosture.REGULAR, 16));
        VBox vBox = new VBox(10);
        vBox.setStyle("-fx-background-color: #3E147F");
        vBox.setMaxWidth(W/2);
        vBox.setMaxHeight(H/2);
        vBox.setAlignment(Pos.CENTER);
        vBox.getChildren().addAll(textField, btn);

        stackPane.getChildren().addAll(vBox);
        stackPane.setStyle("-fx-background-color: #3E147F");

        btn.setOnAction(event -> {
            try
            {
                toServer.writeInt(Integer.parseInt(textField.getText()));
                String status = fromServer.readUTF();

                if (status.equals("Success!"))
                {
                    window.setScene(new Scene(nickPane(), W, H));
                }
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });
        return stackPane;
    }

    public Button kahootButton(String btnColor) {
        Button btn = new Button();
        btn.setMinWidth(W/2-5);
        btn.setMinHeight(H/2-5);
        btn.setStyle("-fx-background-color: " + btnColor);

        return btn;
    }

    public StackPane gamePin()
    {
        StackPane stackPane = new StackPane();
        VBox vbox1 = new VBox(10);
        Button btnRed = kahootButton("red");
        Button btnBlue = kahootButton("blue");
        vbox1.getChildren().addAll(btnRed, btnBlue);
        VBox vBox2 = new VBox(10);
        Button btnOrange = kahootButton("orange");
        Button btnGreen = kahootButton("green");
        vBox2.getChildren().addAll(btnOrange, btnGreen);
        HBox hBox = new HBox(10);
        hBox.getChildren().addAll(vbox1, vBox2);
        stackPane.getChildren().addAll(hBox);

        btnRed.setOnAction(event -> {
            try
            {
                toServer.writeUTF("Red");
                waitPane();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });
        btnBlue.setOnAction(event -> {
            try
            {
                toServer.writeUTF("Blue");
                waitPane();

            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });
        btnOrange.setOnAction(event -> {
            try
            {
                toServer.writeUTF("Orange");
                waitPane();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });
        btnGreen.setOnAction(event -> {
            try
            {
                toServer.writeUTF("Green");
                waitPane();
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }
        });



        return stackPane;
    }

    public StackPane waitPane()
    {
        StackPane pane = new StackPane();
        pane.setStyle("-fx-background-color: #3e147f");
        Label label = new Label("Wait your answer");
        label.setMinWidth(W);
        label.setAlignment(Pos.CENTER);
        pane.getChildren().addAll(label);
        return pane;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        connectToServer();
        root = pinPane();
        primaryStage.setScene(new Scene(root, W, H));
        primaryStage.setTitle("Client");
        window = primaryStage;
        window.show();
        root.requestFocus();
    }
}
