package com.czxy.redyu.model.dto.base;


import lombok.NonNull;

import static com.czxy.redyu.utils.BeanUtils.updateProperties;

/**
 * @author xuhongzu
 * @version 1.0
 * @date 2019/12/10
 */
public interface OutputConverter<DTO extends OutputConverter<DTO, DOMAIN>, DOMAIN> {


    @SuppressWarnings("unchecked")
    @NonNull
    default <T extends DTO> T convertFrom(@NonNull DOMAIN domain) {

        updateProperties(domain, this);

        return (T) this;
    }
}
