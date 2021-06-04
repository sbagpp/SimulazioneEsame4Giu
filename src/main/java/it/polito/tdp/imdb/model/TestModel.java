package it.polito.tdp.imdb.model;

public class TestModel {

	public static void main(String[] args) {
			Model m = new Model();
			for(String s : m.getGenere())
				System.out.println(s);
	}

}
