package peer2peer;

import java.io.*;
import java.net.*;
import java.util.Date;
import java.util.HashMap;
import java.util.ArrayList;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class p2pserver extends Thread
{
    private ServerSocket server = null;
    private static int thisPort;
    private int ptTimeout = p2pThread.DEFAULT_TIMEOUT;
    protected static HashMap<String, ArrayList<String>> directory = new HashMap();

    public static void main(String[] args)
    {

        if (args.length == 0)
        {
            System.err.println ("USAGE: java p2pserver <port number>");
            System.err.println ("   <port number>   the port this service listens on");
        }

        thisPort = Integer.parseInt (args[0]);

        // create and start the p2p thread
        System.err.println (" ** Starting p2p server on port " + thisPort + ". Press CTRL-C to end. **\n");
        p2pserver p2p = new p2pserver (thisPort);
        p2p.start();

        // run forever until cancelled
        while (true)
        {
            try { Thread.sleep (3000); } catch (Exception e) {}
        }

        // p2p.closeSocket () used to close if applicable
    }

    public p2pserver (int port)
    {
        thisPort = port;
    }

    public int getPort ()
    {
        return thisPort;
    }

    public boolean isRunning ()
    {
        if (server == null)
            return false;
        else
            return true;
    }

    public void closeSocket ()
    {
        try
        {
            // close open server socket
            server.close ();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    // boss thread that listens on the port and creates a new worker thread and
    // socket when another client requests to connect
    public void run ()
    {
        try
        {
            // create server socket and listen for client connections
            server = new ServerSocket (thisPort);
            System.out.println ("Started p2p on port " + thisPort);

            while (true)
            {
                Socket client = server.accept ();
                String clientID = client.getRemoteSocketAddress().toString ();
                p2pThread t = new p2pThread (client, clientID);
                t.setTimeout (ptTimeout);
                t.start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        closeSocket ();
    }
}

class p2pThread extends Thread
{
    private Socket pSocket;
    private String clientID;
    private int debugLevel = 0;
    private PrintStream debugOut = System.out;

    public static final int DEFAULT_TIMEOUT = 200 * 1000;
    private int socketTimeout = DEFAULT_TIMEOUT;

    public p2pThread (Socket socket, String id)
    {
        pSocket = socket;
        clientID = id;
    }

    public void setTimeout (int timeout)
    {
        socketTimeout = timeout * 1000;
    }

    public void run ()
    {
        long initialTime = System.currentTimeMillis();
        long currTime;
        String str;
        ArrayList<String> files = new ArrayList<String>();

        try {
            PrintWriter pw = new PrintWriter(pSocket.getOutputStream(), true);

            BufferedReader br = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));

            while ((str = br.readLine()) != null)
            {
                // check current input to see if it matches
                // anything in the hashmap arraylist. If it does,
                // do not add that to files. If it doesn't, add it
                // to files and append the hashmap arraylist

                // loop through hashmap arraylist to compare to string
//                if (p2pserver.directory.get(clientID).size() > 0) {
//                    for (int i = 0; i < p2pserver.directory.get(clientID).size(); i++) {
//                        if (str.equals(p2pserver.directory.get(clientID).get(i))) {
//                            System.out.println("File already exists, not adding.");
//                        } else {
//                            files.add(str);
//                        }
//                    }
//                    System.out.println(p2pserver.directory.get(clientID));
//                } else {
//                    files.add(str);
//                }
                
                while ((str = br.readLine()) != null){
                    files.add(str);
                    p2pserver.directory.put(clientID, files);
                    System.out.println(p2pserver.directory.get(clientID));
                }
            }
            pw.close();
            br.close();

            //p2pserver.directory.get(clientID).clear();
            //p2pserver.directory.put(clientID, files);
        }
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}