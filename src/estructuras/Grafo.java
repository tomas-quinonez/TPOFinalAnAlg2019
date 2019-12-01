package estructuras;

public class Grafo {

    private NodoVertice inicio;
    private int cantidad;
    private int matrizDist[][];
    private static final int TAM = 30;
    private boolean menorCosto; // variable que indica si la matriz ya posee las distancias minimas entre dos nodos cualesquiera

    public Grafo() {
        inicio = null;
        cantidad = 0;
        matrizDist = new int[TAM][TAM];
        menorCosto = false;
    }

    public boolean insertarVertice(Object elem) {
        boolean exito = true;
        NodoVertice aux = new NodoVertice(elem, cantidad);
        if (inicio == null) {
            inicio = aux;
            cantidad++;
        } else {
            NodoVertice temp = inicio;
            int i = 1;
            boolean excep = false;
            while (temp.getSigVertice() != null && !excep) {
                if (temp.getElem().equals(elem)) {
                    excep = true;
                } else {
                    temp = temp.getSigVertice();
                }
            }
            if (temp.getElem().equals(elem)) {
                excep = true;
            }
            if (excep) {
                exito = false;
            } else {
                temp.setSigVertice(aux);
                cantidad++;
            }
        }
        return exito;
    }

    public boolean insertarArco(Object vert1, Object vert2, Object etiqueta) {
        NodoVertice aux1 = buscarNodoVertice(inicio, vert1);
        boolean excep = true, exito = true;
        if (aux1 != null) {
            NodoVertice aux2 = buscarNodoVertice(inicio, vert2);
            if (aux2 != null) {
                NodoVertice temp2 = new NodoVertice(vert2);
                if (recuperarArco(aux1.getAdyacente(), temp2) == null) {
                    if (aux1.getAdyacente() != null) {
                        NodoAdyacente auxAD1 = aux1.getAdyacente();
                        while (auxAD1.getSigAdyacente() != null) {
                            auxAD1 = auxAD1.getSigAdyacente();
                        }
                        auxAD1.setSigAdyacente(new NodoAdyacente(aux2, etiqueta));
                    } else {
                        aux1.setAdyacente(new NodoAdyacente(aux2, etiqueta));
                    }
                    if (aux2.getAdyacente() != null) {
                        NodoAdyacente auxAD2 = aux2.getAdyacente();
                        while (auxAD2.getSigAdyacente() != null) {
                            auxAD2 = auxAD2.getSigAdyacente();
                        }
                        auxAD2.setSigAdyacente(new NodoAdyacente(aux1, etiqueta));
                    } else {
                        aux2.setAdyacente(new NodoAdyacente(aux1, etiqueta));
                    }
                    excep = false;
                }
                if (exito) {
                    //System.out.println(aux1.getPosMatriz()+" "+aux2.getPosMatriz());
                    this.matrizDist[aux1.getPosMatriz()][aux2.getPosMatriz()] = (int) etiqueta;
                    this.matrizDist[aux2.getPosMatriz()][aux1.getPosMatriz()] = (int) etiqueta;
                }
            }
        }
        if (excep) {
            exito = false;
        }
        return exito;
    }

    public boolean eliminarVertice(Object vert) {
        int ind = elimCompleto(inicio, new NodoVertice(vert), 0);
        boolean exito = true;
        if (ind == 1) {
            inicio = inicio.getSigVertice();
        }
        if (ind == 0) {
            exito = false;
        }
        return exito;
    }

    private int elimCompleto(NodoVertice aux, NodoVertice busq, int enc) {
        int ret = 0;
        if (aux != null) {
            if (enc == 1) {
                eliminarArco(aux, aux.getAdyacente(), busq);
                elimCompleto(aux.getSigVertice(), busq, enc);
            } else {
                if (aux.getElem().equals(busq.getElem())) {
                    elimCompleto(aux.getSigVertice(), busq, 1);
                    ret = 1;
                } else {
                    ret = elimCompleto(aux.getSigVertice(), busq, enc);
                    if (ret == 1 || ret == 2) {
                        eliminarArco(aux, aux.getAdyacente(), busq);
                        if (ret == 1) {
                            aux.setSigVertice(aux.getSigVertice().getSigVertice());
                            ret = 2;
                        }
                    }
                }
            }
        }
        return ret;
    }

    public boolean eliminarArco(Object vert1, Object vert2) {
        boolean exito = true;
        NodoVertice aux1 = buscarNodoVertice(inicio, vert1);
        boolean excep = true;
        if (aux1 != null) {
            NodoVertice aux2 = buscarNodoVertice(inicio, vert2);
            if (aux2 != null) {
                NodoVertice temp2 = new NodoVertice(vert2);
                if (recuperarArco(aux1.getAdyacente(), temp2) != null) {
                    eliminarArco(aux1, aux1.getAdyacente(), new NodoVertice(vert2));
                    eliminarArco(aux2, aux2.getAdyacente(), new NodoVertice(vert1));
                    excep = false;
                }
            }
        }
        if (excep) {
            exito = false;
        }
        return exito;
    }

    private void eliminarArco(NodoVertice inicio, NodoAdyacente aux, NodoVertice nodo) {
        if (aux != null) {
            NodoAdyacente sig = aux;
            if (sig.getVerticeAdy().getElem().equals(nodo.getElem())) {
                inicio.setAdyacente(sig.getSigAdyacente());
                sig = null;
            } else {
                while (sig != null) {
                    if (sig.getVerticeAdy().getElem().equals(nodo.getElem())) {
                        aux.setSigAdyacente(sig.getSigAdyacente());
                        sig = null;
                    } else {
                        aux = sig;
                        sig = sig.getSigAdyacente();
                    }
                }
            }
        }
    }

    public int cantidadDeCaminos(Object v1, Object v2) {
        Lista aux;
        aux = this.caminosSimples(v1, v2);
        return aux.longitud();
    }

    public Lista caminosSimples(Object v1, Object v2) {
        Lista ret = new Lista();
        NodoVertice vert = buscarNodoVertice(inicio, v1);
        if (vert != null) {
            Lista aux = new Lista();
            caminosSimples(aux, ret, vert, new NodoVertice(v2));
        }
        ret.invertirLista();
        return ret;
    }

    private void caminosSimples(Lista aux, Lista ret, NodoVertice vert, NodoVertice v2) {
        aux.insertar(vert.getElem(), 1);

        if (vert.getElem().equals(v2.getElem())) {
            Lista aux2 = aux.clone();
            aux2.invertirLista();
            ret.insertar(aux2, 1);
        } else {
            NodoAdyacente temp = vert.getAdyacente();
            while (temp != null) {
                if (aux.localizar(temp.getVerticeAdy().getElem()) == -1) {
                    caminosSimples(aux, ret, temp.getVerticeAdy(), v2);
                }
                temp = temp.getSigAdyacente();
            }
        }
        aux.eliminar(1);
    }

    public Lista caminoMasCorto(Object v1, Object v2) {
        Lista ret = new Lista();
        NodoVertice temp = buscarNodoVertice(inicio, v1);
        if (temp != null) {
            ret = caminoCorto(new Lista(), ret, temp, new NodoVertice(v2));
        }
        ret.invertirLista();
        return ret;
    }

    private Lista caminoCorto(Lista aux, Lista ret, NodoVertice vert, NodoVertice v2) {
        aux.insertar(vert.getElem(), 1);

        //System.out.println(aux.toString());
        if (vert.getElem().equals(v2.getElem())) {
            if (ret.esVacia()) {
                ret = aux.clone();
            } else {
                if (aux.longitud() < ret.longitud()) {
                    ret.vaciar();
                    ret = aux.clone();
                }
            }
        } else {
            NodoAdyacente temp = vert.getAdyacente();
            while (temp != null) {
                if (aux.localizar(temp.getVerticeAdy().getElem()) == -1) {
                    if (ret.esVacia()) {
                        ret = caminoCorto(aux, ret, temp.getVerticeAdy(), v2);
                    } else {
                        if (ret.longitud() > aux.longitud() + 1) {
                            ret = caminoCorto(aux, ret, temp.getVerticeAdy(), v2);
                        }
                    }
                }
                temp = temp.getSigAdyacente();
            }
        }
        aux.eliminar(1);
        return ret;
    }

    public Lista caminoMasLargo(Object v1, Object v2) {
        Lista ret = new Lista();
        NodoVertice temp = buscarNodoVertice(inicio, v1);
        if (temp != null) {
            ret = caminoLargo(new Lista(), ret, temp, new NodoVertice(v2));
        }
        ret.invertirLista();
        return ret;
    }

    private Lista caminoLargo(Lista aux, Lista ret, NodoVertice vert, NodoVertice v2) {
        aux.insertar(vert.getElem(), 1);
        if (vert.getElem().equals(v2.getElem())) {
            if (aux.longitud() > ret.longitud()) {
                ret.vaciar();
                ret = aux.clone();
            }
        } else {
            NodoAdyacente temp = vert.getAdyacente();
            while (temp != null) {
                if (aux.localizar(temp.getVerticeAdy().getElem()) == -1) {
                    ret = caminoLargo(aux, ret, temp.getVerticeAdy(), v2);
                }
                temp = temp.getSigAdyacente();
            }
        }
        aux.eliminar(1);
        return ret;
    }

    public boolean existeCamino(Object v1, Object v2) {
        NodoVertice temp = buscarNodoVertice(inicio, v1);
        if (temp != null) {
            Lista lis = new Lista();
            NodoVertice nv2 = new NodoVertice(v2);
            lis.vaciar();
            return caminoInt(temp, nv2, lis);
        } else {
            return false;
        }
    }

    private boolean caminoInt(NodoVertice aux, NodoVertice v2, Lista lis) {
        if (aux.equals(v2)) {
            return true;
        } else {
            lis.insertar(aux, 1);
            NodoAdyacente temp = aux.getAdyacente();
            boolean enc = false;
            while (!enc && temp != null) {
                if (lis.localizar(temp.getVerticeAdy()) == -1) {
                    enc = caminoInt(temp.getVerticeAdy(), v2, lis);
                }
                temp = temp.getSigAdyacente();
            }
            return enc;
        }
    }

    public int cantidadVertices() {
        return cantidad;
    }

    public Lista obtenerVertices() {
        Lista ret = new Lista();
        NodoVertice aux = inicio;
        while (aux != null) {
            visitar(aux, ret);
            aux = aux.getSigVertice();
        }
        return ret;
    }

    private void visitar(NodoVertice aux, Lista visit) {
        if (visit.localizar(aux.getElem()) == -1) {
            visit.insertar(aux.getElem(), 1);
            NodoAdyacente temp = aux.getAdyacente();
            while (temp != null) {
                visitar(temp.getVerticeAdy(), visit);
                temp = temp.getSigAdyacente();
            }
        }
    }

    public boolean actualizarEtiqueta(Object nombre1, Object nombre2, Object etiqueta) {
        boolean exito = false;
        NodoVertice aux1 = buscarNodoVertice(inicio, nombre1);
        if (aux1 != null) {
            NodoAdyacente temp = aux1.getAdyacente();
            while (temp != null && !(temp.getVerticeAdy().getElem().equals(nombre2))) {
                temp = temp.getSigAdyacente();
            }
            if (temp != null) {
                temp.setEtiqueta(etiqueta);
                exito = true;
            }
        }
        return exito;
    }

    public NodoAdyacente recuperarArco(NodoAdyacente aux, NodoVertice vert) {
        if (aux != null) {
            if (aux.getVerticeAdy().getElem().equals(vert.getElem())) {
                return aux;
            } else {
                return recuperarArco(aux.getSigAdyacente(), vert);
            }
        } else {
            return null;
        }
    }

    private NodoVertice buscarNodoVertice(NodoVertice aux, Object elem) {

        if (aux != null) {
            if (aux.getElem().equals(elem)) {
                return aux;
            } else {
                return buscarNodoVertice(aux.getSigVertice(), elem);
            }
        } else {
            return null;
        }
    }

    @Override
    public String toString() {
        String cadena = "";
        NodoVertice aux = inicio;
        while (aux != null) {
            cadena = toStringInt(aux, cadena);
            aux = aux.getSigVertice();
            cadena = cadena + "\n";
        }
        if (cadena.equals("")) {
            cadena = "Grafo Vacío";
        }
        return cadena;
    }

    private String toStringInt(NodoVertice aux, String cadena) {
        cadena = cadena + "Vertice: " + aux.getElem() + ".";
        NodoAdyacente temp = aux.getAdyacente();
        if (temp != null) {
            cadena = cadena + "Vertices Adyacentes: ";
        }
        while (temp != null) {
            cadena
                    = cadena
                    + temp.getVerticeAdy().getElem()
                    + "(" + temp.getEtiqueta() + ")"
                    + " - ";
            temp = temp.getSigAdyacente();
        }
        return cadena;
    }

    public Camino caminoMenorDistancia(Object elemA, Object elemB) {
        //Se busca el camino más corto en distancia entre el origen y destino
        NodoVertice vertA, vertB;

        vertA = this.buscarNodoVertice(inicio, elemA);
        vertB = this.buscarNodoVertice(inicio, elemB);
        Lista lsVisitados = new Lista();
        Camino camino = new Camino();
        //Se usa un arreglo por tema de referencia, para poder modificarlo según mi conveniencia.
        int[] distanciaMinima = {Integer.MAX_VALUE};
        if (vertA != null & vertB != null) {
            camino.setListaDeNodos(caminoMenorDistanciaAux(vertA, elemB, lsVisitados, 0, camino.getListaDeNodos(), distanciaMinima));
            camino.setDistancia(distanciaMinima[0]);
        }
        return camino;
    }

    private Lista caminoMenorDistanciaAux(NodoVertice n, Object dest, Lista lsVisitados, int distActual, Lista lsCamino, int[] distMin) {
        //        
        lsVisitados.insertar(n.getElem(), lsVisitados.longitud() + 1);
        if (n.getElem().equals(dest)) {
            /**
             * Como "lsVisitados" va a sufrir modificaciones se decide clonarla,
             * porque si sólo se le asigna va a sufrir modificaciones,
             * "lsCamino" va a sufrir las mismas modificaciones (referencia).
             */
            lsCamino = lsVisitados.clone();
            distMin[0] = distActual;

        } else {
            NodoAdyacente ady = n.getAdyacente();
            while (ady != null) {
                distActual += (int) ady.getEtiqueta();
                if (distActual < distMin[0]) {
                    if (lsVisitados.localizar(ady.getVerticeAdy().getElem()) < 0) {
                        lsCamino = caminoMenorDistanciaAux(ady.getVerticeAdy(), dest, lsVisitados, distActual, lsCamino, distMin);
                    }
                }
                //Se resta porque voy a ir por otro camino,y este no continen al adyacente anterior.
                distActual -= (int) ady.getEtiqueta();
                ady = ady.getSigAdyacente();
            }
        }
        lsVisitados.eliminar(lsVisitados.longitud());

        return lsCamino;
    }

    public void borrarDatos() {
        this.inicio = null;
        cantidad = 0;
    }

    public Camino caminoMenorDistanciaImperativo(Object elemA, Object elemB, Lista nodos) {
        //Se busca el camino más corto en distancia entre el origen y destino
        NodoVertice vertA, vertB;

        vertA = this.buscarNodoVertice(inicio, elemA);
        vertB = this.buscarNodoVertice(inicio, elemB);
        Lista lsVisitados = new Lista();
        Camino camino = new Camino();
        //Se usa un arreglo por tema de referencia, para poder modificarlo según mi conveniencia.
        int[] distanciaMinima = {Integer.MAX_VALUE};
        if (vertA != null & vertB != null && this.verificarListaNodos(nodos)) {
            camino.setListaDeNodos(caminoMenorDistanciaImperativoAux(vertA, elemB, lsVisitados, 0, camino.getListaDeNodos(), distanciaMinima, nodos));
            camino.setDistancia(distanciaMinima[0]);
        }
        return camino;
    }

    private Lista caminoMenorDistanciaImperativoAux(NodoVertice n, Object dest, Lista lsVisitados, int distActual, Lista lsCamino, int[] distMin, Lista ciudades) {
        //        
        lsVisitados.insertar(n.getElem(), lsVisitados.longitud() + 1);
        if (n.getElem().equals(dest) && this.verificarListaVisitados(lsVisitados, ciudades)) {
            /**
             * Como "lsVisitados" va a sufrir modificaciones se decide clonarla,
             * porque si sólo se le asigna va a sufrir modificaciones,
             * "lsCamino" va a sufrir las mismas modificaciones (referencia).
             */
            lsCamino = lsVisitados.clone();
            distMin[0] = distActual;

        } else {
            NodoAdyacente ady = n.getAdyacente();
            while (ady != null) {
                distActual += (int) ady.getEtiqueta();
                if (distActual < distMin[0]) {
                    if (lsVisitados.localizar(ady.getVerticeAdy().getElem()) < 0) {
                        lsCamino = caminoMenorDistanciaImperativoAux(ady.getVerticeAdy(), dest, lsVisitados, distActual, lsCamino, distMin, ciudades);
                    }
                }
                //Se resta porque voy a ir por otro camino,y este no continen al adyacente anterior.
                distActual -= (int) ady.getEtiqueta();
                ady = ady.getSigAdyacente();
            }
        }
        lsVisitados.eliminar(lsVisitados.longitud());

        return lsCamino;
    }

    public boolean verificarListaVisitados(Lista lsVisitados, Lista nodos) {
        // Se verifica si la primer lista recibida por parametro contiene todos los elementos de la segunda lista
        boolean exito = true;
        int i = 1;
        while (i <= nodos.longitud() && exito) {
            exito = lsVisitados.localizar(nodos.recuperar(i)) != -1;
            i++;
        }
        return exito;
    }

    public void getMatrizDeDistancia() {
        // Metodo que devuelve la matriz de distancias del grafo (para testeo)
        for (int i = 0; i < cantidad; i++) {
            for (int j = 0; j < cantidad; j++) {
                System.out.print(matrizDist[i][j] + " ");
            }
            System.out.println();
        }
    }

    public int caminoMenorDistanciaDinamico(Object elemA, Object elemB, Lista lsNodos) {
        //Se busca el camino más corto en distancia entre el origen y destino utilizando programación dinámica
        NodoVertice vertA, vertB;
        int distancia = 0;

        vertA = this.buscarNodoVertice(inicio, elemA);
        vertB = this.buscarNodoVertice(inicio, elemB);
        if (vertA != null & vertB != null && verificarListaNodos(lsNodos)) {
            if (!this.menorCosto) { // Si ya está cargada la matriz de costos minimos, no se vuelve a cargar
                this.obtenerMatrizCaminosMasCortos();
                this.menorCosto = true;
            }

            //Recorremos toda la lista de nodos obligatorias para ir sumando las distancias parciales
            distancia += matrizDist[vertA.getPosMatriz()][this.buscarNodoVertice(inicio, lsNodos.recuperar(1)).getPosMatriz()];
            if (distancia < 999999) {
                while (lsNodos.longitud() > 1 && distancia < 999999) {
                    int k = this.buscarNodoVertice(inicio, lsNodos.recuperar(1)).getPosMatriz();
                    int j = this.buscarNodoVertice(inicio, lsNodos.recuperar(2)).getPosMatriz();
                    distancia += matrizDist[k][j];
                    lsNodos.eliminar(1);
                }
                distancia += matrizDist[this.buscarNodoVertice(inicio, lsNodos.recuperar(1)).getPosMatriz()][vertB.getPosMatriz()];
            }
            if (distancia >= 999999) {
                distancia = Integer.MIN_VALUE;
            }

        }
        return distancia;
    }

    private boolean verificarListaNodos(Lista lsNodos) {
        //Se verifica si cada nodo en la lista lsNodos está insertada en el grafo
        boolean exito = true;
        int i = 1;
        while (i < lsNodos.longitud() && exito) {
            if (this.buscarNodoVertice(inicio, lsNodos.recuperar(i)) == null) {
                exito = false;
            }
            i++;
        }
        return exito;
    }

    private void obtenerMatrizCaminosMasCortos() {
        // Metodo que cambia la matriz del grafo para obtener los caminos mas cortos entre cada par de nodos
        int k, i, j, dist;
        for (k = 0; k < cantidad; k++) {
            for (i = 0; i < cantidad; i++) {
                for (j = 0; j < cantidad; j++) {
                    if (i != j) {
                        if (matrizDist[i][j] == 0) {
                            matrizDist[i][j] = 999999;
                        }
                        dist = matrizDist[i][k] + matrizDist[k][j];
                        if (matrizDist[i][j] > dist) {
                            matrizDist[i][j] = dist;
                        }
                    }
                }
            }
        }
    }

}
