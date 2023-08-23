package tests;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.Set;

import main.Grafo;
import org.junit.jupiter.api.Test;

class GrafoTest {

	@Test
	void grafoTest() {
		Grafo grafo = new Grafo(1);
		
		assertEquals(grafo.tamano(), 1);
	}
	
	@Test
	void verificarDistintosTestTrue() {
		Grafo grafo = new Grafo(1);
		
		assertTrue(grafo.verificarDistintos(0, 1));
	}
	
	@Test
	void verificarDistintosTestFalse() {
		Grafo grafo = new Grafo(1);
		
		assertFalse(grafo.verificarDistintos(0, 0));
	}
	
	@Test
	void verificarVerticeTestTrue() {
		Grafo grafo = new Grafo(1);
		
		assertTrue(grafo.verificarVertice(0));
	}
	
	@Test
	void verificarVerticeTestFalse() {
		Grafo grafo = new Grafo(1);
		
		assertFalse(grafo.verificarVertice(2));
	}
	
	@Test
	void existeAristaTestTrue() {
		Grafo grafo = new Grafo(2);
		grafo.agregarArista(0, 1, 1.0);
		
		assertTrue(grafo.existeArista(0, 1));
	}
	
	@Test
	void existeAristaTestFalse() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, (double) 0);
		
		assertFalse(grafo.existeArista(0, 2));
	}
	
	@Test
	void existeAristaTestFalseCondicion() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, (double) 0);
		
		assertFalse(grafo.existeArista(0, 0));
	}
	
	@Test
	void vecinosTestTrue() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, (double) 0);
		grafo.agregarArista(0, 2, (double) 0);
		
		Set<Integer> vecinos = new HashSet<Integer>();
		vecinos.add(1);
		vecinos.add(2);	
		
		assertEquals(grafo.vecinos(0), vecinos);
	}
	
	@Test
	void vecinosTestFalse() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, (double) 0);
		grafo.agregarArista(0, 2, (double) 0);
		
		assertEquals(grafo.vecinos(3), null);
	}
	
	@Test
	void obtenerPesoTest() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, 10.5);

		assertEquals(grafo.obtenerPeso(0, 1), 10.5);
	}
	
	@Test
	void eliminarAristaTest() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, (double) 0);
		grafo.agregarArista(0, 2, (double) 0);
		
		grafo.eliminarArista(0, 1);
		
		assertFalse(grafo.existeArista(0, 1));
	}
	
	@Test
	void eliminarVeriasAristasMenosTest() {
		Grafo grafo = new Grafo(4);
		grafo.agregarArista(0, 1, (double) 0);
		grafo.agregarArista(0, 2, (double) 0);
		grafo.agregarArista(0, 3, (double) 0);
		boolean[][] marcados = new boolean[4][4];			
		marcados[0][1] = true;
		marcados[1][0] = true;
		
		grafo.eliminarVariasAristasMenos(marcados);
		
		assertTrue(grafo.existeArista(0, 1) && !grafo.existeArista(0, 3));
	}
	
	@Test
	void eliminarPesoMayorAristaTestTrue() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, 0.333);
		grafo.agregarArista(1, 2, 100.0);
		grafo.agregarArista(2, 3, 7.799);
		
		grafo.eliminarAristaMayorVecinos();
		
		assertFalse(grafo.existeArista(0, 1));
	}
	
	@Test
	void eliminarPesoMayorAristaTestFalse() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, 10.0);
		
		grafo.eliminarAristaMayorVecinos();
		
		assertTrue(grafo.existeArista(0, 1));
	}
	
	@Test
	void arbolMinimoPrimTrue() {
		Grafo grafo = new Grafo(3);
		grafo.agregarArista(0, 1, 10.0);
		grafo.agregarArista(0, 2, 0.333);
		grafo.agregarArista(0, 3, 8.0);
		grafo.agregarArista(1, 2, 3.333);
		grafo.agregarArista(1, 3, 6.66);
		grafo.agregarArista(2, 3, 0.001);
		
		grafo.arbolMinimoPrim();
		
		assertFalse(grafo.existeArista(0, 1));
	}
	
	@Test
	void arbolMinimoPrimFalse() {
		Grafo grafo = new Grafo(2);
		grafo.agregarArista(0, 1, 10.0);
		grafo.agregarArista(0, 2, 0.333);
		
		grafo.arbolMinimoPrim();
		
		assertTrue(grafo.existeArista(0, 1));
	}
	
	@Test
	void resultadoMatriz() {
		Grafo grafo = new Grafo(2);
		grafo.agregarArista(0, 1, 10.0);
		
		assertEquals(grafo.resultadoMatriz(), "Punto: 1 al Punto: 2 Distancia de: 10.0\n"
				+ "Punto: 2 al Punto: 1 Distancia de: 10.0\n");		
	}
	
	@Test
	void resultadoArbolMinimo() {
		Grafo grafo = new Grafo(1);
		grafo.agregarArista(0, 1, 10.0);
		
		grafo.arbolMinimoPrim();
		
		assertEquals(grafo.resultadoArbolMinimo(), "");		
	}
}
