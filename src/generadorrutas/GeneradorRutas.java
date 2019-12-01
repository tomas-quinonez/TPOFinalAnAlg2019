package generadorrutas;

import estructuras.Camino;
import estructuras.Ruta;
import estructuras.Ciudad;
import utiles.Aleatorio;
import utiles.TecladoIn;
import estructuras.Grafo;
import estructuras.Lista;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Collections;
import java.util.ListIterator;
import utiles.*;

public class GeneradorRutas {

    static List<Ciudad> ciudades = new ArrayList<Ciudad>();
    static Grafo gr = new Grafo();

    public static void main(String[] args) {
        int op, op2;
        do {
            op = menu1();

            if (op == 1) {
                generarArchivo();
            } else if (op == 2) {
                do {
                    op2 = menu2();
                    switch (op2) {
                        case 1:
                            generarGrafo();
                            break;
                        case 2:
                            caminos();
                            break;
                        case 3:
                            System.out.println(gr.toString());
                            break;
                        default:
                            break;
                    }
                } while (op2 != 0);

            }

        } while (op != 0);
    }

    public static void generarArchivo() {
        Ciudad unaCiudad;
        int i;

        // Usando las ciudades cargadas, se generan las rutas entre las ciudades
        // calculando la distancia en Km usando latitud y longitud de cada destino
        // Se genera una cantidad aleatoria de rutas, de 1 al cantRutas
        // las rutas generadas son a los destinos mas cercanos
        System.out.println("Hasta cuántas conexiones por ciudad?");
        //int cantRutas = TecladoIn.readLineInt();
        int cantRutas;

        File f = new File("datos.txt");           //file to be delete  
        f.delete();
        precargaCiudades();
        //cantRutas = Math.min(cantRutas, ciudades.size() - 1);
        cantRutas = Math.min(99999, ciudades.size() - 1);

        //guardams las ciudades en el archivo
        ListIterator<Ciudad> itc = ciudades.listIterator();
        while (itc.hasNext()) {
            unaCiudad = itc.next();
            guardar("C:" + unaCiudad.getCiudad() + "," + unaCiudad.getPais());
        }

        ListIterator<Ruta> itr;
        ArrayList<Ruta> rutas;
        itc = ciudades.listIterator();
        int cantRutasTmp;
        Ruta unaRuta;
        while (itc.hasNext()) {
            unaCiudad = itc.next();
            rutas = generarRutasDesde(unaCiudad);
            itr = rutas.listIterator();
            i = 0;
            //cantRutasTmp = Aleatorio.intAleatorio(1, cantRutas);
            cantRutasTmp = cantRutas;
            System.out.println("Generando " + cantRutasTmp + " ruta desde " + unaCiudad.getCiudad());
            while (itr.hasNext() && i < cantRutasTmp) {
                unaRuta = itr.next();
                guardar("R:"
                        + unaRuta.getDesde().getCiudad() + ","
                        + unaRuta.getHasta().getCiudad() + ","
                        + unaRuta.getDistancia());
                i++;
            }
        }

    }

    public static ArrayList generarRutasDesde(Ciudad ciu) {
        ArrayList<Ruta> rutas = new ArrayList<>();
        Ciudad unaCiudad;
        int distancia;

        ListIterator<Ciudad> itr = ciudades.listIterator();
        while (itr.hasNext()) {
            unaCiudad = itr.next();
            if (ciu.compareTo(unaCiudad) > 0) {
                distancia = (int) Math.ceil(Math.sqrt(
                        Math.pow(unaCiudad.getLatitud() - ciu.getLatitud(), 2)
                        + Math.pow(unaCiudad.getLongitud() - ciu.getLongitud(), 2))
                        * 112);
                rutas.add(new Ruta(ciu, unaCiudad, distancia));
            }
        }
        Collections.sort(rutas);

        return rutas;
    }

    public static void guardar(String cadena) {
        try {
            String ruta = "datos.txt";
            File file = new File(ruta);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file, true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(cadena + "\n");
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int menu1() {
        int op;
        String[] opciones = {
            "Generar archivo de rutas",
            "Cargar archivo en grafo y probar",};

        System.out.println();
        System.out.println("------------------  MENÚ  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Salir");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);
        return op;
    }

    public static int menu2() {
        int op;
        String[] opciones = {
            "Cargar archivo en grafo",
            "Buscar camino más corto",
            "Imprimir grafo"
        };

        System.out.println();
        System.out.println("------------------  MENÚ  ---------------------");
        for (int i = 0; i < opciones.length; i++) {
            System.out.println((i + 1) + ".- " + opciones[i]);
        }
        System.out.println("0.- Salir");
        System.out.println("-----------------------------------------------");

        do {
            op = TecladoIn.readLineInt();
            if (op < 0 || op > opciones.length) {
                System.out.println("El valor ingresado no es válido");
            }
        } while (op < 0 || op > opciones.length);
        return op;
    }

    public static void precargaCiudades() {
        ciudades.add(new Ciudad("Glaciar Perito Moreno", "Argentina", -73.0481, 50.4732, true));
        ciudades.add(new Ciudad("Ushuaia", "Argentina", -54.0872, -68.3040, false));
        ciudades.add(new Ciudad("Jericoacoara", "Brasil", -2.8714, -40.4916, true));
        ciudades.add(new Ciudad("Tayrona", "Colombia", 11.3000, -74.1667, false));
        ciudades.add(new Ciudad("Cartagena de Indias", "Colombia", 10.3997, -75.5144, false));
        ciudades.add(new Ciudad("Maldonado", "Uruguay", -34.9000, -54.9500, false));
        ciudades.add(new Ciudad("Machu Pichu", "Peru", -13.1631, -72.5456, true));
        ciudades.add(new Ciudad("Salar de Uyuni", "Bolivia", -20.1338, -67.4891, true));
    }

    public static void generarGrafo() {
        Ciudad unaCiudad, otraCiudad;

        gr.borrarDatos();

        File archivo = null;
        FileReader fr = null;
        BufferedReader br = null;

        try {
            // Apertura del fichero y creacion de BufferedReader para poder
            // hacer una lectura comoda (disponer del metodo readLine()).
            archivo = new File("datos.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);

            // Lectura del fichero
            String[] arrOfStr;
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.charAt(0) == 'C') {
                    arrOfStr = linea.substring(2).split(",", 5);
                    unaCiudad = new Ciudad(arrOfStr[0], arrOfStr[1]);
                    gr.insertarVertice(unaCiudad);
                } else {
                    arrOfStr = linea.substring(2).split(",", 3);
                    unaCiudad = new Ciudad(arrOfStr[0], "");
                    otraCiudad = new Ciudad(arrOfStr[1], "");

                    gr.insertarArco(
                            unaCiudad,
                            otraCiudad,
                            Integer.parseInt(arrOfStr[2]));

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // En el finally cerramos el fichero, para asegurarnos
            // que se cierra tanto si todo va bien como si salta 
            // una excepcion.
            try {
                if (null != fr) {
                    fr.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
    }

    public static void caminos() {

        Ciudad unaCiudad, otraCiudad;

        otraCiudad = new Ciudad("Cartagena de Indias", "");
        unaCiudad = new Ciudad("Ushuaia", "");
        if (gr.existeCamino(unaCiudad, otraCiudad)) {
            System.out.println("Camino con menos ciudades: " + gr.caminoMasCorto(unaCiudad, otraCiudad));
            System.out.println("Camino con menos Km: " + gr.caminoMenorDistancia(unaCiudad, otraCiudad));

            Camino caminoPorCiudades = gr.caminoMenorDistanciaImperativo(unaCiudad, otraCiudad, obtenerCiudadesObligatorias());
            if (!caminoPorCiudades.getListaDeNodos().esVacia()) {
                System.out.println("Camino con menos kms pasando por ciudades obligatorias (imperativo):"
                        + gr.caminoMenorDistanciaImperativo(unaCiudad, otraCiudad, obtenerCiudadesObligatorias()));
                System.out.println("Camino con menos kms pasando por ciudades obligatorias (dinamico): "
                        + gr.caminoMenorDistanciaDinamico(unaCiudad, otraCiudad, obtenerCiudadesObligatorias()) + " kms");
            } else {
                System.out.println("No existe un camino que pase por las ciudades obligatorias");
            }
        } else {
            System.out.println("No existe un camino entre las ciudades");
        }
    }

    public static Lista obtenerCiudadesObligatorias() {
        /* 
        * Metodo que devuelve una lista con ciudades obligatorias por las cuales se deben pasar.
        * La lista debe estar ordenada. Es decir, el camino del origen a destino debe recorrer las ciudades
        * en el orden en que aparecen en la lista.
         */
        Lista ls = new Lista();
        int i = 1;
        String[] ciudades = {"Glaciar Perito Moreno", "Jericoacoara", "Salar de Uyuni", "Machu Pichu"};
        for (String s : ciudades) {
            ls.insertar(new Ciudad(s, ""), i);
            i++;
        }
        return ls;
    }
}
