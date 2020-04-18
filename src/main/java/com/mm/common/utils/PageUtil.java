package com.mm.common.utils;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页工具类
 *
 * @author lwl
 */
@Data
public class PageUtil implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 响应状态
     */
    private int code = 0;
    /**
     * 每页记录数
     */
    private String msg = "";
    /**
     * 总页数
     */
    private int count;
    /**
     * 列表数据
     */
    private List<?> data;

    /**
     * 分页
     */
    public PageUtil(IPage<?> page) {
        this.data = page.getRecords();
        this.count = (int) page.getTotal();
    }
}
