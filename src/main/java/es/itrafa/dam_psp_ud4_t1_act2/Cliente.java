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

/**
 *
 * @author Juan Morillo Fernandez
 */
public class Cliente {

    private static RMIInterface look_up;

    public static void main(String[] args) throws
            MalformedURLException, RemoteException, NotBoundException {

      Registry registry = LocateRegistry.getRegistry("localhost", 1888);
        
        //Obteniendo los metodos remotos --> polimorfismo con interfaces
      look_up = (RMIInterface) registry.lookup("Bookstore"); //Buscar en el registro...
         
       
        //Ejecutar todos los libros
        List lista= look_up.allBooks();
        StringBuilder mensaje= new StringBuilder();
        //instrucción lambda
        lista.forEach(x -> {
                        mensaje.append(x.toString() + "\n");;
         }); 
        System.out.println(mensaje);
        
        //Buscar un libro por isbn
        Book libro= look_up.findBook(new Book("978-0596007737"));
        
        if (libro != null){
                   System.out.println("Libro encontrado: "+libro);
 
        }else {
             System.out.println("Libro no encontrado");
        }
        
}
}
