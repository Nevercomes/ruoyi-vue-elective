package com.ruoyi.project.elective.course.mapper;

import com.ruoyi.project.elective.course.domain.ElectiveCourse;
import java.util.List;

/**
 * courseMapper接口
 * 
 * @author Sunss
 * @date 2020-02-11
 */
public interface ElectiveCourseMapper 
{
    /**
     * 查询course
     * 
     * @param id courseID
     * @return course
     */
    public ElectiveCourse selectElectiveCourseById(Long id);

    /**
     * 查询course列表
     * 
     * @param electiveCourse course
     * @return course集合
     */
    public List<ElectiveCourse> selectElectiveCourseList(ElectiveCourse electiveCourse);

    /**
     * 新增course
     * 
     * @param electiveCourse course
     * @return 结果
     */
    public int insertElectiveCourse(ElectiveCourse electiveCourse);

    /**
     * 修改course
     * 
     * @param electiveCourse course
     * @return 结果
     */
    public int updateElectiveCourse(ElectiveCourse electiveCourse);

    /**
     * 删除course
     * 
     * @param id courseID
     * @return 结果
     */
    public int deleteElectiveCourseById(Long id);

    /**
     * 批量删除course
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteElectiveCourseByIds(Long[] ids);
}
