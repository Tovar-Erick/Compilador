/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Vista;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

//@author $Erick Tovar

public class Tabla {

    private List<Nodo> table;

    public Tabla() {
        table = Arrays.asList(new Nodo[9999]);
    }

    public void addNodo(Nodo nuevo) {
        String key = nuevo.getDato();
        int hashCode = hash(key);
        boolean valid = false;
        do {
            if (this.table.get(hashCode) == null) {
                valid = true;
            } else {
                key += key;
                hashCode = hash(key);
            }
        } while (valid == false);
        table.set(hashCode, nuevo);
    }

    public boolean find(String buscar) {
        if (this.table.get(hash(buscar)) != null) {
            if(table.get(hash(buscar)).getDato().equals(buscar)){
//                System.out.println("si,  iguales, "+buscar);
                return true;
            }
            else{
//                System.out.println("nel, no iguales, "+buscar);
                return false;
            }
            
        }
        return false;
    }

    public void findLines(Iterator lista) {
        Tokens2 token;
        while(lista.hasNext()){
            token=(Tokens2)lista.next();
            if (this.table.get(hash(token.getToken())) != null) {
                if(table.get(hash(token.getToken())).getDato().equals(token.getToken()))
                    table.get(hash(token.getToken())).addLinea(token.getLinea());
            }
        }
        
    }
    
    public String isType(String type) {
        Nodo tem = this.table.get(hash(type));
        String dType = "";
        if (tem != null) {
            dType = tem.getTipoDato();
            return dType;
        }

        return dType;
    }

    public void addLine(String buscar, int line) {
        Nodo tem = this.table.get(hash(buscar));
        if (tem != null) {
            System.out.println("agregaré la linea = "+line);
            Nodo tem1 = tem;
            tem.addLinea(line);
            this.table.set(this.table.indexOf(tem1), tem);
        }
    }

    public boolean addValue(String buscar, int dato) {
        Nodo tem = this.table.get(hash(buscar));
        if (tem != null) {
            Nodo tem1 = tem;
            tem.setValorNum(dato);
            this.table.set(this.table.indexOf(tem1), tem);
            return true;
        }
        return false;
    }

    public boolean addValue(String buscar, float dato) {
        Nodo tem = this.table.get(hash(buscar));
        if (tem != null) {
            Nodo tem1 = tem;
            tem.setValorNum(dato);
            this.table.set(this.table.indexOf(tem1), tem);
            return true;
        }

        return false;
    }

    public int getValueInt(String buscar) {
        int val = 0;
        Nodo tem = this.table.get(hash(buscar));
        if (tem != null) {
            val = tem.getValorNumInt();
        }
        return val;
    }
    
    public String getTipoDato(String buscar) {
        String tipo="";
        Nodo tem = this.table.get(hash(buscar));
        if (tem != null) {
            tipo = tem.getTipoDato();
        }
        return tipo;
    }

    public float getValuefloat(String buscar) {
        float val = 0;
        Nodo tem = this.table.get(hash(buscar));
        if (tem != null) {
            val = tem.getValorNumFloat();
        }
        return val;
    }

    public int hash(String key) {
        int temp = 0;
        for (int i = 0; i < key.length(); i++) {
            temp = ((temp << 4) + key.charAt(i)) % 9999;
            ++i;
        }
        return temp;
    }

    public ArrayList<Nodo> getObjectTable() {
        Iterator i = this.table.iterator();
        ArrayList list = new ArrayList<Nodo>();
        while (i.hasNext()) {
            Nodo tem = (Nodo) i.next();
            if (tem != null) {
                list.add(tem);
                System.out.println(tem);
            }
            
        }
        return list;
    }
}
