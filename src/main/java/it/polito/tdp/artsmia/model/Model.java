package it.polito.tdp.artsmia.model;

import java.util.HashMap;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {
	
	private Graph<ArtObject, DefaultWeightedEdge> grafo;
	private Map<Integer, ArtObject> idMap;
	
	public Model() {
		this.idMap = new HashMap<>();
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		//Aggiungere i vertici
		ArtsmiaDAO dao = new ArtsmiaDAO();
		dao.listObjects(idMap);
		
		Graphs.addAllVertices(grafo, idMap.values());
		
		//Aaggiungere gli archi
		//Approccio 1: doppio ciclo for sui vertici e quindi dati due vertici controllo se sono collegati
		
		/*
		for(ArtObject a1 : this.grafo.vertexSet()) {
			for(ArtObject a2 : this.grafo.vertexSet()) {
				//Devo collegare a1 con a2? se si' li collego con il determinato peso
				//controllo che non esista gia' l'arco.. tanto non e' orientato
				
				int peso = dao.getPeso(a1, a2);
				if(peso>0)
					if(!this.grafo.containsEdge(a1, a2))
						Graphs.addEdge(this.grafo, a1, a2, peso);
				
			}
		}
		*/
		
		//Mi faccio dare dal DB direttamente tutte le adiacenze
		for(Adiacenza a : dao.getAdiacenze())
			if(a.getPeso()>0) 
				Graphs.addEdge(this.grafo, this.idMap.get(a.getObj1()), this.idMap.get(a.getObj2()), a.getPeso());
			
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
}
