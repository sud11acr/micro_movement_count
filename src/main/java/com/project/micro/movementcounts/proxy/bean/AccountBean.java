package com.project.micro.movementcounts.proxy.bean;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Generated
public class AccountBean {

    private String idAccount;
    private String idCustomer;
    private String accountType;
    private String numberAccount;
    private BigDecimal balance;
    private int limitMovement;
    private int numberMovements;
    private Date fixedTermAccountDate;
    private BigDecimal commission;
    private int numberFeeFreeTransactions;
}
