package com.ruoguang.dcs.pojo.vo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class Base64ResultSet {

    private int size;
    private List<Base64RsultDetail> base64RsultDetails;


    public Base64ResultSet() {
        size = 0;
        base64RsultDetails = new ArrayList<>();
    }

    public boolean add(String base64Str) {
        if (StringUtils.isBlank(base64Str)) {
            return false;
        }
        base64RsultDetails.add(new Base64RsultDetail(++size, base64Str));
        return true;
    }
}
