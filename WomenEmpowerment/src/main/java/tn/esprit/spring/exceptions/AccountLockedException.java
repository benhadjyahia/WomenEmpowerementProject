package tn.esprit.spring.exceptions;

public class AccountLockedException extends Exception{
    public AccountLockedException(String message) {

        super(message);
    }

}
