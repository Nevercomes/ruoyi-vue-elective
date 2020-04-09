package com.ruoyi.project.elective.course.service;

import com.ruoyi.project.elective.course.domain.ElectiveCourse;

import java.util.List;

/**
 * 课程Service接口
 *
 * @author Sunss
 * @date 2020-02-12
 */
public interface IElectiveCourseService {
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
     * 批量删除课程
     *
     * @param ids 需要删除的课程ID
     * @return 结果
     */
    public int deleteElectiveCourseByIds(Long[] ids);

    /**
     * 删除课程信息
     *
     * @param id 课程ID
     * @return 结果
     */
    public int deleteElectiveCourseById(Long id);

    /**
     * 选取用于搜索的courseList plain
     * @param electiveCourse
     * @return
     */
    List<ElectiveCourse> selectPlainList(ElectiveCourse electiveCourse);

    /**
     * 重新申请课程
     * @param electiveCourse
     * @return
     */
    int reApply(ElectiveCourse electiveCourse);
}
