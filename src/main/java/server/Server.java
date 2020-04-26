package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server
{
    private static final int PORT = 9090;
    static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
    static boolean isWorking = true;
    static ServerSocket serverSocket;

    public static void main(String[] args)
    {
        try
        {
            serverSocket = new ServerSocket(PORT);
            while (isWorking)
            {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Client connected to the server");
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
                ClientHandler clientHandler = new ClientHandler(clientSocket, in, out);
                clients.add(clientHandler);
                Thread serverThread = new Thread(clientHandler);
                serverThread.start();
            }
        }
        catch (IOException e)
        {
            System.out.println("Server is shutting down");
            isWorking = false;
            closeConnections();
        }
    }

    private static void closeConnections()
    {
        for (ClientHandler client : clients)
        {
            try
            {
                client.getSocket().close();
            }
            catch (IOException ioException)
            {
                System.out.println("Error. Could not close " +
                        client.getName() + "'s socket");
            }
        }
    }
}
