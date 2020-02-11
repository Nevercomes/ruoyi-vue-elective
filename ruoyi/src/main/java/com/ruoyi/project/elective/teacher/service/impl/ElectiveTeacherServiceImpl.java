package com.ruoyi.project.elective.teacher.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.teacher.mapper.ElectiveTeacherMapper;
import com.ruoyi.project.elective.teacher.domain.ElectiveTeacher;
import com.ruoyi.project.elective.teacher.service.IElectiveTeacherService;

/**
 * 教师管理Service业务层处理
 * 
 * @author Sunss
 * @date 2020-02-11
 */
@Service
public class ElectiveTeacherServiceImpl implements IElectiveTeacherService 
{
    @Autowired
    private ElectiveTeacherMapper electiveTeacherMapper;

    /**
     * 查询教师管理
     * 
     * @param id 教师管理ID
     * @return 教师管理
     */
    @Override
    public ElectiveTeacher selectElectiveTeacherById(Long id)
    {
        return electiveTeacherMapper.selectElectiveTeacherById(id);
    }

    /**
     * 查询教师管理列表
     * 
     * @param electiveTeacher 教师管理
     * @return 教师管理
     */
    @Override
    public List<ElectiveTeacher> selectElectiveTeacherList(ElectiveTeacher electiveTeacher)
    {
        return electiveTeacherMapper.selectElectiveTeacherList(electiveTeacher);
    }

    /**
     * 新增教师管理
     * 
     * @param electiveTeacher 教师管理
     * @return 结果
     */
    @Override
    public int insertElectiveTeacher(ElectiveTeacher electiveTeacher)
    {
        electiveTeacher.setCreateTime(DateUtils.getNowDate());
        return electiveTeacherMapper.insertElectiveTeacher(electiveTeacher);
    }

    /**
     * 修改教师管理
     * 
     * @param electiveTeacher 教师管理
     * @return 结果
     */
    @Override
    public int updateElectiveTeacher(ElectiveTeacher electiveTeacher)
    {
        electiveTeacher.setUpdateTime(DateUtils.getNowDate());
        return electiveTeacherMapper.updateElectiveTeacher(electiveTeacher);
    }

    /**
     * 批量删除教师管理
     * 
     * @param ids 需要删除的教师管理ID
     * @return 结果
     */
    @Override
    public int deleteElectiveTeacherByIds(Long[] ids)
    {
        return electiveTeacherMapper.deleteElectiveTeacherByIds(ids);
    }

    /**
     * 删除教师管理信息
     * 
     * @param id 教师管理ID
     * @return 结果
     */
    @Override
    public int deleteElectiveTeacherById(Long id)
    {
        return electiveTeacherMapper.deleteElectiveTeacherById(id);
    }
}
