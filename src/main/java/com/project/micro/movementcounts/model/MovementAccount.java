package com.project.micro.movementcounts.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Document(collection = "movementAccount")
public class MovementAccount {
    @Id
    private String idMovementAccount;
    private String idAccount;
    private String idAccountRecipient;
    private String MovementType;
    private BigDecimal amount;
    private Date movementDate;
    private Date registrationDate;
    private Date modificationDate;
    private Boolean status;

}
