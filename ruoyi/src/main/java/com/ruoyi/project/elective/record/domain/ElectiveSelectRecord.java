package com.ruoyi.project.elective.record.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.Date;

/**
 * select对象 elective_select_record
 *
 * @author Sunss
 * @date 2020-02-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElectiveSelectRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 选课的学生
     */
    private Long studentId;

    @Excel(name = "学生")
    private String studentName;


    @Excel(name = "班级")
    private String className;

    @Excel(name = "课程")
    private String courseName;

    @Excel(name = "选课")
    private String openName;

    /**
     * 对应选课人数
     */
    private Long courseId;

    @Excel(name = "创建时间")
    private Date createTime;

    /**
     * 对应的开放选课
     */
    private Long openId;

}
