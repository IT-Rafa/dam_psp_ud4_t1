package es.itrafa.dam_psp_ud4_t1_act2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Define los métodos pertenecientes al objeto remoto (JuegoServidor) y que serán
 * llamados por los clientes (objetos JuegoCliente)
 * 
 * @author it-ra
 */
public interface JuegoInterface extends Remote {
    Jugador asignacionJugador()throws RemoteException;
    List <Jugador> consultaRanking()throws RemoteException;
    Jugador consultaPS(int id)throws RemoteException;
    void ataque (int jugadorAtacado)throws RemoteException;
}
