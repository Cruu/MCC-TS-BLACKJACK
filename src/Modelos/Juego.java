package Modelos;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Juego {

    private Integer numeroJugadores;
    private ArrayList<Jugador> jugadores;
    private Croupier croupier;
    private Scanner scanner = new Scanner(System.in);


    public Juego(){
        numeroJugadores = 0;
        jugadores = new ArrayList<Jugador>();
        croupier = new Croupier();
        this.jugar();
    }

    /*Lleva el flujo del juego.*/
    private void jugar() {
        System.out.print("\033[34m");
        System.out.print("\033[34m");
        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*- BlackJack  *-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.out.println("-*-*-*-*-*-*-*-*-*-*-*-*-*-*- INICIA JUEGO  -*-*-*-*-*-*-*-*-*-*-*-*-*-*-");
        System.out.println("\033[30m");
        boolean pararJuego = false;
        this.pedirDatosIniciales();
        do {
            this.pedirApuestaInicial();
            if(jugadores.size()<=0){pararJuego = true; break;}
            Baraja.crearBaraja();
            croupier.barajear();
            croupier.repartoInicial(jugadores);

            for (Jugador jugador : jugadores) {
                System.out.println(this.imprimeMano(jugador));
            }
            System.out.println(this.imprimeMano(croupier));

            //Aquí el croupier válida la mano de cada jugador para tomar decisiones.
            for (int i = 0; i < jugadores.size(); i++) {
                Jugador jugadorEnTurno = jugadores.get(i);
                System.out.print("\033[34m");
                System.out.println("-------------------------------CROUPIER DICE-----------------------------");
                System.out.println("\033[30m");
                if (jugadorEnTurno.getEstatus() == EstatusJugador.JUGANDO) {
                    if (croupier.tieneBlackJack(jugadorEnTurno)) {
                        System.out.println("\033[34m");
                        System.out.println(jugadorEnTurno.getNombre() + " Tiene Black-Jack!!");
                        System.out.println("\033[30m");
                    } else {
                        if (croupier.tieneVeintiUno(jugadorEnTurno)) {
                            System.out.println("\033[34m");
                            System.out.println(jugadorEnTurno.getNombre() + " Tiene 21!!");
                            System.out.println("\033[30m");
                        } else {
                            boolean respuesta = false;
                            do {
                                if (croupier.quiereOtraCarta(jugadorEnTurno)) {
                                    Carta cartaARepartir = croupier.obtenerCartaARepartir();
                                    jugadorEnTurno.mano.add(cartaARepartir);
                                    System.out.print("\033[34m");
                                    System.out.println("---------------------------------ATENCION--------------------------------");
                                    System.out.println("\033[30m");
                                    System.out.println(this.imprimeMano(jugadorEnTurno));
                                    if (croupier.tieneVeintiUno(jugadorEnTurno)) {
                                        System.out.println(jugadorEnTurno.getNombre() + " Tiene 21 !!");
                                    }
                                } else {
                                    jugadorEnTurno.setEstatus(EstatusJugador.PLANTADO);
                                    System.out.println("*-*-*-*-*-*-*-*- " + jugadorEnTurno.getNombre() + " se plantó *-*-*-*-*-*-*-*-");
                                    System.out.println();
                                    System.out.println(this.imprimeMano(jugadorEnTurno));
                                }
                            } while (croupier.jugadorPuedeSeguir(jugadorEnTurno)
                                    && jugadorEnTurno.getEstatus() == EstatusJugador.JUGANDO);
                        }
                    }
                }
            }
            //Parte donde juega el croupier
            if (croupier.tieneBlackJack(croupier)) {
                System.out.println("\033[34m");
                System.out.println(croupier.getNombre() + " Tiene Black-Jack!");
                System.out.println("\033[30m");
            } else {
                if (croupier.tieneVeintiUno(croupier)) {
                    System.out.println("\033[34m");
                    System.out.println(croupier.getNombre() + " Tiene 21!");
                    System.out.println("\033[30m");
                } else {
                    while (croupier.obtenerTotalPuntos() < 17) {
                        boolean hayJugadoresJugando = false;
                        for (Jugador jugador : this.jugadores) {
                            if (jugador.getEstatus() == EstatusJugador.PLANTADO) {
                                hayJugadoresJugando = true;
                            }
                        }
                        if (hayJugadoresJugando) {
                            croupier.quiereOtraCarta(croupier);
                            Carta cartaRecibo = croupier.obtenerCartaARepartir();
                            croupier.mano.add(cartaRecibo);
                        } else {
                            break;
                        }
                    }
                    croupier.setMostrarJuegoCompleto(true);
                    System.out.println(this.imprimeMano(croupier));
                    System.out.println("Total de puntos: " + croupier.obtenerTotalPuntos());
                    System.out.println();
                    System.out.println("\033[34m");
                    System.out.println("---------------------------TABLA DE RESULTADOS---------------------------");
                    System.out.println("\033[30m");

                    for (Jugador jugador : jugadores) {
                        System.out.println(croupier.determinaGanador(jugador) + " vs " + jugador.getNombre());
                    }
                }
            }
        }while(!pararJuego);
    }

    //Método para solicitar cantidad de jugadores y sus datos respectivamente.
    private void pedirDatosIniciales(){
        this.numeroJugadores = 0;
        do{
            scanner = new Scanner(System.in);
            System.out.print("¿Cuántos jugadores participan (1-6)? ");
            try{
                this.numeroJugadores = scanner.nextInt();
            }
            catch(InputMismatchException ex) {
                System.out.println("***Atención: Utilice números para específicar su respuesta***");
                this.numeroJugadores = 0;
            }
        }while (this.numeroJugadores < 1 || this.numeroJugadores > 6);

        for(int i=0 ; i<this.numeroJugadores ; i++){
            System.out.println("Nombre del jugador "+(i+1));
            this.jugadores.add(new Jugador(scanner.next()));
        }
    }

    private void pedirApuestaInicial(){
        scanner = new Scanner(System.in);
        int apuesta = 0;
        quitarJugadoresSinFichas();
        for (Jugador jugador:jugadores) {
            do{
                if(jugador.getCantidadEnFichas() == 0){
                    continue;
                }else{
                    System.out.println(" ");
                    System.out.println("¿ Cuánto desea apostar "+jugador.getNombre()+" usted tiene $"+jugador.getCantidadEnFichas()+" (mínimo $10) ?");
                    apuesta = scanner.nextInt();
                }
                jugador.setApuesta(apuesta);
                if(apuesta > jugador.getCantidadEnFichas()){
                    System.out.println("\033[34m");
                    System.out.println("Sólo tienes "+jugador.getCantidadEnFichas());
                    System.out.println("\033[30m");
                }
            }while(apuesta < 10 || apuesta > jugador.getCantidadEnFichas() && (jugadores.size()>0));
            jugador.setCantidadEnFichas(jugador.getCantidadEnFichas()-apuesta);
        }
    }

    private void quitarJugadoresSinFichas() {
        int idx = 0;
            for (int i=0; i<jugadores.size();i++) {
                if(jugadores.get(i).getCantidadEnFichas() == 0){
                    try{
                        jugadores.remove(i);
                    }catch(Exception ex){
                        System.out.println("Ningún Jugador tiene fichas");
                    }
                }
            }
        if(jugadores.size()<0){
            System.out.println("Ningún Jugador tiene fichas");
        }
    }

    //Método que imprime el detalle (las cartas y puntos) de la mano de un jugador.
    private String imprimeMano(Jugador jugador){

        String msjARegresar = "-*-*-*-*-*-*-*-* Mano de "+jugador.getNombre()+" ";

        boolean esCroupier = jugador instanceof Croupier ? true : false;

        if(!esCroupier || ((Croupier) jugador).isMostrarJuegoCompleto())
        {
            msjARegresar += jugador.obtenerTotalPuntos()+"(pts) -*-*-*-*-*-*-*-*\n";
        }
        else if(!((Croupier) jugador).isMostrarJuegoCompleto())
        {
            msjARegresar += "  -*-*-*-*-*-*-*-* \n";
        }
        for(int i=0 ; i<jugador.getMano().size() ; i++){
            if(esCroupier && jugador.getMano().size() == 2 && !((Croupier) jugador).isMostrarJuegoCompleto())
            {
                if(i == 0)
                {
                    msjARegresar += "\t"+jugador.getMano().get(i).getNombreCarta()+"\n";
                    break;
                }
            }
            else
            {
                msjARegresar += "\t" + jugador.getMano().get(i).getNombreCarta() + "\n";
            }
        }
        return  msjARegresar;
    }

    public int getNumeroJugadores() {
        return numeroJugadores;
    }
    public void setNumeroJugadores(Integer numeroJugadores) {
        this.numeroJugadores = numeroJugadores;
    }

    public ArrayList<Jugador> getJugadores() {
        return jugadores;
    }
    public void setJugadores(ArrayList<Jugador> jugadores) {
        this.jugadores = jugadores;
    }

    public Croupier getCroupier() {
        return croupier;
    }
    public void setCroupier(Croupier croupier) {
        this.croupier = croupier;
    }
}
