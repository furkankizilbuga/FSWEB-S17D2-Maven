package com.workintech.s17d2.rest;

import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.DeveloperTax;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    public Map<Integer, Developer> developers;
    private DeveloperTax taxable;

    @PostConstruct
    public void init() {
        developers = new HashMap<>();
        developers.put(1, new JuniorDeveloper(1, "furkan", 100));
    }

    @Autowired
    public DeveloperController(DeveloperTax taxable) {
        this.taxable = taxable;
    }

    @GetMapping
    public List<Developer> getAllDevelopers() {
        return developers.values().stream().toList();
    }

    @GetMapping("/{id}")
    public Developer getDeveloper(@PathVariable("id") int id) {
        if(developers.get(id) == null) {
            return null;
        } else {
            return developers.get(id);
        }
    }

    @PostMapping
    public void createDeveloper(@RequestBody Developer developer) {
        if(developer.getExperience().equals(Experience.JUNIOR)) {
            Developer junior = new JuniorDeveloper(developer.getId(), developer.getName(), developer.getSalary() - developer.getSalary() * taxable.getSimpleTaxRate()/100);
            developers.put(developer.getId(), junior);
        } else if(developer.getExperience().equals(Experience.MID)) {
            Developer mid = new MidDeveloper(developer.getId(), developer.getName(), developer.getSalary() - developer.getSalary() * taxable.getMiddleTaxRate()/100);
            developers.put(developer.getId(), mid);
        } else if(developer.getExperience().equals(Experience.SENIOR)) {
            Developer senior = new SeniorDeveloper(developer.getId(), developer.getName(), developer.getSalary() - developer.getSalary() * taxable.getUpperTaxRate()/100);
            developers.put(developer.getId(), senior);
        } else {
            System.out.println("İşlem başarısız.");
        }
    }

    @PutMapping("/{id}")
    public void updateDeveloper(@PathVariable("id") int id, @RequestBody Developer developer) {
        developer.setId(id);
        developers.replace(id, developer);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        developers.remove(id);
    }

}
