/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voice.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;

public class VoiceServer {

    /**
     * @param args the command line arguments
     */

    public static void main(String[] args) throws IOException, InterruptedException {
        Helper h = new Helper();
        String fileName;

        if (args.length == 0) {
            System.out.println("please give me a file!");
            return;
        } else {
            fileName = "" + args[0];
        }

        System.out.println("Reading file '" + fileName + "'....");
        int[] file = h.loadFile(fileName);

        if (file == new int[]{-1}) {
            System.out.println("Error reading the file!");
            return;
        }

        System.out.println("File successfully read");
        System.out.println("File length: " + file.length + "\n");

        System.out.println("Server started");
        System.out.println("press \"ctrl + c\" to exit \n");
        
        ServerSocket listener = new ServerSocket(4444); // Port number goes here, so maybe let's change this on both and see if it still works

        try {
            Socket socket = listener.accept();
            ClientHandler ch = new ClientHandler(socket, file, 0);
            new Thread(ch).start();
            
            while(true) Thread.sleep(100);
            // InputStream is = System.in;

            /* boolean exit = false;
            String input = "";
            
            while (!exit) {
                if (is.available() == 0) {
                    Thread.sleep(100);
                } else {
                    char c = (char) is.read();
                    if (c == '\n') {
                        if(input.equals("exit")) {
                            exit = true;
                            System.out.println("Server wird beendet...");
                            ch.stopConnection();
                        }
                        input = "";
                    } else {
                        input += c;
                    }
                }
            } */

        } finally {
            listener.close();
            System.out.println("Server shut down");
        }

    }

}
