package cn.echcz.restboot.auth;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PathAccessCondition {
    /**
     * 路径id
     */
    @Getter
    private Integer pathId;
    /**
     * 路径(每个元素表示路径的一层)
     */
    @Getter
    private List<String> path;
    /**
     * 访问条件
     */
    @Getter
    private AccessCondition accessCondition;
}
