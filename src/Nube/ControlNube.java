/*
 * Autor: Sara Blanco Muñoz
 * Práctica 5: Servicios en Red
 * Fecha: 19/01/2018
 */
package Nube;

import FTP.ModeloLogInFTP;
import Principal.VistaNubEmail;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPFile;

/**
 * Clase encargada del control del panel de gestión de ficheros junto con los
 * botones de Salir e Iniciar Sesión.
 */
public class ControlNube implements ActionListener, ListSelectionListener
{

    private VistaNube vista;
    private ModeloNube datos;
    private ModeloLogInFTP datosUsuario;

    public ControlNube(VistaNubEmail v, ModeloLogInFTP us)
    {
        vista = v.getPnlNube();
        datosUsuario = us;
        datos = new ModeloNube();

        llenarLista();

        vista.getListaArchivos().addListSelectionListener(this);

        for (JButton b : vista.getBotones())
        {
            b.addActionListener(this);
        }
    }

    // Eventos
    @Override
    public void actionPerformed(ActionEvent e)
    {
        JButton btn = (JButton) e.getSource();

        switch (btn.getText())
        {
            case "Subir":
            {
                subirFich();
            }
            break;

            case "Descargar":
            {
                if (datos.getFicActual().isEmpty())
                {
                    mostrarMensaje("Ningún fichero seleccionado.", "Error");
                } else
                {
                    descargarFich();
                }
            }
            break;

            case "Eliminar":
            {
                if (datos.getFicActual().isEmpty())
                {
                    mostrarMensaje("Ningún fichero seleccionado.", "Error");
                } else
                {
                    borrarFichero();
                }
            }
            break;

            case "Crear carpeta":
            {
                newDir();
            }
            break;

            case "Eliminar carpeta":
            {
                if (!datos.getDirActual().equals(datos.getDIR_INI()))
                {
                    eliminarDir();
                } else
                {
                    mostrarMensaje("No se puede eliminar el directorio raiz.", "Error");
                }
            }
            break;
        }
    }

    /**
     * Control de la selección de algún elemento de la lista de directorios y
     * ficheros del servidor FTP.
     */
    @Override
    public void valueChanged(ListSelectionEvent e)
    {
        if (e.getSource() == vista.getListaArchivos() && e.getValueIsAdjusting())
        {
            JList lista = vista.getListaArchivos();
            String seleccionado = lista.getSelectedValue().toString();

            if (!datos.getDirActual().equals(datos.getDIR_INI()) && lista.getSelectedIndex() == 0)
            {
                try
                {
                    datosUsuario.getClienteFTP().changeToParentDirectory();
                    datos.setDirActual(datosUsuario.getClienteFTP().printWorkingDirectory());
                    datos.setFicActual("");
                    llenarLista();
                } catch (IOException ex)
                {
                    mostrarMensaje("Error al cambiar al directorio padre.", "Error");
                }
            } else
            {
                if (seleccionado.contains("(DIR)"))
                {
                    seleccionado = seleccionado.substring(seleccionado.indexOf(" ") + 1);
                    datos.setDirActual(datos.getDirActual() + seleccionado + "/");
                    datos.setFicActual("");
                    try
                    {
                        datosUsuario.getClienteFTP().changeWorkingDirectory(datos.getDirActual());
                        llenarLista();
                    } catch (IOException ex)
                    {
                        mostrarMensaje("Error al cambiar de directorio.", "Error");
                    }
                } else
                {
                    datos.setFicActual(seleccionado);
                }
            }
        }
    }

    /*
     * Método que vacía la JList y la rellena con los archivos del directorio 
     * de trabajo actual
     */
    private void llenarLista()
    {
        FTPFile[] files = null;

        try
        {
            datosUsuario.getClienteFTP().changeWorkingDirectory(datos.getDirActual());
            files = datosUsuario.getClienteFTP().listFiles();
        } catch (IOException ex)
        {
            mostrarMensaje("Error al llenar lista de archivos.", "Error");
        }

        if (files != null)
        {
            vista.getListaArchivos().removeAll();
            DefaultListModel modeloLista = new DefaultListModel();

            try
            {
                String dirAct = datosUsuario.getClienteFTP().printWorkingDirectory();
                String padre = dirAct.substring(dirAct.lastIndexOf("/"));

                if (!datos.getDirActual().equals(datos.getDIR_INI()))
                {
                    modeloLista.addElement(padre);
                }
            } catch (IOException ex)
            {
                System.out.println("ERROR AL IMPRIMIR PADRE.");
            }

            for (FTPFile f : files)
            {
                if (!f.getName().equals("/") && !f.getName().equals(".") && !f.getName().equals(".."))
                {
                    if (f.isDirectory())
                    {
                        modeloLista.addElement("(DIR) " + f.getName());
                    } else
                    {
                        modeloLista.addElement(f.getName());
                    }
                }
            }
            try
            {
                vista.getListaArchivos().setModel(modeloLista);
            } catch (NullPointerException e)
            {
            }
        }
    }

    // Métodos asociados a los botones
    private void borrarFichero()
    {
        String fichero = datos.getFicActual();
        String ruta = datos.getDirActual() + File.separator + fichero;
        int i = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar el fichero?",
                "Seleccione una opción", JOptionPane.YES_NO_OPTION);

        if (i == JOptionPane.YES_OPTION)
        {
            try
            {
                if (datosUsuario.getClienteFTP().deleteFile(ruta))
                {
                    mostrarMensaje(fichero + " eliminado con éxito.", "Información");
                    llenarLista();
                }

            } catch (IOException ex)
            {
                mostrarMensaje("No se ha podido eliminar el archivo.", "Error");
            }
        }
    }

    private void subirFich()
    {
        JFileChooser selectorFic = new JFileChooser();
        selectorFic.setFileSelectionMode(JFileChooser.FILES_ONLY);
        selectorFic.setDialogTitle("Seleccione el fichero a cargar");

        int seleccion = selectorFic.showDialog(selectorFic, "Cargar");
        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                datosUsuario.getClienteFTP().setFileType(FTP.BINARY_FILE_TYPE);
                File fich = selectorFic.getSelectedFile();
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fich.getAbsolutePath()));

                if (datosUsuario.getClienteFTP().storeFile(fich.getName(), bis))
                {
                    mostrarMensaje(fich.getName() + " subido con éxito.", "Información");
                    llenarLista();
                } else
                {
                    throw new IOException();
                }
            } catch (IOException ex)
            {
                mostrarMensaje("Error al cargar el archivo.", "Error");
            }
        }
    }

    private void descargarFich()
    {
        String fichero = datos.getFicActual();
        JFileChooser selectorDir = new JFileChooser();
        selectorDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        selectorDir.setDialogTitle("Guardar en");

        int seleccion = selectorDir.showDialog(selectorDir, "Descargar");

        if (seleccion == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                datosUsuario.getClienteFTP().setFileType(FTP.BINARY_FILE_TYPE);
                File dir = selectorDir.getSelectedFile();
                String rutaLocal = dir.getAbsolutePath() + File.separator + fichero;
                String rutaServ = datos.getDirActual() + File.separator + fichero;
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(rutaLocal));

                if (datosUsuario.getClienteFTP().retrieveFile(rutaServ, bos))
                {
                    mostrarMensaje(fichero + " descargado con éxito.", "Información");
                    llenarLista();
                } else
                {
                    throw new IOException();
                }
            } catch (IOException ex)
            {
                mostrarMensaje("Error al descargar el archivo.", "Error");
            }
        }
    }

    private void newDir()
    {
        String ruta, nombreDir = JOptionPane.showInputDialog(null,
                "Introduzca el nombre del nuevo directorio", null);

        if (nombreDir != null)
        {
            ruta = datos.getDirActual() + File.separator + nombreDir;

            try
            {
                if (datosUsuario.getClienteFTP().makeDirectory(ruta))
                {
                    llenarLista();
                }
            } catch (IOException ex)
            {
                mostrarMensaje("No se ha podido crear el directorio.", "Error");
            }
        }
    }

    private void eliminarDir()
    {
        String dir = datos.getDirActual();
        int i = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea "
                + "eliminar (DIR) " + dir + " y todo su contenido?",
                "Seleccione una opción", JOptionPane.YES_NO_OPTION);

        if (i == JOptionPane.YES_OPTION)
        {
            try
            {
                FTPFile[] contenido = datosUsuario.getClienteFTP().listFiles();

                for (FTPFile f : contenido)
                {
                    datosUsuario.getClienteFTP().deleteFile(dir + File.separator + f.getName());
                }

                datosUsuario.getClienteFTP().changeToParentDirectory();
                String aux = dir.substring(0, dir.length() - 1);
                int ultimaBarra = aux.lastIndexOf("/");

                if (ultimaBarra != 0)
                {
                    datos.setDirActual(aux.substring(0, ultimaBarra));
                } else
                {
                    datos.setDirActual(datos.getDIR_INI());
                }

                if (datosUsuario.getClienteFTP().removeDirectory(dir))
                {
                    mostrarMensaje("(DIR) " + dir + " se ha eliminado con éxito.", "Información");
                }

                llenarLista();
            } catch (IOException ex)
            {
                mostrarMensaje("Error al eliminar fichero.", "Error");
            }
        }
    }

    private void mostrarMensaje(String s, String tipo)
    {
        if (tipo.equals("Error"))
        {
            JOptionPane.showMessageDialog(null, s, tipo, JOptionPane.ERROR_MESSAGE);
        } else
        {
            JOptionPane.showMessageDialog(null, s, tipo, JOptionPane.INFORMATION_MESSAGE);
        }
    }

}
