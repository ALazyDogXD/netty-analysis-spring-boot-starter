package com.alazydogxd.netty.analysis.message;

/**
 * @author Mr_W
 * @date 2021/9/16 1:08
 * @description 通用报文字段
 */
public class CommonMessageField implements MessageField {

    private int order;

    private int len;

    private String fieldName;

    private Object value;

    private String type;

    private String sort;

    private String uniqueMark;

    @Override
    public int getOrder() {
        return order;
    }

    @Override
    public int getLen() {
        return len;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Object getValue() {
        return value;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public String getSort() {
        return sort;
    }

    @Override
    public String getUniqueMark() {
        return uniqueMark;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setLen(int len) {
        this.len = len;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public void setUniqueMark(String uniqueMark) {
        this.uniqueMark = uniqueMark;
    }
}
