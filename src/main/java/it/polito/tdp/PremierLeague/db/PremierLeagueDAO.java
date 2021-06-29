package it.polito.tdp.PremierLeague.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.PremierLeague.model.Action;
import it.polito.tdp.PremierLeague.model.Adiacenze;
import it.polito.tdp.PremierLeague.model.Match;
import it.polito.tdp.PremierLeague.model.Player;

public class PremierLeagueDAO {
	
	public List<Player> listAllPlayers(){
		String sql = "SELECT * FROM Players";
		List<Player> result = new ArrayList<Player>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Player player = new Player(res.getInt("PlayerID"), res.getString("Name"));
				
				result.add(player);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Action> listAllActions(){
		String sql = "SELECT * FROM Actions";
		List<Action> result = new ArrayList<Action>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Action action = new Action(res.getInt("PlayerID"),res.getInt("MatchID"),res.getInt("TeamID"),res.getInt("Starts"),res.getInt("Goals"),
						res.getInt("TimePlayed"),res.getInt("RedCards"),res.getInt("YellowCards"),res.getInt("TotalSuccessfulPassesAll"),res.getInt("totalUnsuccessfulPassesAll"),
						res.getInt("Assists"),res.getInt("TotalFoulsConceded"),res.getInt("Offsides"));
				
				result.add(action);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void listAllMatches(Map<Integer,Match> idMap){
		String sql = "SELECT m.MatchID, m.TeamHomeID, m.TeamAwayID, m.teamHomeFormation, m.teamAwayFormation, m.resultOfTeamHome, m.date, t1.Name, t2.Name   "
				+ "FROM Matches m, Teams t1, Teams t2 "
				+ "WHERE m.TeamHomeID = t1.TeamID AND m.TeamAwayID = t2.TeamID";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(!idMap.containsKey(res.getInt("m.MatchID"))) {
					Match match = new Match(res.getInt("m.MatchID"), res.getInt("m.TeamHomeID"), res.getInt("m.TeamAwayID"), res.getInt("m.teamHomeFormation"), 
							res.getInt("m.teamAwayFormation"),res.getInt("m.resultOfTeamHome"), res.getTimestamp("m.date").toLocalDateTime(), res.getString("t1.Name"),res.getString("t2.Name"));
					idMap.put(match.getMatchID(), match);
				}
			}
			conn.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Match> getVertici(Map<Integer,Match> idMap, int mese){
		String sql="SELECT m.MatchID FROM matches m WHERE MONTH(m.Date)=?";
		Connection conn = DBConnect.getConnection();
		List<Match> result=new ArrayList<>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, mese);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("m.MatchID"))) {
					result.add(idMap.get(res.getInt("m.MatchID")));
				}
			}
			conn.close();
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Adiacenze> getAdiacenze(Map<Integer,Match> idMap, int mese, int min){
		String sql="SELECT m1.MatchID as id1, m2.MatchID AS id2,COUNT(DISTINCT (a1.PlayerID)) AS peso "
				+ "FROM matches m1, matches m2, actions a1, actions a2 "
				+ "WHERE a1.MatchID=m1.MatchID && a2.MatchID=m2.MatchID && a1.PlayerID=a2.PlayerID && m1.MatchID>m2.MatchID "
				+ "     && a1.TimePlayed>=? && a2.TimePlayed>=? && MONTH(m1.Date)=? && MONTH(m2.Date)=MONTH(m1.Date) "
				+ "GROUP BY m1.MatchID, m2.MatchID";
		Connection conn = DBConnect.getConnection();
		List<Adiacenze> result=new ArrayList<>();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, min);
			st.setInt(2, min);
			st.setInt(3, mese);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(idMap.containsKey(res.getInt("id1")) && idMap.containsKey(res.getInt("id2"))) {
					Adiacenze a=new Adiacenze(idMap.get(res.getInt("id1")), idMap.get(res.getInt("id2")),res.getDouble("peso"));
					result.add(a);
				}
			}
			conn.close();
			
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
