package it.polito.tdp.PremierLeague.model;

import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.PremierLeague.db.PremierLeagueDAO;

public class Model {
	
	private SimpleWeightedGraph<Match,DefaultWeightedEdge> grafo;
	private PremierLeagueDAO dao;
	private Map<Integer,Match> idMap;
	List<Match> percorsoMigliore;
	double pesoMax;
	
	public Model() {
		dao=new PremierLeagueDAO();
		idMap=new HashMap<Integer,Match>();
		
		this.dao.listAllMatches(idMap);
	}

	public void creaGrafo(int min, Mesi mese) {
		grafo=new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, dao.getVertici(idMap, mese.getNum()));
		
		for(Adiacenze a:this.dao.getAdiacenze(idMap, mese.getNum(), min)) {
			if(this.grafo.containsVertex(a.m1) && this.grafo.containsVertex(a.m2)) {
				Graphs.addEdge(grafo, a.getM1(), a.getM2(), a.getPeso());
			}
		}
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}
	
	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public boolean getGrafo() {
		if(grafo==null)
			return false;
		else
			return true;
	}

	public List<Adiacenze> connessioneMassima() {
		List<Adiacenze> migliori=new LinkedList<>();
		Adiacenze coppiaMigliore=null;
		double pesoMigliore=Integer.MIN_VALUE;
		
		for(DefaultWeightedEdge e:this.grafo.edgeSet()) {
			double peso=this.grafo.getEdgeWeight(e);
			if(peso>pesoMigliore) {
				pesoMigliore=peso;
				coppiaMigliore=new Adiacenze(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),peso);
				migliori.clear();
				migliori.add(coppiaMigliore);
			}
			else if(peso==pesoMigliore) {
				migliori.add(new Adiacenze(this.grafo.getEdgeSource(e),this.grafo.getEdgeTarget(e),peso));
			}
		}
		
		return migliori;
	}
	
	public Set<Match> getVertici(){
		return this.grafo.vertexSet();
	}

	public List<Match> collegamento(Match partenza, Match arrivo) {
		percorsoMigliore=new LinkedList<Match>();
		pesoMax=Integer.MIN_VALUE;
		
		List<Match> parziale=new LinkedList<Match>();
		parziale.add(partenza);
		
		cerca(parziale,partenza,arrivo);
		
		return percorsoMigliore;
	}

	private void cerca(List<Match> parziale, Match partenza, Match arrivo) {
		if(parziale.get(parziale.size()-1).equals(arrivo)) {
			double peso=calcolaPeso(parziale);
			if(peso > pesoMax) {
				percorsoMigliore=new LinkedList<>(parziale);
				pesoMax=peso;
			}
			return;
		}
		
		for(Match m:Graphs.neighborListOf(grafo,partenza)) {
			if(!parziale.contains(m)) {
				parziale.add(m);
				cerca(parziale,m,arrivo);
				parziale.remove(parziale.size()-1);
			}
		}
	}

	private Integer calcolaPeso(List<Match> parziale) {
		int peso=0;
		int i=0; //indice che mi serve per prendere il match successivo in parziale
		for (Match m : parziale) {
			if (i==(parziale.size()-1)) 
				break;
			DefaultWeightedEdge e = grafo.getEdge(m, parziale.get(i+1));
			i++;
			peso += grafo.getEdgeWeight(e);
		}
		return peso;
	}
	
	public double getPesoMax() {
		return this.pesoMax;
	}
}
