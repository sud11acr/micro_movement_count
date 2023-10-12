package com.project.micro.movementcounts.service;

import com.project.micro.movementcounts.integration.MovementAccountRequest;
import com.project.micro.movementcounts.integration.MovementAccountResponse;
import com.project.micro.movementcounts.integration.MovementTransferAccountRequest;
import com.project.micro.movementcounts.integration.MovementTransferAccountResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IMovementAccountService {

    Mono<MovementAccountResponse> save(String idAccount, Mono<MovementAccountRequest> account);
    Mono<MovementAccountResponse> update(String id, Mono<MovementAccountRequest> account);
    Flux<MovementAccountResponse> findAll();
    Mono<MovementAccountResponse>findById(String id);
    Mono<Void> delete(String id);
    Mono<MovementTransferAccountResponse> transfer(String idAccount, Mono<MovementTransferAccountRequest> account);
}
