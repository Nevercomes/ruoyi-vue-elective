package com.ruoyi.project.elective.record.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * select对象 elective_select_record
 * 
 * @author Sunss
 * @date 2020-02-11
 */
public class ElectiveSelectRecord extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** id */
    private Long id;

    /** 选课的学生 */
    @Excel(name = "选课的学生")
    private Long studentId;

    /** 对应选课人数 */
    @Excel(name = "对应选课人数")
    private Long selectId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setStudentId(Long studentId) 
    {
        this.studentId = studentId;
    }

    public Long getStudentId() 
    {
        return studentId;
    }
    public void setSelectId(Long selectId) 
    {
        this.selectId = selectId;
    }

    public Long getSelectId() 
    {
        return selectId;
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
            .append("studentId", getStudentId())
            .append("selectId", getSelectId())
            .toString();
    }
}
