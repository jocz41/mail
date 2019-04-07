/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Mail.LogIn;

import Mail.ControlMail;
import Mail.Refresh.ThreadRefresh;
import Principal.VistaNubEmail;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import peval5.PlantillaInput;

public class ControlLogInMail implements ActionListener
{

    private VistaNubEmail v;
    private PlantillaInput ventana;
    //private ModeloLogInMail datosMail;

    public ControlLogInMail(VistaNubEmail v, ModeloLogInMail m)
    {
        this.v = v;
        //datosMail = m;
        ventana = new PlantillaInput("Conexión a Mail", "Usuario", "Contraseña");

        ventana.getBtnAceptar().addActionListener(this);
    }

    /**
     * Si todos los datos son correctos al hacer click en el botón Aceptar se 
     * cerrará la ventana, se cargarán los mensajes no leídos en el listado y 
     * se habilitarán los botones para el manejo de emails.
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == ventana.getBtnAceptar())
        {
            ModeloLogInMail datosMail = new ModeloLogInMail(ventana.getTxtUno().getText(), ventana.getTxtDos().getText());
            if (datosMail.conectar())
            {
                ventana.dispose();
                ControlMail c = new ControlMail(v, datosMail);
                new ThreadRefresh(c);
            }
        }

    }
}
