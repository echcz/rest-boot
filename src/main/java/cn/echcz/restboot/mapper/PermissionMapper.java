package cn.echcz.restboot.mapper;

import cn.echcz.restboot.model.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PermissionMapper {
    int add(Permission permission);

    int edit(Permission permission);

    int removeById(Integer id);

    int addPermissionRoles(@Param("permissionId") Integer permissionId, @Param("roleIds") List<Integer> roleIds);

    int removePermissionRolesByPermissionId(Integer permissionId);

    Permission findById(Integer id);

    Permission findDetailById(Integer id);

    List<Permission> findAll();

}
