package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ConnectionHandler implements Runnable
{
    private final Socket socket;
    private final DataInputStream in;
    private final DataOutputStream out;
    private boolean isLoggedIn;

    public ConnectionHandler(Socket socket, DataInputStream in, DataOutputStream out)
    {
        this.socket = socket;
        this.in = in;
        this.out = out;
        isLoggedIn = true;
    }

    public void run()
    {
        Input clientInput = new Input(this, in);
        Output clientOutput = new Output(this, out);
        Thread inputThread = new Thread(clientInput);
        Thread outputThread = new Thread(clientOutput);
        inputThread.start();
        outputThread.start();
    }

    void closeConnection()
    {
        try
        {
            socket.close();
        }
        catch (IOException e)
        {
            System.out.println("Could not close socket");
        }
        finally
        {
            isLoggedIn = false;
            System.out.println("You left the chat; Socket is closed");
        }
    }

    public boolean isLoggedIn()
    {
        return isLoggedIn;
    }
}
