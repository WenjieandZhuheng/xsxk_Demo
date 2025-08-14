package com.example.controller;

import com.example.common.Result;
import com.example.entity.Teacher;
import com.example.mapper.TeacherMapper;
import com.example.service.TeacherService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teacher")

public class TeacherController {

    @Resource
    private TeacherService teacherService;
    @Autowired
    private TeacherMapper teacherMapper;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Teacher teacher){
        teacherService.add(teacher);
        return Result.success();
    }

    /**
     *分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Teacher teacher,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Teacher> pageInfo=  teacherService.selectPage(teacher,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    /**
     *查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Teacher teacher){
        List<Teacher> list=  teacherService.selectAll();
        return Result.success(list);
    }
    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody Teacher teacher) {
        teacherService.updateById(teacher);
        return Result.success();
    }


    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        teacherService.deleteById(id);
        return Result.success();
    }


}
