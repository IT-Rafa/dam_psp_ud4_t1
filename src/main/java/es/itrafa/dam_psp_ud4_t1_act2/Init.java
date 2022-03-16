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

    static final Logger LOG = Logger.getLogger(Init.class.getName());

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        configLog();
        LOG.finest("Preparando contenedor para controlar clientes");
        ArrayList<JuegoCliente> jugCliList = new ArrayList<>();

        try {
            LOG.info("Iniciando servidor para partida");
            new JuegoServidor().run();
            Thread.sleep(300);
            LOG.finest("Esperando jugadores");

            for (int i = 0; i < JuegoServidor.CANTJUGADORES; i++) {
                jugCliList.add(new JuegoCliente(i + 1));

                jugCliList.get(i).callAsignacion();
                LOG.finest(String.format("Asignando al cliente %d, el jugador %d",
                        i + 1,
                        jugCliList.get(i).getJugador().getId())
                );
            }

            LOG.info("jugadores completados. Iniciamos lucha");

            int turnos = 5;
            for (int turno = 0; turno < turnos; turno++) {
                for (int indexCli = 0; indexCli < JuegoServidor.CANTJUGADORES; indexCli++) {

                    JuegoCliente cli = jugCliList.get(indexCli);
                    JuegoInterface movPartida = cli.getPartida();
                    Jugador jugador = jugCliList.get(indexCli).getJugador();
                    int idJugador = jugador.getId();

                    cli.callRanking();
                    cli.callConsultaPS();
                    
                    movPartida.consultaPS(idJugador);

                    int iDagredido;

                    if (jugador.getId() != JuegoServidor.CANTJUGADORES) {
                        iDagredido = jugador.getId() + 1;
                    } else {
                        iDagredido = 1;
                    }
                    LOG.finest(String.format("jugador %d ataca a jugador %d",
                            idJugador,
                            iDagredido)
                    );
                    cli.callAtaque(iDagredido);
                }
            }

            Thread.sleep(300);

            LOG.info("Fin programa (a machete)");
            System.exit(0);

        } catch (RemoteException ex) {
            Logger.getLogger(Init.class.getName()).log(
                    Level.SEVERE, String.format(
                            "MAIN: : Error comunicación con objeto remoto"));

        } catch (InterruptedException ex) {
            Logger.getLogger(Init.class.getName()).log(
                    Level.SEVERE, String.format(
                            "MAIN: Error con margen tiempo servidor"));
        }

    }

    private static void configLog() {
        try {
            System.out.println("Ver logs completos en carpeta logs en proyecto");
            FileHandler handler = new FileHandler("logs\\mainLogs%g.txt", false);
            handler.setFormatter(new LogFormatter());

            LOG.addHandler(handler);
            LOG.setLevel(Level.FINEST);

        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
