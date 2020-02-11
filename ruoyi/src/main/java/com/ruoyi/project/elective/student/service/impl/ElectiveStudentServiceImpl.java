package com.ruoyi.project.elective.student.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.student.mapper.ElectiveStudentMapper;
import com.ruoyi.project.elective.student.domain.ElectiveStudent;
import com.ruoyi.project.elective.student.service.IElectiveStudentService;

/**
 * studentService业务层处理
 * 
 * @author Sunss
 * @date 2020-02-11
 */
@Service
public class ElectiveStudentServiceImpl implements IElectiveStudentService 
{
    @Autowired
    private ElectiveStudentMapper electiveStudentMapper;

    /**
     * 查询student
     * 
     * @param id studentID
     * @return student
     */
    @Override
    public ElectiveStudent selectElectiveStudentById(Long id)
    {
        return electiveStudentMapper.selectElectiveStudentById(id);
    }

    /**
     * 查询student列表
     * 
     * @param electiveStudent student
     * @return student
     */
    @Override
    public List<ElectiveStudent> selectElectiveStudentList(ElectiveStudent electiveStudent)
    {
        return electiveStudentMapper.selectElectiveStudentList(electiveStudent);
    }

    /**
     * 新增student
     * 
     * @param electiveStudent student
     * @return 结果
     */
    @Override
    public int insertElectiveStudent(ElectiveStudent electiveStudent)
    {
        electiveStudent.setCreateTime(DateUtils.getNowDate());
        return electiveStudentMapper.insertElectiveStudent(electiveStudent);
    }

    /**
     * 修改student
     * 
     * @param electiveStudent student
     * @return 结果
     */
    @Override
    public int updateElectiveStudent(ElectiveStudent electiveStudent)
    {
        electiveStudent.setUpdateTime(DateUtils.getNowDate());
        return electiveStudentMapper.updateElectiveStudent(electiveStudent);
    }

    /**
     * 批量删除student
     * 
     * @param ids 需要删除的studentID
     * @return 结果
     */
    @Override
    public int deleteElectiveStudentByIds(Long[] ids)
    {
        return electiveStudentMapper.deleteElectiveStudentByIds(ids);
    }

    /**
     * 删除student信息
     * 
     * @param id studentID
     * @return 结果
     */
    @Override
    public int deleteElectiveStudentById(Long id)
    {
        return electiveStudentMapper.deleteElectiveStudentById(id);
    }
}
