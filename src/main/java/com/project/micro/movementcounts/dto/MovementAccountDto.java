package com.project.micro.movementcounts.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MovementAccountDto {

    private String idMovementAccount;
    private String idAccount;
    private String idAccountRecipient;
    private String movementType;
    private BigDecimal amount;


}
