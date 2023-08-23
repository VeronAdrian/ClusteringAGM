package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Grafo {
		private int _nodos;
		private boolean[][] _matriz;
		private Map<String, Double> _listaPesos;		

		public Grafo(int vertices) {
			_matriz = new boolean[vertices][vertices];
			_listaPesos = new HashMap<String, Double>();
			_nodos = 0;
		}
		
		public void agregarArista(int puntoOrigen, int puntoDestino, Double peso)	{
			if(verificarVertice(puntoOrigen) && verificarVertice(puntoDestino) && verificarDistintos(puntoOrigen, puntoDestino)) {
				_matriz[puntoOrigen][puntoDestino] = true;
				_matriz[puntoDestino][puntoOrigen] = true;
				_listaPesos.put(llaveLista(puntoOrigen, puntoDestino), peso);
				_listaPesos.put(llaveLista(puntoDestino, puntoOrigen), peso);
			}
			_nodos++;
		}
		
        public void arbolMinimoPrim() {
        	if(_matriz.length<3) {
        		return;
        	}
        	boolean[] verticesRecorridos = new boolean [_nodos];
        	boolean[][] marcados = new boolean[tamano()][tamano()];
        	int verticeOrigenMinimo=0;
        	int verticeDestinoMinimo=0;
        	verticesRecorridos[0] = true;
        	for (int k = 0; k < tamano()-1; k++) {
        		double pesoMinimo = 99999999.99999999;	//Ponemos un minimo que es imposible de obtener para empezar
                for (int verticeOrigen = 0; verticeOrigen < tamano(); verticeOrigen++) {
                    for (int verticeDestino = 0; verticeDestino<tamano(); verticeDestino++) {
                        if (verificarDistintos(verticeOrigen, verticeDestino) && verticesRecorridos[verticeOrigen] && !verticesRecorridos[verticeDestino] && obtenerPeso(verticeOrigen, verticeDestino) < pesoMinimo) {   
                        	pesoMinimo = obtenerPeso(verticeOrigen, verticeDestino);
                        	verticeOrigenMinimo = verticeOrigen;
                        	verticeDestinoMinimo = verticeDestino;
                        }
                    }
                }            
                marcados[verticeOrigenMinimo][verticeDestinoMinimo]=true;
                marcados[verticeDestinoMinimo][verticeOrigenMinimo]=true;
                verticesRecorridos[verticeDestinoMinimo] = true;
            }
        	eliminarVariasAristasMenos(marcados);
        }
		
		public void eliminarArista(int verticeOrigen, int verticeDestino) {
			if(verificarVertice(verticeOrigen) && verificarVertice(verticeDestino) && verificarDistintos(verticeOrigen, verticeDestino)) {
				_matriz[verticeOrigen][verticeDestino] = false;
				_matriz[verticeDestino][verticeOrigen] = false;
			}
		}
		
		public void eliminarVariasAristasMenos(boolean[][] marcados) {
			for(int puntoOrigen=0; puntoOrigen<tamano(); puntoOrigen++) {
				for(int puntoDestino=0; puntoDestino<tamano(); puntoDestino++) {
					if(!marcados[puntoOrigen][puntoDestino]) {
						eliminarArista(puntoOrigen, puntoDestino);
					}
				}
			}
		}

		public void eliminarAristaMayorVecinos() {			
			for(int puntoOrigen=0; puntoOrigen<tamano(); puntoOrigen++) {
				Set<Integer> vecinosPuntoOrigen = vecinos(puntoOrigen);
				for(int puntoDestino=0; puntoDestino<tamano(); puntoDestino++) {
					Set<Integer> vecinosPuntoDestino = vecinos(puntoOrigen);
					if(verificarDistintos(puntoOrigen, puntoDestino)&& existeArista(puntoOrigen, puntoDestino) && calcularPesoVecinasMenos(puntoOrigen, vecinosPuntoOrigen, puntoDestino)+
					calcularPesoVecinasMenos(puntoDestino, vecinosPuntoDestino, puntoOrigen)>obtenerPeso(puntoOrigen,puntoDestino)) {
						eliminarArista(puntoOrigen, puntoDestino);
						return;
					}
				}
			}
		}
				
		public double calcularPesoVecinasMenos(int puntoOrigen, Set<Integer> vecinosPuntoOrigen, int puntoDestino) {
			double resultado = 0;
			for(Integer verticeDestino: vecinosPuntoOrigen) {
				if(verticeDestino!=puntoOrigen && verticeDestino!=puntoDestino && existeArista(puntoOrigen, verticeDestino)) {
					resultado+=obtenerPeso(puntoOrigen, verticeDestino);
				}
			}
			return resultado;
		}
		
		public boolean existeArista(int verticeOrigen, int verticeDestino) {
			if(!verificarDistintos(verticeOrigen, verticeDestino)) {
				return false;
			}
			return _matriz[verticeOrigen][verticeDestino];
		}

		public int tamano() {
			return _matriz.length;
		}
		
		public Set<Integer> vecinos(int vertice) {
			if(!verificarVertice(vertice)) {
				return null;
			}			
			Set<Integer> vecinos = new HashSet<Integer>();
			for(int vecino = 0; vecino < tamano(); vecino++) {
				if((vertice != vecino ) && existeArista(vertice, vecino) ) {
					vecinos.add(vecino);
				}
			}
			
			return vecinos;		
		}
		
		public boolean verificarVertice(int vertice) {
			if( vertice < 0 || vertice >= _matriz.length ) {
				return false;
			}
			return true;
		}

		public boolean verificarDistintos(int verticeOrigen, int verticeDestino) {
			if(verticeOrigen == verticeDestino) {
				return false;
			}
			return true;
		}
		
		public Double obtenerPeso(int verticeOrigen, int verticeDestino) {
			return _listaPesos.get(llaveLista(verticeOrigen, verticeDestino));
		}
		
		public String resultadoMatriz() {
			StringBuilder string = new StringBuilder();
			int verticeOrigen = 0;
			while(verticeOrigen<_matriz.length) {
				int verticeDestino = 0;
				while(verticeDestino < _matriz.length) {
					if(verificarDistintos(verticeOrigen, verticeDestino)) {
						string.append("Punto: "+(verticeOrigen+1)+" al Punto: "+(verticeDestino+1)+" Distancia de: "+obtenerPeso(verticeOrigen, verticeDestino)+"\n");
					}				
					verticeDestino++;
				}
				verticeOrigen++;
			}
			return string.toString();
		}
		
		public String resultadoArbolMinimo() {
			StringBuilder string = new StringBuilder();
			int verticeOrigen = 0;
			while(verticeOrigen<_matriz.length) {
				int verticeDestino = 0;
				while(verticeDestino < _matriz.length) {
					if(verificarDistintos(verticeOrigen, verticeDestino) && existeArista(verticeOrigen, verticeDestino)) {
						string.append("Punto: "+(verticeOrigen+1)+" al Punto: "+(verticeDestino+1)+" Distancia de: "+obtenerPeso(verticeOrigen, verticeDestino)+"\n");
					}				
					verticeDestino++;
				}
				verticeOrigen++;
			}
			return string.toString();
		}
		
		private String llaveLista(int verticeOrigen, int verticeDestino) {
			StringBuilder string = new StringBuilder();
			return string.append(verticeOrigen).append(verticeDestino).toString();
		}
}

