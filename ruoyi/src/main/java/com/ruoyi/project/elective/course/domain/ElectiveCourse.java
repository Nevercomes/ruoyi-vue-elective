package com.ruoyi.project.elective.course.domain;

import com.ruoyi.common.utils.spring.SpringUtils;
import com.ruoyi.project.elective.claszz.service.IElectiveClazzService;
import com.ruoyi.project.system.domain.SysDept;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import java.util.List;

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

    private Long classWeekId;

    @Excel(name = "上课星期")
    private Long classWeek;

    private String classLocation;

    private List<ElectiveCoursePeople> peopleList;

    private String enrollPeo;

    public String getEnrollPeo() {
        IElectiveClazzService clazzService = SpringUtils.getBean(IElectiveClazzService.class);
        List<SysDept> gList = clazzService.getGradeList();
        StringBuilder res = new StringBuilder();
        if (peopleList == null) return null;
        for (int i = 0; i < peopleList.size(); i++) {
            ElectiveCoursePeople peo = peopleList.get(i);
            for (SysDept dept : gList) {
                if (peo.getGradeId().equals(dept.getDeptId())) {
                    res.append(dept.getDeptName()).append("（").append(peo.getInitNum()).append("）人");
                    if (i != peopleList.size() - 1)
                        res.append("；\n");
                    break;
                }
            }
        }
        return res.toString();
    }

    /**
     * 用于检索年级课程
     */
    private Long gradeId;

    private boolean onlyCan;

    private Long openId;

    public String getClassTime() {
        return this.classWeek + this.classTime;
    }
}
