
package Vista;


import java.io.BufferedReader;
import java.io.File;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.*;


public class Archivo {

    char [] codigo;
    String name;
    File archivo=null;

    public String getName() {
        return name;
    }

    public void nuevo(){
        archivo=null;
    }

    public JTextArea abrir(JFrame jFrame){
        
        String text = "";
        JTextArea Texto = new JTextArea();
        try{
            JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
            fc.showOpenDialog(jFrame);
            File abrir = fc.getSelectedFile();
            archivo = abrir;
            if (abrir != null){
              name = fc.getSelectedFile().getName();
              jFrame.setTitle(name);
              FileReader archivo = new FileReader(abrir);
              BufferedReader leer = new BufferedReader(archivo);
              while((text = leer.readLine()) != null){
                  Texto.append(text + "\n");
              }
              leer.close();
            }
        }catch(IOException ioe){
            System.out.println(ioe);
        }
        return Texto;
    }
    
    private void comen1(char []codigo){
        int let = 0;
        //Para eliminar el comentario sencillo
        while(let<codigo.length-1){
            if(codigo[let]=='/' && codigo[let+1]=='/'){
                    codigo[let] = ' ';
                    codigo[let+1] = ' ';
                    let++;
                    let++;
                    while(let<codigo.length && codigo[let]!='\n' ){
                        codigo[let] = ' ';
                        let++;
                    }
            }
            else{
               let++;
            }
       }
    }
    
    
    public void comen2(char []codigo){
        int let=0;
        //Para eliminar el comentario /*
        while(let<codigo.length-1){
            if(codigo[let]=='/' && codigo[let+1]=='*'){
                codigo[let] = ' ';
                codigo[let+1] = ' ';
                let++;
                let++;
                while(let<(codigo.length-1)){
                    if(!(codigo[let]=='*' && codigo[let+1]=='/')){
                        codigo[let] = ' ';
                        let++;
                    }else{
                        codigo[let] = ' ';
                        codigo[let+1] = ' ';
                        let=codigo.length;
                    }

                }
                if(let<codigo.length){
                    codigo[let]=' ';
                    codigo[let-1]=' ';
                }
            }
            else{let++;}
        }
    }

    public String deleteComen (JFrame jFrame, JEditorPane Texto){
       String cod = Texto.getText();
        JTextArea Text = new JTextArea();
        codigo = Texto.getText().toCharArray();
        comen1(codigo);
        comen2(codigo);
        String cadenita=String.valueOf(codigo);
        return cadenita;
    }

    public void guardar(JFrame jFrame, JEditorPane Texto){
        if (archivo != null){
            try{
                String nombre = archivo.getName();
                jFrame.setTitle(nombre + " " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND));
                FileWriter fwrite = new FileWriter(archivo);
                fwrite.write(Texto.getText());
                fwrite.close();
            }catch(IOException ioe){
                System.out.println(ioe);
            }
        }
        else
            guardarComo(jFrame,Texto);
    }
    
    public void guardarComo(JFrame jFrame, JEditorPane Texto){
        try{
            JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
            fc.showSaveDialog(jFrame);
            File guardar = fc.getSelectedFile();
            archivo = guardar;
            if (guardar != null){
                String nombre = fc.getSelectedFile().getName();
                jFrame.setTitle(nombre + " " + Calendar.getInstance().get(Calendar.HOUR_OF_DAY) + ":" + Calendar.getInstance().get(Calendar.MINUTE) + ":" + Calendar.getInstance().get(Calendar.SECOND));
                FileWriter fwrite = new FileWriter(guardar);
                fwrite.write(Texto.getText());
                fwrite.close();
            }
        }catch(IOException ioe){
            System.out.println(ioe);
        }

    }

    public void cerrar(){

    }

}
