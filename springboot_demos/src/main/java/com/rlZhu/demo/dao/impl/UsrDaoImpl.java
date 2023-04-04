package com.rlZhu.demo.dao.impl;


import com.rlZhu.demo.dao.UserDao;
import com.rlZhu.demo.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;

@Component
@Repository
public class UsrDaoImpl implements UserDao {
    @Override
    public int saveAll(List<User> bookList) {
        return 0;
    }

    @Override
    public void save(User user) {

    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
