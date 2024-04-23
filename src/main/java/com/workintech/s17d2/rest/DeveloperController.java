package com.workintech.s17d2.rest;


import com.workintech.s17d2.model.Developer;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController

public class DeveloperController {

    public Map<Integer, Developer> developers;

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
    }


    private Taxable taxable;

    @Autowired
    public DeveloperController(Taxable tax) {
        this.taxable = tax;
    }

    @GetMapping("/developers")
    public List<Developer> getAllDevelopers() {
        List<Developer> developerList = new ArrayList<>();
        for (Developer developer : developers.values()) {
            developerList.add(developer);
        }
        return developerList;
    }

    @GetMapping("/developers/{id}")
    public Developer getDeveloperById(@PathVariable int id) {
        return developers.values().stream()
                .filter(developer -> developer.getId() == id)
                .findFirst()
                .orElse(null);
    }


    @PostMapping("/developers")
    @ResponseStatus(HttpStatus.CREATED)
    public Developer addDeveloper(@RequestBody Developer developer) {
        double salary = developer.getSalary();
        switch (developer.getExperience()) {
            case JUNIOR:
                salary -= salary * taxable.getSimpleTaxRate() / 100;
                developer.setSalary(salary);
                break;
            case MID:
                salary -= salary * taxable.getMiddleTaxRate() / 100;
                developer.setSalary(salary);
                break;
            case SENIOR:
                salary -= salary * taxable.getUpperTaxRate() / 100;
                developer.setSalary(salary);
                break;
        }
        developers.put(developer.getId(), developer);
        return developer;
    }

    @PutMapping("/developers/{id}")
    public String updateDeveloper(@PathVariable int id, @RequestBody Developer developer) {
        if (developers.containsKey(id)) {
            developers.put(id, developer);
            return "Developer updated successfully";
        } else {
            return "Developer not found";
        }
    }

    @DeleteMapping("/developers/{id}")
    public String deleteDeveloper(@PathVariable int id) {
        if (developers.containsKey(id)) {
            developers.remove(id);
            return "Developer deleted successfully";
        } else {
            return "Developer not found";
        }
    }

}
