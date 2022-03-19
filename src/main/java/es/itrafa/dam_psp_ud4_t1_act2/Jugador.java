package es.itrafa.dam_psp_ud4_t1_act2;

import java.io.Serializable;

/**
 * Representa a cada jugador en la partida que será asignado a un cliente
 *
 * @author jmor
 */
@SuppressWarnings("serial")
public class Jugador implements Serializable {

    /**
     * Identificador del jugador
     */
    private int id;
    /**
     * Puntos de Salud
     */
    private int ps;
    /**
     * Puntos de Combate
     */
    private int pc;
    /**
     * Posicición en el Rancking
     */
    private int posicion;
    /**
     * Indica si el jugador fue asignado a cliente
     */
    private boolean asignado;
    /**
     * IP del cliente al que está asignado el jugador
     * <p>
     * Nota: para que el servidor pueda identificarlo. no válido si clientes
     * comparten ip
     */
    private String ip;

    // GETTERS & SETTERS
    /**
     * Devuelve posicición del jugador en el Rancking
     *
     * @return
     */
    public int getPosicion() {
        return posicion;
    }

    /**
     * Modifica posicición del jugador en el Rancking
     *
     * @param posicion
     */
    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    /**
     * Devuelve si es cierto o no que el jugador está asignado
     *
     * @return
     */
    public boolean isAsignado() {
        return asignado;
    }

    /**
     * Modifica si es cierto o no que el jugador está asignado
     *
     * @param asignado
     */
    public void setAsignado(boolean asignado) {
        this.asignado = asignado;
    }

    /**
     * Devuelve puntos salud del jugador
     *
     * @return
     */
    public int getPs() {
        return ps;
    }

    /**
     * Modifica puntos salud del jugador
     *
     * @param ps
     */
    public void setPs(int ps) {
        this.ps = ps;
    }

    /**
     * Devuelve puntos de combate del jugador
     *
     * @return
     */
    public int getPc() {
        return pc;
    }
    /**
     * Modifica puntos de combate del jugador
     *
     * @param pc
     */
    public void setPc(int pc) {
        this.pc =  pc;
    }
    /**
     * Devuelve el identificador del jugador
     *
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Modifica el identificador del jugador
     *
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Devuelve la IP del cliente al que se le asigno el jugador
     *
     * @return
     */
    public String getIp() {
        return ip;
    }

    /**
     * Modifica la IP del cliente al que se le asigno el jugador
     *
     * @param ip
     */
    public void setIp(String ip) {
        this.ip = ip;
    }




}
