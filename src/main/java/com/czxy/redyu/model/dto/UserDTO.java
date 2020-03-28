package com.czxy.redyu.model.dto;

import com.czxy.redyu.model.dto.base.OutputConverter;
import com.czxy.redyu.model.entity.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.format.DateTimeFormatter;

import static com.czxy.redyu.utils.BeanUtils.updateProperties;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/11
 */
@Data
@EqualsAndHashCode
@ToString
public class UserDTO implements OutputConverter<UserDTO, User> {
    private Integer id;

    private String username;

    private String nickname;

    private String email;

    private String avatar;

    private String description;

    private String createTime;

    @Override
    public <T extends UserDTO> T convertFrom(User user) {
        this.createTime=user.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        updateProperties(user, this);
        return (T) this;
    }
}
