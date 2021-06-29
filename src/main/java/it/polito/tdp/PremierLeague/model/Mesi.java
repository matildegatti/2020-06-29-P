package it.polito.tdp.PremierLeague.model;

public class Mesi {
	 int num;
	 String mese;
	public Mesi(int num, String mese) {
		super();
		this.num = num;
		this.mese = mese;
	}
	@Override
	public String toString() {
		return mese;
	}
	public int getNum() {
		return num;
	}
	public String getMese() {
		return mese;
	}
	
}
