/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package scaledaemon;

import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author icervantes
 */
public class SerialReader implements SerialPortEventListener{
 
    private BufferedReader inStream;
    private BufferedWriter socketWriter;
     
    // Constructor
    public SerialReader(InputStream is) throws IOException{
        inStream = new BufferedReader(new InputStreamReader(is));
    }
     
     
     
    @Override
    public void serialEvent(SerialPortEvent event) {
         
        String rawInput = null;
         
        switch(event.getEventType()){
        case SerialPortEvent.BI:
        case SerialPortEvent.CD:
        case SerialPortEvent.CTS:
        case SerialPortEvent.DSR:
        case SerialPortEvent.FE:
        case SerialPortEvent.OE:
        case SerialPortEvent.PE:
        case SerialPortEvent.RI:
        case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
            break;
             
        case SerialPortEvent.DATA_AVAILABLE:
            try {
                
                //clientSocket = connectionSocket.accept();
                //socketWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                
                if(inStream.ready()) {
                    rawInput = inStream.readLine();
                    if(rawInput == null){
                        System.out.println("No input on serial port");
                        System.exit(0);
                    }
                    //rawInput = rawInput.trim();
                    //rawInput = rawInput.split("KG")[0];
                    //if(isNumeric(rawInput)) {
                        System.out.println(rawInput);
                        WeightVo.weight = rawInput;
                        
                    //}
                }
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
            break;
             
        default:
            break;
             
        }
                 
    }
    
    public boolean isNumeric(String input) {
        try {
          Integer.parseInt(input);
          return true;
        } catch (NumberFormatException e) {
          return false;
        }
    }
    
}
