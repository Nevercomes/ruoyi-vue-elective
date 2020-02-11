package com.ruoyi.project.elective.config.service.impl;

import java.util.List;

import com.ruoyi.project.elective.config.domain.ElectiveConfigValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.config.mapper.ElectiveConfigValueMapper;
import com.ruoyi.project.elective.config.service.IElectiveConfigValueService;

/**
 * 模板内容Service业务层处理
 *
 * @author Sunss
 * @date 2020-02-08
 */
@Service
public class ElectiveConfigValueServiceImpl implements IElectiveConfigValueService {
    @Autowired
    private ElectiveConfigValueMapper electiveConfigValueMapper;

    /**
     * 查询模板内容
     *
     * @param id 模板内容ID
     * @return 模板内容
     */
    @Override
    public ElectiveConfigValue selectElectiveConfigValueById(Long id) {
        return electiveConfigValueMapper.selectElectiveConfigValueById(id);
    }

    /**
     * 查询模板内容列表
     *
     * @param electiveConfigValue 模板内容
     * @return 模板内容
     */
    @Override
    public List<ElectiveConfigValue> selectElectiveConfigValueList(ElectiveConfigValue electiveConfigValue) {
        return electiveConfigValueMapper.selectElectiveConfigValueList(electiveConfigValue);
    }

    /**
     * 新增模板内容
     *
     * @param electiveConfigValue 模板内容
     * @return 结果
     */
    @Override
    public int insertElectiveConfigValue(ElectiveConfigValue electiveConfigValue) {
        return electiveConfigValueMapper.insertElectiveConfigValue(electiveConfigValue);
    }

    /**
     * 修改模板内容
     *
     * @param electiveConfigValue 模板内容
     * @return 结果
     */
    @Override
    public int updateElectiveConfigValue(ElectiveConfigValue electiveConfigValue) {
        return electiveConfigValueMapper.updateElectiveConfigValue(electiveConfigValue);
    }

    /**
     * 批量删除模板内容
     *
     * @param ids 需要删除的模板内容ID
     * @return 结果
     */
    @Override
    public int deleteElectiveConfigValueByIds(Long[] ids) {
        return electiveConfigValueMapper.deleteElectiveConfigValueByIds(ids);
    }

    /**
     * 删除模板内容信息
     *
     * @param id 模板内容ID
     * @return 结果
     */
    @Override
    public int deleteElectiveConfigValueById(Long id) {
        return electiveConfigValueMapper.deleteElectiveConfigValueById(id);
    }
}
