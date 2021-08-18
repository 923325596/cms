package com.xzjie.cms.model;

import com.xzjie.cms.enums.Sorting;
import lombok.Data;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;

@Data
public abstract class BaseEntity<T extends Object> implements Serializable {

    private Sorting sorting;

    public abstract void copy(T obj);

    protected void copyProperties(Object source, Object target) {
        BeanUtils.copyProperties(source, target, getIgnoreProperty(source));
    }

    public static String[] getIgnoreProperty(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<String>();
        for (java.beans.PropertyDescriptor pd : pds) {
            Object srcValue = src.getPropertyValue(pd.getName());
            if (srcValue == null) emptyNames.add(pd.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
