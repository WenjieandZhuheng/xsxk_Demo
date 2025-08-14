package com.example.service;

import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Account;
import com.example.entity.Notice;
import com.example.exception.CustomException;
import com.example.mapper.NoticeMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 朱恒
 * @version 1.0
 * @description: TODO
 * @date 2025/6/8 15:36
 */


/**
 *  教师信息业务层处理
 */
@Service
public class NoticeService {

    @Resource
    private NoticeMapper noticeMapper;

    /**
     * 新增
     */
    public void add(Notice notice) {
        notice.setTime(DateUtil.now());
        noticeMapper.insert(notice);
    }

    public PageInfo<Notice> selectPage(Notice notice,Integer pageNum, Integer pageSize) {
        List<Notice> list;
        PageHelper.startPage(pageNum,pageSize);
        if (ObjectUtil.isNotEmpty(notice.getTitle())){
            list = noticeMapper.selectByTitle(notice.getTitle());
        }else {
            list = noticeMapper.selectAll();
        }
        return PageInfo.of(list);
    }

    public void updateById(Notice notice) {
        noticeMapper.updateById(notice);
    }

    public void deleteById(Integer id) {
        noticeMapper.deleteById(id);
    }

    public List<Notice> selectAll() {
        return noticeMapper.selectAll();
    }
}
