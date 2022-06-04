package tn.esprit.spring.exceptions;

public class CoursesLimitReached extends Exception {
	public CoursesLimitReached(String message) {
		super(message);
	}
}