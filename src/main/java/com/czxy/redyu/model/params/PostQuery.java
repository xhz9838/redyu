package com.czxy.redyu.model.params;

import com.czxy.redyu.model.enums.PostStatus;
import lombok.Data;
import lombok.ToString;
import org.apache.ibatis.type.JdbcType;
import tk.mybatis.mapper.annotation.ColumnType;

import java.util.List;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2020/1/30
 */
@Data
@ToString
public class PostQuery {

    private Integer  pageNum;

    private Integer pageSize;

    private String keyword;
    @ColumnType(jdbcType = JdbcType.INTEGER)
    private PostStatus status;

    private Integer categoryId;

    private List<Integer> postIds;

}
