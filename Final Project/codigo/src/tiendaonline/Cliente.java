
package tiendaonline;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 *
 * @author dani & jose
 */

public class Cliente {
    
    /*void login() throws IOException{
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        String login, password;
        
        System.out.println("Introduzca su usuario");
        login = entrada.readLine();
        System.out.println("Introduzca su contraseña");
        password = entrada.readLine();
        
        buferEnvio=login+'?'+password;
        
        outPrinter.println(buferEnvio);
    }*/
       

    public static void main(String[] args) throws IOException {
		
        String buferEnvio, buferRecepcion;
        String productos;
        String login, password, producto, direccion_tarjeta, confirmar="";
        boolean sig_paso=false, seguir_comprando=true, valido;
        int opcion=0;
        
        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
		
	// Nombre del host donde se ejecuta el servidor:
	String host="localhost";
	// Puerto en el que espera el servidor:
	int port=8989;
		
	// Socket para la conexión TCP
	Socket socketServicio=null;
		
	try {
        
	// Creamos un socket que se conecte a "host" y "port":
        
        socketServicio = new Socket(host, port);
	
        PrintWriter outPrinter = new PrintWriter(socketServicio.getOutputStream(), true);
        BufferedReader inReader = new BufferedReader(new InputStreamReader(socketServicio.getInputStream()));
		
        // Elegir registrarse o logearse
        while(opcion!=1 && opcion!=2){
            System.out.println("Elija una opción:\n 1 Registrar un nuevo usuario\n 2 Autenticarse");
            opcion = Integer.parseInt(entrada.readLine());
        }
        
        outPrinter.println(opcion);
        
        if(opcion==1)
        while(!sig_paso){
            System.out.println("Introduzca su nuevo usuario");
            login = entrada.readLine();
            System.out.println("Introduzca su nueva contraseña");
            password = entrada.readLine();
        
            buferEnvio=login+'?'+password;
        
            outPrinter.println(buferEnvio);
        
            buferRecepcion = inReader.readLine();
        
            if(!buferRecepcion.equals("OK")){
                System.out.println(buferRecepcion);
            }
            else
             sig_paso=true;
        }
        
	// Login del usuario(falta hacer que si no lo ha hecho bien, se vuelva a pedir)
        else
        while(!sig_paso){
            System.out.println("Introduzca su usuario");
            login = entrada.readLine();
            System.out.println("Introduzca su contraseña");
            password = entrada.readLine();
        
            buferEnvio=login+'?'+password;
        
            outPrinter.println(buferEnvio);
        
            buferRecepcion = inReader.readLine();
        
            if(!buferRecepcion.equals("OK")){
                System.out.println(buferRecepcion);
            }
            else
             sig_paso=true;
        }
        //Seleccionar producto(
        
        while(seguir_comprando){
        
            sig_paso=false;
        
            while(!sig_paso){
                
                productos = inReader.readLine();
                productos = productos.replace("ÇçÇ", "\n");
                System.out.println("\nLa lista de productos disponibles es: \n");
                System.out.println(productos);
                System.out.println("Introduzca el número del producto que desea: ");
                producto = entrada.readLine();
            
                System.out.println("Introduzca el número de unidades que desea de dicho producto: ");
                producto += '?'+entrada.readLine();
            
                outPrinter.println(producto);
        
                buferRecepcion = inReader.readLine();
        
                if(!buferRecepcion.equals("OK")){
                    System.out.println(buferRecepcion);
                }
                else
                    sig_paso = true;
            }
            valido = false;
            while(!valido){
                System.out.println("¿Desea seguir comprando más productos? (Si/No):");
                confirmar = (entrada.readLine());
                if(confirmar.equals("Si")){
                    valido=true;
                }
                if (confirmar.equals("No")){
                    valido=true;
                    seguir_comprando=false;
                }
            }
            outPrinter.println(confirmar);
            buferRecepcion = inReader.readLine();
        
            if(!buferRecepcion.equals("OK")){
                System.out.println(buferRecepcion);
            }
        }
        //Datos como dirección y tarjeta de crédito
        
        sig_paso=false;
        
        while(!sig_paso){
            System.out.println("Introduzca la dirección de facturación");
            direccion_tarjeta = (entrada.readLine());
        
            System.out.println("Introduzca el número de su tarjeta de crédito");
            String tarjeta = entrada.readLine();
            //Condición 16 numeros
            direccion_tarjeta += "?"+ tarjeta;
        
            outPrinter.println(direccion_tarjeta);
        
            buferRecepcion = inReader.readLine();
        
            if(!buferRecepcion.equals("OK")){
                System.out.println(buferRecepcion);
            }
            else
                sig_paso = true;
        }
        
        
        //Confirmación del pedido
        
        sig_paso=false;
        
        while(!sig_paso){
            System.out.println("Confirmar Pedido(Si/No):");
            confirmar = (entrada.readLine());
            
            outPrinter.println(confirmar);
            
            buferRecepcion = inReader.readLine();
        
            if(!buferRecepcion.equals("OK")){
                System.out.println(buferRecepcion);
            }
            else
                sig_paso = true;
        }
        
        System.out.println("Gracias por utilizar nuestra aplicación");
        
        socketServicio.close();
			
	// Excepciones:
	} catch (UnknownHostException e) {
            System.err.println("Error: Nombre de host no encontrado.");
	} catch (IOException e) {
            System.err.println("Error de entrada/salida al abrir el socket.");
	}
    }
}
