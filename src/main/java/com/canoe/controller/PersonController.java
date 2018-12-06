package com.canoe.controller;

import com.canoe.pojo.Person;
import com.canoe.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @program: data-canoe
 * @author: Canoe
 * @create: 2018-12-03 17:23
 */
@Controller
@RequestMapping("/person")
public class PersonController {

    @Autowired
    private PersonService personService ;

    @RequestMapping("/allPerson")
    public String gesdf(){
        int a = 10 ;
        List<Person> personList = personService.getPerson() ;

        return "allPerson";


    }
}
