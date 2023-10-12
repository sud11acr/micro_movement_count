package com.project.micro.movementcounts.controller;

import com.project.micro.movementcounts.integration.MovementAccountRequest;
import com.project.micro.movementcounts.integration.MovementAccountResponse;
import com.project.micro.movementcounts.proxy.bean.AccountBean;
import com.project.micro.movementcounts.service.IMovementAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/movementaccount")
public class MovementAccountController {

    @Autowired
    public IMovementAccountService service;

    @GetMapping("/findAll")
    public Mono<ResponseEntity<Flux<MovementAccountResponse>>> findAll() {
        return Mono.just(
                ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(service.findAll()));
    }
    @GetMapping("/findById/{id}")
    public Mono<ResponseEntity<MovementAccountResponse>> findById(@PathVariable String id) {
        return service.findById(id).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping("/create/{idAccount}")
    public Mono<ResponseEntity<MovementAccountResponse>>save(@PathVariable String idAccount,@Validated @RequestBody  Mono<MovementAccountRequest> movementAccountRequest){
        return service.save(idAccount,movementAccountRequest)
                .map(p -> ResponseEntity.created(URI.create("/create".concat("/").concat(p.getIdAccount())))
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(p));
    }

    @PutMapping("/update/{id}")
    public Mono<ResponseEntity<MovementAccountResponse>>update(@PathVariable String id, @RequestBody Mono<MovementAccountRequest> movementAccountRequest ){
        return service.update(id,movementAccountRequest)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/delete/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable String id) {
        return service.delete(id).then(Mono.just(new ResponseEntity<Void>(HttpStatus.NO_CONTENT)));
    }

    @GetMapping("/ramdon")
    Mono<AccountBean> getRandom(){
        String url="http://localhost:8083/account";
        String id="651b1f496a28d5498f4a8c48";

        WebClient webClient= WebClient.builder().baseUrl(url).build();
        Mono<AccountBean>response=webClient.get().uri("/findById/{id}", Collections.singletonMap("id", id)).retrieve().bodyToMono(AccountBean.class);
        return response;

    }
}
