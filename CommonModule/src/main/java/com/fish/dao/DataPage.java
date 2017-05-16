package com.fish.dao;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 与具体ORM实现无关的分页参数及查询结果封装.
 * 注意所有序号从1开始.
 *
 * @param <T> Page中记录的类型.
 */
public class DataPage<T> implements Serializable {
    private static final long serialVersionUID = -7021480900278637440L;

    //-- 公共变量 --//
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    //-- 分页参数 --//
    private int currentPage = 1;
    private int pageSize = 10;
    private String orderBy = null;
    private String order = null;
    private boolean needData = true;
    private boolean needTotalCount = true;

    //-- 返回结果 --//
    private List<T> dataList = null;
    private long totalCount = -1;
    
    private List<Integer> pageList;

    //-- 构造函数 --//
    public DataPage() {
    }

    public DataPage(int pageSize) {
        this.pageSize = pageSize;
    }

    public DataPage(int currentPage, int pageSize) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public DataPage(boolean needData, boolean needTotalCount) {
        this.needData = needData;
        this.needTotalCount = needTotalCount;
    }

    //-- 分页参数访问函数 --//

    /**
     * 获得当前页的页号,序号从1开始,默认为1.
     */
    public int getCurrentPage() {
        return currentPage;
    }

    /**
     * 设置当前页的页号,序号从1开始,低于1时自动调整为1.
     */
    public void setCurrentPage(final int currentPage) {
        this.currentPage = currentPage;

        if (currentPage < 1) {
            this.currentPage = 1;
        }
    }

    /**
     * 返回Page对象自身的setCurrentPage函数,可用于连续设置。
     */
    public DataPage<T> currentPage(final int theCurrentPage) {
        setCurrentPage(theCurrentPage);
        return this;
    }

    /**
     * 获得每页的记录数量, 默认为-1.
     */
    public int getPageSize() {
        return pageSize;
    }

    /**
     * 设置每页的记录数量.
     */
    public void setPageSize(final int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 返回Page对象自身的setPageSize函数,可用于连续设置。
     */
    public DataPage<T> pageSize(final int thePageSize) {
        setPageSize(thePageSize);
        return this;
    }

    /**
     * 根据CurrentPage和pageSize计算当前页第一条记录在总结果集中的位置,序号从1开始.
     */
    public int getFirst() {
        return ((currentPage - 1) * pageSize);
    }

    /**
     * 获得排序字段,无默认值. 多个排序字段时用','分隔.
     */
    public String getOrderBy() {
        return orderBy;
    }

    /**
     * 设置排序字段,多个排序字段时用','分隔.
     */
    public void setOrderBy(final String orderBy) {
        this.orderBy = orderBy;
    }

    /**
     * 返回Page对象自身的setOrderBy函数,可用于连续设置。
     */
    public DataPage<T> orderBy(final String theOrderBy) {
        setOrderBy(theOrderBy);
        return this;
    }

    /**
     * 获得排序方向, 无默认值.
     */
    public String getOrder() {
        return order;
    }

    /**
     * 设置排序方式向.
     *
     * @param order 可选值为desc或asc,多个排序字段时用','分隔.
     */
    public void setOrder(final String order) {
        String lowcaseOrder = StringUtils.lowerCase(order);

        //检查order字符串的合法值
        String[] orders = StringUtils.split(lowcaseOrder, ',');
        if (orders == null) {
            return;
        }
        for (String orderStr : orders) {
            if (!StringUtils.equals(DESC, orderStr) && !StringUtils.equals(ASC, orderStr)) {
                throw new IllegalArgumentException("排序方向" + orderStr + "不是合法值");
            }
        }

        this.order = lowcaseOrder;
    }

    /**
     * 返回Page对象自身的setOrder函数,可用于连续设置。
     */
    public DataPage<T> order(final String theOrder) {
        setOrder(theOrder);
        return this;
    }

    /**
     * 是否已设置排序字段,无默认值.
     */
    public boolean isOrderBySetted() {
        return (StringUtils.isNotBlank(orderBy) && StringUtils.isNotBlank(order));
    }

    /**
     * 获得查询对象时是否先自动执行count查询获取总记录数, 默认为false.
     */
    public boolean isNeedTotalCount() {
        return needTotalCount;
    }

    public boolean isNeedData() {
        return needData;
    }

    public void setNeedData(boolean needData) {
        this.needData = needData;
    }

    /**
     * 设置查询对象时是否自动先执行count查询获取总记录数.
     */
    public void setNeedTotalCount(final boolean autoCount) {
        this.needTotalCount = autoCount;
    }

    /**
     * 返回Page对象自身的setAutoCount函数,可用于连续设置。
     */
    public DataPage<T> needTotalCount(final boolean theAutoCount) {
        setNeedTotalCount(theAutoCount);
        return this;
    }

    //-- 访问查询结果函数 --//

    /**
     * 获得页内的记录列表.
     */
    public List<T> getDataList() {
        return dataList;
    }

    /**
     * 设置页内的记录列表.
     */
    public void setDataList(final List<T> result) {
        this.dataList = result;
    }

    /**
     * 获得总记录数, 默认值为-1.
     */
    public long getTotalCount() {
        return totalCount;
    }

    /**
     * 设置总记录数.
     */
    public void setTotalCount(final long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * 根据pageSize与totalCount计算总页数, 默认值为-1.
     */
    public long getTotalPages() {
        if (totalCount < 0) {
            return -1;
        }
        pageSize = pageSize < 1 ? 10 : pageSize;
        long count = totalCount / pageSize;
        if (totalCount % pageSize > 0) {
            count++;
        }
        return count;
    }

    /**
     * 是否还有下一页.
     */
    public boolean isHasNext() {
        return (currentPage + 1 <= getTotalPages());
    }

    /**
     * 取得下页的页号, 序号从1开始.
     * 当前页为尾页时仍返回尾页序号.
     */
    public int getNextPage() {
        if (isHasNext()) {
            return currentPage + 1;
        } else {
            return currentPage;
        }
    }

    public int getEndIndex() {
        return currentPage * pageSize;
    }

    public int getStartIndex() {
        return this.getFirst();
    }

    /**
     * 是否还有上一页.
     */
    public boolean isHasPrev() {
        return (currentPage - 1 >= 1);
    }

    /**
     * 取得上页的页号, 序号从1开始.
     * 当前页为首页时返回首页序号.
     */
    public int getPrevPage() {
        if (isHasPrev()) {
            return currentPage - 1;
        } else {
            return currentPage;
        }
    }
    
    public List<Integer> getPageList() {
        pageList = new ArrayList<>();
        if(getTotalPages()<8){ 
            for(int i=1;i<=getTotalPages();i++){    
                pageList.add(i);
            }
        }else{  
            if(getCurrentPage()<5){
                for(int i=1;i<=8;i++){        
                    pageList.add(i);
                }
            }else{
                if(getTotalPages()-getCurrentPage()<8){
                    for(int i=(int)getTotalPages()-7;i<=getTotalPages();i++){
                        pageList.add(i);
                    }
                }else{            
                    for(int i=getCurrentPage()-3;i<=getCurrentPage()+4;i++){            
                        pageList.add(i);
                    }
                }
            }
        }
        
        return pageList;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("DataPage{");
        sb.append("currentPage=").append(currentPage);
        sb.append(", pageSize=").append(pageSize);
        sb.append(", orderBy='").append(orderBy).append('\'');
        sb.append(", order='").append(order).append('\'');
        sb.append(", needData=").append(needData);
        sb.append(", needTotalCount=").append(needTotalCount);
        sb.append(", dataList=").append(dataList);
        sb.append(", totalCount=").append(totalCount);
        sb.append(", pageList=").append(pageList);
        sb.append('}');
        return sb.toString();
    }
}
