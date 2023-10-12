package com.project.micro.movementcounts.proxy.service.impl;

import com.project.micro.movementcounts.proxy.bean.AccountBean;
import com.project.micro.movementcounts.proxy.service.IMovementAccountProxy;
import com.project.micro.movementcounts.util.ExternalProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
public class MovementAccountProxyImpl implements IMovementAccountProxy {

    @Autowired
    private ExternalProperties p;

    @Override
    public Mono<AccountBean> findById(String id) {
        WebClient webClient= WebClient.builder().baseUrl(p.urlBankAccount).build();
        return webClient.get()
                .uri("/findById/{id}", Collections.singletonMap("id", id))
                .retrieve()
                .bodyToMono(AccountBean.class);

    }

    @Override
    public Mono<AccountBean> update(String id, AccountBean accountBean) {
        WebClient webClient= WebClient.builder().baseUrl(p.urlBankAccount).build();
        return webClient.put()
                .uri("/update/{id}", Collections.singletonMap("id", id))
                .body(BodyInserters.fromValue(accountBean))
                .retrieve()
                .bodyToMono(AccountBean.class);

    }


}
