/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package FTP;

import java.io.IOException;
import java.io.PrintWriter;
import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;

/**
 * En esta clase almacenamos el usuario, la contraseña y el cliente FTP, junto
 * con los métodos necesarios de conexión y login.
 */
public class ModeloLogInFTP
{

    private FTPClient clienteFTP;
    private String user, pass;

    public ModeloLogInFTP(String u, String c)
    {
        user = u;
        pass = c;

        clienteFTP = new FTPClient();

        clienteFTP.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));
    }

    public void conectar(String serv, int puerto)
    {
        try
        {
            if (puerto < 0)
            {
                clienteFTP.connect(serv);
            } else
            {
                clienteFTP.connect(serv + ":" + puerto);
            }
        } catch (IOException ex)
        {
            System.out.println("ERROR al conectar con el servidor FTP.");
        }
    }

    public boolean logIn() throws IOException
    {
        return clienteFTP.login(user, pass);
    }

    // Getters
    public FTPClient getClienteFTP()
    {
        return clienteFTP;
    }

    public String getUser()
    {
        return user;
    }

    public String getPass()
    {
        return pass;
    }

}
