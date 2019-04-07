/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Nube;

/**
 * Modelo de los datos necesarios para la gestión de ficheros y directorios del
 * servidor FTP.
 */
public class ModeloNube
{

    private final String DIR_INI = "/";

    private String dirActual, ficActual;

    public ModeloNube()
    {
        dirActual = DIR_INI;
        ficActual = "";
    }

    // Getters    
    public String getDirActual()
    {
        return dirActual;
    }

    public void setDirActual(String dirActual)
    {
        this.dirActual = dirActual;
    }

    public String getFicActual()
    {
        return ficActual;
    }

    public void setFicActual(String ficActual)
    {
        this.ficActual = ficActual;
    }

    public String getDIR_INI()
    {
        return DIR_INI;
    }
}
