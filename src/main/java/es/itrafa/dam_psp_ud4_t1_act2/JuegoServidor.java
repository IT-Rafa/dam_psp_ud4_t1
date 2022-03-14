/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud4_t1_act2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Juan Morillo Fernandez
 */
public class JuegoServidor extends Thread implements JuegoInterface {

    private Registry rgsty;
    protected static final int PORT = 2000;
    protected static final int CANTJUGADORES = 5;
    private final List<Jugador> listaJugadores;
    private static int jugadorLibre = 0;

    // CONSTRUCTOR
    public JuegoServidor() {
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
        if (jugadorLibre < CANTJUGADORES) {
            j = listaJugadores.get(jugadorLibre);
            j.setAsignado(true);
            jugadorLibre++;

            Logger.getLogger(JuegoServidor.class.getName()).log(
                    Level.INFO, String.format(
                            "SERVER: Jugador %d ha sido asignado", j.getId()));
        } else {

            Logger.getLogger(JuegoServidor.class.getName()).log(
                    Level.WARNING, String.format("%s: %s; %s",
                            "SERVER", "Intento de asignación de jugador no válido",
                            "Todos los jugadores de la partida fueron asignados"));
        }

        return j;
    }

    @Override
    public List<Jugador> consultaRanking() throws RemoteException {
        Logger.getLogger(JuegoServidor.class.getName()).log(
                Level.INFO, String.format(
                        "SERVER: Jugador %d Consulta Ranking ", 0));
        return listaJugadores;
    }

    @Override
    public Jugador consultaPS(int id) throws RemoteException {
        Logger.getLogger(JuegoServidor.class.getName()).log(
                Level.INFO, String.format(
                        "SERVER: Jugador %d Consulta PS ", 0));
        return null;
    }

    @Override
    public void ataque(int jugadorAtacado) throws RemoteException {
        Logger.getLogger(JuegoServidor.class.getName()).log(
                Level.INFO, String.format(
                        "SERVER: Jugador Ataca a Jugador %d", jugadorAtacado));

        // reordena rankin tras ataque
        ordenarRankin();
    }

    @Override
    public void run() {
        try {
            //
            rgsty = LocateRegistry.createRegistry(PORT);
            JuegoServidor gameServer = new JuegoServidor();
            //
            rgsty.rebind("GameServer", UnicastRemoteObject.exportObject(gameServer, PORT));
            //
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
        for (int i = 1; i < listaJugadores.size(); i++) {
            listaJugadores.get(i - 1).setPosicion(i);
        }

    }

}
