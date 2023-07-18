package se.roman.FirstRestApp.controllers;

import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import se.roman.FirstRestApp.DTO.PersonDTO;
import se.roman.FirstRestApp.models.Person;
import se.roman.FirstRestApp.services.PeopleService;
import se.roman.FirstRestApp.utils.PersonErrorResponse;
import se.roman.FirstRestApp.utils.PersonNotCreatedException;
import se.roman.FirstRestApp.utils.PersonNotFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final ModelMapper modelMapper;
    @Autowired
    public PeopleController(PeopleService peopleService, ModelMapper modelMapper) {
        this.peopleService = peopleService;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<PersonDTO> findAll(){
        return peopleService.findAll().stream().map(this::convertToPersonDTO).collect(Collectors.toList());
    }
    @GetMapping("/{id}")
    public PersonDTO findPerson(@PathVariable int id){
        return convertToPersonDTO(peopleService.findOne(id));
    }

    @PostMapping()
    public ResponseEntity<HttpStatus> create(@RequestBody @Valid PersonDTO personDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            for (FieldError fieldError : fieldErrors){
                errorMessage.append(fieldError.getField()).append(" -- ").append(fieldError.getDefaultMessage())
                        .append(": ");
            }
            throw new PersonNotCreatedException(errorMessage.toString());
        }

        peopleService.save(convertToPerson(personDTO));
        return ResponseEntity.ok(HttpStatus.OK);
    }

    private Person convertToPerson(PersonDTO personDTO){
        return modelMapper.map(personDTO, Person.class);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotFoundException personNotFoundException){
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(
                "Person with this id was not found", System.currentTimeMillis()
        );
        return new ResponseEntity<>(personErrorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<PersonErrorResponse> handleException(PersonNotCreatedException personNotCreatedException){
        PersonErrorResponse personErrorResponse = new PersonErrorResponse(
                personNotCreatedException.getMessage(), System.currentTimeMillis()
        );
        return new ResponseEntity<>(personErrorResponse, HttpStatus.BAD_REQUEST);
    }

    private PersonDTO convertToPersonDTO(Person person){
        return modelMapper.map(person, PersonDTO.class);
    }
}
