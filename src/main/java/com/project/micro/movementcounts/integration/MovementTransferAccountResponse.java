package com.project.micro.movementcounts.integration;

import com.project.micro.movementcounts.dto.MovementAccountDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MovementTransferAccountResponse extends MovementAccountDto {
    private String idAccountTransmitter;
}
