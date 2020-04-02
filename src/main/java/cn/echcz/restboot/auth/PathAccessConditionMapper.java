package cn.echcz.restboot.auth;

import java.util.List;

/**
 * 路径-访问条件映射器
 */
public interface PathAccessConditionMapper {
    /**
     * 添加一条映射
     * @param pathId 路径id
     * @param path 路径(每个元素表示路径的一层)
     * @param condition 访问条件
     */
    void add(Integer pathId, List<String> path, AccessCondition condition);

    void deleteByPathId(Integer pathId);

    /**
     * 删除一条映射
     * 默认实现为: delete后add
     * 如果在add时抛出异常将导致方法退形为delete，若要在add后抛出异常时保持数据不变，则需要手动添加原来的数据
     */
    default void update(Integer pathId, List<String> path, AccessCondition condition) {
        deleteByPathId(pathId);
        add(pathId, path, condition);
    }

    /**
     * 根据路径找到访问某路径所需要满足的条件
     * @param path 路径
     * @return 访问条件
     */
    AccessCondition getConditionByPath(List<String> path);
}
