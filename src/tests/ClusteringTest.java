package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.openstreetmap.gui.jmapviewer.Coordinate;

import main.Clustering;

class ClusteringTest {
	ArrayList <Coordinate> coordenadas = new ArrayList <Coordinate>();

	@Test
	void crearGrafoTestTrue() {
		coordenadas.add(new Coordinate(0,0));
		coordenadas.add(new Coordinate(100,100));
		coordenadas.add(new Coordinate(200,200));
		coordenadas.add(new Coordinate(300,300));
		coordenadas.add(new Coordinate(50,50));
		Clustering clusterTest = new Clustering(coordenadas.size(), coordenadas);
		
		assertTrue(clusterTest.crearGrafo());
	}
	
	@Test
	void crearGrafoTestFalseNull() {
		Clustering clusterTest = new Clustering(0,null);
		
		assertFalse(clusterTest.crearGrafo());
	}
	
	@Test
	void crearGrafoTestFalseVacio() {
		ArrayList <Coordinate> coordenadasVacias = new ArrayList <Coordinate>();
		Clustering clusterTest = new Clustering(0,coordenadasVacias);
		
		assertFalse(clusterTest.crearGrafo());
	}
	
	@Test
	void distanciaEuclideaTest() {
		ArrayList <Coordinate> coordenadasTest = new ArrayList <Coordinate>();
		coordenadasTest.add(new Coordinate(0,0));
		coordenadasTest.add(new Coordinate(100,100));
		Clustering clusterTest = new Clustering(2, coordenadasTest);
		
		assertEquals(141.421, clusterTest.distanciaEuclidea(coordenadasTest.get(0), coordenadasTest.get(1)));
	}
	
	@Test
	void distanciaEuclideaTestError() {
		ArrayList <Coordinate> coordenadasTest = new ArrayList <Coordinate>();
		coordenadasTest.add(new Coordinate(0,0));
		coordenadasTest.add(new Coordinate(100,100));
		Clustering clusterTest = new Clustering(2, coordenadasTest);
		
		assertEquals(0, clusterTest.distanciaEuclidea(null, null));
	}
	
	@Test
	void conectadoTest() {
		coordenadas.add(new Coordinate(0,0));
		coordenadas.add(new Coordinate(100,100));
		coordenadas.add(new Coordinate(200,200));
		Clustering clusterTest = new Clustering(coordenadas.size(), coordenadas);
		clusterTest.crearGrafo();
		
		assertTrue(clusterTest.conectado(new Coordinate(0,0), new Coordinate(100,100)));
	}
	
	@Test
	void generarClustersUnico() {
		coordenadas.add(new Coordinate(0,0));
		coordenadas.add(new Coordinate(100,100));
		coordenadas.add(new Coordinate(300,300));
		Clustering clusterTest = new Clustering(coordenadas.size(), coordenadas);
		clusterTest.crearGrafo();
		clusterTest.arbolGeneradorMinimo();
		
		clusterTest.generarClusters(1);
		assertTrue(clusterTest.conectado(new Coordinate(100,100), new Coordinate(300,300)));
	}
	
	@Test
	void generarClustersMenor3() {
		coordenadas.add(new Coordinate(0,0));
		coordenadas.add(new Coordinate(100,100));
		Clustering clusterTest = new Clustering(coordenadas.size(), coordenadas);
		clusterTest.crearGrafo();
		clusterTest.arbolGeneradorMinimo();
		
		clusterTest.generarClusters(2);
		assertTrue(clusterTest.conectado(new Coordinate(0,0), new Coordinate(100,100)));
	}
	
	@Test
	void generarClustersVarios() {
		coordenadas.add(new Coordinate(50,50));
		coordenadas.add(new Coordinate(100,100));
		coordenadas.add(new Coordinate(400,400));
		coordenadas.add(new Coordinate(450,450));
		Clustering clusterTest = new Clustering(coordenadas.size(), coordenadas);
		clusterTest.crearGrafo();
		clusterTest.arbolGeneradorMinimo();
		
		clusterTest.generarClusters(2);
		assertFalse(clusterTest.conectado(new Coordinate(50,50), new Coordinate(100,100)));
	}
	
	@Test
	void resultadoArbolMinimoTest(){
		coordenadas.add(new Coordinate(0,0));
		coordenadas.add(new Coordinate(100,100));
		Clustering clusterTest = new Clustering(coordenadas.size(), coordenadas);
		clusterTest.crearGrafo();
		clusterTest.arbolGeneradorMinimo();
		
		assertEquals(clusterTest.resultadoArbolMinimo(), "Punto: 1 al Punto: 2 Distancia de: 141.421\n"
				+ "Punto: 2 al Punto: 1 Distancia de: 141.421\n"
				+ "");
	}
}
