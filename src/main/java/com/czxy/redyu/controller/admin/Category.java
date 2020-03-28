package com.czxy.redyu.controller.admin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * @author zhaorenhui@itcast.cn
 * @version 1.0
 * @date 2020/1/30
 *  商品类目表，类目和商品(spu)是一对多关系，类目与品牌是多对多关系
 */
@Table(name = "tb_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {
    @Id
    private Integer id;
    private String name;
    private Integer parent_id;  //父类目id,顶级类目填0
    private Integer is_parent;  //是否为父节点，0为否，1为是
    private Integer sort;       //排序指数，越小越靠前

//    一个分类下有多个spu商品
//    private List<Spu> CateSpuList;
}
