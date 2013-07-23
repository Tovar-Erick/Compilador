/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JTextArea;



/**
 *
 * @author Administrador
 */
public class BuscaReservadas {
    String[] palReservadasC = {"if", "then", "else", "fi", "do", "until", "while", "read", "write", "float", "int", "bool", "program"};
    String[] simbolosEspeciales = {"+", "-", "*", "/", "<", "<=", ">", ">=", "==", "!=", "=", ";", ",", "(", ")", "{", "}", "++", "--"};
    String[] Identificadores = new String [100];

    BuscaReservadas(){}
    public void addIdentificador() {
        String text = "";
        JTextArea Texto = new JTextArea();
        String str;
        int tam = 0;
        try{
        File abrir =  new File("..\\CompiladorToTo\\iden.txt");
                if (abrir != null){

                  FileReader archivo = new FileReader(abrir);
                  BufferedReader leer = new BufferedReader(archivo);

                  while((text = leer.readLine()) != null){
                      Identificadores[tam] = text;
                      Texto.append(text + "\n");
                      tam++;
                  }
                  
                  
                }

        }catch(IOException ioe){
                System.out.println(ioe.getMessage());
       }
    }
    
     public boolean buscaIden(String str){
        boolean ret = false;
        for (int i=0; i<this.Identificadores.length; i++){
            if(str.equals(this.Identificadores[i])){
                ret = true;
            }
        }
        return ret;
    }
    
    public boolean busca(String str){
        boolean ret = false;
        for (int i=0; i<this.palReservadasC.length; i++){
            if(str.equals(this.palReservadasC[i])){
                ret = true;
            }
        }
        return ret;
    }

    public boolean buscasimboesp(String str){
        boolean ret = false;
        for (int i=0; i<this.simbolosEspeciales.length; i++){
            if(str == null ? this.simbolosEspeciales[i] == null : str.equals(this.simbolosEspeciales[i])){
                ret = true;
            }
        }
        return ret;
    }

    public boolean autoIdentificador(String cadena){

        String cadena2="";
        char letra;
        int estado = 1;
        int i=0;
        while (i<cadena.length() && (estado==1 || estado==2)) {
            letra = cadena.charAt(i);
            i++;
            if(Character.isLetter(letra) && estado==1){
                cadena2+=letra;
                i++;
                estado=2;
            }else if(Character.isLetterOrDigit(letra) && estado ==2){
                cadena2+=letra;
                i++;
            } else{
                estado=3;
            }
        }

        if(cadena2.equals("")){
            return false;
        }
        return true;
    }
}
