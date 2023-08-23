package main;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.openstreetmap.gui.jmapviewer.Coordinate;

public class Clustering {
	private Grafo _grafo;
	private ArrayList <Coordinate> _coordenadas;
	
	public Clustering(int verticesTotales, ArrayList <Coordinate> coordenadasTotales) {
		_grafo = new Grafo(verticesTotales);
		_coordenadas = coordenadasTotales;
	}
	
	public boolean crearGrafo() {
		if(_grafo.tamano()==0 || _coordenadas == null) {
			return false;
		}
		int puntoOrigen = 0;
		for(Coordinate coordenadaOriginal : _coordenadas) {
			int puntoDestino = 0;
			for(Coordinate coordenadaDestino : _coordenadas) {
				if(!coordenadaOriginal.equals(coordenadaDestino)) {
					_grafo.agregarArista(puntoOrigen, puntoDestino, distanciaEuclidea(coordenadaOriginal, coordenadaDestino));
				}
				puntoDestino++;
			}
			puntoOrigen++;
		}
		return true;
	}
	
	public boolean conectado(Coordinate coordenadaOrigen, Coordinate coordenadaDestino) {
		return _grafo.existeArista(_coordenadas.indexOf(coordenadaOrigen), _coordenadas.indexOf(coordenadaDestino));
	}
	
	public void generarClusters(Integer cant) {
		if(cant==1 || _grafo.tamano()<3) {
			return;
		}
		while(cant!=1) {
			_grafo.eliminarAristaMayorVecinos();
			cant--;
		}
	}
	
	public void arbolGeneradorMinimo() {
		_grafo.arbolMinimoPrim();
	}
	
	public Double distanciaEuclidea(Coordinate C1, Coordinate C2) {
		if(C1==null || C2==null) {
			return (double) 0;
		}
		//Distancia Euclidea entre dos puntos = RaizCuadrada((x2-x1)²+(y2-y1)²)
		BigDecimal disEuclidea = new BigDecimal(Math.sqrt(Math.pow(C2.getLat() - C1.getLat(), 2) + Math.pow(C2.getLon() - C1.getLon(), 2)));
		return disEuclidea.setScale(3, RoundingMode.HALF_UP).doubleValue();
	}
	
	public String resultadoMatriz() {
		return _grafo.resultadoMatriz();
	}
	
	public String resultadoArbolMinimo() {
		return _grafo.resultadoArbolMinimo();
	}
}
