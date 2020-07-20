package com.intellijcloud.lesscode.test.app.dal.dataObject;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConfigDO extends BaseDO {
    private Long id;

    private String configType;

    private String keyName;

    private String configValue;

    private String configDesc;

    private String ext1;

    private String isDeleted;

    private String creator;

    private Date gmtCreated;

    private String modifier;

    private Date gmtModified;
}