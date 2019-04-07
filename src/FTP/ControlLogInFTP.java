/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package FTP;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.JOptionPane;
import Principal.ControlNubEmail;
import Server.ModeloPuerto;
import peval5.PlantillaInput;

public class ControlLogInFTP implements ActionListener
{

    private PlantillaInput ventana;
    private ModeloPuerto datosFTP;

    public ControlLogInFTP(ModeloPuerto m)
    {
        datosFTP = m;
        ventana = new PlantillaInput("LogIn FTP", "Usuario", "Contraseña");

        ventana.getBtnAceptar().addActionListener(this);
    }

    /**
     * Si todos los datos son correctos al hacer click en el botón Aceptar se 
     * cerrará la ventana y nos aparecerá la ventana que hace de interfaz para 
     * la gestión de ficheros y de emails.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == ventana.getBtnAceptar())
        {
            ModeloLogInFTP usuario = new ModeloLogInFTP(ventana.getTxtUno().getText(), ventana.getTxtDos().getText());
            usuario.conectar(datosFTP.getServer(), datosFTP.getPuerto());

            try
            {
                if (!usuario.logIn())
                {
                    JOptionPane.showMessageDialog(ventana, "Usuario y/o contraseña incorrectos.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else
                {
                    ventana.dispose();
                    new ControlNubEmail(usuario);
                }
            } catch (IOException ex)
            {
                JOptionPane.showMessageDialog(ventana, "Error de E/S.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
