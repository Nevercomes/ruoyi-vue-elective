package com.ruoyi.project.elective.course.mapper;

import com.ruoyi.project.elective.course.domain.ElectiveCourse;

import java.util.List;

/**
 * 课程Mapper接口
 *
 * @author Sunss
 * @date 2020-02-12
 */
public interface ElectiveCourseMapper {
    /**
     * 查询课程
     *
     * @param id 课程ID
     * @return 课程
     */
    public ElectiveCourse selectElectiveCourseById(Long id);

    /**
     * 查询课程列表
     *
     * @param electiveCourse 课程
     * @return 课程集合
     */
    public List<ElectiveCourse> selectElectiveCourseList(ElectiveCourse electiveCourse);

    /**
     * 新增课程
     *
     * @param electiveCourse 课程
     * @return 结果
     */
    public int insertElectiveCourse(ElectiveCourse electiveCourse);

    /**
     * 修改课程
     *
     * @param electiveCourse 课程
     * @return 结果
     */
    public int updateElectiveCourse(ElectiveCourse electiveCourse);

    /**
     * 删除课程
     *
     * @param id 课程ID
     * @return 结果
     */
    public int deleteElectiveCourseById(Long id);

    /**
     * 批量删除课程
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteElectiveCourseByIds(Long[] ids);
}
