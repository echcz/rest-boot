package cn.echcz.restboot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role {
    private Integer id;
    private String name;
    private Integer state;
    private String remark;

    @JsonIgnore
    public boolean isEnable() {
        return Integer.valueOf(1).equals(state);
    }
}
