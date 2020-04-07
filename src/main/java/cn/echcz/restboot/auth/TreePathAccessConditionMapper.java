package cn.echcz.restboot.auth;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TreePathAccessConditionMapper implements PathAccessConditionMapper {
    private static class TreePathMatcherNode {
        @Getter
        private String name;
        private Set<Integer> pathIds;
        @Getter
        private List<TreePathMatcherNode> children;
        @Getter @Setter
        private AccessCondition condition;

        public TreePathMatcherNode(String name) {
            this.name = name;
            this.pathIds = new HashSet<>();
            this.children = new ArrayList<>();
        }

        public void addPathId(Integer pathId) {
            pathIds.add(pathId);
        }

        public boolean deletedPathId(Integer pathId) {
            return pathIds.remove(pathId);
        }

        public boolean containsPathId(Integer pathId) {
            return pathIds.contains(pathId);
        }

        public boolean pathIdsIsEmpty() {
            return pathIds.isEmpty();
        }
    }

    private static final String singleWildcard = "*";
    private static final String multiWildcard = "**";

    private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private final Lock writeLock;
    private final Lock readLock;
    private TreePathMatcherNode root = new TreePathMatcherNode("");

    public TreePathAccessConditionMapper() {
        writeLock = readWriteLock.writeLock();
        readLock = readWriteLock.readLock();
    }

    @Override
    public void clear() {
        try {
            writeLock.lock();
            root = new TreePathMatcherNode("");
        } finally {
            writeLock.unlock();
        }
    }

    @Override
    public void add(PathAccessCondition pathAccessCondition) {
        try {
            writeLock.lock();
            addToNode(root, pathAccessCondition.getPathId(), pathAccessCondition.getPath(), 0, pathAccessCondition.getAccessCondition());
        } finally {
            writeLock.unlock();
        }
    }

    private void addToNode(TreePathMatcherNode node, Integer pathId, List<String> path, int index, AccessCondition condition) {
        // index == path.size ？
        if (index == path.size()) {
            // true:
            // node.value == null ?
            if (node.getCondition() == null) {
                // true:
                // node.setCondition,node.addPathId,return
                node.setCondition(condition);
                node.addPathId(pathId);
            } else {
                // false:
                // 抛出异常
                throw new RuntimeException("当前路径已被占用，无法添加");
            }
        } else {
            // false:
            // 查找可以添加的子node
            TreePathMatcherNode child = null;
            for (TreePathMatcherNode c : node.getChildren()) {
                if (path.get(index).equals(c.getName())) {
                    child = c;
                    break;
                }
            }
            if (child == null) {
                // 没找到: 创建并添加(后续的addToNode一定成功，故可以安全添加)
                child = new TreePathMatcherNode(path.get(index));
                addChildToNode(node, child);
            }
            // 递归
            addToNode(child, pathId, path, index + 1, condition);
            // node.addPathId,return
            node.addPathId(pathId);
        }
    }

    private void addChildToNode(TreePathMatcherNode node, TreePathMatcherNode child) {
        List<TreePathMatcherNode> children = node.getChildren();
        if (children.isEmpty()) {
            children.add(child);
            return;
        }
        if (multiWildcard.equals(child.getName())) {
            children.add(child);
        } else if (singleWildcard.equals(child.getName())) {
            if (multiWildcard.equals(children.get(children.size() - 1).getName())) {
                children.add(children.size() - 1, child);
            } else {
                children.add(child);
            }
        } else {
            children.add(0, child);
        }
    }

    @Override
    public void deleteByPathId(Integer pathId) {
        try {
            writeLock.lock();
            if (root.deletedPathId(pathId)) {
                deleteByNode(root, pathId);
            }
        } finally {
            writeLock.unlock();
        }
    }

    private void deleteByNode(TreePathMatcherNode node, Integer pathId) {
        // 遍历,child.removePathId
        for (Iterator<TreePathMatcherNode> it = node.getChildren().iterator(); it.hasNext(); ) {
            TreePathMatcherNode child = it.next();
            if (child.deletedPathId(pathId)) {
                // 有删除成功:
                // 查看删除成功的是否有其它pathIds
                if (child.pathIdsIsEmpty()) {
                    // 无:
                    // 删除子
                    it.remove();
                } else {
                    // 有:
                    // 递归
                    deleteByNode(child, pathId);
                }
                return;
            }
        }
        // 无删除成功:
        // node.setCondition(null)
        node.setCondition(null);
    }

    @Override
    public PathAccessCondition findPathByPathId(Integer pathId) {
        try {
            readLock.lock();
            if (root.containsPathId(pathId)) {
                List<String> path = new ArrayList<>();
                PathAccessCondition.PathAccessConditionBuilder builder = PathAccessCondition.builder();
                findPathByNode(root, pathId, path, builder);
                builder.pathId(pathId);
                builder.path(path);
                return builder.build();
            } else {
                return null;
            }
        } finally {
            readLock.unlock();
        }
    }

    private void findPathByNode(TreePathMatcherNode node, Integer pathId,
                                List<String> path, PathAccessCondition.PathAccessConditionBuilder builder) {
        // 遍历, 查找pathId牵连的子
        for (TreePathMatcherNode child : node.getChildren()) {
            if (child.containsPathId(pathId)) {
                // 找到牵连的子，将child.getName加入到path中
                path.add(child.getName());
                // 递归
                findPathByNode(child, pathId, path, builder);
                return;
            }
        }
        // 没有牵连的子，也就是最后一层，则将此层的值放到构造器中
        builder.accessCondition(node.getCondition());
    }

    @Override
    public AccessCondition findConditionByPath(List<String> path) {
        try {
            readLock.lock();
            return findConditionByNode(root, path, -1);
        } finally {
            readLock.unlock();
        }
    }

    private AccessCondition findConditionByNode(TreePathMatcherNode node, List<String> path, int index) {
        // 自己是否匹配
        if (index == -1 || path.get(index).equals(node.getName())
                || singleWildcard.equals(node.getName()) || multiWildcard.equals(node.getName())) {
            // 是:
            // 自己是否为完全匹配
            if (path.size() - 1 == index) {
                // 是:
                // 返回node.getCondition
                return node.getCondition();
            } else {
                // 否:
                // 遍历递归子，是否有返回有效条件
                for (TreePathMatcherNode child : node.getChildren()) {
                    AccessCondition childCodition = findConditionByNode(child, path, index + 1);
                    if (childCodition != null) {
                        // 是：
                        // 返回子返回的条件
                        return childCodition;
                    }
                }
                // 否:
                // 自己是否为**?
                if (multiWildcard.equals(node.getName())) {
                    // 是:
                    // 返回递归
                    return findConditionByNode(node, path, index + 1);
                } else {
                    // 否:
                    // 返回null
                    return null;
                }
            }
        } else {
            // 否:
            // 返回null
            return null;
        }
    }

}
