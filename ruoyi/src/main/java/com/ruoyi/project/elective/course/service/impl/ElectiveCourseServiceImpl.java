package com.ruoyi.project.elective.course.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.course.mapper.ElectiveCourseMapper;
import com.ruoyi.project.elective.course.domain.ElectiveCourse;
import com.ruoyi.project.elective.course.service.IElectiveCourseService;

/**
 * courseService业务层处理
 * 
 * @author Sunss
 * @date 2020-02-11
 */
@Service
public class ElectiveCourseServiceImpl implements IElectiveCourseService 
{
    @Autowired
    private ElectiveCourseMapper electiveCourseMapper;

    /**
     * 查询course
     * 
     * @param id courseID
     * @return course
     */
    @Override
    public ElectiveCourse selectElectiveCourseById(Long id)
    {
        return electiveCourseMapper.selectElectiveCourseById(id);
    }

    /**
     * 查询course列表
     * 
     * @param electiveCourse course
     * @return course
     */
    @Override
    public List<ElectiveCourse> selectElectiveCourseList(ElectiveCourse electiveCourse)
    {
        return electiveCourseMapper.selectElectiveCourseList(electiveCourse);
    }

    /**
     * 新增course
     * 
     * @param electiveCourse course
     * @return 结果
     */
    @Override
    public int insertElectiveCourse(ElectiveCourse electiveCourse)
    {
        electiveCourse.setCreateTime(DateUtils.getNowDate());
        return electiveCourseMapper.insertElectiveCourse(electiveCourse);
    }

    /**
     * 修改course
     * 
     * @param electiveCourse course
     * @return 结果
     */
    @Override
    public int updateElectiveCourse(ElectiveCourse electiveCourse)
    {
        electiveCourse.setUpdateTime(DateUtils.getNowDate());
        return electiveCourseMapper.updateElectiveCourse(electiveCourse);
    }

    /**
     * 批量删除course
     * 
     * @param ids 需要删除的courseID
     * @return 结果
     */
    @Override
    public int deleteElectiveCourseByIds(Long[] ids)
    {
        return electiveCourseMapper.deleteElectiveCourseByIds(ids);
    }

    /**
     * 删除course信息
     * 
     * @param id courseID
     * @return 结果
     */
    @Override
    public int deleteElectiveCourseById(Long id)
    {
        return electiveCourseMapper.deleteElectiveCourseById(id);
    }
}
