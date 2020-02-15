package com.ruoyi.project.elective.course.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 选课人数对象 elective_course_people
 *
 * @author Sunss
 * @date 2020-02-14
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElectiveCoursePeople extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 课程
     */
    private Long courseId;

    /**
     * 年级
     */
    private Long gradeId;

    /**
     * 选课人数
     */
    private Integer selectNum;

    /**
     * 招生人数
     */
    private Integer initNum;
}
