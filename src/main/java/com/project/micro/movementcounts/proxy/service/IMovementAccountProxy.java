package com.project.micro.movementcounts.proxy.service;

import com.project.micro.movementcounts.proxy.bean.AccountBean;
import reactor.core.publisher.Mono;

public interface IMovementAccountProxy {

     Mono<AccountBean> findById(String id);
     Mono<AccountBean> update(String id,AccountBean accountBean);
}
