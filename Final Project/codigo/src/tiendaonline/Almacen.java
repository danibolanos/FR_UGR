/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiendaonline;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author dani & jose
 */

public class Almacen {
    
   public ArrayList<Producto> lista = new ArrayList(); 
   public File fichero;
   
   public Almacen(File f){
       this.fichero=f;
   }
   
   public void cargarFichero(){
   Scanner s = null;

   try {
   // Leemos el contenido del fichero
   s = new Scanner(fichero);

   // Leemos linea a linea el fichero
   while (s.hasNextLine()) {
        String l = s.nextLine();
        String codigo = l.substring(0, l.indexOf("?"));
	String unidades = l.substring(l.indexOf("?")+1, l.indexOf(" ")); 	// Guardamos las lineas en un String
        String descripcion = l.substring(l.indexOf(" ")+1);
	lista.add(new Producto(Integer.parseInt(codigo), Integer.parseInt(unidades),descripcion));      // Guardamos la linea
   }

   } catch (FileNotFoundException ex) {
       System.out.println("Error: " + ex.getMessage());
   } finally {
   // Cerramos el fichero tanto si la lectura ha sido correcta o no
   try {
      if (s != null)
      s.close();
   } catch (Exception ex2) {
      System.out.println("Error: " + ex2.getMessage());
   }
   }
  }
   
  public void escribirFichero() throws FileNotFoundException, UnsupportedEncodingException, IOException {

    ArrayList<Producto> lineas = lista;
    Writer out = null;
    out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("./productos/lista_productos.txt"), "UTF-8"));
    // Escribimos linea a linea en el fichero
    for (Producto p : lineas) {
        try {
            out.write(p.codigo()+ "?" + p.unidades()+" "+p.descripcion()+"\n");
        } catch (IOException ex) {
            System.out.println("Error: " + ex.getMessage());
        }
    }
    try {
        out.close();
    } catch (IOException ex3) {
        System.out.println("Error: " + ex3.getMessage());
    }
  }

/*    
  public void reponerProd(int c, int uds){
      lista.get(c).setUnidades(lista.get(c).unidades()+uds);
  }*/
  
  public String nombreProducto(int code){
      String nombre=null;
      for(Producto c:lista){
          if(c.codigo()==code)
              nombre=c.descripcion();
      }
      return nombre;
  }
  
  public String imprimePantalla(){
    String revista = new String();
    for(Producto c:lista)
       revista += c + "ÇçÇ";
    return revista;
   }
      public static boolean isNumeric(String cadena){
    	try {
    		Long.parseLong(cadena);
    		return true;
    	} catch (NumberFormatException nfe){
    		return false;
    	}
    }
 }