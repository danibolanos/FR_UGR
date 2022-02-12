//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket; 
import java.net.DatagramPacket;
import java.net.InetAddress;

public class YodafyClienteUDP {

    public static void main(String[] args) {
		
        byte[] buferEnvio = new byte[256];
        byte[] buferRecepcion = new byte[256];
		
	// Nombre del host donde se ejecuta el servidor:
	String host="localhost";
	// Puerto en el que espera el servidor:
	int port=8989;
		
	// Datagramas para la conexión UDP
        DatagramSocket socket = null;
        DatagramPacket paquete = null;
        DatagramPacket paqueteModificado = null;
        InetAddress direccion = null;
        
        String respuesta;
		
	try {
        
	socket = new DatagramSocket();
        direccion = InetAddress.getByName("localhost");
        
	buferEnvio="Al monte del volcan debes ir sin demora".getBytes();
        
        paquete = new DatagramPacket(buferEnvio, buferEnvio.length, direccion, port);
        socket.send(paquete);
        
        paqueteModificado = new DatagramPacket(buferRecepcion, buferRecepcion.length);        
        socket.receive(paqueteModificado);
        
        respuesta = new String(paqueteModificado.getData());
			
	// MOstremos la cadena de caracteres recibidos:
	System.out.println("Recibido: ");
        System.out.println(respuesta);
        System.out.println(" ");
        
        socket.close();
			
	// Excepciones:
	} catch (IOException e) {
            System.err.println("Error de entrada/salida al abrir el socket.");
	}
    }
}