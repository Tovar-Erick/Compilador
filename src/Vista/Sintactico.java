/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import java.util.*;
import javax.swing.ImageIcon;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

/**
 *
 * @author Anthonio
 */
public class Sintactico {

    Tokens2 allTokens [];
    private ArrayList<String> errores;
    private Iterator tok;
    private Tokens2 aux;
    private ArrayList lista = new ArrayList();
    private Nodo raiz = null;
    private DefaultTreeModel modelo, modelSem;
    int cont=0;
    private ArrayList<Nodo> tip = new <Nodo>ArrayList();
    private Tabla table=new Tabla();
    private static int localidad = 0;
    

    Sintactico(String name){
        allTokens = Tokens2.allTokens(name);
    }
    
    protected Iterator getAllTokens(){
        return lista.iterator();
    }
    
    protected void generaLista(){
        for (int i = 0; i < allTokens.length; i++) {
            lista.add(new Tokens2(allTokens[i]));
        }
    }
    
    public void analizadorSintactico() {
//        System.out.println("!! Entro a analizadorSintactico");
        generaLista();
        tok = this.lista.iterator();
        errores = new <String>ArrayList();
        aux=(Tokens2)tok.next();
        if(!aux.getTipo().equals(Nodo.tipoToken.EOF)){
            if (aux.getToken().equals("program")) {
                raiz = new Nodo(aux.getToken(), aux.getTipo());
                aux=(Tokens2)tok.next();
                
                if (aux.getTipo()==Nodo.tipoToken.LlaveIzq) {
                    aux=(Tokens2)tok.next();
                } else {error(" Se esperaba { (program)");}
                this.listaDeclaraciones();
                Iterator nex = tip.iterator();
                while(nex.hasNext()){
                    raiz.addNodo((Nodo)nex.next());
                }
                ArrayList<Nodo> lis = this.listaSentencias(0);
                nex = lis.iterator();
                while (nex.hasNext()) {
                    raiz.addNodo((Nodo) nex.next());
                }
                if (aux.getTipo()==Nodo.tipoToken.LlaveDer) {
                    aux=(Tokens2)tok.next();
                }else 
                    error(" Se esperaba } (program)");
                while(aux.getTipo()!=Nodo.tipoToken.EOF){
                    error("Token fuera de bloque program -> "+aux.getToken());
                    aux=(Tokens2)tok.next();
                }
            }
            else {
                error(" Se esperaba program");
            raiz = new Nodo();
            }
            
        }
    }
    
    protected Nodo getRaiz(){
        return this.raiz;
    }
    
    //Regresa la rama principal de sentencias
    protected ArrayList<Nodo> getSentenciasRaiz(){
        Iterator hijos = getRaiz().getNodo().iterator();
        while(hijos.hasNext()){
            Nodo hijo = (Nodo)hijos.next();
            //System.out.println("Evaluando a "+hijo.getDato());
            if(hijo.getDato().equals("Lista_Sentencias"))// && hijo.getToken().equals(null))
                return hijo.getNodo();
            //hijos.remove();
        }
        return null;
        
    }

    private void listaDeclaraciones() {
        if(aux.getTipo()!=Nodo.tipoToken.EOF){
            Nodo tem = this.declaracion();
            if (tem != null) {
                tip.add(tem);
                if (aux.getTipo()==Nodo.tipoToken.PuntoyComa) {
                    aux=(Tokens2)tok.next();
                    this.listaDeclaraciones();
                } else {
                    error(" Se esperaba ;");
                }
            }
            if (aux.getTipo()!=Nodo.tipoToken.EOF) {
                if(aux.getTipo()!=Nodo.tipoToken.Identificador
                && aux.getTipo()!=Nodo.tipoToken.LlaveDer) {
                    if (!(aux.getToken().equals("int") || aux.getToken().equals("float") 
                          || aux.getToken().equals("bool"))) {
                        error("Token invalido: "+aux.getToken()+" Revise sintaxis listaDeclaraciones");
                        aux=(Tokens2)tok.next();
                    }
                    this.listaDeclaraciones();
                }
            }
        }
    }

    private Nodo declaracion() {
//        System.out.println("Entro a declaracion");
        Nodo tem = this.tipo();
        if(tem != null) {
            var.clear();
            this.listaVariables(tem.getTipoDato());
            Iterator nex = var.iterator();
            while (nex.hasNext()) {
                tem.addNodo((Nodo) nex.next());
            }
            return tem;
        }
        return null;
    }

    private Nodo tipo() {
        if (aux.getToken().equals("int") || aux.getToken().equals("float") || aux.getToken().equals("bool")) {
            Nodo tem = new Nodo(aux.getToken(), aux.getTipo(),aux.getToken());
            aux=(Tokens2)tok.next();
            return tem;
        }
        return null;
    }
    
    private ArrayList<Nodo> var = new <Nodo>ArrayList();

    private void listaVariables(String tipoDato) {
        if (aux.getTipo()==Nodo.tipoToken.Identificador) {
            Nodo tem=new Nodo(aux.getToken(), aux.getTipo(), tipoDato);
            if(!table.find(aux.getToken())){
                table.addNodo(tem);
            }else{
                error("La variable ya se encuentra declarada");
            }
            var.add(tem);
            aux=(Tokens2)tok.next();
        } else {
            error("Se esperaba identificador");
        }
        if (aux.getTipo()==Nodo.tipoToken.Coma) {
            aux=(Tokens2)tok.next();
            this.listaVariables(tipoDato);
        }
    }

    private ArrayList<Nodo> listaSentencias(int ciclo){
        ArrayList<Nodo> lis=new <Nodo>ArrayList();
        Nodo tem;
        boolean condicion=false;
        while(aux.getTipo()!=Nodo.tipoToken.EOF){
            tem=sentencia();
            if(tem!=null){
                lis.add(tem);
                if(aux.getTipo()==Nodo.tipoToken.PuntoyComa){
                    aux=(Tokens2)tok.next();
                }else{
                    error("No se encontro el punto y coma");
                }
            }else{
                switch(ciclo){
                    case 0:
                        if(aux.getTipo()==Nodo.tipoToken.LlaveDer){
                            condicion=true;
                        }
                        break;
                    case 1:
                        if(aux.getToken().equals("else")||aux.getToken().equals("fi")){
                            condicion=true;
                        }
                        break;
                    case 2:
                        if(aux.getToken().equals("until")){
                            condicion=true;
                        }
                        break;
                }
                if(condicion){
                    break;
                }else{
                    error("Token no valido");
                    aux=(Tokens2)tok.next();
                }
            }
        }
        return lis;
    }

    private Nodo sentencia() {
        Nodo tem = null;
        if (aux.getToken().equals("if")) {
            aux=(Tokens2)tok.next();
            tem = this.seleccion();
        } else if (aux.getToken().equals("while")) {
            aux=(Tokens2)tok.next();
            tem = this.iteracion();
        } else if (aux.getToken().equals("do")) {
            aux=(Tokens2)tok.next();
            tem = this.repeticion();
        } else if (aux.getToken().equals("read")) {
            aux=(Tokens2)tok.next();
            tem = this.sentIn();
        } else if (aux.getToken().equals("write")) {
            aux=(Tokens2)tok.next();
            tem = this.sentOut();
        } else {
            if (aux.getTipo()==Nodo.tipoToken.Identificador) {
                tem = new Nodo(aux.getToken(), aux.getTipo());
                aux=(Tokens2)tok.next();
                tem=asignacion(tem);
            }
        }
        return tem;
    }

    private Nodo seleccion() {
        ArrayList<Nodo> lis;
        Nodo tem = new Nodo("if", Nodo.tipoToken.PalabraReservada);
        if (aux.getTipo()==Nodo.tipoToken.ParentesisIzquierdo) {
            aux=(Tokens2)tok.next();
        } else {
            error("Se esperaba (");
        }
        Nodo temR = new Nodo("Condición", null);
        Nodo res=expresion();
        if(res==null){
            error("No se encontró expresión");
        }else{
            if(!res.getTipoDato().equals("bool")){
                error("Expresión incompatible");
            }
        }
        temR.addNodo(res);
        tem.addNodo(temR);
        if (aux.getTipo()==Nodo.tipoToken.ParentesisDerecho) {
            aux=(Tokens2)tok.next();
        } else {
            error(" Se esperaba )");
        }
        lis = this.listaSentencias(1);
        
        Nodo tem2 = new Nodo("Lista_Sentencias_true", null);
        Iterator nex = lis.iterator();
        while (nex.hasNext()) {
            tem2.addNodo((Nodo) nex.next());
        }
        tem.addNodo(tem2);
        if (aux.getToken().equals("else")) {
            aux=(Tokens2)tok.next();
            lis.clear();
            lis = listaSentencias(1);
            
            tem2 = new Nodo("Lista_Sentencias_else", null);
            nex = lis.iterator();
            while (nex.hasNext()) {
                tem2.addNodo((Nodo) nex.next());
            }
            tem.addNodo(tem2);
        } 
        if (aux.getToken().equals("fi")) {
            aux=(Tokens2)tok.next();
        } else {
            error(" Se esperaba fi");
        }
        return tem;
    }

    private Nodo iteracion() {
        ArrayList<Nodo> lis;// = new <Nodo>ArrayList();
        Nodo tem = new Nodo("while", Nodo.tipoToken.PalabraReservada);
        if (aux.getTipo()==Nodo.tipoToken.ParentesisIzquierdo) {
            aux=(Tokens2)tok.next();
        } else {
            error("Se esperaba (");
        }
        Nodo temR = new Nodo("Expresion", null);
        Nodo con=expresion();
        if(con==null){
            error("No se encontro expresion");
        }else{
            if(!con.getTipoDato().equals("bool")){
                error("Expresion incompatible");
            }
            temR.addNodo(con);
        }
        tem.addNodo(temR);
        if (aux.getTipo()==Nodo.tipoToken.ParentesisDerecho) {
            aux=(Tokens2)tok.next();
        } else {
            error(" Se esperaba )");
        }
        if (aux.getTipo()==Nodo.tipoToken.LlaveIzq) {
            aux=(Tokens2)tok.next();
        } else {
            error(" Se esperaba {");
        }
        lis = this.listaSentencias(0);
        Nodo tem2 = new Nodo("Contenido", null);
        Iterator nex = lis.iterator();
        while (nex.hasNext()) {
            tem2.addNodo((Nodo) nex.next());
        }
        tem.addNodo(tem2);
        if (aux.getTipo()==Nodo.tipoToken.LlaveDer) {
            aux=(Tokens2)tok.next();
        } else {
            error(" Se esperaba }");
        }
        return tem;
    }

    private Nodo repeticion() {
        ArrayList<Nodo> lis;
        Nodo tem = new Nodo("do", Nodo.tipoToken.PalabraReservada);
        lis = this.listaSentencias(2);
        Nodo tem2 = new Nodo("Contenido", null);
        Iterator nex = lis.iterator();
        while (nex.hasNext()) {
            tem2.addNodo((Nodo) nex.next());
        }
        tem.addNodo(tem2);
        if (aux.getToken().equals("until")) {
            aux=(Tokens2)tok.next();
        } else {
            error(" Se esperaba until");
        }
        Nodo temR = new Nodo("Expresion", null);
        Nodo con=expresion();
        if(con==null){
            error("No se encontró expresion");
        }else{
            if(!con.getTipoDato().equals("bool")){
                error("Expresion incorrecta");
            }
            temR.addNodo(con);
        }
        tem.addNodo(temR);
        if (aux.getToken().equals(";")) {
            aux=(Tokens2)tok.next();
        } else {
            error(" Se esperaba ;");
        }
        return tem;
    }

    private Nodo sentIn() {
        Nodo tem = new Nodo("read", Nodo.tipoToken.PalabraReservada);
        if (aux.getTipo()==Nodo.tipoToken.Identificador) {
            if(!table.find(aux.getToken())){
                error("Variable no declarada en READ  -> "+aux.getToken());
            }
            String tipo=table.getTipoDato(aux.getToken());
            tem.setTipoDato(tipo);
            tem.addNodo(new Nodo(aux.getToken(), aux.getTipo(),tipo));
            aux=(Tokens2)tok.next();
        } else {
            error("Sentencia Incorrecta");
        }
        return tem;
    }

    private Nodo sentOut() {
        Nodo tem = new Nodo("write", Nodo.tipoToken.PalabraReservada);
        Nodo ouT=expresion();
        if(ouT==null){
            error("No se encontro expresion a mostrar");
        }else{
            if(ouT.getTipoDato().equals("bool")){
                error("Expresion no correcta");
            }
            tem.addNodo(ouT);
        }
        tem.setTipoDato(ouT.getTipoDato());
        return tem;
    }

    private Nodo asignacion(Nodo tem) {
        if(!this.table.find(tem.getDato())){
            error("Variable no declarada -> "+tem.getDato());
            tem.setTipoDato("");
        }else{
            tem.setTipoDato(this.table.isType(tem.getDato()));
        }
        Nodo temA=new Nodo("=",Nodo.tipoToken.Asignacion);
        if(aux.getToken().equals("++")||aux.getToken().equals("--")){
            Nodo temB;
            if(aux.getToken().equals("++")) {
                        temB=new Nodo("+",Nodo.tipoToken.Suma);
                        
             }else{
                        temB=new Nodo("-",Nodo.tipoToken.Resta);
            }
            Nodo nuevo=new Nodo(tem.getDato(),tem.getToken(), tem.getLinea());
            if(tem.getToken()==Nodo.tipoToken.Identificador){
                if(this.table.find(tem.getDato())){
                    if(this.table.isType(tem.getDato()).equals("int") ){
                            nuevo.setTipoDato("int");
                            nuevo.setValorNum(this.table.getValueInt(tem.getDato())+1);
                    }else if(this.table.isType(tem.getDato()).equals("float") ){
                            nuevo.setTipoDato("float");
                            nuevo.setValorNum(this.table.getValuefloat(tem.getDato())+1);
                    }else{
                            nuevo.setTipoDato("");
                            error("Tipo incompatible");
                    }
                }else{
                    nuevo.setTipoDato("");
                    error("Variable no declarada -> "+tem.getDato());
                }
            }else if(tem.getToken()==Nodo.tipoToken.Numero){
                try{
                    nuevo.setValorNum(Integer.parseInt(tem.getDato())+1);
                    nuevo.setTipoDato("int");
                }catch(Exception e){
                    nuevo.setValorNum(Float.valueOf(tem.getDato())+1);
                    nuevo.setTipoDato("float");
                }
            }else{
                nuevo.setTipoDato("");
                error("Suma no posible");
            }
            temB.addNodo(nuevo);
            Nodo n=new Nodo("1",Nodo.tipoToken.Numero,"int");
            n.setValorNum(1);
            temB.addNodo(n);
            if(nuevo.getToken()==tem.getToken()){
                if(tem.getTipoDato().equals("int")){
                        tem.setValorNum(nuevo.getValorNumInt());
                        temA.setValorNum(nuevo.getValorNumInt());
                        temA.setTipoDato(nuevo.getTipoDato());
                        this.table.addValue(tem.getDato(), nuevo.getValorNumInt());
                }else if(tem.getTipoDato().equals("float")){
                        temA.setValorNum(nuevo.getValorNumFloat());
                        tem.setValorNum(nuevo.getValorNumFloat());
                        temA.setTipoDato(nuevo.getTipoDato());
                        this.table.addValue(tem.getDato(), nuevo.getValorNumFloat());
                }
            }else if("float".equals(tem.getTipoDato())&&"int".equals(nuevo.getTipoDato())){
                temA.setValorNum(nuevo.getValorNumInt());
                tem.setValorNum(nuevo.getValorNumInt());
                temA.setTipoDato(tem.getTipoDato());
                this.table.addValue(tem.getDato(), tem.getValorNumFloat());
            }else{
                error("Tipo de dato incompatible");
            }
            temA.addNodo(temB);
            tem.addNodo(temA);
            aux=(Tokens2)tok.next();
        }else{
            if(aux.getToken().equals("=")){
                aux=(Tokens2)tok.next();
            }else{
                error("Falta el signo igual");
            }
            Nodo val=this.expresion();
            if(val==null){
                error("No se asigna ningun valor");
            }else{
                if(val.getTipoDato().equals("bool")){
                    error("Expresion incorrecta");
                }else{
                    if(val.getTipoDato().equals(tem.getTipoDato())){
                        if(tem.getTipoDato().equals("int")){
                                tem.setValorNum(val.getValorNumInt());
                                temA.setValorNum(val.getValorNumInt());
                                temA.setTipoDato(val.getTipoDato());
                                this.table.addValue(tem.getDato(), val.getValorNumInt());
                        }else if(tem.getTipoDato().equals("float")){
                                temA.setValorNum(val.getValorNumFloat());
                                tem.setValorNum(val.getValorNumFloat());
                                temA.setTipoDato(val.getTipoDato());
                                this.table.addValue(tem.getDato(), val.getValorNumFloat());
                        }
                    }else if("float".equals(tem.getTipoDato())&&"int".equals(val.getTipoDato())){
                        temA.setValorNum(val.getValorNumInt());
                        tem.setValorNum(val.getValorNumInt());
                        temA.setTipoDato(tem.getTipoDato());
                        this.table.addValue(tem.getDato(), tem.getValorNumFloat());
                    }else{
                        error("Tipo de dato incompatible");
                    }
                }
                temA.addNodo(val);
            }
            tem.addNodo(temA);
        }
        return tem;
        
    }

    private Nodo expresion() {
        Nodo tem = this.expresionSimple();
        boolean err=false;
        if (aux.getToken().equals("<") || aux.getToken().equals(">") || aux.getToken().equals("<=")
                || aux.getToken().equals(">=") || aux.getToken().equals("==") || aux.getToken().equals("!=")) {
            Nodo temR = new Nodo(aux.getToken(), aux.getTipo(),"bool");
            temR.addNodo(tem);
            aux=(Tokens2)tok.next();
            
            Nodo com=this.expresionSimple();
            if(tem.getTipoDato().equals("int")) {
                    if (com.getTipoDato().equals("int")) {
                            temR.setComando(this.comparar(tem.getValorNumInt(),com.getValorNumInt(),temR.getToken()));
                    }else if (com.getTipoDato().equals("float")) {
                            temR.setComando(this.comparar(tem.getValorNumInt(),com.getValorNumFloat(),temR.getToken()));
                    } else{
                            err=true;
                    }
            }else if(tem.getTipoDato().equals("float")) {
                    if (com.getTipoDato().equals("int")) {
                            temR.setComando(this.comparar(tem.getValorNumFloat(),com.getValorNumInt(),temR.getToken()));
                    }else if (com.getTipoDato().equals("float")) {
                            temR.setComando(this.comparar(tem.getValorNumFloat(),com.getValorNumFloat(),temR.getToken()));
                    }else{
                            err=true;
                    }
            }else{
                    err=true;
            }
            if(err){
                error("Expresion no valida");
            }
            temR.addNodo(com);
            tem=temR;
        }
        return tem;
    }

    private Nodo expresionSimple() {
        Nodo tem = this.termino();
        while (aux.getToken().equals("+") || aux.getToken().equals("++") || 
                aux.getToken().equals("-") || aux.getToken().equals("--")) {
            if (aux.getTipo()==Nodo.tipoToken.SumaUnitaria) {
                Nodo temA = new Nodo("asignación", null);
                Nodo temB = new Nodo("+", Nodo.tipoToken.Suma);
                temB.addNodo(new Nodo(tem.getDato(), tem.getToken(),tem.getTipoDato(), tem.getLinea()));
                temB.addNodo(new Nodo("1", Nodo.tipoToken.Numero));
                temA.addNodo(temB);
                tem.addNodo(temA);
                aux=(Tokens2)tok.next();
            } else if (aux.getTipo()==Nodo.tipoToken.RestaUnitaria) {
                Nodo temA = new Nodo("asignación", null);
                Nodo temB = new Nodo("-", Nodo.tipoToken.Resta);
                temB.addNodo(new Nodo(tem.getDato(), tem.getToken(),tem.getTipoDato(), tem.getLinea()));
                temB.addNodo(new Nodo("1", Nodo.tipoToken.Numero));
                temA.addNodo(temB);
                tem.addNodo(temA);
                aux=(Tokens2)tok.next();
            } else {
                Nodo temR = new Nodo(aux.getToken(), aux.getTipo());
                temR.addNodo(tem);
                aux=(Tokens2)tok.next();
                
                if(tem != null){
                    if(tem.getTipoDato().equals("")) {
                            error("Variable no declarada -> "+tem.getDato());
                            tem=this.termino();
                            if(tem.getTipoDato().equals("float")) {
                                    temR.setTipoDato("float");
                                    temR.setValorNum(tem.getValorNumFloat());
                            }else if(tem.getTipoDato().equals("int")) {
                                    temR.setTipoDato("int");
                                    temR.setValorNum(tem.getValorNumInt());
                            }else if(tem.getTipoDato().equals("")) {
                                    error("Variable no declarada -> "+tem.getDato());
                            }
                    }else if(tem.getTipoDato().equals("int")) {
                            int intVal=tem.getValorNumInt();
                            tem=this.termino();
                            if(tem.getTipoDato().equals("float")) {
                                    temR.setTipoDato("float");
                                    temR.setValorNum(this.operaciones(intVal,tem.getValorNumFloat(),temR.getToken()));
                            }else if(tem.getTipoDato().equals("int")) {
                                    temR.setTipoDato("int");
                                    temR.setValorNum(this.operaciones(intVal, tem.getValorNumInt(), temR.getToken()));
                            }else if(tem.getTipoDato().equals("")) {
                                    temR.setTipoDato("int");
                                    temR.setValorNum(tem.getValorNumInt());
                                    error("Variable no declarada -> "+tem.getDato());
                            }
                    }else if(tem.getTipoDato().equals("float")) {
                            float floatVal=tem.getValorNumFloat();
                            tem=this.termino();
                            if(tem.getTipoDato().equals("float")) {
                                    temR.setTipoDato("float");
                                    temR.setValorNum(this.operaciones(floatVal,tem.getValorNumFloat(),temR.getToken()));
                            }else if(tem.getTipoDato().equals("int")) {
                                    temR.setTipoDato("float");
                                    temR.setValorNum(this.operaciones(floatVal,tem.getValorNumInt(),temR.getToken()));
                            }else if(tem.getTipoDato().equals("")) {
                                    temR.setTipoDato("float");
                                    temR.setValorNum(floatVal);
                                    error("Variable no declarada -> "+tem.getDato());
                            }
                    }
                } 
                temR.addNodo(tem);
                tem = temR;
            }
        }
        return tem;
    }

    private Nodo termino() {
        Nodo tem = this.factor();
        while (aux.getToken().equals("*") || aux.getToken().equals("/")) {
            Nodo temR = new Nodo(aux.getToken(), aux.getTipo());
            temR.addNodo(tem);
            aux=(Tokens2)tok.next();
            
            if(tem.getTipoDato().equals("")){
                    error("Variable no declarada -> "+tem.getDato());
                    tem=this.factor();
                    if(tem.getTipoDato().equals("float")){
                            temR.setTipoDato("float");
                            temR.setValorNum(tem.getValorNumFloat());
                    }else if(tem.getTipoDato().equals("int")){
                            temR.setTipoDato("int");
                            temR.setValorNum(tem.getValorNumInt());
                    }else if(tem.getTipoDato().equals("")){
                            error("Variable no declarada -> "+tem.getDato());
                    }
            }else if(tem.getTipoDato().equals("int")){
                    int intVal=tem.getValorNumInt();
                    tem=this.factor();
                    if(tem.getTipoDato().equals("float")){
                            temR.setTipoDato("float");
                            temR.setValorNum(this.operaciones(intVal, tem.getValorNumFloat(), temR.getToken()));
                    }else if(tem.getTipoDato().equals("int")){
                            temR.setTipoDato("int");
                            temR.setValorNum(this.operaciones(intVal, tem.getValorNumFloat(), temR.getToken()));
                    }else if(tem.getTipoDato().equals("")){
                            temR.setTipoDato("int");
                            temR.setValorNum(intVal);
                            error("Variable no declarada -> "+tem.getDato());
                    }
            }else if(tem.getTipoDato().equals("float")){
                    float floatVal=tem.getValorNumFloat();
                    tem=this.factor();
                    if(tem.getTipoDato().equals("float")){
                            temR.setTipoDato("float");
                            temR.setValorNum(this.operaciones(floatVal, tem.getValorNumFloat(), temR.getToken()));
                    } else if(tem.getTipoDato().equals("int")){
                            temR.setTipoDato("float");
                            temR.setValorNum(this.operaciones(floatVal, tem.getValorNumInt(), temR.getToken()));
                    }else if(tem.getTipoDato().equals("")){
                            temR.setTipoDato("float");
                            temR.setValorNum(floatVal);
                            error("Variable no declarada -> "+tem.getDato());
                    }
            }
            temR.addNodo(tem);
            
            tem = temR;
        }
        return tem;
    }
    
    private int operaciones(int val,int val2,Nodo.tipoToken op){
        switch(op){
            case Suma:
                val+=val2;
                break;
            case Resta:
                val-=val2;
                break;
            case Multiplicacion:
                val*=val2;
                break;
            case Division:
                val/=val2;
                break;
        }
        return val;
    }
    
    private float operaciones(float val,float val2,Nodo.tipoToken op){
        switch(op){
            case Suma:
                val+=val2;
                break;
            case Resta:
                val-=val2;
                break;
            case Multiplicacion:
                val*=val2;
                break;
            case Division:
                val/=val2;
                break;
        }
        return val;
    }

    private Nodo factor() {
        Nodo tem = null;

        if (aux.getTipo()==Nodo.tipoToken.ParentesisIzquierdo){
            aux=(Tokens2)tok.next();
                tem = this.expresion();
            if (aux.getTipo()==Nodo.tipoToken.ParentesisDerecho) {
                aux=(Tokens2)tok.next();
            } else {
                error(" Se esperaba )");
            }
        }else{
            if (aux.getTipo()==Nodo.tipoToken.Numero){
                try{
                    int num=Integer.parseInt(aux.getToken());
                    tem=new Nodo(aux.getToken(),aux.getTipo(),"int");
                    tem.setValorNum(num);
                }catch(Exception e){
                    float numf=Float.parseFloat(aux.getToken());
                    tem=new Nodo(aux.getToken(),aux.getTipo(),"float");
                    tem.setValorNum(numf);
                }
                aux=(Tokens2)tok.next();
            }else{
                if (aux.getTipo()==Nodo.tipoToken.Identificador){
                    tem = new Nodo(aux.getToken(), aux.getTipo(),table.isType(aux.getToken()));
//                    if(this.table.isType(aux.getToken())==null || this.table.isType(aux.getToken())==""){
                    if(this.table.find(aux.getToken())==false){
                        error("Identificador no declarado -> "+aux.getToken());
                    }else if("int".equals(tem.getTipoDato())){
                        tem.setValorNum(this.table.getValueInt(aux.getToken()));
                    }else if("float".equals(tem.getTipoDato())){
                        tem.setValorNum(this.table.getValuefloat(aux.getToken()));
                    }else{
                        error("Tipo incompatible");
                    }
                    aux=(Tokens2)tok.next();
                }else{
                    error(" Error de Sintaxis ");
                }
            }
        }
        return tem;
    }

    private void error(String msg) {
        errores.add("Error en " + (aux.getLinea()) + " --- > " + msg + "\n");
    }

    private void llenadoJTree() {


        if (raiz != null) {
            DefaultMutableTreeNode principal = new DefaultMutableTreeNode(raiz.getDato());
            modelo = new DefaultTreeModel(principal);
            JTree tree = new JTree(modelo);
        // Cambiamos los iconos
            DefaultTreeCellRenderer render= (DefaultTreeCellRenderer)tree.getCellRenderer();
            render.setLeafIcon(new ImageIcon("c:/arbol3.png"));
            render.setOpenIcon(new ImageIcon("c:/arbol2.png"));
            render.setClosedIcon(new ImageIcon("c:/arbol1.png"));
            this.imprimirElementor(this.raiz.getNodo(), principal);
        } else {
            modelo = null;
        }
    }

    private void imprimirElementor(ArrayList<Nodo> tem, DefaultMutableTreeNode padre) {
        JTree tree = new JTree(modelo);
        DefaultTreeCellRenderer render= (DefaultTreeCellRenderer)tree.getCellRenderer();
        render.setLeafIcon(new ImageIcon("c:/arbol3.png"));
        render.setOpenIcon(new ImageIcon("arbol1.png"));
        render.setClosedIcon(new ImageIcon("arbol2.png"));
        Iterator nex = tem.iterator();
        Nodo a;
        int con = 0;
        try {
            while (nex.hasNext()) {
                a = (Nodo) nex.next();
                DefaultMutableTreeNode hijo = new DefaultMutableTreeNode(a.getDato());
                modelo.insertNodeInto(hijo, padre, con++);
                this.imprimirElementor(a.getNodo(), hijo);
            }
        } catch (NullPointerException as) {
            System.out.println("EROOR en imprimirElementor, "+ as.getMessage());
        }
    }

    public DefaultTreeModel getTree() {
        llenadoJTree();
        return modelo;
    }

    private String comparar(float val,float val2,Nodo.tipoToken tip){
        String bal="false";
        switch(tip){
            case IgualIgual:
                if(val==val2){
                    bal="true";
                }
                break;
            case Diferente:
                if(val!=val2){
                    bal="true";
                }
                break;
            case MenorQue:
                if(val<val2){
                    bal="true";
                }
                break;
            case MenorOIgualQue:
                if(val<=val2){
                    bal="true";
                }
                break;
            case MayorQue:
                if(val>val2){
                    bal="true";
                }
                break;
            case MayorOIgualQue:
                if(val>=val2){
                    bal="true";
                }
                break;
        }
        return bal;
    }
    
    public ArrayList<String> getErrores() {return errores;}
    
    private void llenadoJTreeSem(){
        if(raiz!=null){
            DefaultMutableTreeNode principal = new DefaultMutableTreeNode(raiz.getDato());
            modelSem = new DefaultTreeModel(principal);
            this.imprimirElementorSin(this.raiz.getNodo(),principal);
        }else{
            modelSem=null;
        }
    }
    
    public DefaultTreeModel getTreeSem(){
        llenadoJTreeSem();
        return modelSem;
    }
    
    private void imprimirElementorSin(ArrayList<Nodo> tem,DefaultMutableTreeNode padre){
        Iterator nex=tem.iterator();
        Nodo a;
        int con=0;
        
        try{
            while(nex.hasNext()){
            	String tabla = "";
                a=(Nodo)nex.next();
                DefaultMutableTreeNode hijo;
                if(a.getTipoDato().equals("int")) {
                    hijo= new DefaultMutableTreeNode(a.getDato()+", Tipo = "+a.getTipoDato()+", Valor = "+a.getValorNumInt());
                }else if(a.getTipoDato().equals("float")) {
                    hijo= new DefaultMutableTreeNode(a.getDato()+", Tipo = "+a.getTipoDato()+", Valor = "+a.getValorNumFloat());
                }else if(a.getTipoDato().equals("")) {
                    hijo= new DefaultMutableTreeNode(a.getDato()+", Tipo = Indefinido");
                }else if(a.getTipoDato().equals("bool")) {
                    hijo= new DefaultMutableTreeNode(a.getDato()+", Tipo = "+a.getTipoDato()+
                            ", Valor = "+a.getComando());
                }else{
                    hijo= new DefaultMutableTreeNode(a.getDato()+", Tipo = "+a.getTipoDato()+
                            ", Valor = ("+a.getValorNumInt()+"-"+a.getValorNumFloat()+")");
                }
                if(a.getTipoDato().equals("int")){
                	if(!a.getDato().equals("int"))                	{
                            tabla = "Dato = " + a.getDato() + " Tipo de dato = " +a.getTipoDato()+ 
                                    " Valor = " + a.getValorNumInt() + " Localidad: " + localidad++;
                	}
                }
                else if(a.getTipoDato().equals("real")){
                	tabla = "Dato = " + a.getDato() + " Tipo de dato = " + a.getTipoDato() + 
                                " Valor = " + a.getValorNumFloat() + " Localidad: " + localidad++;
                }
                modelSem.insertNodeInto(hijo, padre, con);
                con++;
                this.imprimirElementorSin(a.getNodo(), hijo);
            }
        }
        catch(NullPointerException as){}
    }
    
    public JTable getTabla() {                                               
    	ArrayList list = this.table.getObjectTable();
        DefaultTableModel datos = new DefaultTableModel();
        datos.addColumn("HashCode");
        datos.addColumn("Variable");
        datos.addColumn("Tipo");
        datos.addColumn("Valor");
        datos.addColumn("Registro");
        datos.addColumn("Lineas");
        Iterator i = list.iterator();
        int j = 1;
        table.findLines(lista.iterator());
        while (i.hasNext()) {
            Nodo tem = (Nodo) i.next();
            if (tem.getTipoDato().equals("int")) {
                Object[] obj = {this.table.hash(tem.getDato()), tem.getDato(), tem.getTipoDato(), tem.getValorNumInt(), j++, tem.getLinea()};
                datos.addRow(obj);
            }else if (tem.getTipoDato().equals("bool")) {
                Object[] obj = {this.table.hash(tem.getDato()), tem.getDato(), tem.getTipoDato(), tem.getComando(), j++, tem.getLinea()};
                datos.addRow(obj);
            }else {
                Object[] obj = {this.table.hash(tem.getDato()), tem.getDato(), tem.getTipoDato(), tem.getValorNumFloat(), j++, tem.getLinea()};
                datos.addRow(obj);
            }
        }
        JTable tabla = new JTable(datos);
        return tabla;
    }
}

