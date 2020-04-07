package cn.echcz.restboot.mapper;

import cn.echcz.restboot.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    int add(User user);

    int edit(User user);

    int addUserRoles(@Param("userId") Integer userId, @Param("roleIds") List<Integer> roleIds);

    int removeUserRolesByUserId(Integer userId);

    User findById(Integer id);

    User findDetailById(Integer id);

    List<User> findAll();

    User findByUsername(String username);
}
