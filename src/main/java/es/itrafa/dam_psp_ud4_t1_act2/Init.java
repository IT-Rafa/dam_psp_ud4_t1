/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package es.itrafa.dam_psp_ud4_t1_act2;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author it-ra
 */
public class Init {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ArrayList<JuegoCliente> jugCliList = new ArrayList<>();

        try {
            new JuegoServidor().run();
            Thread.sleep(300);
            System.out.println("MAIN: Esperando jugadores");
            
            for (int i = 0; i < JuegoServidor.CANTJUGADORES; i++) {
                jugCliList.add(new JuegoCliente(i + 1));

                jugCliList.get(i).callAsignacion();
            }
            System.out.println("MAIN: jugadores completados. Iniciamos lucha");

            for (int i = 0; i < JuegoServidor.CANTJUGADORES; i++) {
                JuegoCliente movimientoJugador = jugCliList.get(i);

                movimientoJugador.callRanking();

                movimientoJugador.callAtaque(1);
                movimientoJugador.callAtaque(1);
                movimientoJugador.callAtaque(1);
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
