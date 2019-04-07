/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Mail.Refresh;

import Mail.ControlMail;
import java.io.IOException;
import org.apache.commons.net.pop3.POP3SClient;

// Hilo que refresca la lista de mensajes del mail.
public class ThreadRefresh implements Runnable
{

    private Thread t;
    private ControlMail controlCliente;

    public ThreadRefresh(ControlMail c)
    {
        t = new Thread(this);
        controlCliente = c;

        t.start();
    }

    @Override
    public void run()
    {
        POP3SClient cliente = controlCliente.getDatos().getClientePOP();

        // Refrescará la lista siempre y cuando el usuario esté conectado.
        while (cliente.isConnected())
        {
            try
            {
                t.sleep(10000);

                synchronized (cliente)
                {
                    if (cliente.isConnected())
                    {
                        cliente.logout();
                        cliente.disconnect();
                        cliente.connect(controlCliente.getDatos().getPopServ(), controlCliente.getDatos().getPopPort());
                        cliente.login(controlCliente.getDatos().getUser(), controlCliente.getDatos().getPass());
                        controlCliente.listaMensajes();
                    }
                }

            } catch (InterruptedException ex)
            {
                System.out.println("Error al dormir el hilo.");
            } catch (IOException ex)
            {
                System.out.println("Error de I/O en el hilo.");
            }
        }
    }

}
