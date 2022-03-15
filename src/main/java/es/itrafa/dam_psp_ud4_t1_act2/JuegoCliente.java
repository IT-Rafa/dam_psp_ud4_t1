/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud4_t1_act2;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Morillo Fernandez
 */
public class JuegoCliente {

    private static final int PORT = JuegoServidor.PORT;
    private JuegoInterface partida;
    private Jugador jugador;
    private final int idCli;

    JuegoCliente(int idCli) {
        this.idCli = idCli;
    }

    public JuegoInterface getPartida() {
        return partida;
    }

    public void setPartida(JuegoInterface partida) {
        this.partida = partida;
    }

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     *
     *
     * @throws RemoteException
     */
    public void callAsignacion() throws RemoteException {
        try {
            //
            Registry registry = LocateRegistry.getRegistry("localhost", PORT);

            //Obteniendo los metodos remotos --> polimorfismo con interfaces
            partida = (JuegoInterface) registry.lookup("GameServer"); //Buscar en el registro...

            // Entrando en partida
            jugador = partida.asignacionJugador();
            if (jugador != null) {
                Logger.getLogger(JuegoCliente.class.getName()).log(
                        Level.INFO, String.format(
                                "CLIENTE_%d: Tiene asignado el jugador %d", idCli, jugador.getId()));

            } else {
                Logger.getLogger(JuegoCliente.class.getName()).log(
                        Level.WARNING, String.format(
                                "CLIENTE_%d: Servidor no pudo asignar jugador. Posible partida completa", idCli));
            }
        } catch (NotBoundException ex) {
            Logger.getLogger(JuegoCliente.class.getName()).log(
                    Level.SEVERE, String.format(
                            "CLIENTE_%d: Error al buscar objeto remoto", idCli));
        }

    }

    public void callRanking() throws RemoteException {
        List<Jugador> ranking = partida.consultaRanking();
        Logger.getLogger(JuegoCliente.class.getName()).log(
                Level.INFO, String.format(
                        "CLIENTE_%d: Recibe Ranking; %s", idCli, ranking));
    }

    void callAtaque(int jAtacado) throws RemoteException {

        Logger.getLogger(JuegoCliente.class.getName()).log(
                Level.INFO, String.format(
                        "CLIENTE_%d: Intento de ataque, como jugador %d, al jugador %d;", idCli, jugador.getId(), jAtacado));
        
        partida.ataque(jugador.getId()*10 + jAtacado);
    }

}
