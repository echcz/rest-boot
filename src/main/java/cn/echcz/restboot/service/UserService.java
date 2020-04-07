package cn.echcz.restboot.service;

import cn.echcz.restboot.mapper.UserMapper;
import cn.echcz.restboot.model.Role;
import cn.echcz.restboot.model.User;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserService {
    @NonNull
    private UserMapper userMapper;

    @Transactional
    public synchronized void add(User user) {
        userMapper.add(user);
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            List<Integer> roleIds = user.getRoles().stream()
                    .map(Role::getId).collect(Collectors.toList());
            userMapper.addUserRoles(user.getId(), roleIds);
        }
    }

    @Transactional
    public synchronized void edit(User user) {
        userMapper.edit(user);
        if (user.getRoles() != null) {
            userMapper.removeUserRolesByUserId(user.getId());
            if (!user.getRoles().isEmpty()) {
                List<Integer> roleIds = user.getRoles().stream()
                        .map(Role::getId).collect(Collectors.toList());
                userMapper.addUserRoles(user.getId(), roleIds);
            }
        }
    }

    public User findById(Integer id) {
        return userMapper.findById(id);
    }

    public User findDetailById(Integer id) {
        return userMapper.findDetailById(id);
    }

    public List<User> findAll() {
        return userMapper.findAll();
    }

    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
}
