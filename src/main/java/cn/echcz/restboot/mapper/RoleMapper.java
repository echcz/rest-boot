package cn.echcz.restboot.mapper;

import cn.echcz.restboot.model.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface RoleMapper {
    int add(Role role);

    int edit(Role role);

    Role findById(Integer id);

    List<Role> findAll();
}
