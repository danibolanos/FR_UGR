/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiendaonline;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author dani & jose
 */

public class Servidor{
    
    public static void main(String[] args) {
    // Puerto de escucha
    int port=8989;
    Almacen almacen;
    
                
    ServerSocket serverSocket;
	
    
    try {               
	// Abrimos el socket en modo pasivo, escuchando el en puerto indicado por "port"
	//////////////////////////////////////////////////
	// ...serverSocket=... (completar)
	//////////////////////////////////////////////////
        
        almacen = new Almacen(new File("./productos/lista_productos.txt"));
        almacen.cargarFichero();
        serverSocket = new ServerSocket(port);

	// Mientras ... siempre!
	do {
            
	// Aceptamos una nueva conexión con accept()
	/////////////////////////////////////////////////        
	// socketServicio=... (completar)
	//////////////////////////////////////////////////
        
        
        Socket socketServicio = serverSocket.accept();
        Tienda tienda = new Tienda(socketServicio,almacen);
        tienda.start();
        
        } while (true);
			
	} catch (IOException e) {
            System.err.println("Error al escuchar en el puerto "+port);
	}
    }
}
