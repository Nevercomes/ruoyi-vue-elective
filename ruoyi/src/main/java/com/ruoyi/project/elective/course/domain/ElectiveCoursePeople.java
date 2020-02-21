package com.ruoyi.project.elective.course.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
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
     * 开课时间
     */
    private Long semesterId;

    /**
     * 年级
     */
    private Long gradeId;

    @Excel(name = "课程")
    private String courseName;

    @Excel(name = "教师")
    private String teacherName;

    @Excel(name = "年级")
    private String gradeName;

    /**
     * 招生人数
     */
    @Excel(name = "计划招生")
    private Integer initNum;

    /**
     * 选课人数
     */
    @Excel(name = "选课人数")
    private Integer selectNum;

    public ElectiveCoursePeople(Long courseId) {
        this.courseId = courseId;
    }


}
