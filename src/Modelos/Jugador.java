package Modelos;

import java.util.ArrayList;

public class Jugador {
    private String nombre="";
    private EstatusJugador estatus = EstatusJugador.JUGANDO;
    private int cantidadEnFichas = 100;
    private int apuesta = 0;

    public ArrayList<Carta> mano  = new ArrayList<Carta>();

    public Jugador(){ }
    public Jugador(String nombre){
        this.setNombre(nombre);
    }

    protected EstatusJugador getEstatus() {
        return estatus;
    }

    protected void setEstatus(EstatusJugador estatus) { this.estatus = estatus; }

    public String getNombre() {
        return nombre.toUpperCase();
    }

    protected void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Carta> getMano() {
        return mano;
    }

    public int getCantidadEnFichas() {
        return cantidadEnFichas;
    }

    public void setCantidadEnFichas(int cantidadEnFichas) {
        this.cantidadEnFichas = cantidadEnFichas;
    }

    public int getApuesta() {
        return apuesta;
    }

    public void setApuesta(int apuesta) {
        this.apuesta = apuesta;
    }

    //Método que obtiene el total de puntos, y evalúa la forma que le convenga el AS.
    public int obtenerTotalPuntos(){
        int sumaTotal = 0;
        int posicionAsEnMano = 0;
        for(int i=0 ; i<this.mano.size() ; i++){
            if(this.mano.get(i).getValor() == 1 || this.mano.get(i).getValor() == 11)
            {
                posicionAsEnMano = (i+1);
            }
            sumaTotal += this.mano.get(i).getValor();
        }
        //Ajustar los indices para saber que si es >=0 es porque encontró un as
        if(posicionAsEnMano > 0)
        {
            Carta cartaAS = this.mano.get(posicionAsEnMano-1);
            if(cartaAS.getValor() == 11)
            {
                if(sumaTotal > 21)
                {
                    cartaAS.setValor(1);
                    sumaTotal -= 10;
                }
            }
            else if(cartaAS.getValor() == 1)
            {
                if (((sumaTotal+10) <= 21))
                {
                    this.mano.get(posicionAsEnMano-1).setValor(11);
                    sumaTotal += 10;
                }
            }
        }
        return  sumaTotal;
    }
    public void limpiarMano(){
        this.mano = new ArrayList<Carta>();
    }
}
