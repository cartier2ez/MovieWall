import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class MovieWall {

	/**
	 * Holds the path to the csv file provided at the command line
	 */
	// declared static so that any class can access its value by calling using the
	// class name.
	private static String path;

	/*
	 * Map is chosen because it makes insertion and retrieval easier which forms
	 * major part of this MovieWall Map has a time complexity of O(1) It holds the
	 * names of film makers as key and list of names of movies that film maker is
	 * featured in
	 */
	private static Map<String, List<String>> movieFilMakerMap = new HashMap<>();

	/**
	 * object for reading user input from console
	 */
	private static Scanner sc = new Scanner(System.in);

	public static void main(String[] args) {

		if (args.length != 1) {
			System.out.println("You must provide the path of the csv file as the only ");
		} else {
			path = args[0];
		}

		boolean quit = false;

		System.out.println("Welcome to the Movie Wall!\n");
		while (!quit) {

			System.out.println("Enter the name of an actor (or \"EXIT\" to quit):");
			String actorName = sc.nextLine();

			if (actorName.equals("EXIT"))
				quit = true;
			displayWallForActor(actorName);

		}
	}

	/**
	 * Displays films an actor has feature in
	 * 
	 * @param actorName the name of the actor to display films that they have
	 *                  featured.
	 */
	private static void displayWallForActor(String actorName) {

		// one line read from csv contains data of a movie wall
		String line = "";

		// sets comma a delimiter for splitting string to different components
		String splitBy = ",";
		try {
			// parsing a CSV file into BufferedReader class constructor
			BufferedReader movieWallBufferedReader = new BufferedReader(new FileReader(path));

			// keeps track of the number of lines read
			// to avoid reading the header part
			int count = 0;

			// variables to hold data derived from strings read
			String movieName = "";// holds name of the movie

			while ((line = movieWallBufferedReader.readLine()) != null) {
				if (count != 0) {

					String[] movieWall = line.split(splitBy);

					// obtain the name of the movie
				
					movieName = movieWall[1].toUpperCase();

					if (Arrays.toString(movieWall).toUpperCase().contains(actorName.toUpperCase())) {

						// create a new cast
						FilmMaker filmMaker = new Cast(actorName);
						updateFilMakerMovies(filmMaker, movieName, actorName);

					}
				}
				count++;
			}
			// close the buffered reader to relieve the system of resources.
			movieWallBufferedReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// display information of the actor
		if (movieFilMakerMap.containsKey(actorName)) {
			System.out.println("Actor: " + actorName);
			List<String> movies = movieFilMakerMap.get(actorName);

			for (String movie : movies) {
				System.out.println("* Movie: " + movie);
			}
		} else {// look for actor that has almost similar name

			// two strings that completely don't match has a matching point of zero
			int matchingPoints = 0;
			String alternativeName = "Morgan Freeman";// a placeholder name in case we don't find alternative

			for (Map.Entry<String, List<String>> entry : movieFilMakerMap.entrySet()) {

				String actorInStorage = entry.getKey();
				int matchExtend = countNumberOfMatchingCharacters(actorName.toUpperCase(),
						actorInStorage.toUpperCase());

				if (matchExtend > matchingPoints) {
					alternativeName = actorInStorage;
					matchingPoints++;

				}

			}
			System.out.println("No such actor. Did you mean \"" + alternativeName + "\"" + " (Y/N): Y)");
			String userChoice = sc.nextLine();
			if (userChoice.equalsIgnoreCase("Y")) {
				displayWallForActor(alternativeName);
			}

		}
	}

	/**
	 * Helper function to update film maker movies
	 * 
	 * @param filmMaker filmaker that hold information to be searched if it exists
	 * @param movieName the name of movie to be added to fil maker list of movies
	 * @param actorName the name of the actor to be updated list of movies
	 */
	private static void updateFilMakerMovies(FilmMaker filmMaker, String movieName, String actorName) {

		if (movieFilMakerMap.containsKey(filmMaker.getName())) {

			// if actor exists update his/her movies she participates
			List<String> actorMovies = movieFilMakerMap.get(filmMaker.getName());

			// only add a movie if not already included
			if (!actorMovies.contains(movieName)) {
				actorMovies.add(movieName);

				// remove the previous data
				movieFilMakerMap.remove(actorName);

				// add the new updated data
				movieFilMakerMap.put(actorName, actorMovies);
			}
		} else {
			// no data exists prior
			List<String> movies = new ArrayList<String>();
			movies.add(movieName);
			movieFilMakerMap.put(actorName, movies);
		}
	}

	/**
	 * A helper method to help check number of matching characters in two strings
	 * Required for determining closer matching strings in case a user makes a
	 * typing mistake of an actor while searching in the database
	 * 
	 * @param actorName1 the first string to be checked
	 * @param actorName2 the second string to be checked
	 */
	private static int countNumberOfMatchingCharacters(String actorName1, String actorName2) {
		int x = 0;
		int z = 0;

		for (int i = 0; i < actorName1.length(); i++) {
			if (actorName2.indexOf(actorName1.charAt(i)) >= 0) {
				x += 1;
			}
		}
		return x;
	}
}
