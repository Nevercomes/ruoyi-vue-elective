package com.ruoyi.project.elective.course.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.course.mapper.ElectiveCourseMapper;
import com.ruoyi.project.elective.course.domain.ElectiveCourse;
import com.ruoyi.project.elective.course.service.IElectiveCourseService;

/**
 * 课程Service业务层处理
 *
 * @author Sunss
 * @date 2020-02-12
 */
@Service
public class ElectiveCourseServiceImpl implements IElectiveCourseService {
    @Autowired
    private ElectiveCourseMapper electiveCourseMapper;

    /**
     * 查询课程
     *
     * @param id 课程ID
     * @return 课程
     */
    @Override
    public ElectiveCourse selectElectiveCourseById(Long id) {
        return electiveCourseMapper.selectElectiveCourseById(id);
    }

    /**
     * 查询课程列表
     *
     * @param electiveCourse 课程
     * @return 课程
     */
    @Override
    public List<ElectiveCourse> selectElectiveCourseList(ElectiveCourse electiveCourse) {
        return electiveCourseMapper.selectElectiveCourseList(electiveCourse);
    }

    /**
     * 新增课程
     *
     * @param electiveCourse 课程
     * @return 结果
     */
    @Override
    public int insertElectiveCourse(ElectiveCourse electiveCourse) {
        electiveCourse.setCreateTime(DateUtils.getNowDate());
        return electiveCourseMapper.insertElectiveCourse(electiveCourse);
    }

    /**
     * 修改课程
     *
     * @param electiveCourse 课程
     * @return 结果
     */
    @Override
    public int updateElectiveCourse(ElectiveCourse electiveCourse) {
        electiveCourse.setUpdateTime(DateUtils.getNowDate());
        return electiveCourseMapper.updateElectiveCourse(electiveCourse);
    }

    /**
     * 批量删除课程
     *
     * @param ids 需要删除的课程ID
     * @return 结果
     */
    @Override
    public int deleteElectiveCourseByIds(Long[] ids) {
        return electiveCourseMapper.deleteElectiveCourseByIds(ids);
    }

    /**
     * 删除课程信息
     *
     * @param id 课程ID
     * @return 结果
     */
    @Override
    public int deleteElectiveCourseById(Long id) {
        return electiveCourseMapper.deleteElectiveCourseById(id);
    }
}
