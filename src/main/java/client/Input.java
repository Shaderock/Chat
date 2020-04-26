package client;

import java.io.DataInputStream;
import java.io.IOException;

public class Input implements Runnable
{
    private final ConnectionHandler connectionHandler;
    private final DataInputStream in;

    public Input(ConnectionHandler connectionHandler, DataInputStream in)
    {
        this.connectionHandler = connectionHandler;
        this.in = in;
    }

    public void run()
    {
        while (connectionHandler.isLoggedIn())
        {
            try
            {
                String message = in.readUTF();
                System.out.println(message);
            }
            catch (IOException e)
            {
                connectionHandler.closeConnection();
                System.out.println("Connection closed; can not read from socket anymore");
            }
        }
    }
}
