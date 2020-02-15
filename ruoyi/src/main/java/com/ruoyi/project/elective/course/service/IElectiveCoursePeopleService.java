package com.ruoyi.project.elective.course.service;

import com.ruoyi.project.elective.course.domain.ElectiveCoursePeople;

import java.util.List;

/**
 * 选课人数Service接口
 *
 * @author Sunss
 * @date 2020-02-14
 */
public interface IElectiveCoursePeopleService {
    /**
     * 查询选课人数
     *
     * @param id 选课人数ID
     * @return 选课人数
     */
    public ElectiveCoursePeople selectElectiveCoursePeopleById(Long id);

    /**
     * 查询选课人数列表
     *
     * @param electiveCoursePeople 选课人数
     * @return 选课人数集合
     */
    public List<ElectiveCoursePeople> selectElectiveCoursePeopleList(ElectiveCoursePeople electiveCoursePeople);

    /**
     * 新增选课人数
     *
     * @param electiveCoursePeople 选课人数
     * @return 结果
     */
    public int insertElectiveCoursePeople(ElectiveCoursePeople electiveCoursePeople);

    /**
     * 修改选课人数
     *
     * @param electiveCoursePeople 选课人数
     * @return 结果
     */
    public int updateElectiveCoursePeople(ElectiveCoursePeople electiveCoursePeople);

    /**
     * 批量删除选课人数
     *
     * @param ids 需要删除的选课人数ID
     * @return 结果
     */
    public int deleteElectiveCoursePeopleByIds(Long[] ids);

    /**
     * 删除选课人数信息
     *
     * @param id 选课人数ID
     * @return 结果
     */
    public int deleteElectiveCoursePeopleById(Long id);

    /**
     * 根据课程Id获取选课人数
     *
     * @param courseId
     * @return
     */
    public List<ElectiveCoursePeople> selectElectiveCoursePeopleListByCourseId(Long courseId);

}
