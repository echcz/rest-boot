package cn.echcz.restboot.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PathUtils {
    private PathUtils() {
        // util class
    }

    private static Pattern pattern = Pattern.compile("/([^/]+)");

    /**
     * 将路径字符串转成列表，其中每一个元素表示一层路径
     * 主意: 只支持‘/’作为路径分隔符
     */
    public static List<String> pathStrToList(String pathStr) {
        List<String> result = new ArrayList<>();
        Matcher m = pattern.matcher(pathStr);
        while (m.find()) {
            result.add(m.group(1));
        }
        return result;
    }
}
