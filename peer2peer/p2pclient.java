package peer2peer;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class p2pclient implements Runnable {

    private static String serverHost = "localhost"; // default
    private static int serverPort = 8080; // default
    private static String client_IP;
    private static Socket socket;
    private static ArrayList<String> files = new ArrayList<String>();
    private long connectTime;

    public p2pclient(String host, int port) {
        serverHost = host;
        serverPort = port;
        
        try {
            socket = new Socket(serverHost, serverPort);
            connectTime = System.currentTimeMillis();
        } 
        catch (SocketException e) {
            System.out.println("Error: Unable to connect to server port ");
        } 
        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void populateFileList () {
        File folder = new File ("/Users/walkerbrandt/Desktop/Shared");
        File[] listOfFiles = folder.listFiles();
        
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                files.add(listOfFiles[i].getName());
            }
        }
    }
    
    public static void main(String[] args) throws IOException {
        int init = 0;

        try {
            InetAddress iAddress = InetAddress.getLocalHost();
            client_IP = iAddress.getHostAddress();
            System.out.println("Current IP address : " + client_IP);
        } 
        catch (UnknownHostException e) {
            e.printStackTrace ();
        }

        if (init == 0) {
            System.out.println("error: Failed to initialize ");
            System.exit(0);

        }
    }

    public static void initialize() 
    {   
        long startTime = System.currentTimeMillis();
        long currTime;
        
        try
        {
            PrintWriter pw = new PrintWriter(socket.getOutputStream(), true);

            for (int i = 0; i < files.size(); i++) {
                pw.println(files.get(i));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        
        // UDP Datagram send
        try
        {
            DatagramSocket clientSocket = new DatagramSocket ();
            
            InetAddress serverIP = InetAddress.getByName (serverHost);
            
            byte[] message = new byte[5];
            String str = "HELLO";
            message = str.getBytes();
            
            DatagramPacket sendPacket = new DatagramPacket (message, message.length, serverIP, serverPort);
                
            while (socket != null)
            {
                currTime = System.currentTimeMillis();
                
                if (currTime - startTime >= 60000) {
                    clientSocket.send (sendPacket);
                    startTime = System.currentTimeMillis();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeSocket (Socket socket) {
        try {
            socket.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Socket getSocket () {
        return socket;
    }
    
    public void run()
    {
        
    }
}

