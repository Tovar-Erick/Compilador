/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

//@author $Erick Tovar

import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JTextArea;


public class CodigoP {
    Nodo raiz;

    public CodigoP(Nodo raiz){
        this.raiz=raiz;
    }
  
    protected void muestraCodigoPenArea(JTextArea area){
        area.setText("");
        area.setText(codigoIntermedio());
    }

    
    public String codigoIntermedio(){
        ArrayList<Nodo> com=this.raiz.getNodo();
        String res="";
        for(Iterator x=com.iterator();x.hasNext();){
            res+=this.checkType(((Nodo)x.next()));
        }
        return res;
    }
    
    private String checkType(Nodo chk){
        String res=new String();
        switch(chk.getToken()){
            case Identificador:
                res+=this.genCode(Nodo.KindToken.ID,((Nodo)chk.getNodo().iterator().next()),chk.getDato());
                break;
            case PalabraReservada:
                if(chk.getDato().equals("if")){
                    res+=this.genCode(Nodo.KindToken.IF, chk, chk.getDato());
                }else if(chk.getDato().equals("while")){
                    res+=this.genCode(Nodo.KindToken.WHILE, chk, chk.getDato());
                }else if(chk.getDato().equals("do")){
                    res+=this.genCode(Nodo.KindToken.DO, chk, chk.getDato());
                }else if(chk.getDato().equals("int")){
                    res+=this.genCode(Nodo.KindToken.INT, chk, chk.getDato());
                }else if(chk.getDato().equals("float")){
                    res+=this.genCode(Nodo.KindToken.FLOAT, chk, chk.getDato());
                }else if(chk.getDato().equals("write")){
                    res+=this.genCode(Nodo.KindToken.WRITE, chk, chk.getDato());
                }else if(chk.getDato().equals("read")){
                    res+=this.genCode(Nodo.KindToken.READ, chk, chk.getDato());
                }
        }
        return res;
    }
    
    
    private String genCode(Nodo.KindToken kind,Nodo chk,String var){
        String res="";
        String lab1,lab2="";
        switch(kind){
            case EXP:
                Nodo help=(Nodo)chk.getNodo().get(0);
                String v=help.getDato();
                res+=this.expresion(help.getNodo().get(0));
                res+=this.expresion(help.getNodo().get(1));
                if(v.equals("<"))
                    res+="\nlrt";
                else if(v.equals(">"))
                    res+="\ngrt";
                else if(v.equals("<="))
                    res+="\nleq";
                else  if(v.equals(">="))
                    res+="\ngeq";
                else if(v.equals("=="))
                    res+="\nequ";
                else if(v.equals("!="))
                        res+="\nnot";
                break;
            case ID:
                res+="\nlda "+var;
                res+=this.expresion(((Nodo)chk.getNodo().iterator().next()));
                res+="\nsto";
                break;
            case IF:{
                ArrayList<Nodo> r=chk.getNodo();
                res+=this.genCode(Nodo.KindToken.EXP, r.get(0), "");
                lab1=this.genLabel();
                res+="\nfjp "+lab1;
                for(Iterator x=r.get(1).getNodo().iterator();x.hasNext();){
                    res+=this.checkType(((Nodo)x.next()));
                }
                if(!r.get(2).getNodo().isEmpty()){
                    lab2=this.genLabel();
                    res+="\nujp "+lab2;
                }
                res+="\nlab "+lab1;
                for(Iterator x=r.get(2).getNodo().iterator();x.hasNext();){
                    res+=this.checkType(((Nodo)x.next()));
                }
                if(!r.get(2).getNodo().isEmpty()){
                    res+="\nlab "+lab2;
                }}
                break;
            case WHILE:{
                ArrayList<Nodo> r=chk.getNodo();
                lab1=this.genLabel();
                res+="\nlab "+lab1;
                res+=this.genCode(Nodo.KindToken.EXP, r.get(0), "");
                lab2=this.genLabel();
                res+="\nfjp "+lab2;
                for(Iterator x=r.get(1).getNodo().iterator();x.hasNext();){
                    res+=this.checkType(((Nodo)x.next()));
                }
                res+="\nujp "+lab1;
                res+="\nlab "+lab2;
                }
                break;
            case DO:{
                ArrayList<Nodo> r=chk.getNodo();
                lab1=this.genLabel();
                res+="\nlab "+lab1;
                for(Iterator x=r.get(0).getNodo().iterator();x.hasNext();){
                    res+=this.checkType(((Nodo)x.next()));
                }
                res+=this.genCode(Nodo.KindToken.EXP, r.get(1), "");
                lab2=this.genLabel();
                res+="\nfjp "+lab2;
                res+="\nujp "+lab1;
                res+="\nlab "+lab2;
                }
                break;
            case INT:{
                ArrayList<Nodo> r=chk.getNodo();
                for(Iterator x=r.iterator();x.hasNext();){
                    res+="\nint "+((Nodo)x.next()).getDato();
                }
                break;}
            case FLOAT:{
                ArrayList<Nodo> r=chk.getNodo();
                for(Iterator x=r.iterator();x.hasNext();){
                    res+="\nflt "+((Nodo)x.next()).getDato();
                }
                break;}
            case WRITE:
                if(chk.getTipoDato().equals("int"))
                    res+=this.expresion(chk.getNodo().get(0))+"\nwri";
                else
                    res+=this.expresion(chk.getNodo().get(0))+"\nwrf";
                break;
                
            case READ:
                if(chk.getTipoDato().equals("int"))
                    res+="\nlda "+chk.getNodo().get(0).getDato()+"\nrdi";
                else
                    res+="\nlda "+chk.getNodo().get(0).getDato()+"\nrdf";
                break;
        }
        return res;
    }
    
    private int conLabel=0;
    
    private String genLabel(){
        return "Label"+this.conLabel++;
    }  
    
    private String expresion(Nodo chk){
        String tem=new String();
        Iterator e=chk.getNodo().iterator();
        switch(chk.getToken()){
            case Suma:
                
                tem+=this.expresion(((Nodo)e.next()));
                tem+=this.expresion(((Nodo)e.next()));
                tem+="\nadi";
                break;
            case Resta:
                
                tem+=this.expresion(((Nodo)e.next()));
                tem+=this.expresion(((Nodo)e.next()));
                tem+="\nsbi";
                break;
            case Division:
                
                tem+=this.expresion(((Nodo)e.next()));
                tem+=this.expresion(((Nodo)e.next()));
                tem+="\ndvi";
                break;
            case Multiplicacion:
                
                tem+=this.expresion(((Nodo)e.next()));
                tem+=this.expresion(((Nodo)e.next()));
                tem+="\nmpi";
                break;
            case Numero:
                
                if(chk.getTipoDato().equals("int")){
                    tem+="\nldc "+chk.getValorNumInt();
                }else{
                    tem+="\nldc "+chk.getValorNumFloat();
                }                
                break;
            case Identificador:
                tem+="\nlod "+chk.getDato();
                break;
        }
        return tem;
    }
    
    
}
