/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package es.itrafa.dam_psp_ud4_t1_act2;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author it-ra
 */
public class Init {

    private static final Logger LOG = Logger.getLogger(Init.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        configLog();
        LOG.finest("Preparando contenedor para controlar clientes");
        ArrayList<JuegoCliente> jugCliList = new ArrayList<>();

        try {
            LOG.info("Iniciando servidor para partida");
            int cantJugadores = 5;
            new JuegoServidor(cantJugadores).run();
            Thread.sleep(300);
            LOG.finest("Esperando jugadores");

            for (int i = 0; i < cantJugadores; i++) {
                JuegoCliente cli = new JuegoCliente(i + 1);
                jugCliList.add(cli);

                jugCliList.get(i).callAsignacion();
                if (jugCliList.get(i).getJugador() != null) {
                    LOG.finest(String.format("Asignando al cliente %d, el jugador %d",
                            i + 1,
                            jugCliList.get(i).getJugador().getId())
                    );
                } else {
                    LOG.warning(String.format("Asignación no válida"));
                }

            }

            LOG.info("jugadores completados. Iniciamos lucha");

            int turnos = 10;
            for (int turno = 1; turno <= turnos; turno++) {
                for (int indexCli = 0; indexCli < cantJugadores; indexCli++) {

                    JuegoCliente cli = jugCliList.get(indexCli);
                    Jugador jugador = jugCliList.get(indexCli).getJugador();
                    if (jugador != null) {
                        int idJugadorActual = jugador.getId();

                        int idJugadorObjetivo;

                        int[][] rankingById = cli.callRanking();

                        if (rankingById[1][1] <= 0) {
                            LOG.info("Fin programa por partida acabada");
                            System.exit(0);
                        }
                        if (rankingById[0][0] != idJugadorActual) {
                            idJugadorObjetivo = rankingById[0][0];
                        } else {
                            idJugadorObjetivo = rankingById[1][0];
                        }
                        cli.callConsultaPS();

                        LOG.finest(String.format("jugador %d ataca a jugador %d",
                                idJugadorActual,
                                idJugadorObjetivo)
                        );
                        cli.callAtaque(idJugadorObjetivo);
                    } else {
                        LOG.warning("Cliente sin jugador asignado no puede hacer nada");
                    }

                }
            }

            Thread.sleep(300);

            LOG.info("Fin programa (a machete)");
            System.exit(0);

        } catch (RemoteException ex) {
            LOG.severe(String.format(
                    "Error comunicación con objeto remoto"));

        } catch (InterruptedException ex) {
            LOG.severe(String.format(
                    "MAIN: Error con margen tiempo servidor"));

        }

    }

    private static void configLog() {
        try {
            FileHandler handler = new FileHandler("logs\\mainLogs%g.txt", false);
            handler.setFormatter(new LogFormatter());

            LOG.addHandler(handler);
            LOG.setLevel(Level.FINEST);

        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
