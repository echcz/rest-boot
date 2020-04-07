package cn.echcz.restboot.service;

import cn.echcz.restboot.mapper.RoleMapper;
import cn.echcz.restboot.model.Role;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class RoleService {
    @NonNull
    private RoleMapper roleMapper;

    public void add(Role role) {
        roleMapper.add(role);
    }

    public void edit(Role role) {
        roleMapper.edit(role);
    }

    public Role findById(Integer id) {
        return roleMapper.findById(id);
    }

    public List<Role> findAll() {
        return roleMapper.findAll();
    }
}
