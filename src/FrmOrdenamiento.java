import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.WindowConstants;

import entidades.Documento;
import servicios.BusquedaRenderer;
import servicios.ServicioDocumento;
import servicios.Util;

import java.awt.BorderLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FrmOrdenamiento extends JFrame {
    private JButton btnAnterior;
    private JButton btnSiguiente;
    private JButton btnOrdenarBurbuja;
    private JButton btnOrdenarRapido;
    private JButton btnOrdenarInsercion;
    private JToolBar tbOrdenamiento;
    private JComboBox cmbCriterio;
    private JTextField txtTiempo;
    private JButton btnBuscar;
    private JTextField txtBuscar;

    private JTable tblDocumentos;

    public FrmOrdenamiento() {

        tbOrdenamiento = new JToolBar();
        btnOrdenarBurbuja = new JButton();
        btnOrdenarInsercion = new JButton();
        btnOrdenarRapido = new JButton();
        cmbCriterio = new JComboBox();
        txtTiempo = new JTextField();

        btnBuscar = new JButton();
        txtBuscar = new JTextField();

        tblDocumentos = new JTable();

        setSize(600, 400);
        setTitle("Ordenamiento Documentos");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        btnOrdenarBurbuja.setIcon(new ImageIcon(getClass().getResource("/iconos/Ordenar.png")));
        btnOrdenarBurbuja.setToolTipText("Ordenar Burbuja");
        btnOrdenarBurbuja.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarBurbujaClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarBurbuja);

        btnOrdenarRapido.setIcon(new ImageIcon(getClass().getResource("/iconos/OrdenarRapido.png")));
        btnOrdenarRapido.setToolTipText("Ordenar Rapido");
        btnOrdenarRapido.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarRapidoClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarRapido);

        btnOrdenarInsercion.setIcon(new ImageIcon(getClass().getResource("/iconos/OrdenarInsercion.png")));
        btnOrdenarInsercion.setToolTipText("Ordenar Inserción");
        btnOrdenarInsercion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnOrdenarInsercionClick(evt);
            }
        });
        tbOrdenamiento.add(btnOrdenarInsercion);

        cmbCriterio.setModel(new DefaultComboBoxModel(
                new String[] { "Nombre Completo, Tipo de Documento", "Tipo de Documento, Nombre Completo" }));
        tbOrdenamiento.add(cmbCriterio);
        tbOrdenamiento.add(txtTiempo);

        btnBuscar.setIcon(new ImageIcon(getClass().getResource("/iconos/Buscar.png")));
        btnBuscar.setToolTipText("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                btnBuscar(evt);
            }
        });

        tbOrdenamiento.add(btnBuscar);
        tbOrdenamiento.add(txtBuscar);

        btnAnterior = new JButton("Anterior");
        btnAnterior.addActionListener(e -> mostrarAnterior());
        tbOrdenamiento.add(btnAnterior);

        btnSiguiente = new JButton("Siguiente");
        btnSiguiente.addActionListener(e -> mostrarSiguiente());
        tbOrdenamiento.add(btnSiguiente);

        JScrollPane spDocumentos = new JScrollPane(tblDocumentos);

        getContentPane().add(tbOrdenamiento, BorderLayout.NORTH);
        getContentPane().add(spDocumentos, BorderLayout.CENTER);

        String nombreArchivo = System.getProperty("user.dir")
                + "/src/datos/Datos.csv";

        ServicioDocumento.cargar(nombreArchivo);
        ServicioDocumento.mostrar(tblDocumentos);
    }

    private void btnOrdenarBurbujaClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            Util.iniciarCronometro();
            ServicioDocumento.ordenarBurbuja(cmbCriterio.getSelectedIndex());
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            ServicioDocumento.mostrar(tblDocumentos);
        }
    }

    private void btnOrdenarRapidoClick(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() >= 0) {
            Util.iniciarCronometro();
            ServicioDocumento.ordenarRapido(cmbCriterio.getSelectedIndex());
            txtTiempo.setText(Util.getTextoTiempoCronometro());
            ServicioDocumento.mostrar(tblDocumentos);
        }
    }

    private void btnOrdenarInsercionClick(ActionEvent evt) {

    }

    private void btnBuscar(ActionEvent evt) {
        if (cmbCriterio.getSelectedIndex() == 0) { 
            String texto = txtBuscar.getText().trim();
            if (texto.isEmpty()) {
                Util.mostrarMensaje("Ingrese parte del nombre o apellido para buscar.");
                return;
            }

            BusquedaRenderer renderer = new BusquedaRenderer();
        renderer.setTextoBuscar(texto);
        for (int i = 1; i <= 3; i++) { 
            tblDocumentos.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        tblDocumentos.repaint();

            Documento resultado = ServicioDocumento.buscarBinariaRecursiva(texto);
            txtTiempo.setText(Util.getTextoTiempoCronometro());

            if (resultado != null) {
                Util.mostrarMensaje("Documento encontrado:\n" +
                        resultado.getNombreCompleto() + " - " + resultado.getDocumento());
            } else {
                Util.mostrarMensaje("No se encontró coincidencia.");
            }
        } else {
            Util.mostrarMensaje("Debe estar ordenado por Nombre Completo para usar búsqueda binaria.");
        }
    }

    private void mostrarAnterior() {
        Documento anterior = ServicioDocumento.getAnterior();
        if (anterior != null) {
            Util.mostrarMensaje("Anterior:\n" + anterior.getNombreCompleto() + " - " + anterior.getDocumento());
        } else {
            Util.mostrarMensaje("No hay documento anterior.");
        }
    }

    private void mostrarSiguiente() {
        Documento siguiente = ServicioDocumento.getSiguiente();
        if (siguiente != null) {
            Util.mostrarMensaje("Siguiente:\n" + siguiente.getNombreCompleto() + " - " + siguiente.getDocumento());
        } else {
            Util.mostrarMensaje("No hay documento siguiente.");
        }
    }
}