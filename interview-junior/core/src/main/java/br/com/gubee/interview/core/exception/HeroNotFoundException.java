package br.com.gubee.interview.core.exception;

public class HeroNotFoundException extends RuntimeException {
    public HeroNotFoundException(String s) {
        super(s);
    }
}
