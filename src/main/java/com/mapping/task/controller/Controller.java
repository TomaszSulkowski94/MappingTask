package com.mapping.task.controller;

import com.mapping.task.service.MappingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/map")
@Tag(name = "Controller", description = "Mapping task controller")
public class Controller {

    private final MappingService mappingFileService;

    @Operation(
            summary = "Map numbers",
            description = "Mapping numbers based on provided category and number list"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    description = "OK",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                             {
                                                 "1": [
                                                     "Mazda"
                                                 ],
                                                 "2": [
                                                     "Mazda",
                                                     "BMW"
                                                 ],
                                                 "4": [
                                                     "Mazda",
                                                     "BMW",
                                                     "Toyota"
                                                 ]
                                             }
                                            """)
                    )),
            //localhost:8080/map?category=Car&numberLists=1,2,4 -> query example 200
            @ApiResponse(
                    responseCode = "400",
                    description = "Internal Server Error",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = "Header name 'NotExist' not found. Available columns are: [Divisor, Animals, Furniture, Car, Fruit, Country, Color, Job, Sport, City, Technology]"
                            )))}
            //localhost:8080/map?category=NotExist&numberLists=1,2,4 -> query example 400
    )
    //http://localhost:8080/swagger-ui/index.html -> link to docs
    @GetMapping
    public ResponseEntity<?> mapValues(
            @Parameter(example = "Car", description = "Category from mapping file", required = true) @RequestParam String category,
            @Parameter(example = "1,2,4", description = "Comma separated list of numbers", required = true) @RequestParam String numberLists) {
        try {
            return ResponseEntity.ok(mappingFileService.mapValues(category, numberLists));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
