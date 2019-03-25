import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramSocket; 
import java.net.DatagramPacket;
import java.net.InetAddress;

//
// YodafyServidorIterativo
// (CC) jjramos, 2012
//
public class YodafyServidorIterativo {

    public static void main(String[] args) {
	
    // Puerto de escucha
    int port=8989;
    byte[] buferRecepcion = new byte[256];
    byte[] buferEnvio = new byte[256];
    // Número de bytes leídos
    int bytesLeidos=0;
    
    DatagramPacket paquete = null;            
    DatagramSocket socketServidor = null;
    DatagramPacket paqueteModificado = null;
    InetAddress direccion;
		
    try {               
        
	do {
        
        socketServidor = new DatagramSocket(port);   
                
        paquete = new DatagramPacket(buferRecepcion, buferRecepcion.length);
        
        socketServidor.receive(paquete);
				
	// Creamos un objeto de la clase ProcesadorYodafy, pasándole como 
	// argumento el nuevo socket, para que realice el procesamiento
	// Este esquema permite que se puedan usar hebras más fácilmente.
        
	ProcesadorYodafy procesador=new ProcesadorYodafy(paquete);
        
        paquete.setData(procesador.procesa().getBytes());
        
        socketServidor.send(paquete);
        
        socketServidor.close();
				
	} while (true);
			
	} catch (IOException e) {
            System.err.println("Error al escuchar en el puerto "+port);
	}
    }
}