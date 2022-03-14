/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud4_t1_act2;

import java.net.MalformedURLException;
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
public class JuegoCliente extends Thread {

    private static final int PORT = JuegoServidor.PORT;

    private JuegoInterface partida;
    private Jugador jugador;
    private int idCli;

    JuegoCliente(int idCli) {
        this.idCli = idCli;
    }

    @Override
    public void run() {
        try {
            init();

        } catch (MalformedURLException | RemoteException | NotBoundException ex) {
            Logger.getLogger(JuegoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param args
     * @throws MalformedURLException
     * @throws RemoteException
     * @throws NotBoundException
     */
    private void init() throws
            MalformedURLException, RemoteException, NotBoundException {
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

            // Consulta Rankin inicial
            List<Jugador> lista = partida.consultaRanking();
            Logger.getLogger(JuegoCliente.class.getName()).log(
                    Level.INFO, String.format(
                            "CLIENTE_%d: pidió Ranking jugadores: %s", idCli, lista.toString()));

            // cada 3 ataques preguntamos rankin para definir primero
            
            // Si somos el primero  Atacamos al segundo hasta que muera
            // si no, Atacamos al primero hasta que muera
            
        } else {
            Logger.getLogger(JuegoCliente.class.getName()).log(
                    Level.WARNING, String.format(
                            "CLIENTE_%d:: Servidor no pudo asignar jugador. Posible partida completa", idCli));
        }

    }
}
