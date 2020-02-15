package com.ruoyi.project.elective.open.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.open.mapper.ElectiveOpenSelectMapper;
import com.ruoyi.project.elective.open.domain.ElectiveOpenSelect;
import com.ruoyi.project.elective.open.service.IElectiveOpenSelectService;

/**
 * 开放选课Service业务层处理
 *
 * @author Sunss
 * @date 2020-02-15
 */
@Service
public class ElectiveOpenSelectServiceImpl implements IElectiveOpenSelectService {
    @Autowired
    private ElectiveOpenSelectMapper electiveOpenSelectMapper;

    /**
     * 查询开放选课
     *
     * @param id 开放选课ID
     * @return 开放选课
     */
    @Override
    public ElectiveOpenSelect selectElectiveOpenSelectById(Long id) {
        return electiveOpenSelectMapper.selectElectiveOpenSelectById(id);
    }

    /**
     * 查询开放选课列表
     *
     * @param electiveOpenSelect 开放选课
     * @return 开放选课
     */
    @Override
    public List<ElectiveOpenSelect> selectElectiveOpenSelectList(ElectiveOpenSelect electiveOpenSelect) {
        return electiveOpenSelectMapper.selectElectiveOpenSelectList(electiveOpenSelect);
    }

    /**
     * 新增开放选课
     *
     * @param electiveOpenSelect 开放选课
     * @return 结果
     */
    @Override
    public int insertElectiveOpenSelect(ElectiveOpenSelect electiveOpenSelect) {
        electiveOpenSelect.setCreateTime(DateUtils.getNowDate());
        return electiveOpenSelectMapper.insertElectiveOpenSelect(electiveOpenSelect);
    }

    /**
     * 修改开放选课
     *
     * @param electiveOpenSelect 开放选课
     * @return 结果
     */
    @Override
    public int updateElectiveOpenSelect(ElectiveOpenSelect electiveOpenSelect) {
        electiveOpenSelect.setUpdateTime(DateUtils.getNowDate());
        return electiveOpenSelectMapper.updateElectiveOpenSelect(electiveOpenSelect);
    }

    /**
     * 批量删除开放选课
     *
     * @param ids 需要删除的开放选课ID
     * @return 结果
     */
    @Override
    public int deleteElectiveOpenSelectByIds(Long[] ids) {
        return electiveOpenSelectMapper.deleteElectiveOpenSelectByIds(ids);
    }

    /**
     * 删除开放选课信息
     *
     * @param id 开放选课ID
     * @return 结果
     */
    @Override
    public int deleteElectiveOpenSelectById(Long id) {
        return electiveOpenSelectMapper.deleteElectiveOpenSelectById(id);
    }
}
