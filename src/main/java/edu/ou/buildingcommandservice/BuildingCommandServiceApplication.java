package edu.ou.buildingcommandservice;

import edu.ou.coreservice.annotation.BaseCommandAnnotation;
import org.springframework.boot.SpringApplication;

@BaseCommandAnnotation
public class BuildingCommandServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(BuildingCommandServiceApplication.class, args);
    }

}
