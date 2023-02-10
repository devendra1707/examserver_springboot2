package com.exam.helper;

public class UserNotFoundException extends Exception {

	public UserNotFoundException() {
	
	super("User with this Username is already there in DB !! try with another ...");
	}
	public UserNotFoundException(String msg) {super(msg);}
}
