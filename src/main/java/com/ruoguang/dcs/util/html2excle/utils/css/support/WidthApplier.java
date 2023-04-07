package com.ruoguang.dcs.util.html2excle.utils.css.support;


import com.ruoguang.dcs.util.html2excle.utils.css.CssApplier;
import com.ruoguang.dcs.util.html2excle.utils.css.CssUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFSheet;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 0.0.1
 * @since 0.0.1
 * author cc cc Shaun Chyxion <br>
 * chyxion@163.com <br>
 * Oct 24, 2014 5:14:22 PM
 */
public class WidthApplier implements CssApplier {

	/**
	 * {@inheritDoc}
	 */
    @Override
	public Map<String, String> parse(Map<String, String> style) {
    	Map<String, String> mapRtn = new HashMap<String, String>();
    	String width = style.get(WIDTH);
    	if (CssUtils.isNum(width)) {
    		mapRtn.put(WIDTH, width);
    	}
	    return mapRtn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public void apply(HSSFCell cell, HSSFCellStyle cellStyle, Map<String, String> style) {
    	int width = Math.round(CssUtils.getInt(style.get(WIDTH)) * 2048 / 8.43F);
    	HSSFSheet sheet = cell.getSheet();
    	int colIndex = cell.getColumnIndex();
    	if (width > sheet.getColumnWidth(colIndex)) {
    		if (width > 255 * 256) {
    			width = 255 * 256;
    		}
    		sheet.setColumnWidth(colIndex, width);
    	}
    }
}
