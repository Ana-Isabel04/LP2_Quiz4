package servicios;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import entidades.Documento;

public class ServicioDocumento {

    private static List<Documento> documentos = new ArrayList<>();
   
    public static List<Documento> getDocumentos() {
        return documentos;
    }

    public static void cargar(String nombreArchivo) {
        documentos.clear();
        BufferedReader br = Archivo.abrirArchivo(nombreArchivo);
        if (br != null) {
            try {
                String linea = br.readLine(); // linea de encabezados
                linea = br.readLine();
                while (linea != null) {
                    String[] textos = linea.split(";");
                    Documento documento = new Documento(textos[0], textos[1], textos[2], textos[3]);
                    documentos.add(documento);
                    linea = br.readLine();
                }
            } catch (Exception ex) {

            }
        }
    }

    public static void mostrar(JTable tbl) {
        String[] encabezados = new String[] { "#", "Apellido 1", "Apellido2", "Nombre", "Documento" };
        String[][] datos = new String[documentos.size()][encabezados.length];
        int fila = 0;
        for (var documento : documentos) {
            datos[fila][0] = String.valueOf(fila + 1);
            datos[fila][1] = documento.getApellido1();
            datos[fila][2] = documento.getApellido2();
            datos[fila][3] = documento.getNombre();
            datos[fila][4] = documento.getDocumento();
            fila++;
        }

        DefaultTableModel dtm = new DefaultTableModel(datos, encabezados);
        tbl.setModel(dtm);
        ;
    }

    private static boolean esMayor(Documento d1, Documento d2, int criterio) {
        if (criterio == 0) {
            return d1.getNombreCompleto().compareTo(d2.getNombreCompleto()) > 0 ||
                    (d1.getNombreCompleto().equals(d2.getNombreCompleto()) &&
                            d1.getDocumento().compareTo(d2.getDocumento()) > 0);
        } else {
            return d1.getDocumento().compareTo(d2.getDocumento()) > 0 ||
                    (d1.getDocumento().equals(d2.getDocumento()) &&
                            d1.getNombreCompleto().compareTo(d2.getNombreCompleto()) > 0);
        }
    }

    private static void intercambiar(int origen, int destino) {
        var temporal = documentos.get(origen);
        documentos.set(origen, documentos.get(destino));
        documentos.set(destino, temporal);
    }

    public static void ordenarBurbuja(int criterio) {
        for (int i = 0; i < documentos.size() - 1; i++) {
            for (int j = i + 1; j < documentos.size(); j++) {
                if (esMayor(documentos.get(i), documentos.get(j), criterio)) {
                    intercambiar(i, j);
                }
            }
        }
    }

    private static int getPivote(int inicio, int fin, int criterio) {
        int pivote = inicio;
        Documento documentoPivote = documentos.get(pivote);
        for (int i = inicio + 1; i <= fin; i++) {
            if (esMayor(documentoPivote, documentos.get(i), criterio)) {
                pivote++;
                if (i != pivote)
                    intercambiar(i, pivote);
            }
        }
        if (inicio != pivote) {
            intercambiar(inicio, pivote);
        }
        return pivote;
    }

    private static void ordernarRapido(int inicio, int fin, int criterio) {
        if (fin > inicio) {
            int pivote = getPivote(inicio, fin, criterio);
            ordernarRapido(inicio, pivote - 1, criterio);
            ordernarRapido(pivote + 1, fin, criterio);
        }
    }

    public static void ordenarRapido(int criterio) {
        ordernarRapido(0, documentos.size() - 1, criterio);
    }
    
    public static Documento buscarBinariaRecursiva(String textoBuscar) {
    Util.iniciarCronometro();
    Documento resultado = buscarBinariaRecursiva(textoBuscar.toLowerCase(), 0, documentos.size() - 1);
    System.out.println("Tiempo de búsqueda: " + Util.getTextoTiempoCronometro());
    return resultado;
}

private static Documento buscarBinariaRecursiva(String textoBuscar, int izquierda, int derecha) {
    if (izquierda > derecha) {
        return null;
    }

    int medio = (izquierda + derecha) / 2;
    Documento actual = documentos.get(medio);
    String nombreCompleto = actual.getNombreCompleto().toLowerCase();

    if (nombreCompleto.contains(textoBuscar)) {
        return actual;
    }

    if (nombreCompleto.compareTo(textoBuscar) > 0) {
        return buscarBinariaRecursiva(textoBuscar, izquierda, medio - 1);
    } else {
        return buscarBinariaRecursiva(textoBuscar, medio + 1, derecha);
    }
}
private static int indiceEncontrado = -1;

public static Documento buscarBinariaRecursivaConIndice(String textoBuscar) {
    Util.iniciarCronometro();
    indiceEncontrado = -1;
    Documento resultado = buscarBinariaRecursivaIndice(textoBuscar.toLowerCase(), 0, documentos.size() - 1);
    System.out.println("Tiempo: " + Util.getTextoTiempoCronometro());
    return resultado;
}

private static Documento buscarBinariaRecursivaIndice(String textoBuscar, int izquierda, int derecha) {
    if (izquierda > derecha) {
        return null;
    }

    int medio = (izquierda + derecha) / 2;
    Documento actual = documentos.get(medio);
    String nombreCompleto = actual.getNombreCompleto().toLowerCase();

    if (nombreCompleto.contains(textoBuscar)) {
        indiceEncontrado = medio;
        return actual;
    }

    if (nombreCompleto.compareTo(textoBuscar) > 0) {
        return buscarBinariaRecursivaIndice(textoBuscar, izquierda, medio - 1);
    } else {
        return buscarBinariaRecursivaIndice(textoBuscar, medio + 1, derecha);
    }
}

public static Documento getAnterior() {
    if (indiceEncontrado > 0) {
        indiceEncontrado--;
        return documentos.get(indiceEncontrado);
    }
    return null;
}

public static Documento getSiguiente() {
    if (indiceEncontrado >= 0 && indiceEncontrado < documentos.size() - 1) {
        indiceEncontrado++;
        return documentos.get(indiceEncontrado);
    }
    return null;
}


}
