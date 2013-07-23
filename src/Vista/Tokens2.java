/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import Vista.Nodo.tipoToken;
import java.io.*;
import java.util.StringTokenizer;
import javax.swing.JTextArea;


/**
 *
 * @author Anthonio
 */
public class Tokens2 {
    int linea;
    String token;
    Nodo.tipoToken tipo;

    public Tokens2() { 
    }

    public Tokens2(Tokens2 obj) {
        this.linea = obj.linea;
        this.token = obj.getToken();
        this.tipo = obj.getTipo();
    }
    
    

    public int getLinea() {return linea;}

    public void setLinea(int linea) {this.linea = linea;}

    public Nodo.tipoToken getTipo() {return tipo;}
    
        
    //Regresa un Nodo.tipoToken dependiendo de la cadena
    public static Nodo.tipoToken identificaToken(String simbolo){
        boolean ret = false;
        int i;
        String[] palabrasReservadas = {"if", "then", "else", "fi", "do", "until", 
                                       "while", "read", "write", "float", "int", 
                                       "bool", "program"};
        
        for (i=0; i<palabrasReservadas.length && !ret; i++){
            if(simbolo.equals(palabrasReservadas[i])){
                ret=true;
            }
        }
        if(ret){
            return Nodo.tipoToken.PalabraReservada;
        }
            
        
        String[] simbolosEspeciales = {"+", "-", "*", "/", "<", "<=", ">", ">=", 
                                       "==", "!=", "=", ";", ",", "(", ")", "{", 
                                       "}", "++", "--","\n"};
    
        for (i=0; i<simbolosEspeciales.length; i++){
            if(simbolo.equals(simbolosEspeciales[i])){
                //System.out.println("Se encontró como i="+i);
                break;
            }
        }
        //"+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "=", ";", ",", "(", ")", "{", "}", "++", "--", "\n"
        // 0    1    2    3    4     5     6   7      8     9   10    11   12   13   14   15  16    17    18    19
        switch(i){
            case 0:
                return Nodo.tipoToken.Suma;
            case 1:
                return Nodo.tipoToken.Resta;
            case 2:
                return Nodo.tipoToken.Multiplicacion;
            case 3:
                return Nodo.tipoToken.Division;
            case 4:
                return Nodo.tipoToken.MenorQue;
            case 5:
                return Nodo.tipoToken.MenorOIgualQue;  
            case 6:
                return Nodo.tipoToken.MayorQue;
            case 7:
                return Nodo.tipoToken.MayorOIgualQue;
            case 8:
                return Nodo.tipoToken.IgualIgual;
            case 9:
                return Nodo.tipoToken.Diferente;
            case 10:
                return Nodo.tipoToken.Asignacion;
            case 11:
                return Nodo.tipoToken.PuntoyComa;
            case 12:
                return Nodo.tipoToken.Coma;
            case 13:
                return Nodo.tipoToken.ParentesisIzquierdo;
            case 14:
                return Nodo.tipoToken.ParentesisDerecho;
            case 15:
                return Nodo.tipoToken.LlaveIzq;
            case 16:
                return Nodo.tipoToken.LlaveDer;
            case 17:
                return Nodo.tipoToken.SumaUnitaria;
            case 18:
                return Nodo.tipoToken.RestaUnitaria;    
            case 19:
                return Nodo.tipoToken.EOF;
            default:
                return Nodo.tipoToken.Error;
        }
    }

    public void setTipo(Nodo.tipoToken tipo) {this.tipo = tipo;}

    public String getToken() {return token;}

    public void setToken(String token) {this.token = token;}

    public String linea (){
        String res;
        res = String.valueOf(linea) + " " + this.token + " " + this.tipo.toString();
        return res;
    }
    public static Tokens2 [] allTokens(String name){
        int tam;
        Tokens2 allTokens [] = null;
        String text = "";
        JTextArea Texto = new JTextArea();
        String str;
        tam = 0;
        try{
                File abrir =  new File("..\\CompiladorToTo\\"+name+"Tokens.txt");
                if (abrir != null){
                  FileReader archivo = new FileReader(abrir);
                  BufferedReader leer = new BufferedReader(archivo);
                  //Buscamos conocer el número de líneas en el archivo, lo guardamos en tam
                  while((text = leer.readLine()) != null){
                      Texto.append(text + "\n");
                      tam++;
                  }
                  leer.close();
                  allTokens = new Tokens2 [tam];
                  String lineaa;
                  StringTokenizer tokens = new StringTokenizer(Texto.getText(),"\n");
                  int k = 0;
                  while(tokens.hasMoreTokens()){
                        lineaa = tokens.nextToken();
                        StringTokenizer tokenn = new StringTokenizer(lineaa);
                        int i=0;
                        allTokens[k] = new Tokens2();
                        while(tokenn.hasMoreTokens()){
                            str = tokenn.nextToken();
                            if (i==0){ 
                                allTokens[k].setLinea(Integer.parseInt(str)); }
                            if (i==1){ 
                                allTokens[k].setToken(str); }
                            if (i==2){ 
                                allTokens[k].setTipo(Nodo.tipoToken.valueOf(str)); }
                            i++;
                        }
                      k++;
                    }
                }
       }catch(IOException ioe){
                System.out.println(ioe);
       }finally{
            return allTokens;
        }
    }    
}

