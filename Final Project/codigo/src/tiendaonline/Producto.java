/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tiendaonline;

/**
 *
 * @author dani & jose
 */

public class Producto {
    private String descripcion;
    private int code;
    private int unidades;
    
    public Producto(int c, int u, String d){
        this.descripcion = d;
        this.unidades = u;
        this.code = c;
    }
    
    public void setCode(int cod){
        this.code = cod;
    }
    
    public void setDescripcion(String s){
        this.descripcion = s;
    }
    
    public void setUnidades(int u){
        this.unidades = u;
    }
    
    public String descripcion(){
        return this.descripcion;
    }
    
    public int codigo(){
        return this.code;
    }
    
    public int unidades(){
        return this.unidades;
    }
    
    public String toString(){
        return "Id: " + codigo() + ". " + descripcion() + ". Uds:  " + unidades();
    }
}
