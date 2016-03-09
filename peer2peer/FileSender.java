package peer2peer;

import java.lang.*;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.Socket;

public class FileSender {
    
    // host and port of receiver
    private static final int thisPort = 4770;
    private static String thisHost = "localhost"; // default, can specify
    
    public FileSender (String host) {
        thisHost = host;
    }
    
    public void setHost (String host) {
        thisHost = host;
    }
    
    public String getHost () {
        return thisHost;
    }
    
    public static void main (String [] args) {
        try {
            Socket socket = new Socket (thisHost, thisPort);
            OutputStream os = socket.getOutputStream ();
            
            int numFiles = args.length;
            
            // How many files?
            ByteStream.toStream (os, numFiles);
            
            for (int i = 0; i < numFiles; i++) {
                ByteStream.toStream (os, args[i]);
                ByteStream.toStream (os, new File (args[i]));
            }
        }
        catch (Exception e) {
            e.printStackTrace ();
        }
    }
}
