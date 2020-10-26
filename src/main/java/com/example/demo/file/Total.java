package com.example.demo.file;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

import java.math.BigDecimal;

public class Total {
    public String time;

    @ExcelProperty("load1")
    public BigDecimal cpuload_1 = BigDecimal.ZERO;
    @ExcelIgnore
    public BigDecimal cpuload_5 = BigDecimal.ZERO;
    @ExcelIgnore
    public BigDecimal cpuload_15 = BigDecimal.ZERO;
    @ExcelIgnore
    public BigDecimal total = BigDecimal.ZERO;
    @ExcelIgnore
    public BigDecimal buff_cache = BigDecimal.ZERO;
    @ExcelIgnore
    public BigDecimal used = BigDecimal.ZERO;
    @ExcelIgnore
    public BigDecimal free = BigDecimal.ZERO;
    @ExcelIgnore
    public BigDecimal us = BigDecimal.ZERO;
    @ExcelIgnore
    public BigDecimal sy = BigDecimal.ZERO;
    @ExcelIgnore
    public BigDecimal id = BigDecimal.ZERO;
    @ExcelIgnore
    public BigDecimal wa = BigDecimal.ZERO;
    @ExcelProperty("memoryUtilization")
    public BigDecimal cacheUsed = BigDecimal.ZERO;



    public BigDecimal getCpuload_1() {
        return cpuload_1;
    }

    public void setCpuload_1(BigDecimal cpuload_1) {
        this.cpuload_1 = cpuload_1;
    }

    public BigDecimal getCpuload_5() {
        return cpuload_5;
    }

    public void setCpuload_5(BigDecimal cpuload_5) {
        this.cpuload_5 = cpuload_5;
    }

    public BigDecimal getCpuload_15() {
        return cpuload_15;
    }

    public void setCpuload_15(BigDecimal cpuload_15) {
        this.cpuload_15 = cpuload_15;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getBuff_cache() {
        return buff_cache;
    }

    public void setBuff_cache(BigDecimal buff_cache) {
        this.buff_cache = buff_cache;
    }

    public BigDecimal getUsed() {
        return used;
    }

    public void setUsed(BigDecimal used) {
        this.used = used;
    }

    public BigDecimal getFree() {
        return free;
    }

    public void setFree(BigDecimal free) {
        this.free = free;
    }

    public BigDecimal getUs() {
        return us;
    }

    public void setUs(BigDecimal us) {
        this.us = us;
    }

    public BigDecimal getSy() {
        return sy;
    }

    public void setSy(BigDecimal sy) {
        this.sy = sy;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public BigDecimal getWa() {
        return wa;
    }

    public void setWa(BigDecimal wa) {
        this.wa = wa;
    }

    public BigDecimal getCacheUsed() {
        return cacheUsed;
    }

    public void setCacheUsed(BigDecimal cacheUsed) {
        this.cacheUsed = cacheUsed;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
