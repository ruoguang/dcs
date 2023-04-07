package com.ruoguang.dcs.pojo.vo;

import lombok.Data;

@Data
public class ResponseVO {
    private String token;
    private String sign;
    private String md5;
    private String signData;
    private String signString;
    private String reposebody;
}

