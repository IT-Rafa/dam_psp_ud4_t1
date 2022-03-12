/**
 * Paquete que contiene las clases usadas para el siguiente enunciado:
 * 
 *  * <h3>Enunciado PSP04 - Actividad 4.1</h3>
 * Modifica el ejercicio 1 de la unidad 3 para que el servidor permita trabajar 
 * de forma concurrente con varios clientes que le soliciten tickets.
 * 
 * <h3>Referencia: Enunciado PSP03 - Actividad 3.1</h3>
 * El objetivo del ejercicio es crear una aplicación cliente/servidor que se comunique 
 * por el puerto 2000 y realice lo siguiente:
 * <ul>
 *  <li>Diseña una aplicación cliente/servidor TCP donde el cliente le envía
 *      el nombre del usuario, unidades, fecha y el tipo de entrada al parque de atracciones.</li>
 *  <li>Hay cuatro tipos de entradas del parque:
 *      <ul>
 *          <li>Normal: 10 €</li>
 *          <li>Niños: 3 €</li>
 *          <li>Carnet joven: 5 €</li>
 *          <li>3ª edad: 4 €</li>
 *      </ul>
 *  <li>El servidor le quitará un ticket en el que figure los datos del usuario, fecha y 
 *      el importe total.</li>
 *  <li>El paso de datos del cliente al servidor se hará a través de un objeto de la clase 
 *      ticket y viceversa, que has de definir.</li>
 * </ul>
 * 
 */
package es.itrafa.dam_psp_ud4_t1_act1;
