package com.stefano.nextbid.repo;

import com.stefano.nextbid.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}
