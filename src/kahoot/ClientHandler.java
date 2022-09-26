package kahoot;

import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable
{

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private DataInputStream fromClient;
    private DataOutputStream toClient;
    private String clientUsername;
    public Server server;

    public ClientHandler(Socket socket, Server server, int pin)
    {
        this.server = server;
        try
        {
            this.socket = socket;
            this.fromClient = new DataInputStream(socket.getInputStream());
            this.toClient = new DataOutputStream(socket.getOutputStream());

            int clientPin = fromClient.readInt();

            if (clientPin != pin)
            {
                toClient.writeUTF("Wrong PIN!");
            } else
            {
                toClient.writeUTF("Success!");
            }

            this.clientUsername = fromClient.readUTF();

            clientHandlers.add(this);
            server.clientHandlers = clientHandlers;
            System.out.println(server.clientHandlers);
//            broadCastData("Server: " + clientUsername + " has entered");

        } catch (IOException e)
        {
            closeEverthing(socket, fromClient, toClient);
        }
    }

    @Override
    public void run()
    {
//        String messageFromClient;

//        while (socket.isConnected())
//        {
//            try
//            {
////                messageFromClient = fromClient.readUTF();
////                broadCastData(messageFromClient);
//            } catch (IOException e)
//            {
//                closeEverthing(socket, fromClient, toClient);
//                break;
//            }
//        }
    }

    public void broadCastString(String messageToSend)
    {
        for (ClientHandler clientHandler : clientHandlers)
        {
            try
            {
                if (!clientHandler.clientUsername.equals(clientUsername))
                {
                    clientHandler.toClient.writeUTF(messageToSend);
                }
            } catch (IOException e)
            {
                closeEverthing(socket, fromClient, toClient);
            }
        }
    }

    public void broadCastBoolean(Boolean boolToSend)
    {
        for (ClientHandler clientHandler : clientHandlers)
        {
            try
            {
                if (!clientHandler.clientUsername.equals(clientUsername))
                {
                    clientHandler.toClient.writeBoolean(boolToSend);
                }
            } catch (IOException e)
            {
                closeEverthing(socket, fromClient, toClient);
            }
        }
    }

    public void removeClientHandler()
    {
        clientHandlers.remove(this);
        broadCastString("Server: "+clientUsername+" has left");
    }

    public void closeEverthing(Socket socket, DataInputStream fromClient, DataOutputStream toClient)
    {
        removeClientHandler();
        try
        {
            if (fromClient != null)
                fromClient.close();
            if (toClient != null)
                toClient.close();
            if (socket != null)
                socket.close();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public ArrayList<ClientHandler> getClientHandlers()
    {
        return clientHandlers;
    }
    public String getClientUsername()
    {
        return clientUsername;
    }
    public Socket getSocket()
    {
        return socket;
    }
    public DataInputStream getFromClient()
    {
        return fromClient;
    }
    public DataOutputStream getToClient()
    {
        return toClient;
    }
}
