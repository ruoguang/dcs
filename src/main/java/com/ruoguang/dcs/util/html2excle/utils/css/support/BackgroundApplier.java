package com.ruoguang.dcs.util.html2excle.utils.css.support;


import com.ruoguang.dcs.util.html2excle.utils.css.CssApplier;
import com.ruoguang.dcs.util.html2excle.utils.css.CssUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 0.0.1
 * @since 0.0.1
 * author cc cc Shaun Chyxion <br>
 * chyxion@163.com <br>
 * Oct 24, 2014 5:03:32 PM
 */
public class BackgroundApplier implements CssApplier {

	/**
	 * {@inheritDoc}
	 */
    public Map<String, String> parse(Map<String, String> style) {
    	Map<String, String> mapRtn = new HashMap<String, String>();
    	String bg = style.get(BACKGROUND);
    	String bgColor = null;
    	if (StringUtils.isNotBlank(bg)) {
    		for (String bgAttr : bg.split("(?<=\\)|\\w|%)\\s+(?=\\w)")) {
    			if ((bgColor = CssUtils.processColor(bgAttr)) != null) {
    				mapRtn.put(BACKGROUND_COLOR, bgColor);
    				break;
    			}
    		}
    	}
    	bg = style.get(BACKGROUND_COLOR);
    	if (StringUtils.isNotBlank(bg) && 
    			(bgColor = CssUtils.processColor(bg)) != null) {
    		mapRtn.put(BACKGROUND_COLOR, bgColor);
    		
    	}
    	if (bgColor != null) {
    		bgColor = mapRtn.get(BACKGROUND_COLOR);
    		if ("#ffffff".equals(bgColor)) {
    			mapRtn.remove(BACKGROUND_COLOR);
    		}
    	}
	    return mapRtn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public void apply(HSSFCell cell, HSSFCellStyle cellStyle, Map<String, String> style) {
    	String bgColor = style.get(BACKGROUND_COLOR);
    	if (StringUtils.isNotBlank(bgColor)) {
    		cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
    		cellStyle.setFillForegroundColor(
    			CssUtils.parseColor(cell.getSheet().getWorkbook(), 
    					bgColor).getIndex());
    	}
    }
}
