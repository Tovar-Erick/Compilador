/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import java.util.ArrayList;

/**
 *
 * @author Administrador
 */
public class Nodo {
    private ArrayList<Nodo> nodo;   //Hijos
    private String dato;            //Nombre de token
    private tipoToken token;        //Tipo de token
    private String comando;         //Comparacion
    private String tipoDato;        //Tipo de dato
    private float valorNum;         //Valor de variale numerica, si es entero será truncado
    String lineas;

    //Enumeraciones para el tipo de valor
    public static enum tipoToken{
        Identificador, PalabraReservada, 
        ParentesisIzquierdo, ParentesisDerecho,  
        LlaveIzq, LlaveDer,   
        Coma, PuntoyComa,          
        Asignacion, IgualIgual, Diferente,       
        MenorQue, MenorOIgualQue, MayorQue, MayorOIgualQue, 
        Division, Multiplicacion, 
        Suma, SumaUnitaria, Resta, RestaUnitaria,     
        Numero, Error, EOF
            
    }
    
    public static enum KindToken{
        WHILE,  IF,  FI,  ELSE,   DO,  UNTIL,
        READ,   WRITE,  
        ID,     EXP,  NUM,
        BOOL,  INT,    FLOAT
    }
    
    public float getValorNumFloat() {
        return valorNum;
    }

    public int getValorNumInt() {
        return (int)valorNum;
    }

    public void setValorNum(float valorNum) {
        this.valorNum = valorNum;
    }

    public String getComando() {
        return comando;
    }

    public void setComando(String comando) {
        this.comando = comando;
    }

    public String getTipoDato() {
        return tipoDato;
    }

    public void setTipoDato(String tipoDato) {
        this.tipoDato = tipoDato;
    }
    
    

    Nodo(){this.nodo=new <Nodo>ArrayList();}

    Nodo(String dato, tipoToken tipoToken, String tipoDato){
	this.dato=dato;
        this.token = tipoToken;
        this.nodo=new <Nodo>ArrayList();
        this.tipoDato=tipoDato;
        this.comando="";
        this.lineas="";
    }
    
    Nodo(String dato, tipoToken tipoToken, String tipoDato, String linea){
	this.dato=dato;
        this.token = tipoToken;
        this.nodo=new <Nodo>ArrayList();
        this.tipoDato=tipoDato;
        this.comando="";
        this.lineas=""+linea;
    }
    
    Nodo(String dato, tipoToken tipoToken){
	this.dato=dato;
        this.token = tipoToken;
        this.comando="";
        tipoDato="";
        this.nodo=new <Nodo>ArrayList();
        this.lineas="";
    }
    

    public String getDato() {return dato;}

    public tipoToken getToken() {return token;}

    public void setDato(String dato, tipoToken token) {
        this.dato = dato;
        this.token = token;
    }

    public String getLinea() {
        return lineas;
    }

    public void addLinea(int linea) {
        if(this.lineas.equals(""))
            this.lineas=""+linea;
        else
            this.lineas += ", "+linea;
    }
    
    

    public void addNodo(Nodo tem){nodo.add(tem);}

    public ArrayList<Nodo> getNodo(){return this.nodo;}
}
