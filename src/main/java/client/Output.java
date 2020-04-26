package client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Output implements Runnable
{
    private final ConnectionHandler connectionHandler;
    private final DataOutputStream out;
    private final Scanner scanner;

    public Output(ConnectionHandler connectionHandler, DataOutputStream out)
    {
        this.connectionHandler = connectionHandler;
        this.out = out;
        scanner = new Scanner(System.in);
    }

    public void run()
    {
        while (connectionHandler.isLoggedIn())
        {
            String msg = scanner.nextLine();
            try
            {
                out.writeUTF(msg);
            }
            catch (IOException e)
            {
                System.out.println("Connection closed; can not write to socket anymore");
            }
        }
    }
}
