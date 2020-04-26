package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler implements Runnable
{
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private String name;
    private boolean isLoggedIn;

    public ClientHandler(Socket socket, DataInputStream in, DataOutputStream out)
    {
        this.socket = socket;
        this.in = in;
        this.out = out;
        this.name = "unnamed";
        isLoggedIn = false;
    }

    public void run()
    {
        try
        {
            ReadName();
            String receivedMessage;
            messageToAll(name + " connected to the chat");
            isLoggedIn = true;
            while (isLoggedIn)
            {
                receivedMessage = in.readUTF();

                if (receivedMessage.equals("quit"))
                {
                    closeConnection();
                    messageToAll(name + " left the chat");
                    System.out.println("Client "
                            + name + " disconnected from the server");
                    break;
                }

                if (receivedMessage.equals("SERVER STOP"))
                {
                    Server.isWorking = false;
                    Server.serverSocket.close();
                }

                messageToAll(name + ": " + receivedMessage);
            }
        }
        catch (IOException e)
        {
            System.out.println("Connection is closed; " +
                    "cannot read from socket anymore");
            closeConnection();
        }
    }

    private void ReadName() throws IOException
    {
        out.writeUTF("Enter your name: ");
        name = in.readUTF();
        out.writeUTF("Type 'SERVER STOP' to stop server");
        out.writeUTF("Type 'quit' to leave the chat");
    }

    public void closeConnection()
    {
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Error. Could not close socket connection");
        }
        finally
        {
            System.out.println(name + "'s socket is closed");
            isLoggedIn = false;
            Server.clients.remove(this);
        }
    }

    private void messageToAll(String message)
    {
        for (ClientHandler client : Server.clients)
        {
            try
            {
                client.getOut().writeUTF(message);
            }
            catch (IOException e)
            {
                System.out.println("Connection is closed" +
                        "Could not send to " + client.getName());
            }
        }
    }

    public DataOutputStream getOut()
    {
        return out;
    }

    public String getName()
    {
        return name;
    }

    public Socket getSocket()
    {
        return socket;
    }
}
