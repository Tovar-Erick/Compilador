/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import java.io.*;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;

/**
 *
 * @author Anthonio
 */
public class Lexico {
    String name;   //nombre del archivo
    String str;
    String linea;
    JTextArea VecTokens = new JTextArea();
    JTextArea ArchivoTokens = new JTextArea();
    JTextArea Error = new JTextArea();
    JTextArea areaCodigo = new JTextArea();
    StringTokenizer tokens;
    String codigo;
    int line=0;
    
    BuscaReservadas buscarRes = new BuscaReservadas();

    Lexico (String codigo, String name){
        this.codigo = codigo;
        this.name = name;
    }

    public String getError() {return Error.getText();}

    public String obtenerCodigo (){
        //Este código separa los tokens correctamente
        tokens = new StringTokenizer(codigo,"\n");
        while(tokens.hasMoreTokens()){
            linea = tokens.nextToken();
            StringTokenizer token = new StringTokenizer(linea);
            areaCodigo.append("\n");
            line++;
            areaCodigo.append(FuncionTokens(linea, line));
        }
        System.out.println(areaCodigo);
        return areaCodigo.getText();
    }

    //TODO: Modificar VecTokens para que salga bien el tipo de token que debe de ser, 
    //usar ArchivoTokens.append(tokenT.linea() + "\n");
    public String obtenerTokens (){
        String identificadores = "";
        tokens = new StringTokenizer(codigo,"\n");
        int lin = 0;
        Tokens2 tokenT = new Tokens2();
        VecTokens.setText("");
        tokens = new StringTokenizer(areaCodigo.getText(),"\n");
        while(tokens.hasMoreTokens()){
            tokenT = new Tokens2();
            linea=tokens.nextToken();
            lin++;
            StringTokenizer token = new StringTokenizer(linea);
            while(token.hasMoreTokens()){
                str = token.nextToken();
                tokenT.setLinea(lin);
                if(buscarRes.autoIdentificador(str)){
                    if(buscarRes.busca(str)){
                        VecTokens.append(lin + "\t"+str);
                        for (int i=0; i<10-str.length(); i++) {
                            VecTokens.append(" ");
                        }
                        VecTokens.append(" Palabra Reservada" + "\n");
                        tokenT.setToken(str);
                        tokenT.setTipo(Nodo.tipoToken.PalabraReservada);
                        
                    }else{
                        tokenT.setToken(str);
                        tokenT.setTipo(Nodo.tipoToken.Identificador);
                        identificadores += (str + "\n");
                        VecTokens.append(lin + "\t"+str);
                        for (int i=0; i<10-str.length(); i++) {
                            VecTokens.append(" ");
                        }
                        VecTokens.append(" Identificador" + "\n");
                    }
                }else if (buscarRes.buscasimboesp(str)){
                    
                    tokenT.setToken(str);
                    //System.out.println("llamada en lexico");
                    tokenT.setTipo(tokenT.identificaToken(str));
                    //System.out.println("se asigna un "+tokenT.getTipo());
                    VecTokens.append(lin + "\t"+str);
                        for (int i=0; i<10-str.length(); i++)
                            VecTokens.append(" ");
                        VecTokens.append(" SimboloEspecial" + "\n");
                }else if (Character.isDigit(str.charAt(0))){
                    
                    tokenT.setToken(str);
                    tokenT.setTipo(Nodo.tipoToken.Numero);
                    VecTokens.append(lin + "\t"+str);
                        for (int i=0; i<10-str.length(); i++)
                            VecTokens.append(" ");
                        VecTokens.append(" Digito"+ "\n");
                }else{

                    tokenT.setToken(str);
                    tokenT.setTipo(Nodo.tipoToken.Error);
                    Error.append("Error en línea"+ lin + "\t"+str);
                        for (int i=0; i<10-str.length(); i++)
                            Error.append(" ");
                        Error.append(" No valido" + "\n");
                }
                ArchivoTokens.append(tokenT.linea() + "\n");
            }
        }
        try{
            tokenT.setToken("EOF");
            tokenT.setTipo(Nodo.tipoToken.EOF);
            tokenT.setLinea(lin);
            ArchivoTokens.append(tokenT.linea());
            
            File fichero1 = new File ("..\\CompiladorToto\\"+name+"Tokens.txt");
            File archivo2 = new File(fichero1.toString());
            FileWriter canal2 = new FileWriter(archivo2);
            BufferedWriter buffer = new BufferedWriter(canal2);
            PrintWriter escritor2 = new PrintWriter(buffer);
            escritor2.print(ArchivoTokens.getText());
            escritor2.close();

            fichero1 = new File ("..\\CompiladorToTo\\iden.txt");
            archivo2 = new File(fichero1.toString());
            canal2 = new FileWriter(archivo2);
            buffer = new BufferedWriter(canal2);
            escritor2 = new PrintWriter(buffer);
            escritor2.print(identificadores);
            escritor2.close();
        }
        catch (IOException ex){
            JOptionPane.showMessageDialog(null,"Error no se creo el archivo!", "Oops! Error", JOptionPane.ERROR_MESSAGE);
        }
//        return VecTokens.getText();
        return ArchivoTokens.getText();

    }
    
    private String FuncionTokens(String Cadena, int line){
            
        String cadenat="";
        String Cadena2="";
        int caso=0;
        int cont=0;
        char letra;
        int contc=0;

        for(int i=0; i<Cadena.length(); i++){
            letra = Cadena.charAt(i);

            switch (caso) {
            case 0:
                    Cadena2=Cadena2+letra;
                    if(Character.isLetterOrDigit(letra)){
                        if(Character.isLetter(letra)){  caso=1; }
                        if(Character.isDigit(letra)){   caso=2; }
                    }else{
                        caso=3;
                        if(letra=='-'){ contc =1 ; }
                        if(letra=='+'){ contc =2 ; }
                        if(letra=='='){ contc =3 ; }
                        if(letra=='/'){ contc =4 ; }
                        if(letra=='*'){ contc =5 ; }
                        if(letra=='<'){ contc =6 ; }
                        if(letra=='>'){ contc =7 ; }
                        if(letra=='!'){ contc =8 ; }

                    }
                    ; break;
            case 1:
                    if(Character.isLetterOrDigit(letra)){
                        Cadena2 = Cadena2 + letra;
                        caso=1;
                    }else{
                        caso=4;
                        i--;
                    }
                    if(i==(Cadena.length()-1)){}
                    ; break;
            case 2:
                    if(Character.isDigit(letra)){
                        Cadena2 = Cadena2 + letra;
                        caso=2;
                    }else{
                        caso=4;
                        i--;
                        if(letra=='.'&&cont==0){
                            Cadena2 = Cadena2 + letra;
                            caso=2;
                            cont++;
                            i++;
                        }else{}
                    };
                    if(i==(Cadena.length()-1)){}
                    break;
            case 3:
                    if(Character.isLetterOrDigit(letra)){
                        caso=4;
                        i--;
                    }else{
                        if(letra=='-'&&contc==1){ contc =14;}
                        if(letra=='+'&&contc==2){ contc =14;}
                        if(letra=='='&&contc==3){ contc =14;}
                        if(letra=='='&&contc==6){ contc =14;}
                        if(letra=='='&&contc==7){ contc =14;}
                        if(letra=='='&&contc==8){ contc =14;}
                        if(letra=='/'&&contc==4){ contc =14;}
                        if(letra=='*'&&contc==4){ contc =14;}
                        if(letra=='/'&&contc==5){ contc =14;}
                        if(letra=='<'&&contc==6){ contc =14;}
                        if(letra=='>'&&contc==7){ contc =14;}
                        if(contc==14){
                            Cadena2 = Cadena2 + letra;
                            cadenat= cadenat +" "+ Cadena2;
                            caso=4;
                        }
                        else{
                            caso=4;
                            i--;
                        }
                    }
                    break;
            case 4:
                    if(contc==14){}
                    else{cadenat= cadenat +" "+ Cadena2 ;}
                    Cadena2= "";
                    caso=0;
                    cont=0;
                    contc=0;
                    i--;
                    break;
            default:
                ; break;
            }
        }
        if(caso==4){
        }else{cadenat=cadenat +" "+ Cadena2;}
        return cadenat;
    }    
}
