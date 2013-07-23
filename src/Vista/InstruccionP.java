/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

//@author $Erick Tovar

public class InstruccionP {
    String instruccion;
    String dato;

    public InstruccionP(String instrucción, String dato) {
        this.instruccion = instrucción;
        this.dato = dato;
    }
    
    public String getDato() {
        return dato;
    }

    public void setDato(String dato) {
        this.dato = dato;
    }

    public String getInstruccion() {
        return instruccion;
    }

    public void setInstruccion(String instruccion) {
        this.instruccion = instruccion;
    }
}
