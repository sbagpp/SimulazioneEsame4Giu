package it.polito.tdp.imdb.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	private ImdbDAO dao;
	private Map<Integer, Actor> idMap;
	private SimpleDirectedWeightedGraph <Actor, DefaultWeightedEdge> grafo;
	
	public Model() {
		this.dao = new ImdbDAO();
		this.idMap = new HashMap<>();
		this.dao.loadIdMap(this.idMap);
	}
	
	public List<String> getGenere() {
		return this.dao.getGenere();
	}
	
	public void creaGrafo(String genere) {
		this.grafo = new  SimpleDirectedWeightedGraph <> (DefaultWeightedEdge.class);
		List<Actor> vertex = this.dao.getVertex(this.idMap, genere);
		Graphs.addAllVertices(this.grafo, vertex);
		
	}

}
