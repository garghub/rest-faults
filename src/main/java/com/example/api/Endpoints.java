package com.example.api;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController("/api")
public class Endpoints {

    private int z = 0;

    private FooDto k = new FooDto();

    @GetMapping("/x")
    public String getX(){
        //#1 thrown exception
        throw new RuntimeException("Simulated thrown exception");
    }


    @GetMapping(value = "/y", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FooDto> getY(){

        FooDto dto = new FooDto();
        dto.i = 42;
        dto.s = "FOO";
        dto.b = null; //#2 null is not valid type for boolean

        //#3 schema gives different status code
        return ResponseEntity.status(201).body(dto);
    }

    @PostMapping(value = "/y", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity postY(
            @RequestBody FooDto dto
    ){
        //not checking  any constraint
        //#4,#5,#6: 3 different constraints on data

        //#7: location header leading to 404
        return ResponseEntity.created(URI.create("/api/doesNotExist")).build();
    }

    @DeleteMapping("/y")
    public void deleteY(){
        //do nothing
        //#8 GET would still return data
    }

    @PutMapping(value = "/k", consumes = MediaType.APPLICATION_JSON_VALUE)
    public void putK(
            @RequestBody FooDto dto
    ){
        //k = dto; //this would had been correct
        //#9: partial update
        if(dto.b != null){
            k.b = dto.b;
        }
        if(dto.s != null){
            k.s = dto.s;
        }
        if(dto.i != null){
            k.i = dto.i;
        }
    }

    @GetMapping(value = "/k", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FooDto> getK(){

        return ResponseEntity.status(200).body(k);
    }


    @GetMapping("/z")
    public int getZ(){
        return z;
    }

    @PutMapping("/z")
    public void putZ(
            @RequestBody int delta
    ){
        //#10 not idempotent
        z += delta;
    }


}
