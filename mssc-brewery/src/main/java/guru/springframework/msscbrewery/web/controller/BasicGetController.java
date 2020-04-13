package guru.springframework.msscbrewery.web.controller;

import guru.springframework.msscbrewery.services.BasicGetService;
import guru.springframework.msscbrewery.web.model.BasicGetDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/test")
public class BasicGetController {
    private final BasicGetService service;

    public BasicGetController(BasicGetService service){
        this.service = service;
    }

    @GetMapping("/{name}/{uuid}")
    public ResponseEntity<BasicGetDto> testGet(@PathVariable String name, @PathVariable UUID uuid){
        return  new ResponseEntity(service.basicGet(name,uuid), HttpStatus.OK);
    }
}
