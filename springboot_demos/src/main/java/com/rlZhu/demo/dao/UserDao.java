package com.rlZhu.demo.dao;


import com.rlZhu.demo.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserDao {

    @Insert({
            "<script>",
            "insert into new_table (id, type, name, description)",
            "values ",
            "<foreach collection='userList' item='user' separator=','>",
            "(#{user.id}, #{user.type}, #{user.name}, #{user.description})",
            "</foreach>",
            "</script>"
    })
    public int saveAll(@Param("userList") List<User> userList);


    @Insert("insert into new_table (type, name, description) values ( #{type}, #{name}, #{description})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    public void save(User user);



    @Select("select * from new_table")
    public List<User> getAll();
}
