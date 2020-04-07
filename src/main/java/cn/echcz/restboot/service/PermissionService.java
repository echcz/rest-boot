package cn.echcz.restboot.service;

import cn.echcz.restboot.auth.HasAnyRoleAccessCondition;
import cn.echcz.restboot.auth.PathAccessCondition;
import cn.echcz.restboot.auth.PathAccessConditionMapper;
import cn.echcz.restboot.mapper.PermissionMapper;
import cn.echcz.restboot.model.Permission;
import cn.echcz.restboot.model.Role;
import cn.echcz.restboot.util.PathUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class PermissionService {
    @NonNull
    private PermissionMapper permissionMapper;
    @NonNull
    private final PathAccessConditionMapper permissionContext;

    @PostConstruct
    private void init() {
        resetPermissionContext();
    }

    public void clearPermissionContext() {
        synchronized (permissionContext) {
            permissionContext.clear();
        }
    }

    public void resetPermissionContext() {
        synchronized (permissionContext) {
            List<Permission> permissions = findAll();
            permissionContext.clear();
            for (Permission permission : permissions) {
                try {
                    addPermissionToContext(permission);
                } catch (Exception e) {
                    log.warn("添加permission({})到permissionContext时发生异常: {}", permission.getId(), e.getMessage());
                }
            }
        }
    }

    private void addPermissionToContext(Permission permission) {
        permissionContext.add(permissionToPathAccessCondition(permission));
    }

    private PathAccessCondition permissionToPathAccessCondition(Permission permission) {
        List<String> path = PathUtils.pathStrToList(permission.getUri());
        path.add(permission.getMethod());
        Set<String> roleNames = permission.getRoles().stream()
                .map(Role::getName).collect(Collectors.toSet());
        return PathAccessCondition.builder()
                .pathId(permission.getId())
                .path(path)
                .accessCondition(new HasAnyRoleAccessCondition(roleNames))
                .build();
    }

    @Transactional
    public synchronized void add(Permission permission) {
        permissionMapper.add(permission);
        if (!CollectionUtils.isEmpty(permission.getRoles())) {
            List<Integer> roleIds = permission.getRoles().stream()
                    .map(Role::getId).collect(Collectors.toList());
            permissionMapper.addPermissionRoles(permission.getId(), roleIds);
        }
    }

    @Transactional
    public void addAndRefreshContext(Permission permission) {
        add(permission);
        refreshContextById(permission.getId());
    }

    private void refreshContextById(Integer id) {
        synchronized (permissionContext) {
            Permission newPermission = findById(id);
            if (newPermission != null) {
                updatePermissionToContext(newPermission);
            } else {
                permissionContext.deleteByPathId(id);
            }
        }
    }

    private void updatePermissionToContext(Permission permission) {
        permissionContext.update(permissionToPathAccessCondition(permission));
    }

    @Transactional
    public synchronized void edit(Permission permission) {
        permissionMapper.edit(permission);
        if (permission.getRoles() != null) {
            permissionMapper.removePermissionRolesByPermissionId(permission.getId());
            if (!permission.getRoles().isEmpty()) {
                List<Integer> roleIds = permission.getRoles().stream()
                        .map(Role::getId).collect(Collectors.toList());
                permissionMapper.addPermissionRoles(permission.getId(), roleIds);
            }
        }
    }

    @Transactional
    public void editAndRefreshContext(Permission permission) {
        edit(permission);
        refreshContextById(permission.getId());
    }

    @Transactional
    public synchronized void removeById(Integer id) {
        permissionMapper.removeById(id);
        permissionMapper.removePermissionRolesByPermissionId(id);
    }

    @Transactional
    public void removeByIdAndRefreshContext(Integer id) {
        removeById(id);
        refreshContextById(id);
    }

    public Permission findById(Integer id) {
        return permissionMapper.findById(id);
    }

    public Permission findDetailById(Integer id) {
        return permissionMapper.findDetailById(id);
    }

    public List<Permission> findAll() {
        return permissionMapper.findAll();
    }
}
