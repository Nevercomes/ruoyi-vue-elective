package com.ruoyi.project.elective.course.service;

import com.ruoyi.project.elective.course.domain.ElectiveCourse;
import java.util.List;

/**
 * courseService接口
 * 
 * @author Sunss
 * @date 2020-02-11
 */
public interface IElectiveCourseService 
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
     * 批量删除course
     * 
     * @param ids 需要删除的courseID
     * @return 结果
     */
    public int deleteElectiveCourseByIds(Long[] ids);

    /**
     * 删除course信息
     * 
     * @param id courseID
     * @return 结果
     */
    public int deleteElectiveCourseById(Long id);
}
