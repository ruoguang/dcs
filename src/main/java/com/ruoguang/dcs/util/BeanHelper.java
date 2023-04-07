package com.ruoguang.dcs.util;


import com.ruoguang.dcs.enums.BusinessExceptionEnum;
import com.ruoguang.dcs.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * BeanHelper
 * 对象转换工具
 *
 * @author cc
 * @date 2020/06/01
 */
@Slf4j
@SuppressWarnings("all")
public class BeanHelper {

    public static <T> T copyProperties(Object source, Class<T> target) {
        try {
            T t = target.newInstance();
            BeanUtils.copyProperties(source, t);
            return t;
        } catch (IllegalArgumentException e) {
            log.error("【数据转换】数据源不存在{}", e.getMessage());
            throw new BusinessException(BusinessExceptionEnum.SOURCE_IS_NULL);
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new BusinessException(BusinessExceptionEnum.DATA_TRANSFER_ERROR);
        }
    }

    public static <T> List<T> copyWithCollection(List<?> sourceList, Class<T> target) {
        try {
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            log.error("【数据转换】数据源不存在{}", e.getMessage());
            throw new BusinessException(BusinessExceptionEnum.SOURCE_IS_NULL);
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new BusinessException(BusinessExceptionEnum.DATA_TRANSFER_ERROR);
        }
    }

    public static <T> Set<T> copyWithCollection(Set<?> sourceList, Class<T> target) {
        try {
            return sourceList.stream().map(s -> copyProperties(s, target)).collect(Collectors.toSet());
        } catch (IllegalArgumentException e) {
            log.error("【数据转换】数据源不存在{}", e.getMessage());
            throw new BusinessException(BusinessExceptionEnum.SOURCE_IS_NULL);
        } catch (Exception e) {
            log.error("【数据转换】数据转换出错，目标对象{}构造函数异常", target.getName(), e);
            throw new BusinessException(BusinessExceptionEnum.DATA_TRANSFER_ERROR);
        }
    }
}
