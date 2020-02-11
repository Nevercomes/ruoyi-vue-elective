package com.ruoyi.project.elective.record.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.project.elective.record.mapper.ElectiveCheckRecordMapper;
import com.ruoyi.project.elective.record.domain.ElectiveCheckRecord;
import com.ruoyi.project.elective.record.service.IElectiveCheckRecordService;

/**
 * checkService业务层处理
 * 
 * @author Sunss
 * @date 2020-02-11
 */
@Service
public class ElectiveCheckRecordServiceImpl implements IElectiveCheckRecordService 
{
    @Autowired
    private ElectiveCheckRecordMapper electiveCheckRecordMapper;

    /**
     * 查询check
     * 
     * @param id checkID
     * @return check
     */
    @Override
    public ElectiveCheckRecord selectElectiveCheckRecordById(Long id)
    {
        return electiveCheckRecordMapper.selectElectiveCheckRecordById(id);
    }

    /**
     * 查询check列表
     * 
     * @param electiveCheckRecord check
     * @return check
     */
    @Override
    public List<ElectiveCheckRecord> selectElectiveCheckRecordList(ElectiveCheckRecord electiveCheckRecord)
    {
        return electiveCheckRecordMapper.selectElectiveCheckRecordList(electiveCheckRecord);
    }

    /**
     * 新增check
     * 
     * @param electiveCheckRecord check
     * @return 结果
     */
    @Override
    public int insertElectiveCheckRecord(ElectiveCheckRecord electiveCheckRecord)
    {
        electiveCheckRecord.setCreateTime(DateUtils.getNowDate());
        return electiveCheckRecordMapper.insertElectiveCheckRecord(electiveCheckRecord);
    }

    /**
     * 修改check
     * 
     * @param electiveCheckRecord check
     * @return 结果
     */
    @Override
    public int updateElectiveCheckRecord(ElectiveCheckRecord electiveCheckRecord)
    {
        electiveCheckRecord.setUpdateTime(DateUtils.getNowDate());
        return electiveCheckRecordMapper.updateElectiveCheckRecord(electiveCheckRecord);
    }

    /**
     * 批量删除check
     * 
     * @param ids 需要删除的checkID
     * @return 结果
     */
    @Override
    public int deleteElectiveCheckRecordByIds(Long[] ids)
    {
        return electiveCheckRecordMapper.deleteElectiveCheckRecordByIds(ids);
    }

    /**
     * 删除check信息
     * 
     * @param id checkID
     * @return 结果
     */
    @Override
    public int deleteElectiveCheckRecordById(Long id)
    {
        return electiveCheckRecordMapper.deleteElectiveCheckRecordById(id);
    }
}
