package com.czxy.redyu.dao;

import com.czxy.redyu.model.entity.Attachment;
import com.czxy.redyu.model.params.AttachmentQuery;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface AttachmentMapper extends Mapper<Attachment> {

    @Select("<script>" +
            "select * from attachment" +
            "<where>" +
            "<if test='name != null and name!= \"\"'> and name like #{name}</if>" +
            "<if test='suffix != null and suffix!= \"\"'> and suffix = #{suffix}</if>" +
            "<if test='type != null and type!= \"\"'> and type = #{type}</if>" +
            "</where>" +
            "</script>")
    List<Attachment> listAllByParam(AttachmentQuery attachmentQuery);

    @Select("SELECT media_type FROM attachment GROUP BY media_type")
    List<String> listAllMediaType();

    @Select("SELECT TYPE FROM attachment GROUP BY type")
    List<Integer> listTypes();

    @Select("SELECT suffix FROM attachment GROUP BY suffix")
    List<String> listSuffix();
}