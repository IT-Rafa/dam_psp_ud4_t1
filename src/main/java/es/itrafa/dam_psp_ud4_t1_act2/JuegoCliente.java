/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud4_t1_act2;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.List;
import java.util.logging.Logger;

/**
 * Representa un cliente que quiere jugar una partida, para lo que tendrá asignado
 * un jugador. Los métodos controlan las llamadas remotas y muestran el resultado
 * 
 * @author it-ra
 */
public class JuegoCliente {

    private static final Logger LOG = Logger.getLogger(JuegoCliente.class.getName());
    /**
     * Puerto del servidor de objetos RMI (donde se gestiona el juego)
     */
    private static final int PORT = Registry.REGISTRY_PORT;
    /**
     * Interface de objeto remoto para ejecutar métodos del juego
     */
    private JuegoInterface partida;
    /**
     * Jugador que nos asigna el objeto remoto
     */
    private Jugador jugador;
    /**
     * Identificador de cliente (no confundir con id del jugador asigndo)
     */
    private int idCli;

    // CONSTRUCTORS
    /**
     * Crea el objeto Juego cliente con el id asignado y prepara la conexión al
     * servidor del juego (objeto remoto)
     *
     * @param idCli
     * @throws RemoteException
     */
    JuegoCliente(int idCli) throws RemoteException {
        this.idCli = idCli;
        try {
            // Capturamos el servidor de objetos remotos en equipo servidor
            Registry registry = LocateRegistry.getRegistry("localhost", PORT);

            // Buscamos objeto remoto por nombre
            partida = (JuegoInterface) registry.lookup("GameServer");

        } catch (NotBoundException ex) {
            LOG.severe(String.format("CLIENTE_%d: Error al buscar objeto remoto",
                    idCli));
        }
    }

    /**
     * Devuelve la inteface del objeto remoto
     *
     * @return partida
     */
    public JuegoInterface getPartida() {
        return partida;
    }

    /**
     * Modifica la inteface del objeto remoto
     *
     * @param partida
     */
    public void setPartida(JuegoInterface partida) {
        this.partida = partida;
    }

    /**
     * Devuelve el jugador asignado
     *
     * @return jugador
     */
    public Jugador getJugador() {
        return jugador;
    }

    /**
     * Modifica el jugador asignado
     *
     * @param jugador
     */
    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    /**
     * Devuelve id del cliente (No es el id del jugador)
     *
     * @return idCli
     */
    public int getIdCli() {
        return idCli;
    }

    /**
     * Modifica id del cliente (No es el id del jugador)
     *
     * @param idCli
     */
    public void setIdCli(int idCli) {
        this.idCli = idCli;
    }

    /**
     * Llama al método del objeto remoto para asignar un jugador y muestra la
     * información
     *
     * @throws RemoteException
     */
    public void callAsignacion() throws RemoteException {

        // Entrando en partida
        jugador = partida.asignacionJugador();

        if (jugador != null) {
            LOG.info(String.format("CLIENTE_%d: Recibida asignación a Jugador %d",
                    idCli, jugador.getId()));

        } else {
            LOG.warning(String.format("CLIENTE_%d: Asignación rechazada. %s",
                    idCli, "Posible partida completa"));
        }

    }

    /**
     * Llama al método del objeto remoto para Pedir el ranking de jugadores y
     * muestra la información. Solo si tenemos jugador asignado
     *
     * @return Lista de jugadores ordenada por puntos salud
     * @throws RemoteException
     */
    public List<Jugador> callRanking() throws RemoteException {
      
        // Pedimos lista jugadores ordenada por PS al objeto remoto
        List<Jugador> ranking = partida.consultaRanking();

        // Formateamos lista para mostrarla
        String rankingList = "";

        if (ranking.get(1).getPs() > 0) { // Segundo sigue vivo ( partida en progreso)
            for (Jugador j : ranking) {

                if (j.getPs() > 0) {
                    rankingList = rankingList.
                            concat("\t" + j.getPosicion() + "º: jugador " + j.getId()).
                            concat(" con " + j.getPs() + " de salud\n");
                }

            }
        } else { // PARTIDA ACABADA. Se pueden enviar peticiones, pero serán nulas
            rankingList = "\tPartida finalizada: Ganador... ¡¡ Jugador " + ranking.get(0).getId() + "!!";
        }

        // Quitamos salto línea sobrante
        rankingList = rankingList.substring(0, rankingList.length() - 2);

        // Mostramos ranking en consola
        LOG.info(String.format("CLIENTE_%d: Recibido Ranking; \n%s", idCli, rankingList));

        return ranking;
    }

    /**
     * Llama al método del objeto remoto para Pedir Consular Puntos salud y
     * combate y muestra la información. Solo si ya tenemos jugador asignado
     *
     * @throws RemoteException
     */
    void callConsultaPS() throws RemoteException {
        if (jugador == null) {
            LOG.warning(String.format(
                    "CLIENTE_nulo: No tiene jugador asignado. No se puede pedir PS %s",
                    "Recuerda reiniciar servidor para reiniciar partida"));
            return;
        }
        // actualizamos datos de nuestro jugador asignado
        setJugador(partida.consultaPS(jugador.getId()));

        // mostramos los datos
        LOG.info(String.format(
                "CLIENTE_%d: Como jugador %d: Tiene %s PS y %d PC",
                idCli, jugador.getId(), jugador.getPs(), jugador.getPc()));

    }

    /**
     * Llama al método del objeto remoto para atacar a otro jugador y muestra la
     * información. Solo si ya tenemos jugador asignado
     *
     * @throws RemoteException
     */
    void callAtaque(int jAtacado) throws RemoteException {

        if (jugador == null) {
            LOG.warning(String.format(
                    "CLIENTE_nulo: No tiene jugador asignado. %s",
                    "Recuerda reiniciar servidor para reiniciar partida"));

        } else {
            LOG.info(String.format(
                    "CLIENTE_%d: Intento de ataque, como jugador %d, al jugador %d;",
                    idCli, jugador.getId(), jAtacado)
            );

            // Enviamos id atacante e id atacado oon mismo parámetro int
            // decena= id_nuestro_jugador; unidades= id_jugador atacado
            partida.ataque(jugador.getId() * 10 + jAtacado);
        }

    }

}
