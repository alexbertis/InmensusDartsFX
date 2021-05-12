package main.serial;

import purejavacomm.CommPortIdentifier;
import purejavacomm.SerialPort;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

public class SerialReader {
    public SerialReader(){
        try {
            // Finding the port
            String portName = "COM3";
            CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(portName);

            // Opening the port
            SerialPort port = (SerialPort) portId.open("Example1", 1000);
            OutputStream outStream = port.getOutputStream();
            InputStream inStream = port.getInputStream();

            // Sending data
            //byte[] dataToSend = { 0x11, 0x22, 0x33, 0x44, 0x55 };
            //outStream.write(dataToSend, 0, dataToSend.length);

            Scanner scanner = new Scanner(inStream);
            // Receiving data
            while (true){
                if (scanner.hasNextLine()){
                    String line = scanner.nextLine();
                    // TODO: hacer cosas con la linea y en algun momento romper el bucle
                }
            }

            //System.out.println("Done");
            //port.close();
        } catch (Throwable thwble) {
            thwble.printStackTrace();
        }
    }
}
