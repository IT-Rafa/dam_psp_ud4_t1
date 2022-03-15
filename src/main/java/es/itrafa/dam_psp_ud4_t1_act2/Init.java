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
import java.util.logging.SimpleFormatter;

/**
 *
 * @author it-ra
 */
public class Init {

    static final Logger LOG = Logger.getLogger(Init.class.getName());

    public Init() {
        try {
            FileHandler handler  = new FileHandler("logs.txt");
            handler.setFormatter(new SimpleFormatter());

            LOG.addHandler(handler);

            LOG.setLevel(Level.FINEST);

            LOG.finest("Set Geeks=CODING");
        } catch (IOException | SecurityException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<JuegoCliente> jugCliList = new ArrayList<>();

        try {
            new JuegoServidor().run();
            Thread.sleep(300);
            LOG.finest("MAIN: Esperando jugadores");

            for (int i = 0; i < JuegoServidor.CANTJUGADORES; i++) {
                jugCliList.add(new JuegoCliente(i + 1));

                jugCliList.get(i).callAsignacion();
            }
            System.out.println("MAIN: jugadores completados. Iniciamos lucha");

            jugCliList.get(0).callRanking();

            for (int i = 0; i < JuegoServidor.CANTJUGADORES; i++) {
                JuegoCliente movimientoJugador = jugCliList.get(i);

                if (i == JuegoServidor.CANTJUGADORES - 1) {
                    movimientoJugador.callAtaque(1);
                } else {
                    movimientoJugador.callAtaque(i + 1);
                }

            }

            Thread.sleep(300);
            jugCliList.get(0).callRanking();

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

}
