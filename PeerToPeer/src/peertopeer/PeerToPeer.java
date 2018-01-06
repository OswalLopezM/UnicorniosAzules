/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peertopeer;

import Conexion.HiloPrincipalServidor;
import Conexion.PeticionCoordinador;
import ConexionArchivos.HiloEnvioArchivo;
import ConexionArchivos.HiloPrincipalArchivo;
import ConexionArchivos.HiloRecepcionArchivo;
import DAO.DAOUsuario;
import Dominio.Usuario;
import Interfaz.Interfaz;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author oswal
 */
public class PeerToPeer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new DAOUsuario().eliminarUsuarios();
        String entradaTeclado="";
        while(!entradaTeclado.equals("0")){
            System.out.println("¿QUE DESEA HACER?");
            System.out.println("5: Registrar usuario");
            System.out.println("6: Salir PeerToPeer");
            System.err.println("0: Salir");
            Scanner entradaEscaner = new Scanner (System.in); //Creación de un objeto Scanner
            entradaTeclado = entradaEscaner.nextLine (); //Invocamos un método sobre un objeto Scanner
            /*if ((entradaTeclado.equals("1"))){
                new HiloPrincipalServidor().start();
            }else if((entradaTeclado.equals("2"))){
                
            }else if((entradaTeclado.equals("3"))){
                //new HiloEnvioArchivo().start();
            }else if((entradaTeclado.equals("4"))){
                new HiloRecepcionArchivo().start();
            }else*/
            if(entradaTeclado.equals("5")){
                PeerToPeer.registrarmeConCoordinador();
                new HiloPrincipalServidor().start();
                new HiloPrincipalArchivo().start();
            }else if(entradaTeclado.equals("6")){
                PeerToPeer.salirmeConCoordinador();
            }
        }
    }
    
    public static void registrarmeConCoordinador(){
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            Usuario usuario = new PeticionCoordinador().AgregarPeerToPeer("REGISTRO;"+ip);
            new DAOUsuario().agregarUsuario(usuario);
        } catch (UnknownHostException ex) {
            Logger.getLogger(PeerToPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public static void salirmeConCoordinador(){
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            new PeticionCoordinador().salirPeerToPeer("SALIR;"+ip);
            
        } catch (UnknownHostException ex) {
            Logger.getLogger(PeerToPeer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}