package com.project.micro.movementcounts.repo;

import com.project.micro.movementcounts.model.MovementAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface IMovementAccountRepo extends ReactiveMongoRepository<MovementAccount,String> {
}
