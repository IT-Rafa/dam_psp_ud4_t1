package es.itrafa.dam_psp_ud4_t1_act2;

import java.io.Serializable;

/**
 *
 * @author jmor
 */
@SuppressWarnings("serial")
public class Jugador implements Serializable{
   private int id; // identificador del jugador
   private String clienteAsignado; 
   private int ps; //puntos de salud
   private int pc; //puntos de combate
   private int posicion; //ranking
   private boolean asignado;

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public boolean isAsignado() {
        return asignado;
    }

    public void setAsignado(boolean asignado) {
        this.asignado = asignado;
    }

    public int getPs() {
        return ps;
    }

    public void setPs(int ps) {
        this.ps = ps;
    }

    public int getPc() {
        return pc;
    }

    public void setPc(int pc) {
        this.pc = pc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return String.format("%s: Jugador %d con %d PS", posicion, id , ps);
    }

    /**
     * @return the clienteAsignado
     */
    public String getClienteAsignado() {
        return clienteAsignado;
    }

    /**
     * @param clienteAsignado the clienteAsignado to set
     */
    public void setClienteAsignado(String clienteAsignado) {
        this.clienteAsignado = clienteAsignado;
    }
   
   
    
}
