/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Server;

import FTP.ControlLogInFTP;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import peval5.PlantillaInput;

// Controla que el campo del servidor no esté vacío y que el puerto sea numérico.
public class ControlPuerto implements ActionListener
{

    private PlantillaInput ventana;

    public ControlPuerto()
    {
        ventana = new PlantillaInput("Conexión a FTP", "Servidor", "Puerto");

        ventana.getBtnAceptar().addActionListener(this);
    }

    /**
     * Si todos los datos cumplen el formato al hacer click en el botón Aceptar
     * se cerrará la ventana y nos aparecerá la ventana para iniciar sesión en
     * el servidor.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == ventana.getBtnAceptar())
        {
            String serv;
            int puer = -1;

            serv = ventana.getTxtUno().getText();
            try
            {
                if (!ventana.getTxtDos().getText().isEmpty())
                {
                    puer = Integer.parseInt(ventana.getTxtDos().getText());
                }

                if (serv.isEmpty())
                {
                    JOptionPane.showMessageDialog(ventana, "Indique un servidor.", "Error", JOptionPane.ERROR_MESSAGE);
                } else
                {
                    this.ventana.dispose();
                    new ControlLogInFTP(new ModeloPuerto(serv, puer));
                }
            } catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(ventana, "El puerto es un campo numérico.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
