/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

/**
 *
 * @author Administrador
 */
public class Seleccion {
    String expresion;
    String bloque1;
    String bloque2;
    Seleccion nuevo;

    public Seleccion() {
        this.expresion = "";
        this.bloque1 = "";
        this.bloque2 = "";
    }

    public String getBloque1() {return bloque1;}

    public void setBloque1(String bloque1) {this.bloque1 = bloque1;}

    public String getBloque2() {return bloque2;}

    public void setBloque2(String bloque2) {this.bloque2 = bloque2;}

    public String getExpresion() {return expresion;}

    public void setExpresion(String expresion) {this.expresion = expresion;}

    public Seleccion getNuevo() {return nuevo;}

    public void setNuevo(Seleccion nuevo) {this.nuevo = nuevo;}    
}
