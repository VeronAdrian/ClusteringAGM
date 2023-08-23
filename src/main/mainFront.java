package main;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.openstreetmap.gui.jmapviewer.Coordinate;
import org.openstreetmap.gui.jmapviewer.JMapViewer;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;
import org.openstreetmap.gui.jmapviewer.MapPolygonImpl;
import org.openstreetmap.gui.jmapviewer.interfaces.ICoordinate;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dialog.ModalExclusionType;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JFileChooser;
import java.awt.Insets;
import javax.swing.JMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.Button;
import javax.swing.JSeparator;
import java.awt.Choice;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class mainFront {
	private JFrame frame;
	private JMapViewer mapa;
	private ArrayList <Coordinate> coordenadas;
	private Clustering cluster;
	private JTextField textCant;

	/**
	 * Launch the application.
	 */
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					mainFront window = new mainFront();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public mainFront() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frame.setResizable(false);
		frame.setBounds(100, 100, 900, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Mapa");
		
		coordenadas = new ArrayList<Coordinate>();
		
		//						Agregar un punto
		Coordinate coordenada = new Coordinate(-34.521, -58.719);
		
		mapa = new JMapViewer();
		mapa.setZoomControlsVisible(false);
		mapa.setZoomContolsVisible(false);
		mapa.setDisplayPosition(coordenada, 14);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		frame.getContentPane().add(mapa);
		mapa.setLayout(null);
		
		JPanel panelInfo = new JPanel();
		panelInfo.setBounds(620, 0, 264, 546);
		mapa.add(panelInfo);
		panelInfo.setLayout(null);
		
		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelInfo.setVisible(false);
			}
		});
		btnCerrar.setBounds(90, 512, 89, 23);
		panelInfo.add(btnCerrar);
		
		JTextArea textInformacion = new JTextArea();
		textInformacion.setEditable(false);
		textInformacion.setBounds(10, 11, 244, 490);
		panelInfo.add(textInformacion);
		panelInfo.setVisible(false);
		
		JScrollPane scroll = new JScrollPane(textInformacion, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setBounds(10, 11, 244, 490);
		panelInfo.add(scroll);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setMargin(new Insets(20, 20, 20, 20));
		frame.setJMenuBar(menuBar);
		
		JMenu mnArchivo = new JMenu("Archivo\r\n");
		menuBar.add(mnArchivo);
		
		JMenuItem mntmNuevo = new JMenuItem("Nuevo");
		mntmNuevo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {				
				coordenadas = new ArrayList<Coordinate>();
				mapa.removeAllMapMarkers();
				mapa.removeAllMapPolygons();
				cluster = new Clustering(coordenadas.size(), coordenadas);
				panelInfo.setVisible(false);
			}
		});
		mnArchivo.add(mntmNuevo);
		
		JMenuItem mntmAbrir = new JMenuItem("Abrir");
		mntmAbrir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				panelInfo.setVisible(false);
				abrirArchivo();
			}
		});
		mnArchivo.add(mntmAbrir);
		
		JMenuItem mntmGuardar = new JMenuItem("Guardar");
		mntmGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				guardarArchivo();
			}
		});
		mnArchivo.add(mntmGuardar);
		
		JSeparator separator = new JSeparator();
		mnArchivo.add(separator);
		
		JMenuItem mntmSalir = new JMenuItem("Salir");
		mntmSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mnArchivo.add(mntmSalir);
		
		Button buttonCluster = new Button("                    Crear Cluster                    ");
		buttonCluster.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int cant = Integer.parseInt(textCant.getText());
				
				cluster.generarClusters(cant);				
				dibujarLineasGrafo(mapa);
				buttonCluster.setEnabled(false);
			}
		});
		buttonCluster.setEnabled(false);
		
		
		Button buttonEjecutarArbolMinimo = new Button("         Generar Arbol Minimo         ");
		buttonEjecutarArbolMinimo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {	
				cluster = new Clustering(coordenadas.size(), coordenadas);
				cluster.crearGrafo();	
				cluster.arbolGeneradorMinimo();
				
				dibujarLineasGrafo(mapa);
				buttonCluster.setEnabled(true);
			}
		});
		menuBar.add(buttonEjecutarArbolMinimo);
		
		JLabel lblCantCluster = new JLabel("Cantidad de Clusters");
		menuBar.add(lblCantCluster);
		
		textCant = new JTextField();
		textCant.setText("1");
		menuBar.add(textCant);
		textCant.setColumns(1);
		menuBar.add(buttonCluster);
		
		Button buttonMostrarInformacion = new Button("                    Mostrar Informacion                    ");
		buttonMostrarInformacion.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cluster!=null) {
					panelInfo.setVisible(true);	
					textInformacion.setText("---Informacion inicial: ---\n"+cluster.resultadoMatriz()+"\n"
					+"---Arbol Minimo: ---\n"+cluster.resultadoArbolMinimo());
				}
				else {
					panelInfo.setVisible(true);	
					textInformacion.setText("\n             Para mostrar informacion \n         se debe crear un Arbol Minimo");
				}
				}
		});
		menuBar.add(buttonMostrarInformacion);
		
		mapa.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked (MouseEvent e) {
				panelInfo.setVisible(false);	
				ICoordinate punto = mapa.getPosition(e.getPoint());
				colocarPuntoMapa(punto.getLat(), punto.getLon());
			}
		});
	}
	
	private void colocarPuntoMapa(double lat, double lon) {
		coordenadas.add(new Coordinate(lat, lon));
		MapMarkerDot coordenada = new MapMarkerDot("Punto "+coordenadas.size(), coordenadas.get(coordenadas.size()-1));
		coordenada.getStyle().setBackColor(Color.red);
		coordenada.getStyle().setColor(Color.GREEN.brighter());
		mapa.addMapMarker(coordenada);
		dibujarLineasCoordenadas(mapa);		
	}
	
	private void dibujarLineasCoordenadas(JMapViewer mapa) {
		mapa.removeAllMapPolygons();
		if(!(coordenadas.size()<2)) {
			for(int i=0; i<coordenadas.size(); i++) {
				for(int j=0; j<coordenadas.size(); j++) {
					mapa.addMapPolygon(new MapPolygonImpl(coordenadas.get(i),coordenadas.get(j),coordenadas.get(j)));
				}
			}
		}
	}
	
	private void dibujarLineasGrafo(JMapViewer mapa) {
		mapa.removeAllMapPolygons();
		if(!(coordenadas.size()<2)) {
			for(int i=0; i<coordenadas.size(); i++) {
				for(int j=0; j<coordenadas.size(); j++) {
					if(cluster.conectado(coordenadas.get(i), coordenadas.get(j))) {
						mapa.addMapPolygon(new MapPolygonImpl(coordenadas.get(i),coordenadas.get(j),coordenadas.get(j)));
					}
				}
			}
		}
	}
	
	private void abrirArchivo() {
		String ruta = "";
		JFileChooser jf = new JFileChooser("saves/");
		FileNameExtensionFilter valido = new FileNameExtensionFilter(".txt", "txt");
		jf.setFileFilter(valido);		
		jf.showOpenDialog(jf);
		File archivo = jf.getSelectedFile();
		if(archivo != null) {
			ruta = archivo.getAbsolutePath();
			try {
				archivo = new File (ruta);
				String linea;
				FileReader fr = new FileReader (archivo);
				BufferedReader br = new BufferedReader(fr);
				while((linea=br.readLine())!=null){
					if(!(linea.indexOf(" ")<0)) {	
						colocarPuntoMapa(stringToDouble(linea.substring(0, linea.indexOf(" "))), stringToDouble(linea.substring(linea.indexOf(" ")+1, linea.length())));
					}
				}
			}
			catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				
			}
		}
	}
	
	private double stringToDouble(String string) {
		return Double.parseDouble(string);		
	}
	
	private void guardarArchivo() {
		StringBuilder string = new StringBuilder();
		int contador = 1;
		String nombreArchivoContado = string.append("instancia"+contador).toString();
		try {
			File f = new File("saves/"+ nombreArchivoContado + ".txt");
			while(f.exists()) {
				string = new StringBuilder();
				contador++;
				nombreArchivoContado = string.append("instancia"+contador).toString();
				f = new File("saves/"+ nombreArchivoContado + ".txt");
			}			
				BufferedWriter bw=new BufferedWriter(new FileWriter("saves/"+nombreArchivoContado+".txt"));
				escribirTXT(bw);
				if(bw!=null) {
					bw.close();
			 }
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void escribirTXT(BufferedWriter bw) {
		for(Coordinate coor: coordenadas) {
			try {
				bw.write(coor.getLat()+" "+coor.getLon()+"\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
