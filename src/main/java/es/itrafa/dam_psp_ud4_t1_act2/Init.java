package es.itrafa.dam_psp_ud4_t1_act2;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Clase para testear el inicio y control de los movimientos de cada jugador en la partida
 *
 * @author it-ra
 */
public class Init {

    private static final Logger LOG = Logger.getLogger(Init.class.getName());

    /**
     * Inicia y controla y finaliza la partida al existir ganador
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LOG.finest("Preparando contenedor para controlar clientes");
        ArrayList<JuegoCliente> jugCliList = new ArrayList<>();

        try {
            int cantJugadores = 5;
            new JuegoServidor(cantJugadores).run();
            LOG.finest(String.format("Iniciando servidor para partida de %d jugadores",
                    cantJugadores));

            
            Thread.sleep(300);
            LOG.finest("Esperando clientes para asignarles jugador");

            // Por cada cliente
            LOG.finest("Añadiendo clientes al contenedor");
            for (int i = 0; i < cantJugadores; i++) {

                LOG.finest("Creamos cliente y le asignamos id");
                JuegoCliente cli = new JuegoCliente(i + 1);

                LOG.finest("Añadimos cliente a la lista");
                jugCliList.add(cli);

                LOG.finest("Petición a objeto remoto para que nos asigne jugador");
                jugCliList.get(i).callAsignacion();

                if (jugCliList.get(i).getJugador() != null) {
                    LOG.finest(String.format("Asignado al cliente %d, el jugador %d",
                            jugCliList.get(i).getIdCli(),
                            jugCliList.get(i).getJugador().getId())
                    );
                } else {
                    // solo debería llegar aqui si repetimos ejecución main o bucle mal formado
                    LOG.warning(String.format("Asignación no válida. Probable partida completa."));
                }

            }
            LOG.info("jugadores completados. Iniciamos lucha\n");

            // Después de hacer varias pruebas, con 10 turnos bastan para finalizar la partida
            int turnos = 10;
            LOG.finest(String.format("Cada cliente hará sus movimientos (max. %d) hasta que halla ganador.", turnos));

            // Por cada turno
            for (int turno = 1; turno <= turnos; turno++) {
                // Por cada cliente
                for (int indexCli = 0; indexCli < cantJugadores; indexCli++) {
                    // Capturamos cliente y sus datos para manejarlo
                    JuegoCliente cli = jugCliList.get(indexCli);
                    Jugador jugador = jugCliList.get(indexCli).getJugador();

                    if (jugador != null) {
                        int idJugadorActual = jugador.getId();
                        LOG.info(String.format("Inicio Movimientos turno %d: Cliente %d(Jugador %d)\n",
                                turno, cli.getIdCli(), idJugadorActual));

                        LOG.finest(String.format("Cliente %d(Jugador %d): pide Ranking",
                                cli.getIdCli(), idJugadorActual));

                        // El cliente nos devuelve un ranking simplificado con el id del jugador y su salud
                        // Usaremos esto para saber a quien atacar y cuando finaliza la partida
                        List<Jugador> newRanking = cli.callRanking();

                        // Si el 2º del Ranking se quedo sin PS la partida finalizo
                        // y gano el 1º, si no prosigue
                        if (newRanking.get(1).getPs() <= 0) {
                            LOG.info("Fin programa por partida acabada");
                            System.exit(0);
                        }

                        LOG.finest(String.format("Cliente %d(Jugador %d): pide sus puntos de salud y ataque",
                                cli.getIdCli(), idJugadorActual));
                        cli.callConsultaPS();

                        LOG.finest(String.format("Cliente %d(Jugador %d): elige enemigo",
                                cli.getIdCli(), idJugadorActual));
                        int idJugadorObjetivo;

                        if (idJugadorActual != newRanking.get(0).getId()) {
                            // Si no somos el primero, le atacamos
                            idJugadorObjetivo = newRanking.get(0).getId();
                        } else {
                            //Si somos el primero, atacamos al segundo
                            idJugadorObjetivo = newRanking.get(1).getId();
                        }

                        LOG.finest(String.format("Cliente %d(Jugador %d) ataca a jugador %d",
                                cli.getIdCli(), idJugadorActual, idJugadorObjetivo));
                        cli.callAtaque(idJugadorObjetivo);

                    } else {
                        LOG.warning("Cliente sin jugador asignado no puede hacer nada");
                    }

                }
            }

            Thread.sleep(300);

            LOG.warning("Turnos finalizados sin decidir ganador. Aumenta turnos");
            System.exit(0);

        } catch (RemoteException ex) {
            LOG.severe(String.format(
                    "Error comunicación con objeto remoto"));

        } catch (InterruptedException ex) {
            LOG.severe(String.format(
                    "MAIN: Error con margen tiempo servidor"));

        }

    }

}
