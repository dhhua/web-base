package com.web.base.persist.auth.repository;

import com.web.base.persist.auth.entity.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author dong
 * @date 2020/5/10
 */
@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long> {

    SystemUser findOneByAccountAndPassword(String account, String password);

    SystemUser findOneByAccount(String account);

}
