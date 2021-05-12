package main.serial;

import jssc.SerialPort;
import jssc.SerialPortException;

public class SerialReaderJSSC {
    public SerialReaderJSSC(){
        String portName = "COM3";
        SerialPort serialPort = new SerialPort(portName);
        try {
            serialPort.openPort();//Open serial port
            serialPort.setParams(9600, 8, 1, 0);//Set params.
            while(true) {
                byte[] buffer = serialPort.readBytes(10);
                if(buffer!=null) {
                    for(byte b:buffer) {
                        System.out.print(b);
                    }
                }
            }
        } catch (SerialPortException e) {
            e.printStackTrace();
        }
    }
}
