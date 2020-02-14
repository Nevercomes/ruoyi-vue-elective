package com.ruoyi.project.elective.course.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * 课程对象 elective_course
 *
 * @author Sunss
 * @date 2020-02-12
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElectiveCourse extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 课程名
     */
    @Excel(name = "课程名")
    private String name;

    /**
     * 课程状态 字典值 0 审核中 1 审核通过 2 退回
     */
    private String status;

    private Long teacherId;

    /**
     * 上课老师
     */
    @Excel(name = "上课老师")
    private String teacherName;

    private String teacherAvatar;

    /**
     * 学期
     */
    @Excel(name = "学期")
    private String semester;

    private Long semesterId;

    /**
     * 课程简介
     */
    private String intro;

    /**
     * 目标
     */
    private String objective;

    /**
     * 特别声明
     */
    private String specialNote;

    /**
     * 上课时间
     */
    @Excel(name = "上课时间")
    private String classTime;

    private Long classTimeId;

    private String classLocation;

}
