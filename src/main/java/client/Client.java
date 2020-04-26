package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client
{
    private static final int PORT = 9090;
    private static final String IP = "127.0.0.1";

    public static void main(String[] args)
    {
        try
        {
            Socket socket = new Socket(IP, PORT);
            DataInputStream in = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());

            ConnectionHandler connectionHandler = new ConnectionHandler(socket, in, out);
            Thread connectionThread = new Thread(connectionHandler);
            connectionThread.start();
        }
        catch (IOException e)
        {
            System.out.println("Error: Socket connection could not be established");
        }
    }
}
