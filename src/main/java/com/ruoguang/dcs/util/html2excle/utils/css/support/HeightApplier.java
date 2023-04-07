package com.ruoguang.dcs.util.html2excle.utils.css.support;


import com.ruoguang.dcs.util.html2excle.utils.css.CssApplier;
import com.ruoguang.dcs.util.html2excle.utils.css.CssUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 0.0.1
 * @since 0.0.1
 * author cc cc Shaun Chyxion <br>
 * chyxion@163.com <br>
 * Oct 24, 2014 5:18:57 PM
 */
public class HeightApplier implements CssApplier {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public Map<String, String> parse(Map<String, String> style) {
    	Map<String, String> mapRtn = new HashMap<String, String>();
    	String height = style.get(HEIGHT);
    	if (CssUtils.isNum(height)) {
    		mapRtn.put(HEIGHT, height);
    	}
	    return mapRtn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public void apply(HSSFCell cell, HSSFCellStyle cellStyle, Map<String, String> style) {
    	int height = Math.round(CssUtils.getInt(style.get(HEIGHT)) * 255 / 12.75F);
    	HSSFRow row = cell.getRow();
    	if (height > row.getHeight()) {
    		row.setHeight((short) height);
    	}
    }
}
