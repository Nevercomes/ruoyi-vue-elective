package com.ruoyi.project.elective.course.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * course对象 elective_course
 * 
 * @author Sunss
 * @date 2020-02-11
 */
public class ElectiveCourse extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 课程名 */
    @Excel(name = "课程名")
    private String name;

    /** 课程状态 字典值 0 申请中 1 申请通过 2 退回 */
    @Excel(name = "课程状态 字典值 0 申请中 1 申请通过 2 退回")
    private String status;

    /** 上课老师 */
    @Excel(name = "上课老师")
    private Long teacherId;

    /** 对应的开课时间 字典值 */
    @Excel(name = "对应的开课时间 字典值")
    private String semester;

    /** 课程简介 */
    @Excel(name = "课程简介")
    private String intro;

    /** 目标 */
    @Excel(name = "目标")
    private String objective;

    /** 特别声明 */
    @Excel(name = "特别声明")
    private String specialNote;

    /** 上课时间 字典值 */
    @Excel(name = "上课时间 字典值")
    private String classTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setTeacherId(Long teacherId) 
    {
        this.teacherId = teacherId;
    }

    public Long getTeacherId() 
    {
        return teacherId;
    }
    public void setSemester(String semester) 
    {
        this.semester = semester;
    }

    public String getSemester() 
    {
        return semester;
    }
    public void setIntro(String intro) 
    {
        this.intro = intro;
    }

    public String getIntro() 
    {
        return intro;
    }
    public void setObjective(String objective) 
    {
        this.objective = objective;
    }

    public String getObjective() 
    {
        return objective;
    }
    public void setSpecialNote(String specialNote) 
    {
        this.specialNote = specialNote;
    }

    public String getSpecialNote() 
    {
        return specialNote;
    }
    public void setClassTime(String classTime) 
    {
        this.classTime = classTime;
    }

    public String getClassTime() 
    {
        return classTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .append("remark", getRemark())
            .append("name", getName())
            .append("status", getStatus())
            .append("teacherId", getTeacherId())
            .append("semester", getSemester())
            .append("intro", getIntro())
            .append("objective", getObjective())
            .append("specialNote", getSpecialNote())
            .append("classTime", getClassTime())
            .toString();
    }
}
