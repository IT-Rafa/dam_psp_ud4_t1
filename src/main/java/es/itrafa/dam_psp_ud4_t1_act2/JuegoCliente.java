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
    private final int idCli;

    JuegoCliente(int idCli) {
        this.idCli = idCli;
    }

    @Override
    public void run() {
        try {
            init();
            fight();

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

        } else {
            Logger.getLogger(JuegoCliente.class.getName()).log(
                    Level.WARNING, String.format(
                            "CLIENTE_%d:: Servidor no pudo asignar jugador. Posible partida completa", idCli));
        }

    }

    private void fight() {
        try {
            // atacamos 1 vez al jugador de la derecha (siguiente en la lista)
            // (Entendemos el mas a la izquierda el 1 y el mas a la derecha el 5.
            //  Al llegar a 5, ataca al 1.)
            // Si vivo. repetimos ataque
            // Si muerto, vamos al proximo a su derecha que esté vivo
            
            // Atacamos al siguiente: j1 a j2, j2->j3,... j5->j1
            
            
            partida.ataque(jugador.getId(), jugador.getId() +1);
            
            
        } catch (RemoteException ex) {
            Logger.getLogger(JuegoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
