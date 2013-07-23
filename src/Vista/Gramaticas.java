/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;
 
import java.io.*;
import java.util.StringTokenizer;
import javax.swing.JTextArea;

/**
 *
 * @author Administrador
 */
public class Gramaticas {

    Seleccion sentIf = new Seleccion();;
    int tam;
    int contador = 0;
    Tokens2 allTokens [];
    String token;

//    private Tokens2 [] abrir(){
//        String text = "";
//        JTextArea Texto = new JTextArea();
//        String str;
//        tam = 0;
//        try{
//            File abrir =  new File("..\\CompiladorToTo\\Tokens.txt");
//            if (abrir != null){
//                  FileReader archivo = new FileReader(abrir);
//                  BufferedReader leer = new BufferedReader(archivo);
//                  while((text = leer.readLine()) != null){
//                      Texto.append(text + "\n");
//                      tam++;
//                  }
//                  leer.close();
//                  allTokens = new Tokens2 [tam];
//                  String linea;
//                  StringTokenizer tokens = new StringTokenizer(Texto.getText(),"\n");
//                  int k = 0;
//                  while(tokens.hasMoreTokens()){
//                        linea = tokens.nextToken();
//                        StringTokenizer token = new StringTokenizer(linea);
//                        int i=0;
//                        allTokens[k] = new Tokens2();
//                        while(token.hasMoreTokens()){
//                            str = token.nextToken();
//                            if (i==0){ allTokens[k].setLinea(Integer.parseInt(str)); }
//                            if (i==1){ allTokens[k].setToken(str); }
//                            if (i==2){ 
//                                System.out.println("llamada en abrir");
//                                allTokens[k].setTipo(Tokens2.identificaToken(str)); }
//                            i++;
//                        }
//                        k++;
//                    }
//                }
//       return this.allTokens;
//       }catch(IOException ioe){System.out.println(ioe);}
//        return this.allTokens;
//    }
  

    Seleccion sentenciaIF (Tokens2 [] allTokens, int cont){
        contador = cont;
        this.allTokens = allTokens;
        token = this.getToken();
        if (token.equals("if")){
            token = this.getToken();
            if(token.equals("(")){
                token = this.getToken();
                while (!token.equals(")") && contador<allTokens.length ){
                    sentIf.setExpresion(sentIf.getExpresion()+token);
                    token = this.getToken();
                }
                if(contador>=allTokens.length){
                    sentIf.setExpresion("Error");
                    return sentIf;
                }else{
                    if(token.equals(")")){
                        token = this.getToken();
                        while (!token.equals("fi")  && !token.equals("else")){
                            sentIf.setBloque1(sentIf.getBloque1()+token);
                            token = this.getToken();
                        }
                        if(token.equals("fi")){
                                return sentIf;
                        }else{
                            if(contador>=allTokens.length){
                                sentIf.setExpresion("Error");
                                return sentIf;
                            }else{
                                if(token.equals("else")){
                                    token = this.getToken();
                                    while (!token.equals("if") && contador<this.allTokens.length ){
                                        sentIf.setBloque2(sentIf.getBloque2()+token);
                                        token = this.getToken();
                                    }
                                    if(token.equals("fi")){
                                            return sentIf;
                                    }else{
                                        if (contador>=this.allTokens.length){
                                            sentIf.setExpresion("Error");
                                            return sentIf;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return sentIf;
    }

    private String getToken(){
        String tokens = null;
        if (contador<this.allTokens.length){
            tokens = allTokens[contador].getToken();
            contador++;
        }
        return tokens;
    }
}
