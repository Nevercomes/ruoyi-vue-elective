package com.ruoyi.project.elective.record.mapper;

import com.ruoyi.project.elective.record.domain.ElectiveCheckRecord;
import java.util.List;

/**
 * checkMapper接口
 * 
 * @author Sunss
 * @date 2020-02-11
 */
public interface ElectiveCheckRecordMapper 
{
    /**
     * 查询check
     * 
     * @param id checkID
     * @return check
     */
    public ElectiveCheckRecord selectElectiveCheckRecordById(Long id);

    /**
     * 查询check列表
     * 
     * @param electiveCheckRecord check
     * @return check集合
     */
    public List<ElectiveCheckRecord> selectElectiveCheckRecordList(ElectiveCheckRecord electiveCheckRecord);

    /**
     * 新增check
     * 
     * @param electiveCheckRecord check
     * @return 结果
     */
    public int insertElectiveCheckRecord(ElectiveCheckRecord electiveCheckRecord);

    /**
     * 修改check
     * 
     * @param electiveCheckRecord check
     * @return 结果
     */
    public int updateElectiveCheckRecord(ElectiveCheckRecord electiveCheckRecord);

    /**
     * 删除check
     * 
     * @param id checkID
     * @return 结果
     */
    public int deleteElectiveCheckRecordById(Long id);

    /**
     * 批量删除check
     * 
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteElectiveCheckRecordByIds(Long[] ids);
}
