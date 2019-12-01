package tests;

import estructuras.Ciudad;
import estructuras.Grafo;
import estructuras.Lista;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import org.junit.Test;

public class TestCaminos {

    private static final Grafo gr = new Grafo();

    @Test
    public void testCaminoMenorDist() {
        boolean exitoTest = false;
        int i = 1;
        String[] ciudades = {"Ushuaia", "Glaciar Perito Moreno", "Jericoacoara", "Salar de Uyuni", "Machu Pichu",
            "Cartagena de Indias"};
        Lista listaCiudades = new Lista();

        //Creamos la lista de ciudades por las que se debe pasar
        for (String s : ciudades) {
            listaCiudades.insertar(new Ciudad(s, ""), i);
            i++;
        }

        generarGrafo();
        Ciudad origen = new Ciudad("Ushuaia", "");
        Ciudad destino = new Ciudad("Cartagena de Indias", "");

        //Obtenemos la lista de ciudades que se deben recorrer para poder realizar el test
        Lista caminoCiudades = gr.caminoMenorDistanciaImperativo(origen, destino, obtenerCiudadesObligatorias()).getListaDeNodos();

        //Realizamos la comparación para verificar si todas las ciudades obligatorias han sido recorridas
        exitoTest = gr.verificarListaVisitados(caminoCiudades, listaCiudades);

        assert exitoTest == true;
    }

    @Test
    public void testTiemposCaminos() {
        generarGrafo();
        int i;

        //Calculo el tiempo de las iteraciones del metodo de camino de menor distancia imperativo
        double tpoInicial = System.currentTimeMillis();
        for (i = 0; i <= 10000; i++) {
            gr.caminoMenorDistanciaImperativo(new Ciudad("Ushuaia", ""), new Ciudad("Cartagena de Indias", ""),
                    obtenerCiudadesObligatorias());
        }
        double tpoFinal = System.currentTimeMillis();
        //
        //Calculo el tiempo de las iteraciones del metodo de camino de menor distancia dinámico
        double tpoInicial2 = System.currentTimeMillis();
        for (i = 0; i <= 10000; i++) {
            gr.caminoMenorDistanciaDinamico(new Ciudad("Ushuaia", ""), new Ciudad("Cartagena de Indias", ""),
                    obtenerCiudadesObligatorias());
        }
        double tpoFinal2 = System.currentTimeMillis();
        //
        System.out.println("Tiempo de iteración de camino de forma imperativa " + (tpoFinal - tpoInicial));
        System.out.println("Tiempo de iteración de camino de forma dinámica " + (tpoFinal2 - tpoInicial2));
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

    public static Lista obtenerCiudadesObligatorias() {
        // Metodo que devuelve una lista con ciudades obligatorias por las cuales se deben pasar
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
