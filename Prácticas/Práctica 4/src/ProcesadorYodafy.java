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
import java.util.Random;


//
// Nota: si esta clase extendiera la clase Thread, y el procesamiento lo hiciera el método "run()",
// ¡Podríamos realizar un procesado concurrente! 
//
public class ProcesadorYodafy {
    
    private DatagramPacket paquete = null;
	
    // Para que la respuesta sea siempre diferente, usamos un generador de números aleatorios.
    private Random random;
	
    // Constructor que tiene como parámetro una referencia al socket abierto en por otra clase
    public ProcesadorYodafy(DatagramPacket paquete) {
        this.paquete=paquete;
	random=new Random();
    }
	
	
    // Aquí es donde se realiza el procesamiento realmente:
    String procesa(){		
		
	String peticion=new String(paquete.getData(),0,paquete.getLength());
	// Yoda reinterpreta el mensaje:
	String respuesta=yodaDo(peticion);
	// Convertimos el String de respuesta en una array de bytes:
        
        return respuesta;
    }
    // Yoda interpreta una frase y la devuelve en su "dialecto":
    
    private String yodaDo(String peticion) {
    // Desordenamos las palabras:
    String[] s = peticion.split(" ");
    String resultado="";
		
    for(int i=0;i<s.length;i++){
	int j=random.nextInt(s.length);
	int k=random.nextInt(s.length);
	String tmp=s[j];
			
	s[j]=s[k];
	s[k]=tmp;
    }
		
    resultado=s[0];
    for(int i=1;i<s.length;i++){
        resultado+=" "+s[i];
    }
		
    return resultado;
    }
}