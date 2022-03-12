/**
 * Paquete que contiene las clases usadas para el siguiente enunciado:
 * <h3>Enunciado PSP04 - Actividad 4.2</h3>
 * Se necesita desarrollar un pequeño videojuego con arquitectura cliente
 * servidor con RMI. El juego dispondrá de 5 jugadores que se deben generar en
 * el servidor.:
 *
 * Estos jugadores tendrán dos parámetros:
 * <ul>
 * <li>Puntos de Salud (PS) .</li>
 * <li>Puntos de Combate (PC).</li>
 * </ul>
 *
 * Se deben generar de manera aleatoria dichos parámetros. Los PS entre 40-60 y
 * PC entre 20-100.
 *
 * El servidor debe asignar al cliente uno de los 5 jugadores y atender a tres
 * acciones del cliente:
 * <ul>
 * <li>Consulta ranking completo de los 5 jugadores.</li>
 * <li>Ataque de un jugador a otro.</li>
 *  *  <li>
 * Formula de ataque y descuento de salud:
 *  * <ul>
 *      <li>Consulta ranking completo de los 5 jugadores.</li>
 *      <li>Ataque de un jugador a otro.</li>
 *      <li>Formula de ataque y descuento de salud:</li>
 *     </ul>
 * </li>
 * </ul>
 *
 *
Formula de ataque y descuento de salud: Ataque = PC (jugador que ataca) * ( 1
 * - random * random ) / 5 ; Nota: random = número aleatorio entre 0 y 1 PS
 * (jugador atacado) = PS (jugador atacado) - Ataque Consulta de puntos de salud
 * y puntos de combate (invariable) del cliente.
 *
 */
package es.itrafa.dam_psp_ud4_t1_act2;
