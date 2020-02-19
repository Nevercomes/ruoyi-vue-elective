package com.ruoyi.project.elective.course.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.course.mapper.ElectiveCoursePeopleMapper;
import com.ruoyi.project.elective.course.domain.ElectiveCoursePeople;
import com.ruoyi.project.elective.course.service.IElectiveCoursePeopleService;

/**
 * 选课人数Service业务层处理
 *
 * @author Sunss
 * @date 2020-02-14
 */
@Service
public class ElectiveCoursePeopleServiceImpl implements IElectiveCoursePeopleService {
    @Autowired
    private ElectiveCoursePeopleMapper electiveCoursePeopleMapper;

    /**
     * 查询选课人数
     *
     * @param id 选课人数ID
     * @return 选课人数
     */
    @Override
    public ElectiveCoursePeople selectElectiveCoursePeopleById(Long id) {
        return electiveCoursePeopleMapper.selectElectiveCoursePeopleById(id);
    }

    /**
     * 查询选课人数列表
     *
     * @param electiveCoursePeople 选课人数
     * @return 选课人数
     */
    @Override
    public List<ElectiveCoursePeople> selectElectiveCoursePeopleList(ElectiveCoursePeople electiveCoursePeople) {
        return electiveCoursePeopleMapper.selectElectiveCoursePeopleList(electiveCoursePeople);
    }

    /**
     * 新增选课人数
     *
     * @param electiveCoursePeople 选课人数
     * @return 结果
     */
    @Override
    public int insertElectiveCoursePeople(ElectiveCoursePeople electiveCoursePeople) {
        return electiveCoursePeopleMapper.insertElectiveCoursePeople(electiveCoursePeople);
    }

    /**
     * 修改选课人数
     *
     * @param electiveCoursePeople 选课人数
     * @return 结果
     */
    @Override
    public int updateElectiveCoursePeople(ElectiveCoursePeople electiveCoursePeople) {
        return electiveCoursePeopleMapper.updateElectiveCoursePeople(electiveCoursePeople);
    }

    /**
     * 批量删除选课人数
     *
     * @param ids 需要删除的选课人数ID
     * @return 结果
     */
    @Override
    public int deleteElectiveCoursePeopleByIds(Long[] ids) {
        return electiveCoursePeopleMapper.deleteElectiveCoursePeopleByIds(ids);
    }

    /**
     * 删除选课人数信息
     *
     * @param id 选课人数ID
     * @return 结果
     */
    @Override
    public int deleteElectiveCoursePeopleById(Long id) {
        return electiveCoursePeopleMapper.deleteElectiveCoursePeopleById(id);
    }

    /**
     *
     * @param courseId
     * @return
     */
    @Override
    public List<ElectiveCoursePeople> selectElectiveCoursePeopleListByCourseId(Long courseId) {
        ElectiveCoursePeople query = new ElectiveCoursePeople();
        query.setCourseId(courseId);
        return electiveCoursePeopleMapper.selectElectiveCoursePeopleList(query);
    }

    /**
     * 统计选课人数
     * @param electiveCoursePeople
     * @return
     */
    @Override
    public List<ElectiveCoursePeople> listStatistic(ElectiveCoursePeople electiveCoursePeople) {
        return electiveCoursePeopleMapper.listStatistic(electiveCoursePeople);
    }
}
