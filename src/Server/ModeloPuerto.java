/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Server;

// Clase que almacena el servidor y el puerto donde se halla el ftp
public class ModeloPuerto
{

    private String server;
    private int puerto;

    public ModeloPuerto(String s, int p)
    {
        server = s;
        puerto = p;
    }

    // Getters
    public String getServer()
    {
        return server;
    }

    public int getPuerto()
    {
        return puerto;
    }

}
