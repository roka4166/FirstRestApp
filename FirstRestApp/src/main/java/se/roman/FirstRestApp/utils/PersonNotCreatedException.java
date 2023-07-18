package se.roman.FirstRestApp.utils;

import se.roman.FirstRestApp.models.Person;

public class PersonNotCreatedException extends RuntimeException{
    public PersonNotCreatedException(String msg){
        super(msg);
    }
}
