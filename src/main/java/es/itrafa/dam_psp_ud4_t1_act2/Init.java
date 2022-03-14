/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package es.itrafa.dam_psp_ud4_t1_act2;

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
        try {
            new JuegoServidor().start();
            Thread.sleep(300);
            
            for (int i = 1; i <= JuegoServidor.CANTJUGADORES +1; i++) {
                new JuegoCliente(i).start();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Init.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
