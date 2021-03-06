package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacente;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<String> getGenere() {
		String sql = "select Distinct(`genre`) as genere "
				+ "from `movies_genres` "
				+ "order by `genre` ";
		List<String> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {				
				result.add(res.getString("genere"));
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void loadIdMap(Map<Integer, Actor> idMap) {
		String sql = "SELECT * FROM actors";
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				idMap.put(actor.getId(),actor);
			}
			conn.close();
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<Actor> getVertex(Map<Integer, Actor> idMap, String genere) {
		String sql = "SELECT Distinct(a.`id` ) as id "
				+ "FROM `actors` as a, `roles` as r, `movies` as m, `movies_genres` as g "
				+ "WHERE g.`genre`= ? and r.`actor_id`=a.`id` and r.`movie_id`=m.`id` and g.`movie_id`=m.`id` ";
		List<Actor> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,  genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Actor a = idMap.get(res.getInt("id"));
				if(a != null) {
					result.add(a);
				}
			}
			conn.close();
			return result;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public List<Adiacente> getAdicenze(Map<Integer, Actor> idMap, String genere) {
		String sql = "SELECT r2.`actor_id` as id1 ,r1.`actor_id` as id2, COUNT(*) as peso "
				+ "FROM `actors` as a1, `roles` as r1, `movies_genres` as g1, `actors` as a2, `roles` as r2, `movies_genres` as g2 "
				+ "WHERE g1.`genre`= ?  "
				+ "		and r1.`actor_id`=a1.`id`  "
				+ "		and r1.`movie_id`= g1.`movie_id` "
				+ "		and g2.`genre`= g1.`genre` "
				+ "		and r2.`actor_id`=a2.`id`  "
				+ "		and r2.`movie_id`= g2.`movie_id` "
				+ "		and r2.`movie_id`=r1.`movie_id` "
				+ "		and r2.`actor_id`>r1.`actor_id` "
				+ "GROUP BY r2.`actor_id`,r1.`actor_id` ";
		List<Adiacente> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1,  genere);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Actor a1 = idMap.get(res.getInt("id1"));
				Actor a2 = idMap.get(res.getInt("id2"));
				if(a1 != null && a2 != null) {
					result.add(new Adiacente(a1, a2, res.getDouble("peso")));
				}
			}
			conn.close();
			return result;
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	}
	
	
	
	
	
	
