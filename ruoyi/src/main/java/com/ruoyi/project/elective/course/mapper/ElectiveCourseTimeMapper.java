package com.ruoyi.project.elective.course.mapper;

import com.ruoyi.project.elective.course.domain.ElectiveCourseTime;

import java.util.List;

/**
 * 课程时间Mapper接口
 *
 * @author sun
 * @date 2020-04-09
 */
public interface ElectiveCourseTimeMapper {
    /**
     * 查询课程时间
     *
     * @param id 课程时间ID
     * @return 课程时间
     */
    public ElectiveCourseTime selectElectiveCourseTimeById(Long id);

    /**
     * 查询课程时间列表
     *
     * @param electiveCourseTime 课程时间
     * @return 课程时间集合
     */
    public List<ElectiveCourseTime> selectElectiveCourseTimeList(ElectiveCourseTime electiveCourseTime);

    /**
     * 新增课程时间
     *
     * @param electiveCourseTime 课程时间
     * @return 结果
     */
    public int insertElectiveCourseTime(ElectiveCourseTime electiveCourseTime);

    /**
     * 修改课程时间
     *
     * @param electiveCourseTime 课程时间
     * @return 结果
     */
    public int updateElectiveCourseTime(ElectiveCourseTime electiveCourseTime);

    /**
     * 删除课程时间
     *
     * @param id 课程时间ID
     * @return 结果
     */
    public int deleteElectiveCourseTimeById(Long id);

    /**
     * 批量删除课程时间
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteElectiveCourseTimeByIds(Long[] ids);
}