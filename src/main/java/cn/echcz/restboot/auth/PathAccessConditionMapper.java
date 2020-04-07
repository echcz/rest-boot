package cn.echcz.restboot.auth;

import java.util.List;

/**
 * 路径-访问条件映射器
 */
public interface PathAccessConditionMapper {
    void clear();

    /**
     * 添加一条映射
     */
    void add(PathAccessCondition pathAccessCondition);

    /**
     * 删除一条映射
     */
    void deleteByPathId(Integer pathId);

    /**
     * 修改一条映射
     * 默认实现为: delete后add
     */
    default void update(PathAccessCondition pathAccessCondition) {
        Integer pathId = pathAccessCondition.getPathId();
        PathAccessCondition oldPath = findPathByPathId(pathId);
        deleteByPathId(pathId);
        try {
            add(pathAccessCondition);
        } catch (RuntimeException e) {
            if (oldPath != null) {
                add(oldPath);
            }
            throw e;
        }
    }

    PathAccessCondition findPathByPathId(Integer pathId);

    /**
     * 根据路径找到访问某路径所需要满足的条件
     * @param path 路径
     * @return 访问条件
     */
    AccessCondition findConditionByPath(List<String> path);
}
