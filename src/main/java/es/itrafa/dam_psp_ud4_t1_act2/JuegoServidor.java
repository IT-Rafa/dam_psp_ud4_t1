/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud4_t1_act2;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Morillo Fernandez
 */
public class JuegoServidor implements JuegoInterface {

    static final Logger LOG = Logger.getLogger(JuegoServidor.class.getName());

    private Registry rgsty;
    protected static final int PORT = 2000;
    protected static final int CANTJUGADORES = 5;
    private final List<Jugador> listaJugadores;
    private static int jugadorLibre = 0;

    // CONSTRUCTOR
    public JuegoServidor() {
        configLog();
        listaJugadores = new ArrayList<>();

        for (int i = 1; i <= CANTJUGADORES; i++) {
            Jugador j = new Jugador();
            // incializar datos jugador
            j.setId(i);
            j.setPs(40 + new Random().nextInt(21)); // 40-60
            j.setPc(20 + new Random().nextInt(81)); // 20-100
            j.setAsignado(false);

            listaJugadores.add(j);
        }
        ordenarRankin();

    }

    @Override
    public Jugador asignacionJugador() throws RemoteException {
        Jugador j = null;
        String ipCli = null;

        try {
            ipCli = RemoteServer.getClientHost();
        } catch (ServerNotActiveException ex) {
            LOG.severe(ex.getMessage());

        }

        if (jugadorLibre < CANTJUGADORES) {
            j = listaJugadores.get(jugadorLibre);
            j.setAsignado(true);
            j.setIp(ipCli);
            jugadorLibre++;

            LOG.info(String.format("SERVER: Asignado el jugador %d al cliente con IP:%s",
                    j.getId(), j.getIp()));

        } else {
            LOG.warning(String.format("SERVER: %s; %s",
                    "Intento de asignación de jugador no válido",
                    "Todos los jugadores de la partida fueron asignados"));

        }

        return j;
    }

    @Override
    public List<Jugador> consultaRanking() throws RemoteException {
        LOG.info(String.format(
                "SERVER: Cliente Consulta Ranking "));

        return Collections.unmodifiableList(listaJugadores);
    }

    @Override
    public Jugador consultaPS(int id) throws RemoteException {
        Jugador j = findJugadorById(id);

        LOG.info(String.format("SERVER: Cliente con jugador %d Consulta sus PS ",
                j.getId()));
        return j;
    }

    @Override
    public void ataque(int idsAtacanteAtacado) throws RemoteException {
        // SOLUCIÓN IMPROVISADA INTERFAZ REMOTA
        // AL ATACAR, SERVIDOR NO CONOCE QUIEN INICIA EL ATAQUE
        // Válido mientras cant jugadores sea 1-9
        // parámetro jugadorAtacado: void ataque (int jugadorAtacado) es
        // convertida en idsAtacanteAtacado (decenas=atacante, unidades=atacado)
        // Ejemplo: jugador 3 ataca a jugador 2
        // idsAtacanteAtacado = idAtacante*10 + idAtacado 3 *10 +2 = 32

        // Reconvertimos en id
        // 32 / 10 = 3;
        int idJugadorAtacante = idsAtacanteAtacado / 10;
        // 32 - 3*10= 2
        int idJugadorAtacado = idsAtacanteAtacado - idJugadorAtacante * 10;
        Jugador agresor = findJugadorById(idJugadorAtacante);
        Jugador agredido = findJugadorById(idJugadorAtacado);

        // Controlamos ataque
        if (listaJugadores.size() == CANTJUGADORES) {
            if (agresor.equals(agredido)) {
                // Pegarse a si mismo e estupido
                Logger.getLogger(JuegoServidor.class.getName()).log(
                        Level.INFO, String.format(
                                "SERVER: Intento ataque jugador %d al jugador %d nulo; Ataque a si mismo",
                                idJugadorAtacante, idJugadorAtacado));

            } else if (agresor.getPs() <= 0) {
                // agresor ya murio y no puede atacar
                Logger.getLogger(JuegoServidor.class.getName()).log(
                        Level.INFO, String.format(
                                "SERVER: Intento ataque jugador %d al jugador %d nulo; Ataque de jugador ya eliminado",
                                idJugadorAtacante, idJugadorAtacado));

            } else if (agredido.getPs() <= 0) {
                // agredido ya murio y no puede ser atacado
                Logger.getLogger(JuegoServidor.class.getName()).log(
                        Level.INFO, String.format(
                                "SERVER: Intento ataque jugador %d al jugador %d nulo; Ataque a jugador ya eliminado",
                                idJugadorAtacante, idJugadorAtacado));

            } else {
                int oldPS = agredido.getPs();
                int ataque = agresor.getPc()
                        * (1 - new Random().nextInt(2) * new Random().nextInt(2))
                        / 5;
                agredido.setPs(agredido.getPs() - ataque);
                Logger.getLogger(JuegoServidor.class.getName()).log(
                        Level.INFO, String.format(
                                "SERVER: Ataque exitoso del jugador %d al jugador %d de %d puntos: J%d PS = %d -%d = %d",
                                idJugadorAtacante, idJugadorAtacado, ataque, idJugadorAtacado, oldPS, ataque, agredido.getPs()));

                ordenarRankin();
            }

        } else {
            Logger.getLogger(JuegoServidor.class.getName()).log(
                    Level.WARNING, String.format(
                            "SERVER: Intento ataque del jugador %d nulo; Jugadores incompletos",
                            idJugadorAtacante));
        }

    }

    public void run() {
        try {
            //

            rgsty = LocateRegistry.createRegistry(PORT);
            JuegoServidor gameServer = new JuegoServidor();
            //
            rgsty.rebind("GameServer", UnicastRemoteObject.exportObject(gameServer, PORT));

            Logger.getLogger(JuegoServidor.class.getName()).log(
                    Level.INFO, String.format(
                            "SERVER: Listening in port %d", PORT));

        } catch (RemoteException e) {
            if (e.getMessage().startsWith("Port already in use")) {
                Logger.getLogger(JuegoServidor.class.getName()).log(
                        Level.WARNING, String.format(
                                "SERVER: %s - %s",
                                "bind exeption",
                                "Ese puerto está en uso. Posiblemente mal cerrado"));
            } else {
                Logger.getLogger(JuegoServidor.class.getName()).log(
                        Level.SEVERE, String.format(
                                "SERVER ERROR: %s - %s",
                                "RemoteException",
                                e.getCause()));
            }

        }
    }

    private void ordenarRankin() {
        Collections.sort(listaJugadores, (Jugador j1, Jugador j2)
                -> Integer.valueOf(j2.getPs()).compareTo(j1.getPs()));

        for (int i = 1; i < listaJugadores.size() + 1; i++) {
            listaJugadores.get(i - 1).setPosicion(i);
        }

    }

    private Jugador findJugadorById(int idJugador) {
        for (int i = 0; i < listaJugadores.size(); i++) {
            if (listaJugadores.get(i).getId() == idJugador) {
                return listaJugadores.get(i);
            }
        }

        return null;
    }

    private static void configLog() {
        try {
            System.out.println("Ver logs completos en carpeta logs en proyecto");
            FileHandler handler = new FileHandler("logs\\serverLogs%g.txt", false);
            handler.setFormatter(new LogFormatter());

            LOG.addHandler(handler);
            LOG.setLevel(Level.FINEST);

        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
