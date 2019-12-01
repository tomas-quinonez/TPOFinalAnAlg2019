package estructuras;

public class NodoVertice {


    private Object elem;
    private NodoVertice sigVertice;
    private NodoAdyacente verticeAdy;
    private int posMatriz;


    public NodoVertice(){
        elem=null;
        sigVertice=null;
        verticeAdy=null;
        posMatriz=0;
    }
    public NodoVertice(Object nuevoVert, int pos){
        elem=nuevoVert;
        sigVertice=null;
        verticeAdy=null;
        posMatriz=pos;
    }
    
    public NodoVertice(Object nuevoVert){
        elem=nuevoVert;
        sigVertice=null;
        verticeAdy=null;
        posMatriz=0;
    }
    
    public int getPosMatriz(){
        return this.posMatriz;
    }

    public Object getElem(){
        return elem;
    }
    public NodoAdyacente getAdyacente(){
        return verticeAdy;
    }
    public NodoVertice getSigVertice(){
        return sigVertice;
    }


    public void setElem(Object nuevoVert){
        elem=nuevoVert;
    }
    public void setAdyacente(NodoAdyacente nuevoAdy){
        verticeAdy=nuevoAdy;
    }
    public void setSigVertice(NodoVertice nuevoVert){
        sigVertice=nuevoVert;
    }


    public boolean equals(NodoVertice aux){
        return (this.elem.equals(aux.getElem()));
    }

}
