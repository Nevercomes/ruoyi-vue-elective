package com.ruoyi.project.elective.record.domain;

import com.ruoyi.project.elective.course.domain.ElectiveCourse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

/**
 * check对象 elective_check_record
 *
 * @author Sunss
 * @date 2020-02-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElectiveCheckRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 申请课程
     */
    private Long courseId;

    /**
     * 申请记录
     */
    private Long applyId;

    /**
     * 审核结果
     */
    @Excel(name = "审核结果", readConverterExp = "1=通过,2=退回")
    private String result;

    @Excel(name = "申请课程")
    private String courseName;

    @Excel(name = "申请教师")
    private String teacherName;

    private ElectiveCourse course;

}
