package com.ruoguang.dcs.util.html2excle.utils.css.support;


import com.ruoguang.dcs.util.html2excle.utils.css.CssApplier;
import com.ruoguang.dcs.util.html2excle.utils.css.CssUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * border[-[pos][-attr]]: [border-width] || [border-style] || [border-color]; <br>
 * border-style: none | hidden | dotted | dashed | solid | double
 * @version 0.0.1
 * @since 0.0.1
 * author cc cc Shaun Chyxion <br>
 * chyxion@163.com <br>
 * Oct 24, 2014 5:21:51 PM
 */
public class BorderApplier implements CssApplier {
	private static final Logger log = LoggerFactory.getLogger(BorderApplier.class);
	private static final String NONE = "none";
	private static final String HIDDEN = "hidden";
	private static final String SOLID = "solid";
	private static final String DOUBLE = "double";
	private static final String DOTTED = "dotted";
	private static final String DASHED = "dashed";
	// border styles
	private final static String[] BORDER_STYLES = new String[] {
        // Specifies no border	 
         NONE,
        // The same as "none", except in border conflict resolution for table elements
         HIDDEN,
        // Specifies a dotted border	 
         DOTTED,
        // Specifies a dashed border	 
         DASHED,
        // Specifies a solid border	 
         SOLID,
        // Specifies a double border	 
         DOUBLE
	};

	/**
	 * {@inheritDoc}
	 */
    @Override
	public Map<String, String> parse(Map<String, String> style) {
    	Map<String, String> mapRtn = new HashMap<String, String>();
    	for (String pos : new String[] {null, TOP, RIGHT, BOTTOM, LEFT}) {
    		// border[-attr]
    		if (pos == null) {
    			setBorderAttr(mapRtn, pos, style.get(BORDER));
    			setBorderAttr(mapRtn, pos, style.get(BORDER + "-" + COLOR));
    			setBorderAttr(mapRtn, pos, style.get(BORDER + "-" + WIDTH));
    			setBorderAttr(mapRtn, pos, style.get(BORDER + "-" + STYLE));
    		}
    		// border-pos[-attr]
    		else {
    			setBorderAttr(mapRtn, pos, style.get(BORDER + "-" + pos));
    			for (String attr : new String[] {COLOR, WIDTH, STYLE}) {
    				String attrName = BORDER + "-" + pos + "-" + attr;
    				String attrValue = style.get(attrName);
    				if (StringUtils.isNotBlank(attrValue)) {
    					mapRtn.put(attrName, attrValue);
    				}
    			}
    		}
    	}
	    return mapRtn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public void apply(HSSFCell cell, HSSFCellStyle cellStyle, Map<String, String> style) {
    	for (String pos : new String[] {TOP, RIGHT, BOTTOM, LEFT}) {
    		String posName = StringUtils.capitalize(pos.toLowerCase());
    		// color
    		String colorAttr = BORDER + "-" + pos + "-" + COLOR;
    		HSSFColor poiColor = CssUtils.parseColor(cell.getSheet().getWorkbook(), style.get(colorAttr));
    		if (poiColor != null) {
    			try {
	                MethodUtils.invokeMethod(cellStyle, 
	                		"set" + posName + "BorderColor", 
	                		poiColor.getIndex());
                }
                catch (Exception e) {
                	log.error("Set Border Color Error Caused.", e);
                }
    		}
    		// width
    		int width = CssUtils.getInt(style.get(BORDER + "-" + pos + "-" + WIDTH));
    		String styleAttr = BORDER + "-" + pos + "-" + STYLE;
    		String styleValue = style.get(styleAttr);
    		short shortValue = -1;
    		// empty or solid
    		if (StringUtils.isBlank(styleValue) || "solid".equals(styleValue)) {
    			if (width > 2) {
    				shortValue = BorderStyle.THICK.getCode();
    			}
    			else if (width > 1) {
    				shortValue = BorderStyle.MEDIUM.getCode();
    			}
    			else {
    				shortValue = BorderStyle.THIN.getCode();
    			}
    		}
    		else if (ArrayUtils.contains(new String[] {NONE, HIDDEN}, styleValue)) {
    			shortValue = BorderStyle.NONE.getCode();
    		}
    		else if (DOUBLE.equals(styleValue)) {
    			shortValue = BorderStyle.DOUBLE.getCode();
    		}
    		else if (DOTTED.equals(styleValue)) {
    			shortValue = BorderStyle.DOTTED.getCode();
    		}
    		else if (DASHED.equals(styleValue)) {
    			if (width > 1) {
    				shortValue = BorderStyle.MEDIUM_DASHED.getCode();
    			}
    			else {
    				shortValue = BorderStyle.DASHED.getCode();
    			}
    		}
    		// border style
    		if (shortValue != -1) {
    			try {
	                MethodUtils.invokeMethod(cellStyle, 
	                		"setBorder" + posName, 
	                		shortValue);
                }
                catch (Exception e) {
                	log.error("Set Border Style Error Caused.", e);
                }
    		}
    	}
    }

    // --
    // private methods

    private void setBorderAttr(Map<String, String> mapBorder, String pos, String value) {
    	if (StringUtils.isNotBlank(value)) {
    		String borderColor = null;
    		for (String borderAttr : value.split("\\s+")) {
    			if ((borderColor = CssUtils.processColor(borderAttr)) != null) {
    				setBorderAttr(mapBorder, pos, COLOR, borderColor);
    			}
    			else if (CssUtils.isNum(borderAttr)) {
    				setBorderAttr(mapBorder, pos, WIDTH, borderAttr);
    			}
    			else if (isStyle(borderAttr)) {
    				setBorderAttr(mapBorder, pos, STYLE, borderAttr);
    			}
    			else {
    				log.info("Border Attr [{}] Is Not Suppoted.", borderAttr);
    			}
    		}
    	}
    }

    private void setBorderAttr(Map<String, String> mapBorder, String pos,
            String attr, String value) {
    	if (StringUtils.isNotBlank(pos)) {
    		mapBorder.put(BORDER + "-" + pos + "-" + attr, value);
    	}
    	else {
    		for (String name : new String[] {TOP, RIGHT, BOTTOM, LEFT}) {
    			mapBorder.put(BORDER + "-" + name + "-" + attr, value);
    		}
    	}
    }
    
    private boolean isStyle(String value) {
    	return ArrayUtils.contains(BORDER_STYLES, value);
    }
}
