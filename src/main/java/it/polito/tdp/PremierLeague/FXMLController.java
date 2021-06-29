/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.PremierLeague;

import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.PremierLeague.model.Adiacenze;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Mesi;
import it.polito.tdp.PremierLeague.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnConnessioneMassima"
    private Button btnConnessioneMassima; // Value injected by FXMLLoader

    @FXML // fx:id="btnCollegamento"
    private Button btnCollegamento; // Value injected by FXMLLoader

    @FXML // fx:id="txtMinuti"
    private TextField txtMinuti; // Value injected by FXMLLoader

    @FXML // fx:id="cmbMese"
    private ComboBox<Mesi> cmbMese; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM1"
    private ComboBox<Match> cmbM1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbM2"
    private ComboBox<Match> cmbM2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doConnessioneMassima(ActionEvent event) {
    	this.txtResult.clear();
    	
    	if(this.model.getGrafo()==false) {
    		this.txtResult.setText("Creare prima il grafo");
    		return;
    	}
    	
    	List<Adiacenze> list= this.model.connessioneMassima();
    	
    	for(Adiacenze a:list) {
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	this.txtResult.clear();
    	
    	int min;
    	try {
    		min=Integer.parseInt(txtMinuti.getText());
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un numero intero");
    		return;
    	}
    	
    	Mesi mese=this.cmbMese.getValue();
    	
    	if(mese==null) {
    		this.txtResult.setText("Inserire un mese");
    		return;
    	}
    	
    	this.model.creaGrafo(min,mese);
    	
    	this.cmbM1.getItems().addAll(this.model.getVertici());
    	this.cmbM2.getItems().addAll(this.model.getVertici());
    
    	this.txtResult.setText("GRAFO CREATO: \n"+"# vertici: "+this.model.nVertici()+"\n"+"# archi: "+this.model.nArchi());
    }

    @FXML
    void doCollegamento(ActionEvent event) {
    	this.txtResult.clear();
    	
    	Match m1=this.cmbM1.getValue();
    	Match m2=this.cmbM2.getValue();
    	
    	if(m1==null || m2==null) {
    		this.txtResult.setText("Selezionare due match");
    		return;
    	}
    	
    	if(m1.getMatchID().equals(m2.getMatchID())) {
    		this.txtResult.setText("Selezionare due match diversi");
    		return;
    	}
    	
    	List<Match> result=this.model.collegamento(m1,m2);
    	this.txtResult.appendText("Cammino massimo con peso "+this.model.getPesoMax()+"\n");
    	for(Match m:result) {
    		this.txtResult.appendText(m.toString()+"\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnConnessioneMassima != null : "fx:id=\"btnConnessioneMassima\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCollegamento != null : "fx:id=\"btnCollegamento\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMinuti != null : "fx:id=\"txtMinuti\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbMese != null : "fx:id=\"cmbMese\" was not injected: check your FXML file 'Scene.fxml'.";        assert cmbM1 != null : "fx:id=\"cmbM1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbM2 != null : "fx:id=\"cmbM2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
  
    	List<Mesi> mesi=new LinkedList<Mesi>();
    	mesi.add(new Mesi(1,"Gennaio"));
    	mesi.add(new Mesi(2,"Febbraio"));
    	mesi.add(new Mesi(3,"Marzo"));
    	mesi.add(new Mesi(4,"Aprile"));
    	mesi.add(new Mesi(5,"Maggio"));
    	mesi.add(new Mesi(6,"Giugno"));
    	mesi.add(new Mesi(7,"Luglio"));
    	mesi.add(new Mesi(8,"Agosto"));
    	mesi.add(new Mesi(9,"Settembre"));
    	mesi.add(new Mesi(10,"Ottobre"));
    	mesi.add(new Mesi(11,"Novembre"));
    	mesi.add(new Mesi(12,"Dicembre"));
    	
    	this.cmbMese.getItems().addAll(mesi);
    }
    
    
}
