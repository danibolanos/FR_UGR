/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiendaonline;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dani & jose
 */

public class Tienda extends Thread{
    
    BufferedReader inReader;
    private String archivo;
    Almacen almacen;
    private Socket socketServicio;
  //Tenemos una lista de usuarios registrados y comprueba si el usuario y la contraseña 
  //introducidos coincide con alguno de ellos
    
  public Tienda(Socket socketServicio, Almacen almacen) {
	this.socketServicio=socketServicio;
        this.almacen = almacen;
    }  
  
  public void run(){
        try {
            boolean sig_paso = false, seguir_comprando=true;
            PrintWriter outPrinter;
            outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
            //Registrar usuario o autenticarse

            socketServicio.getOutputStream();
            //Login
            if(registrarse(socketServicio)){
                socketServicio.getOutputStream();
                nuevoRegistro(socketServicio);
                outPrinter.println("OK");
                sig_paso = true;
            }
            else
                while(!sig_paso){
                    socketServicio.getOutputStream();
                    
                    if(loginVerificado(socketServicio)){
                        outPrinter.println("OK");
                        sig_paso = true;
                    }
                    else
                        outPrinter.println("Usuario o contraseña no válidos");
                }   

            //SeleccionarProducto
            
            while (seguir_comprando){
                sig_paso = false;
                while(!sig_paso){
                    outPrinter.println(almacen.imprimePantalla()); //Mandar Lista de productos
                    socketServicio.getOutputStream();
                    if(pedirProducto(socketServicio)){
                        outPrinter.println("OK");
                        sig_paso = true;
                    }
                    else
                        outPrinter.println("Producto no válido");
                }
                socketServicio.getOutputStream();
                seguir_comprando = seguirComprando(socketServicio);
                outPrinter.println("OK");
            }   //Direccion y tarjeta de crédito
            sig_paso = false;
            while(!sig_paso){
                socketServicio.getOutputStream();

                if(datosFacturacion(socketServicio)){
                    outPrinter.println("OK");
                    sig_paso=true;
                }
                else
                    outPrinter.println("Datos incorrectos");
            }   //Confirmar pedido
            
            sig_paso = false;
            while(!sig_paso){
                socketServicio.getOutputStream();
                
                if(confirmarPedido(socketServicio)){
                    outPrinter.println("OK");
                    sig_paso=true;
                }
                else
                    outPrinter.println("Datos incorrectos");
            }
        } catch (IOException ex) {
            Logger.getLogger(Tienda.class.getName()).log(Level.SEVERE, null, ex);
        }
	
  }
    
  public boolean buscarPassword(String pass) throws FileNotFoundException{
   boolean cond=false;   
   Scanner s = null;
   File f = new File("./passwords/users.txt");

   // Leemos el contenido del fichero
   s = new Scanner(f);
   // Leemos linea a linea el fichero
   while (s.hasNextLine() && !cond) {
       String userpass = s.nextLine(); 	// Guardamos las lineas en un String
       if(userpass.equals(pass))
           cond = true;
   }
   // Cerramos el fichero tanto si la lectura ha sido correcta o no
   try {
       if (s != null)
           s.close();
   } catch (Exception ex2) {
       System.out.println("Error: " + ex2.getMessage());
   }
   return cond;
  }
  
  boolean registrarse(Socket socketServicio) throws IOException{
        boolean cond = false;
        inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
	if(Integer.parseInt(inReader.readLine())==1)
            cond = true;
        return cond;
    }
  
  public void nuevoRegistro(Socket socketServicio) throws FileNotFoundException, UnsupportedEncodingException, IOException {
    String antiguo="";
    String userpass;
    Scanner s = null;

    inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
    userpass=inReader.readLine();
    
    archivo = "Usuario: " + userpass.substring(0,userpass.indexOf('?'));
    
    // Leemos el contenido del fichero
    s = new Scanner(new File("./passwords/users.txt"));
    // Leemos linea a linea el fichero
    while (s.hasNextLine()) {
        antiguo+=s.nextLine()+"\n";      // Guardamos la linea
    }
    // Cerramos el fichero tanto si la lectura ha sido correcta o no
    try {
        if (s != null)
            s.close();
    } catch (Exception ex2) {
        System.out.println("Error: " + ex2.getMessage());
    }
    antiguo+=userpass;
    Writer out = null;
    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./passwords/users.txt"), "UTF-8"));
    // Escribimos linea a linea en el fichero
        try {
            out.write(antiguo);
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    try {
        out.close();
    } catch (IOException ex3) {
        System.out.println("Error: " + ex3.getMessage());
    }
  }
    
    boolean loginVerificado(Socket socketServicio) throws IOException{
        boolean correcto;
        String logine;
        inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
	logine=inReader.readLine();
        archivo = "Usuario: ";
        archivo += logine.substring(0, logine.indexOf("?"));
        if( buscarPassword(logine) ){
            correcto = true;
        }
        else
            correcto = false;
        return correcto;
    }
    
    //Comprueba que el número del producto existe y que quedan copias disponibles
    
    boolean verificarProducto(int p, int n){
        boolean cond = false;
        for(Producto c: almacen.lista )
           if(c.codigo()==p && c.unidades()>=n){
               cond = true;
               c.setUnidades(c.unidades()- n);
           }
        return cond;
    }
    
    boolean pedirProducto(Socket socketServicio) throws IOException{
        boolean correcto=false;
        int producto;
        int num_producto;
        String recibido;
        inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
        recibido = inReader.readLine();
        producto = Integer.parseInt(recibido.substring(0,recibido.indexOf('?')));
	num_producto=Integer.parseInt(recibido.substring(recibido.indexOf('?')+1));
        if( verificarProducto(producto, num_producto) ) {
            correcto = true;
            archivo += "\nLista de productos encargados: \n";
            archivo += num_producto + " " + almacen.nombreProducto(producto);
        }
        return correcto;
    }
    
    boolean seguirComprando(Socket socketServicio) throws IOException{
        boolean seguir=false;
        String recibido;
        inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
        recibido = inReader.readLine();
        if(recibido.equals("Si")){
            seguir = true;
        }
        return seguir;
    }
    
    //la dirección y tarjeta de crédito deben guardarse y debe comprobar que la tarjeta
    //de crédito tiene 16 números
    
   public void escribirInfo(String d, java.util.Date nombre) throws FileNotFoundException, UnsupportedEncodingException, IOException {
    Writer out = null;
    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./envios/"+nombre+".txt"), "UTF-8"));
    // Escribimos linea a linea en el fichero
        try {
            out.write(d);
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    try {
        out.close();
    } catch (IOException ex3) {
        System.out.println("Error: " + ex3.getMessage());
    }
  }
    
    boolean datosFacturacion(Socket socketServicio) throws IOException{
        boolean cond = false;
        inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
	String info = inReader.readLine();
        String direccion = info.substring(0, info.indexOf('?'));
        String tarjeta = info.substring(info.indexOf('?')+1);
        if(tarjeta.length()==16 && almacen.isNumeric(tarjeta)){
            cond = true;
            archivo += "\nDireccion: " + direccion + "\nTarjeta: " + tarjeta;
        }
        return cond;
    }
    
    
    
    boolean confirmarPedido(Socket socketServicio) throws IOException{
        boolean confirmado;
        String confirmar;
        inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
	confirmar=inReader.readLine();
        if(confirmar.equals("Si")){
            confirmado = true;
            java.util.Date fecha = new Date();
            escribirInfo(archivo,fecha);
            almacen.escribirFichero();
        }
        else{
           if(confirmar.equals("No")){
               confirmado=true;
               almacen.lista.clear();
               almacen.cargarFichero();
           }
           else
               confirmado = false;
        }
        return confirmado;
    }
    
}
