package com.intellijcloud.lesscode.test.app.dal.mapper;

import com.intellijcloud.lesscode.test.app.dal.dataObject.ConfigDO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ConfigDOMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ConfigDO record);

    int insertSelective(ConfigDO record);

    ConfigDO selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(ConfigDO record);

    int updateByPrimaryKey(ConfigDO record);

    int batchInsert(@Param("list") List<ConfigDO> list);

    List<ConfigDO> selectByCondition(ConfigDO params);
}