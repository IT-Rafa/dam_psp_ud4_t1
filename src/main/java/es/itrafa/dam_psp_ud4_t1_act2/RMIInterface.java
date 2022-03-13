/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud4_t1_act2;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 *
 * @author Juan Morillo Fernandez
 */
public interface RMIInterface extends Remote {

    Book findBook(Book b) throws RemoteException;
    List<Book> allBooks() throws RemoteException;

}
