
package voice.server;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author julian
 */

public class ClientHandler implements Runnable{
    int valPerPack = 800;
    
    int filePointer, clientId = 0;
    boolean stop = false;
    Socket socket;
    InputStream is;
    int[] file = null;
    BufferedOutputStream bos;
    
    
    ClientHandler(Socket s, int[] file, int clientId){
        this.socket = s;
        this.file = file;
        this.clientId = clientId;

    }
    
    void stopConnection(){
        stop = true;
    }

    @Override
    public void run() {
        try {
            System.out.println("New Client connected. ");
            System.out.println("Client IP: " + socket.getInetAddress()) ;
            
            is = socket.getInputStream();
            bos = new BufferedOutputStream(socket.getOutputStream());
            // BufferedInputStream bis = new BufferedInputStream( socket.getInputStream() );

            while (!stop) {
                
                while (is.available() == 0 && !stop) Thread.sleep(1);
                while (is.available() >= 1 && !stop) {
                    int readInt = is.read();

                    if (readInt == 0b11111111) {
                        
                        for (int i = 0; i < valPerPack; i++) {
                            if (filePointer + i < file.length) {
                                bos.write(file[filePointer + i]);
                            } else {
                                bos.write(file[file.length-1]);
                            }
                        }
                        bos.flush(); 
                        
                        filePointer = filePointer + valPerPack;
                        if(filePointer >= file.length) {
                            filePointer = 0;
                        }
                    } else if (readInt == 0b10000001) {
                        System.out.println("Client send filepointer reset");
                        filePointer = 0;
                    }
                }

            }
            is.close();
            bos.close();
            socket.close();

        } catch (IOException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }  catch (InterruptedException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                is.close();
                bos.close();
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Client connection removed");
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(ClientHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
