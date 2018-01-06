/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ConexionArchivos;

import Dominio.Status;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.Socket;

/**
 *
 * @author oswal
 */
public class HiloEnvioArchivo extends Thread{
    
    Socket clientSocket;

    public HiloEnvioArchivo(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    
    
    
    public void run(){
        BufferedInputStream bis;
        BufferedOutputStream bos;
        int in;
        byte[] byteArray;
        //Fichero a transferir
        final String nombreArchivo = "test.pdf";

        try{
            final File localFile = new File( nombreArchivo );
            bis = new BufferedInputStream(new FileInputStream(localFile));
            bos = new BufferedOutputStream(clientSocket.getOutputStream());
            //Enviamos el nombre del fichero
            DataOutputStream dos=new DataOutputStream(clientSocket.getOutputStream());
            dos.writeUTF(localFile.getName());
            Status status = new Status(nombreArchivo,"envio");
            int acumulado = 0 , cont = 0;
            //Enviamos el fichero
            byteArray = new byte[(int) localFile.length()];
            while ((in = bis.read(byteArray)) != -1){
                bos.write(byteArray,0,in);
                if(cont%100 == 0){
                    acumulado = acumulado + in;
                    status.actualizarArchivo(acumulado);
                }
            }
            status.eliminarArchivo();
            System.out.println("Se ha terminado el envio del archivo");
            bis.close();
            bos.close();
            clientSocket.close();
        }catch ( Exception e ) {
            System.err.println(e);
        }
    }
}