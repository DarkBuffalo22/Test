package peer2peer;

import java.lang.*;
import java.io.File;
import java.io.InputStream;
import java.io.FileInputStream;
import java.net.Socket;
import java.net.ServerSocket;

public class FileReceiver implements Runnable {
    
    private Socket socket;
    private static final int thisPort = 4770; // default port for client to client
    
    public FileReceiver () {
        
    }
    
    public static void main (String[] args) {
        try {
            ServerSocket listener = new ServerSocket (thisPort);
            
            while (true) {
                FileReceiver file_rec = new FileReceiver ();
                file_rec.socket = listener.accept ();
                
                new Thread (file_rec).start ();
            }
        }
        catch (java.lang.Exception e) {
            e.printStackTrace ();
        }
    }
    
    public void run () {
        try {
            InputStream in = socket.getInputStream ();
            
            int numFiles = ByteStream.toInt (in);
            
            for (int i = 0; i < numFiles; i++) {
                String file_name = ByteStream.toString (in);
                
                File file = new File (file_name);
            }
        }
        catch (java.lang.Exception e) {
            e.printStackTrace ();
        }
    }
}
