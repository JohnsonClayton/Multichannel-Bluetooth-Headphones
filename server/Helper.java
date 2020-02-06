/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voice.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author julian
 */
public class Helper {
    public static int toUnsignedInt(byte x) {
        return ((int) x) & 0xff;
    }
    
    int[] loadFile(String fileName) {
        FileInputStream fis = null;
        int[] data = null;

        try {
            File file = new File(fileName);
            fis = new FileInputStream(file);

            long size = file.length();

            data = new int[(int) size];

            int by, i = 0;
            while ((by = fis.read()) != -1) {
                data[i++] = toUnsignedInt((byte) by);
            }

        } catch (IOException ex) {
            System.err.println("Failed to open File!");
            System.err.println(ex);
            data = new int[] { -1 };
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception ex) {
                    data = new int[] { -1 };
                }
            }
        }
        return data;
    }
    
    void returnBinary(int value) {
        
        for(int i = 15; i>=0; i--) {
            int a = value & (1 << i);
            if(a > 0) a = 1;
            System.out.print( a + " " );
        }
        
        System.out.println( "\n" );
        
    }
}
