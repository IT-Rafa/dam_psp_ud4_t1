/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud4_t1_act2;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Morillo Fernandez
 */
public class JuegoCliente {

    private static final Logger LOG = Logger.getLogger(JuegoCliente.class.getName());

    private static final int PORT = JuegoServidor.PORT;
    private JuegoInterface partida;
    private Jugador jugador;
    private final int idCli;

    JuegoCliente(int idCli) {
        this.idCli = idCli;

        configLog();
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
                LOG.info(String.format("CLIENTE_%d: Recibida asignación a jugador", jugador.getId()));

            } else {
                LOG.warning(String.format("CLIENTE_%d: Asignación rechazada. %s",
                        idCli, "Posible partida completa"));

            }
        } catch (NotBoundException ex) {
            LOG.severe(String.format("CLIENTE_%d: Error al buscar objeto remoto",
                    idCli));

        }

    }

    public void callRanking() throws RemoteException {
        List<Jugador> ranking = partida.consultaRanking();
        
        
        LOG.info(String.format("CLIENTE_%d: Recibido Ranking; %s", idCli, ranking));
    }

    void callConsultaPS() throws RemoteException {
        setJugador(partida.consultaPS(jugador.getId()));
        
        LOG.info(String.format(
                        "CLIENTE_%d: Como jugador %d: Tiene %s PS y %d PC",
                        idCli, jugador.getId(), jugador.getPs(), jugador.getPc()));
    }

    void callAtaque(int jAtacado) throws RemoteException {

        if (jugador == null) {
            LOG.warning(String.format(
                            "CLIENTE_nulo: No tiene jugador asignado. %s",
                    "Recuerda reiniciar servidor para reiniciar partida"));

        } else {
            LOG.info(String.format(
                            "CLIENTE_%d: Intento de ataque, como jugador %d, al jugador %d;",
                            idCli, jugador.getId(), jAtacado)
            );
            

            partida.ataque(jugador.getId() * 10 + jAtacado);
        }

    }

    private static void configLog() {
        try {
            FileHandler handler = new FileHandler("logs\\clientLogs%g.txt", false);
            handler.setFormatter(new LogFormatter());

            LOG.addHandler(handler);
            LOG.setLevel(Level.FINEST);

        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
