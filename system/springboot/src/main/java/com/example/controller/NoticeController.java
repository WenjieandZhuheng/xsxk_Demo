package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notice;
import com.example.mapper.NoticeMapper;
import com.example.service.NoticeService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notice")

public class NoticeController {

    @Resource
    private NoticeService noticeService;
    @Autowired
    private NoticeMapper noticeMapper;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Notice notice){
        noticeService.add(notice);
        return Result.success();
    }

    /**
     *分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Notice notice,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Notice> pageInfo=  noticeService.selectPage(notice,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody Notice notice) {
        noticeService.updateById(notice);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        noticeService.deleteById(id);
        return Result.success();
    }


    /**
     *查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<Notice> list = noticeService.selectAll();
        return Result.success(list);
    }


}
