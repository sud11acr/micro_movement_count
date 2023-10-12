package com.project.micro.movementcounts.service;

import com.project.micro.movementcounts.proxy.bean.AccountBean;
import reactor.core.publisher.Mono;

public interface IAccountService {

    Mono<AccountBean> findById(String id);
}
