// Created by Ilan Godik

package Main;

import Interfaces.IExercise;
import Util.ExerciseComparator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeSet;

import static Util.Util.*;

public class Main {
	private static Main instance;
	private ArrayList<IExercise> exercises = new ArrayList<IExercise>();
	private HashSet<String> ignore = new HashSet<String>();

	public static void main(String[] args) {
		instance = new Main();
	}

	public Main() {
		load();
		sort();
		menu();
	}

	private void menu() {
		pl("These are my exercises:");
		IExercise currentExercise;
		for (int i = 0; i < exercises.size(); i++) {
			currentExercise = exercises.get(i);
			pl((i + 1) + ":   " + currentExercise.getChapterNumber() + "." + currentExercise.getExerciseNumber() + ":  " + currentExercise.getName());
		}
		boolean validSelection = false;
		p("Please enter your selection: ");
		while (!validSelection) {
			int selection = in.nextInt();
			if (selection <= exercises.size() && selection > 0) {
				pl("");
				exercises.get(selection - 1).run();
				validSelection = true;
			} else {
				p("Please enter a valid selection: ");
			}
		}
	}

	private void load() {
		String packageName = "Exercises";
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		ArrayList<String> names = new ArrayList<String>();
		URL packageURL = classLoader.getResource(packageName);
		if (packageURL != null) {
			File folder = new File(packageURL.getFile().replaceAll("%20", " "));
			File[] files = folder.listFiles();
			String entryName;

			if (files != null) {
				for (File actual : files) {
					entryName = actual.getName();
					if (!ignore.contains(entryName) && !actual.isDirectory()) {
						entryName = entryName.substring(0, entryName.lastIndexOf('.'));
						names.add(entryName);
					}
				}

				for (String s : names) {
					try {
						add((IExercise) Class.forName("Exercises." + s).newInstance());
					} catch (ClassNotFoundException e) {
						pl("Class not found.");
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			} else {
				pl("\"files\" is null! Please add some class files for the launcher to work.");
				System.exit(0);
			}
		} else {
			pl("packageURL is null! Aborting!");
			System.exit(1);
		}
	}

	public void addToIgnoreList(String s) {
		ignore.add(s);
	}

	private void add(IExercise exercise) {
		exercises.add(exercise);
	}

	public void sort() {
		TreeSet<IExercise> sortedExercises = new TreeSet<IExercise>(new ExerciseComparator());
		sortedExercises.addAll(exercises);
		exercises.clear();
		exercises.addAll(sortedExercises);
	}

	public static Main getInstance() {
		return instance;
	}

}
