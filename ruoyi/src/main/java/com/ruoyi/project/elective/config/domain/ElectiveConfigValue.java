package com.ruoyi.project.elective.config.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 配置模板下内容的label与value对象 elective_config_value
 *
 * @author Sunss
 * @date 2020-02-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElectiveConfigValue extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 标签
     */
    @Excel(name = "标签")
    private String label;

    /**
     * 模板id
     */
    @Excel(name = "模板id")
    private Long templateId;

    /**
     * 排序
     */
    @Excel(name = "排序")
    private Integer sort;

}
