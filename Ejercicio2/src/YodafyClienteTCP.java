//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class YodafyClienteTCP {

    public static void main(String[] args) {
		
        String buferEnvio;
        String buferRecepcion;
		
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
			
	// Escribimos en un string el mensaje que queremos enviar.
        
	buferEnvio="Al monte del volcan debes ir sin demora";
	
	// Enviamos el array 
        
        outPrinter.println(buferEnvio + "\n");
			
	// Leemos la respuesta del servidor.
        
        buferRecepcion = inReader.readLine();
			
	// Mostremos la cadena de caracteres recibidos:
        
	System.out.println("Recibido: ");

        System.out.println(buferRecepcion);
        
        System.out.println(" ");
			
	// Una vez terminado el servicio, cerramos el socket
        
        socketServicio.close();
			
	// Excepciones:
	} catch (UnknownHostException e) {
            System.err.println("Error: Nombre de host no encontrado.");
	} catch (IOException e) {
            System.err.println("Error de entrada/salida al abrir el socket.");
	}
    }
}