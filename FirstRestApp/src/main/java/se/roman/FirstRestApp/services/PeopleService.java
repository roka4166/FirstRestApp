package se.roman.FirstRestApp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.roman.FirstRestApp.models.Person;
import se.roman.FirstRestApp.repositories.PeopleRepository;
import se.roman.FirstRestApp.utils.PersonNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PeopleService {
    private final PeopleRepository peopleRepository;
    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll(){
        return peopleRepository.findAll();
    }

    public Person findOne(int id){
        Optional<Person> person = peopleRepository.findById(id);
        return person.orElseThrow(PersonNotFoundException::new);
    }
    @Transactional
    public void save(Person person){
        peopleRepository.save(enrichPerson(person));
    }

    private Person enrichPerson(Person person){
        person.setCreated_at(LocalDateTime.now());
        person.setUpdated_at(LocalDateTime.now());
        person.setCreated_who("ADMIN");
        return person;
    }

}
