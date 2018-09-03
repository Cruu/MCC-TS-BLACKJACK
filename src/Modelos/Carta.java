package Modelos;

public class Carta {
    private int numero;
    private Figura figura;
    private int valor;

    public int getNumero() {
        return numero;
    }

    public Figura getFigura() {
        return figura;
    }

    public String getNombreCarta() {
        String letra;
        switch (numero)
        {
            case 1:letra="A";
                    valor=1;
                break;
            case 11:letra="J";
                    valor=10;
                break;
            case 12: letra="Q";
                    valor=10;
                break;
            case 13:letra="K";
                    valor=10;
                break;
                default:letra=Integer.toString(numero);
                break;
        }
        return letra+" de "+figura;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setFigura(Figura figura) {
        this.figura = figura;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getValor() {
        return valor;
    }
}
