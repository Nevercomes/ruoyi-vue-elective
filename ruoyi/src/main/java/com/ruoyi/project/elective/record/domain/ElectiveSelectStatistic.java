package com.ruoyi.project.elective.record.domain;

import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Sunss
 * @since 2020/2/19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ElectiveSelectStatistic extends BaseEntity {

    /**
     * 用于查询的选课统计
     */
    private Long[] openIds;

    /**
     * 用于查询的年级数据
     */
    private Long[] gradeIds;

    @Excel(name = "课程")
    private String courseName;

    @Excel(name = "教师")
    private String teacherName;

    @Excel(name = "年级")
    private String gradeName;

    @Excel(name = "招生人数")
    private Integer initNum;

    @Excel(name = "选课人数")
    private Integer selectNum;

}
