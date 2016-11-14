package com.jkm.dao;

import com.jkm.entity.SendMessageCountRecord;
import org.apache.ibatis.annotations.Param;

/**
 * 
 * SendMessageCountRecordMapper数据库操作接口类
 * 
 **/

public interface SendMessageCountRecordDao {
	/**
	 * 根据sn查询输入验证码次数
	 * @param id
	 * @return
	 */
	int selctCountBySn(@Param("sn") long sn);

	/**
	 * 
	 * 添加 （匹配有值的字段）
	 * 
	 **/
	int insertSelective(SendMessageCountRecord record);



}