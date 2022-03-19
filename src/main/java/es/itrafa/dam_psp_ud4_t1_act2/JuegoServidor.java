/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.itrafa.dam_psp_ud4_t1_act2;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Se encarga de preparar un objeto remoto y publicarlo, que almacenará los
 * datos y métodos para dirigir una partida entre varios jugadores asignados a
 * clientes
 *
 * @author it-ra
 */
public class JuegoServidor implements JuegoInterface {

    static final Logger LOG = Logger.getLogger(JuegoServidor.class.getName());
    private static final int PORT = Registry.REGISTRY_PORT;

    /**
     * Lista jugadores (se irá ordenando por PS cuando cambien)
     */
    private final List<Jugador> listaJugadores;
    /**
     * Cantidad de jugadores que entrarán en LA partida
     */
    private final int cantJugadores;
    /**
     * Controla el el siguiente jugador a asignar
     */
    private int jugadorLibre = 0;

    // CONSTRUCTOR
    /**
     * Prepara lista jugadores que serán asignados a clientes y, a cada uno, le
     * asigna unos puntos aleatorios
     *
     * @param cantJugadores
     */
    public JuegoServidor(int cantJugadores) {
        listaJugadores = new ArrayList<>();
        this.cantJugadores = cantJugadores;

        // asginamos puntos aleatorios a cada jugador
        for (int i = 1; i <= cantJugadores; i++) {
            Jugador j = new Jugador();
            // incializar datos jugador
            // id jugador
            j.setId(i);
            // ps (puntos salud) jugador
            j.setPs(40 + new Random().nextInt(21)); // 40-60
            // pc (puntos combate) jugador
            j.setPc(20 + new Random().nextInt(81)); // 20-100
            j.setAsignado(false);

            listaJugadores.add(j);
        }
        // reordenamos por puntos de salud
        ordenarRankin();

    }

    /**
     * Al recibir la petición de un cliente, le asigna un jugador, si queda
     * alguno libre, le modifica los datos correspondientes y se lo envía.
     * También se prepara para la siguiente asignación.
     *
     * @return El objeto Jugador asignado, si quedan vacantes. Si no devuelve
     * null
     * @throws RemoteException
     */
    @Override
    public Jugador asignacionJugador() throws RemoteException {
        // prepara jugador a asignar
        Jugador j = null;
        String ipCli;

        // Intenta capturar ip del cliente que pide asignación
        // En principio era para que el servidor identificara quien le llamaba, pero
        // como las pruebas las hice en el mismo equipo todos tienen la misma IP
        // Lo deje porque en un entorno real podría ser útil
        try {
            ipCli = RemoteServer.getClientHost();
        } catch (ServerNotActiveException ex) {
            LOG.warning("Error al intentar capturar la ip del cliente.");
            ipCli = "IP NO DETECTADA";
        }

        // Solo acepta clientes si quedan vacantes
        if (jugadorLibre < cantJugadores) {
            // modificamos datos jugador por estar asignado
            j = listaJugadores.get(jugadorLibre);
            j.setAsignado(true);
            j.setIp(ipCli);

            // preparamos para siguiente asignación
            jugadorLibre++;

            LOG.info(String.format("Asignado el jugador %d al cliente con IP:%s",
                    j.getId(), j.getIp()));

        } else {
            // Ya no se aceptan clientes
            LOG.warning(String.format("Intento de asignación de jugador no válido; %s",
                    "Todos los jugadores de la partida fueron asignados"));

        }

        // devuelve jugador asignado o null
        return j;
    }

    /**
     * Al recibir la petición de un cliente, si todos los jugadores están
     * asignados, le devuelve una lista de los jugadores ordenada por los puntos
     * de salud. Si no están asignados, devuelve null
     *
     * @return Si todos Jugadores están asignados, la lista de objetos Jugador
     * ordenada por puntos de salud. Si no null.
     *
     * @throws RemoteException
     */
    @Override
    public List<Jugador> consultaRanking() throws RemoteException {
        // Si se hace la petición antes de que estén todos los jugadores asignados
        if (jugadorLibre < cantJugadores) {
            LOG.info(String.format("Cliente Consulta Ranking; Jugadores incompletos "));
            return null;
        }

        LOG.info(String.format("Cliente Consulta Ranking. Enviamos "));
        return Collections.unmodifiableList(listaJugadores);
    }

    /**
     * Devuelve PS Puntos Salud de un jugador según su id
     * <p>
     * Nota: según enunciado el cliente pediría solo sus puntos de salud, pero
     * servidor no sabe quien es cliente. Además interfaz de enunciado ya está
     * definida.
     *
     * @param id
     * @return Si localiza al jugador por su id, devuelve el jugador. Si no null
     * @throws RemoteException
     */
    @Override
    public Jugador consultaPS(int id) throws RemoteException {
        //Localizamos jugador el listaJugadores
        Jugador j = findJugadorById(id);

        LOG.info(String.format("Cliente Consulta datos jugador %d",
                j.getId()));
        return j;
    }

    /**
     * Al recibir la petición de un cliente, si localiza el cliente y el jugador
     * a atacar, calcula el daño en base a los PC del cliente y se los resta al
     * jugador atacado.El ataque será nulo en el caso de que no se encuentre
     * alguno de los jugadores o alguno de los jugadores haya sido elimindo
     * (PS=0 o menos)
     * <p>
     * <b>AVISO: SOLUCIÓN IMPROVISADA PARA PROBLEMA ENUNCIADO INTERFAZ REMOTA:
     * AL ATACAR, SERVIDOR NO CONOCE QUIEN INICIA EL ATAQUE</p>
     * <p>
     * (Válido mientras cant jugadores sea 1-9)</p>
     * <p>
     * parámetro <b>jugadorAtacado</b> en método de interfaz remota <b>void
     * ataque (int jugadorAtacado)</b> es interpretada como
     * <b>idsAtacanteAtacado</b> (siendo: decenas=atacante,
     * unidades=atacado)</p>
     * <p>
     * Ejemplo: jugador 3 ataca a jugador 2 idsAtacanteAtacado = idAtacante*10 +
     * idAtacado 3 *10 +2 = 32</b>
     *
     * @param idsAtacanteAtacado
     * @throws RemoteException
     */
    @Override
    public void ataque(int idsAtacanteAtacado) throws RemoteException {
        // Reconvertimos en id de atacante e id de atacado
        // 32 / 10 = 3;
        int idJugadorAtacante = idsAtacanteAtacado / 10;
        // 32 - 3*10= 2
        int idJugadorAtacado = idsAtacanteAtacado - idJugadorAtacante * 10;
        Jugador agresor = findJugadorById(idJugadorAtacante);
        Jugador agredido = findJugadorById(idJugadorAtacado);

        // Controlamos ataque
        if (listaJugadores.size() == cantJugadores) {
            // Jugadores completos
            if (agresor.equals(agredido)) {
                // Pegarse a si mismo e estupido
                LOG.warning(String.format("%s %d %s %d %s",
                        "Intento ataque jugador", idJugadorAtacante,
                        "al jugador", idJugadorAtacado,
                        "nulo; Ataque a si mismo"));

            } else if (agresor.getPs() <= 0) {
                // agresor ya murio, por tanto no puede atacar
                LOG.warning(String.format("%s %d %s %d %s",
                        "Intento ataque jugador", idJugadorAtacante,
                        "al jugador", idJugadorAtacado,
                        "nulo; Ataque de jugador ya eliminado"));

            } else if (agredido.getPs() <= 0) {
                // agredido ya murio, está feo atacarle
                LOG.warning(String.format("%s %d %s %d %s",
                        "Intento ataque jugador", idJugadorAtacante,
                        "al jugador", idJugadorAtacado,
                        "nulo; Ataque a jugador ya eliminado"));

            } else {
                // ataque válido
                int oldPS = agredido.getPs();
                int ataque = agresor.getPc()
                        * (1 - new Random().nextInt(2) * new Random().nextInt(2))
                        / 5;
                agredido.setPs(agredido.getPs() - ataque);

                LOG.info(String.format("%s %d %s %d tuvo éxito(%d). J%d PS = %d -%d = %d",
                        "Intento ataque del jugador", idJugadorAtacante,
                        "al jugador", idJugadorAtacado,
                        ataque, idJugadorAtacado, oldPS, ataque, agredido.getPs()));

                // Reordenamos ranking
                ordenarRankin();
            }

        } else {
            // faltan jugadores
            LOG.warning(String.format("Intento ataque del jugador %d nulo; %s",
                    idJugadorAtacante,
                    "Jugadores incompletos"));

        }

    }

    /**
     * Publica el objeto remoto en equipo servidor, asignandole un puerto.
     */
    public void run() {
        try {
            //

            Registry rgsty;
            // Añadimos registro de objeto remoto a servidor de objetos de nuestro equipo
            rgsty = LocateRegistry.createRegistry(PORT);

            // Creamos objeto que queremos que sea remoto
            JuegoServidor gameServer = new JuegoServidor(cantJugadores);

            // Asignamos el objeto al registro creado y le asignamos un nombre
            rgsty.rebind("GameServer", UnicastRemoteObject.exportObject(gameServer, PORT));

            LOG.info(String.format("Start RMI Server; Listening in port %d", PORT));

        } catch (RemoteException e) {
            if (e.getMessage().startsWith("Puerto en uso. Comprobar que main() no se ejecuto antes")) {
                LOG.warning(String.format(
                        "SERVER: %s - %s",
                        "bind exeption",
                        "Ese puerto está en uso. Revisar arranques previos"));

            } else {
                LOG.severe(String.format(
                        "ERROR: %s - %s",
                        "RemoteException",
                        e.getCause()));
            }

        }
    }

    /**
     * Ordena la lista de jugadores según puntos de salud
     */
    private void ordenarRankin() {
        Collections.sort(listaJugadores, (Jugador j1, Jugador j2)
                -> Integer.valueOf(j2.getPs()).compareTo(j1.getPs()));

        for (int i = 1; i < listaJugadores.size() + 1; i++) {
            listaJugadores.get(i - 1).setPosicion(i);
        }

    }

    /**
     * Localiza a un jugador de listaJugadores por su id
     *
     * @param idJugador - id del jugador a buscar
     * @return El objeto Jugador localizado, o null si no se localiza.
     */
    private Jugador findJugadorById(int idJugador) {
        for (int i = 0; i < listaJugadores.size(); i++) {
            if (listaJugadores.get(i).getId() == idJugador) {
                return listaJugadores.get(i);
            }
        }

        return null;
    }

}
