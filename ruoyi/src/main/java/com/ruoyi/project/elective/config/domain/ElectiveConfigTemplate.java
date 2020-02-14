package com.ruoyi.project.elective.config.domain;

import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 配置模板对象 elective_config_template
 *
 * @author Sunss
 * @date 2020-02-08
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElectiveConfigTemplate extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 模板名
     */
    private String name;

    /**
     * 类别
     */
    private String type;

    /**
     * 状态
     */
    private String status;

    /**
     * 显示排序
     */
    private int sort;



}
