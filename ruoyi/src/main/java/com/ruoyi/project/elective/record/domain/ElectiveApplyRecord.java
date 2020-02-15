package com.ruoyi.project.elective.record.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * apply对象 elective_apply_record
 *
 * @author Sunss
 * @date 2020-02-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElectiveApplyRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 编号
     */
    private Long id;

    /**
     * 申请教师
     */
    private Long teacherId;

    @Excel(name = "申请教师")
    private String teacherName;

    /**
     * 申请课程
     */
    private Long courseId;

    @Excel(name = "申请课程")
    private String courseName;

    /**
     * 申请状态
     */
    @Excel(name = "申请状态", readConverterExp = "0=申请中,1=已通过,2=退回")
    private String status;

}
