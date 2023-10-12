package com.project.micro.movementcounts.service.impl;

import com.project.micro.movementcounts.exception.ErrorException;
import com.project.micro.movementcounts.integration.MovementAccountRequest;
import com.project.micro.movementcounts.integration.MovementAccountResponse;
import com.project.micro.movementcounts.integration.MovementTransferAccountRequest;
import com.project.micro.movementcounts.integration.MovementTransferAccountResponse;
import com.project.micro.movementcounts.model.MovementAccount;
import com.project.micro.movementcounts.proxy.service.impl.MovementAccountProxyImpl;
import com.project.micro.movementcounts.proxy.bean.AccountBean;
import com.project.micro.movementcounts.repo.IMovementAccountRepo;
import com.project.micro.movementcounts.service.IMovementAccountService;

import com.project.micro.movementcounts.util.Constants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import static com.project.micro.movementcounts.mapper.MovementAccountMapper.*;

@Service
public class MovementAccountServiceImpl implements IMovementAccountService {

    private static final Logger logger = Logger.getLogger(MovementAccountServiceImpl.class.getName());

    @Autowired
    private IMovementAccountRepo repo;

    @Autowired
    private MovementAccountProxyImpl movementAccountProxy;

    @Override
    public Mono<MovementAccountResponse> save(String idAccount, Mono<MovementAccountRequest> movementCountRequest) {
        return movementAccountProxy.findById(idAccount)
                .flatMap(account->{
                    if (!validateNumberMovementsAllowedAccountType(account)){
                        return Mono.error(new ErrorException("Número de movimientos no permitido.."));
                    }
                        return movementCountRequest.map(p-> toMovementCountModelReq(p))
                                .flatMap(
                                        p->{
                                            BigDecimal newAmount=calculateNewBalance(account,p);

                                            if(newAmount.compareTo(BigDecimal.ZERO)==-1){
                                                return Mono.error(new ErrorException("Retiro mayor al saldo"));
                                            }

                                            BeanUtils.copyProperties(movementCountRequest,p);
                                            BigDecimal amountBefore=p.getAmount();
                                            p.setAmount(p.getAmount().add(validatedNumberFeeFreeTransactions(account)));
                                            p.setRegistrationDate(new Date());
                                            p.setModificationDate(new Date());
                                            p.setStatus(true);

                                            accountUpdate(p,account,newAmount,amountBefore);
                                            return repo.save(p);

                                        }).map(p-> toMovementCountModelRes(p));

                });

    }

    private static boolean validateNumberMovementsAllowedAccountType(AccountBean account) {
        return account.getAccountType().equals(Constants.CURRENT_ACCOUNT)||validateFixedTermAccount(account);
    }

    private static boolean validateFixedTermAccount(AccountBean account) {
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        boolean allowed = account.getNumberMovements() < account.getLimitMovement();
        if (Constants.FIXED_TERM_ACCOUNT.equals(account.getAccountType())) {
            allowed = allowed&&formatDate.format(account.getFixedTermAccountDate()).equals(formatDate.format(new Date()));
        }
        return allowed;
    }
    private Mono<Boolean> accountUpdate(MovementAccount movementAccount,AccountBean accountBeanMono,BigDecimal newBalance,BigDecimal amountBefore){

            AccountBean accountBean=new AccountBean();
            accountBean.setBalance(newBalance);
            accountBean.setNumberMovements(accountBeanMono.getNumberMovements()+1);

            movementAccountProxy.update(movementAccount.getIdAccount(),accountBean).subscribe();
            movementAccountProxy.findById(movementAccount.getIdAccountRecipient()).
                    map(p->{
                        p.setBalance(p.getBalance().add(amountBefore));
                        accountUpdateRecipient(movementAccount,p);
                        return p;
                    }).subscribe();

            return Mono.just(true);


    }
    private Mono<Boolean>accountUpdateRecipient(MovementAccount movementAccount,AccountBean accountBeanMono){
        movementAccountProxy.update(movementAccount.getIdAccountRecipient(),accountBeanMono).subscribe();
        return Mono.just(true);
    }

    @Override
    public Mono<MovementAccountResponse> update(String id, Mono<MovementAccountRequest> movementCountRequest) {
        Mono<MovementAccount> monoBody = movementCountRequest.map(p-> toMovementCountModelReq(p));
        Mono<MovementAccount> monoBD = repo.findById(id);

        return monoBD.zipWith(monoBody,(bd,pl)->{
                    BeanUtils.copyProperties(pl,bd);
                    bd.setIdMovementAccount(id);
                    return bd;
                }).flatMap(p->repo.save(p))
                .map(c-> toMovementCountModelRes(c));
    }

    @Override
    public Flux<MovementAccountResponse> findAll() {
        return repo.findAll().map(p->toMovementCountModelRes(p));
    }

    @Override
    public Mono<MovementAccountResponse> findById(String id) {
        return repo.findById(id).map(p->toMovementCountModelRes(p));
    }

    @Override
    public Mono<Void> delete(String id) {
        return repo.deleteById(id);
    }

    @Override
    public Mono<MovementTransferAccountResponse> transfer(String idAccount, Mono<MovementTransferAccountRequest> account) {
        return null;
    }

    private static BigDecimal validatedNumberFeeFreeTransactions(AccountBean accountBean){
        return (accountBean.getNumberMovements()>accountBean.getNumberFeeFreeTransactions()?accountBean.getCommission():BigDecimal.ZERO);
    }


    private static BigDecimal calculateNewBalance(AccountBean accountBeanMono,MovementAccount movementAccount){
        if(movementAccount.getMovementType().equals(Constants.DEPOSIT)){
            return (movementAccount.getAmount().add(accountBeanMono.getBalance().subtract(validatedNumberFeeFreeTransactions(accountBeanMono))));
        }else{
            return (accountBeanMono.getBalance().subtract(movementAccount.getAmount().add(validatedNumberFeeFreeTransactions(accountBeanMono))));
        }
    }


}

