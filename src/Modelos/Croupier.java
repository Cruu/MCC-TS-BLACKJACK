package Modelos;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Croupier extends  Jugador {
    private boolean mostrarJuegoCompleto = false;
    public Croupier(){
        this.setNombre("Croupier");
        //Baraja.crearBaraja();
    }

    //Método que representa barajear las cartas realizando un reacomodo de las posiciones.
    public void barajear(){
        final int numeroCartas = Baraja.cartas.size();
        ArrayList<Carta> cartasBarajeadas = new ArrayList<Carta>();
        Random rn = new Random();
        for(int i=0 ; i<numeroCartas ; i++){
            int posicion = rn.nextInt(Baraja.cartas.size());
            cartasBarajeadas.add(Baraja.cartas.get(posicion));
            Baraja.cartas.remove(posicion);
        }
        Baraja.cartas = cartasBarajeadas;
    }
    //Método que reparte las primeras 2 cartas a todos los jugadores incluyendo a croupier.
    public void repartoInicial(ArrayList<Jugador> jugadores){
        for (Jugador jugador:jugadores) {
            jugador.limpiarMano();
            jugador.setEstatus(EstatusJugador.JUGANDO);
            jugador.mano = new ArrayList<Carta>();
        }
        for(int i=1 ; i <=2 ; i++){
            for(int j=0; j<jugadores.size() ; j++) {
                jugadores.get(j).mano.add(this.obtenerCartaARepartir());
            }
            this.limpiarMano();
            this.mano.add(this.obtenerCartaARepartir());
        }
    }
    //MÉTODO QUE SACA UNA CARTA DE LA BARAJA
    public Carta obtenerCartaARepartir(){
        Random rnd = new Random();
        Carta carta = new Carta();
        int cartaARepartir = rnd.nextInt(Baraja.cartas.size());
        carta = Baraja.cartas.get(cartaARepartir);
        Baraja.cartas.remove(cartaARepartir);
        return carta;
    }
    //MÉTODO QUE VÁLIDA SI UN JUGADOR PUEDE SEGUIR EN EL JUEGO O YA PERDIÓ
    public Boolean jugadorPuedeSeguir(Jugador jugador){
        boolean menorVeintiuno = false;
        menorVeintiuno = jugador.obtenerTotalPuntos() < 21 ? true : false;
        if(!menorVeintiuno) {
            if(jugador.obtenerTotalPuntos() == 21){
                jugador.setEstatus(EstatusJugador.PLANTADO);
            }else{
                jugador.setEstatus(EstatusJugador.PERDIDO);
                System.out.println("\033[34m");
                System.out.println(jugador.getNombre()+" Ha perdido con "+jugador.obtenerTotalPuntos()+" Puntos");
                System.out.println("\033[30m");
            }
        }
        return menorVeintiuno;
    }

    //Método que válida la mano de los jugadores vs el croupier.
    public String determinaGanador(Jugador jugador) {
        int sumaPuntosJugador = 0;
        int sumaPuntosCroupier=0;
        String msjARegresar = "";
        if(jugador.getEstatus() == EstatusJugador.PLANTADO && this.getEstatus() == EstatusJugador.JUGANDO){
            for (Carta carta:jugador.getMano())
            {
                sumaPuntosJugador += carta.getValor();
            }
            for(Carta carta: this.getMano())
            {
                sumaPuntosCroupier += carta.getValor();
            }
            if(sumaPuntosCroupier == sumaPuntosJugador)
            {
                msjARegresar = "Empate";
                jugador.setCantidadEnFichas((jugador.getApuesta())+jugador.getCantidadEnFichas());
            }
            else if(sumaPuntosCroupier > sumaPuntosJugador && sumaPuntosCroupier <= 21)
            {
                msjARegresar =  "\033[32mGana Croupier\033[30m";
                jugador.setCantidadEnFichas(jugador.getCantidadEnFichas()-jugador.getApuesta());
                if(jugador.getCantidadEnFichas() < 0){ jugador.setCantidadEnFichas(0); }
            }
            else
            {
                msjARegresar = "\033[31mPierde Croupier\033[30m";
                if(this.tieneBlackJack(jugador)){
                    jugador.setCantidadEnFichas((int)(jugador.getApuesta()*2.5)+jugador.getCantidadEnFichas());
                }else{
                    jugador.setCantidadEnFichas((jugador.getApuesta()*2)+jugador.getCantidadEnFichas());
                }
            }
        }
        else if(jugador.getEstatus() == EstatusJugador.PERDIDO)
        {
            msjARegresar =  "\033[32mGana Croupier\033[30m";
        }
        else
        {
            msjARegresar = "\033[31mPierde Croupier\033[30m";
        }
        return msjARegresar;
    }
    //Método que retorna un boolean haciendo referencia a: si tiene o no blackjack un jugador.
    public Boolean tieneBlackJack(Jugador jugador) {
        boolean blackJack = false;
        if(this.tieneVeintiUno(jugador) && jugador.getMano().size() == 2){
            blackJack = true;
        }else{
            blackJack = false;
        }
        return blackJack;
    }
    //MÉTODO QUE VALIDA SI EL TOTAL DE PUNTOS ES = A 21 Y CAMBIA ESTATUS A PLANTADO
    public Boolean tieneVeintiUno(Jugador jugador) {
        boolean veintiuno = false;
        if(jugador.obtenerTotalPuntos() == 21){
            jugador.setEstatus(EstatusJugador.PLANTADO);
            veintiuno = true;
        }else{
            veintiuno = false;
        }
        return veintiuno;
    }

    /*MÉTODO QUE PREGUNTA SI EL JUGADOR QUIERE OTRA CARTA
    SIEMPRE Y CUANDO NO SE PASE DE 21.
    EN EL CASO DE CROUPIER TOMA UN SI POR DEFAULT*/
    public Boolean quiereOtraCarta(Jugador jugador) {
        Scanner scn = new Scanner(System.in);
        String resp = "";
        boolean quiereMas = false;
        do{
            if(jugador instanceof Croupier){
                resp="s";
            }else{
                if(jugador.obtenerTotalPuntos()>=21){
                    break;
                }else{
                    System.out.println("Es el turno del jugador: "+jugador.getNombre().toUpperCase());
                    System.out.println("¿Quiere otra carta?, tienes "+jugador.obtenerTotalPuntos()+" puntos (Respuestas: Si o No)");
                    resp = scn.next();
                    System.out.println(" ");
                }
            }
            quiereMas = resp.toLowerCase().charAt(0) == 's' ? true : false;
        }while ((resp.toLowerCase()).charAt(0) != 's' &&  (resp.toLowerCase()).charAt(0) != 'n');
        return quiereMas;
    }

    public boolean isMostrarJuegoCompleto() {
        return mostrarJuegoCompleto;
    }

    public void setMostrarJuegoCompleto(boolean mostrarJuegoCompleto) {
        this.mostrarJuegoCompleto = mostrarJuegoCompleto;
    }
}
