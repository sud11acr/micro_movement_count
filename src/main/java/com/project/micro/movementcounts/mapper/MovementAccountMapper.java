package com.project.micro.movementcounts.mapper;

import com.project.micro.movementcounts.integration.MovementAccountRequest;
import com.project.micro.movementcounts.integration.MovementAccountResponse;
import com.project.micro.movementcounts.model.MovementAccount;
import com.project.micro.movementcounts.proxy.bean.AccountBean;
import org.springframework.beans.BeanUtils;

public class MovementAccountMapper {


    public static MovementAccount toMovementCountModelReq(MovementAccountRequest movementCountRequest){

        MovementAccount movementCount=new MovementAccount();
        BeanUtils.copyProperties(movementCountRequest,movementCount);
        return movementCount;
    }

    public static MovementAccountResponse toMovementCountModelRes(MovementAccount movementCount){

        MovementAccountResponse movementCountResponse=new MovementAccountResponse();
        BeanUtils.copyProperties(movementCount,movementCountResponse);
        return movementCountResponse;
    }

    public static AccountBean toAccountBeanModel(MovementAccount movementCount){

        AccountBean accountBean=new AccountBean();
        BeanUtils.copyProperties(movementCount,accountBean);
        return accountBean;
    }
}
