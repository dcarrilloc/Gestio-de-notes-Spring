package com.esliceu.controllers;

import com.esliceu.services.NoteService;
import com.esliceu.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class Controller1 {
    @Autowired
    NoteService noteService;

    @Autowired
    UserService userService;

    @GetMapping("/hello")
    @ResponseBody
    public String hello() {
        return "Hello Spring!";
    }
    /*

    @GetMapping("/persons")
    @ResponseBody
    public String getPersons() {
        List<Person> persons = personRepo.findAll();
        return persons.toString();
    }

    @GetMapping("/jobperson")
    @ResponseBody
    public String jobPerson(@RequestParam Long idPersona) {
        Person person = personRepo.findById(idPersona).get();
        Job job = person.getJob();
        return job.toString();
    }

    @GetMapping("/personsjob")
    @ResponseBody
    public String personsJob(@RequestParam Long idJob) {
        Job job = jobRepo.findById(idJob).get();
        return job.getPersons().toString();
    }

    @PostMapping("/newperson")
    @ResponseBody
    @Transactional
    public String newPerson(@RequestParam String firstname, @RequestParam String lastname, @RequestParam String birthday ) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dt = LocalDate.parse(birthday, dtf);
        Person p = new Person();
        p.setBirthday(dt);
        p.setFirstname(firstname);
        p.setLastname(lastname);
        Person insertedPerson = personRepo.save(p);
        return insertedPerson.toString();
    }

    @PostMapping("/personchangejob")
    @ResponseBody
    @Transactional
    public String personChangeJob(@RequestParam Long idPerson, @RequestParam Long idJob) {
        Person person = personRepo.findById(idPerson).get();
        Job job = jobRepo.findById(idJob).get();
        person.setJob(job);
        Person updatedPerson = personRepo.save(person);
        return updatedPerson.toString();
    }
*/
}
