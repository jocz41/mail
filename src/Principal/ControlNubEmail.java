/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Principal;

import Mail.LogIn.ControlLogInMail;
import FTP.ModeloLogInFTP;
import Mail.LogIn.ModeloLogInMail;
import Nube.ControlNube;
import Server.ControlPuerto;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 * Clase encargada del manejo de la ventana que hace de interfaz de usuario
 * tanto para el servicio de ficheros como el de emails.
 */
public class ControlNubEmail implements ActionListener
{

    private VistaNubEmail ventana;
    private ModeloLogInFTP datosFTP;
    private ModeloLogInMail datosMail;

    public ControlNubEmail(ModeloLogInFTP m)
    {
        ventana = new VistaNubEmail();
        datosFTP = m;

        new ControlNube(ventana, datosFTP);

        JButton b = buscaBoton(ventana.getPnlNube().getBotones(), "Salir");

        if (b.getActionListeners().length == 0)
        {
            b.addActionListener(this);
        }

        b = buscaBoton(ventana.getPnlMail().getBotones(), "Salir");
        if (b.getActionListeners().length == 0)
        {
            b.addActionListener(this);
        }

        b = buscaBoton(ventana.getPnlMail().getBotones(), "Iniciar Sesión");
        if (b.getActionListeners().length == 0)
        {
            b.addActionListener(this);
        }

        ventana.pack();
    }

    // Eventos
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton btn = (JButton) e.getSource();

        switch (btn.getText())
        {
            /**
             * Al hacer click en dicho botón nos aparecerá la pantalla de inicio
             * de sesión al email.
             */
            case "Iniciar Sesión":
            {
                new ControlLogInMail(ventana, datosMail);
            }
            break;

            /**
             * Al hacer click en Salir seremos desconectados de todos los
             * servicios abiertos y volverá a la pantalla de configuración del
             * servidor ftp.
             */
            case "Salir":
            {
                ventana.dispose();
                new ControlPuerto();

                try
                {
                    // Desconexión de FTP
                    datosFTP.getClienteFTP().disconnect();

                    // Desconexión de POP3
                    if (datosMail != null && datosMail.getClientePOP().isConnected())
                    {
                        synchronized (datosMail.getClientePOP())
                        {
                            datosMail.getClientePOP().logout();
                            datosMail.getClientePOP().disconnect();
                        }
                    }

                } catch (IOException ex)
                {
                    JOptionPane.showMessageDialog(ventana, "Error al "
                            + "desconectar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            break;
        }

    }

    // Método que busca un botón según el texto que le identifique.
    private JButton buscaBoton(ArrayList<JButton> b, String s)
    {
        int c = 0;

        while (c < b.size() && !b.get(c).getText().equals(s))
        {
            c++;
        }

        return b.get(c);
    }

}
