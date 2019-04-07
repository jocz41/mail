/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Mail.Redactar;

/**
 * Esta clase recoge los datos necesarios para la generación de la vista de un
 * correo.
 */
public class ModeloRedactar
{

    private String dest, rem, asunto, contenido;

    public ModeloRedactar(String dest, String rem, String asunto, String contenido)
    {
        this.dest = dest;
        this.rem = rem;
        this.asunto = asunto;
        this.contenido = contenido;
    }

    public String getDest()
    {
        return dest;
    }

    public String getRem()
    {
        return rem;
    }

    public String getAsunto()
    {
        return asunto;
    }

    public String getContenido()
    {
        return contenido;
    }

}
