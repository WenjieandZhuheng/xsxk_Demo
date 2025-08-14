package com.example.controller;

import com.example.common.Result;
import com.example.entity.Speciality;
import com.example.mapper.SpecialityMapper;
import com.example.service.SpecialityService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/speciality")

public class SpecialityController {

    @Resource
    private SpecialityService specialityService;
    @Autowired
    private SpecialityMapper specialityMapper;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Speciality speciality){
        specialityService.add(speciality);
        return Result.success();
    }

    /**
     *分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Speciality speciality,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "5") Integer pageSize){
        PageInfo<Speciality> pageInfo=  specialityService.selectPage(speciality,pageNum,pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 更新
     */
    @PutMapping("/update")
    public Result update(@RequestBody Speciality speciality) {
        specialityService.updateById(speciality);
        return Result.success();
    }

    /**
     * 删除
     */
    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        specialityService.deleteById(id);
        return Result.success();
    }


    /**
     *查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(){
        List<Speciality> list = specialityService.selectAll();
        return Result.success(list);
    }


}
