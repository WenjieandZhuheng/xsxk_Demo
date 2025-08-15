package com.example.service;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import com.example.entity.Choice;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.exception.CustomException;
import com.example.mapper.ChoiceMapper;
import com.example.mapper.CourseMapper;
import com.example.mapper.StudentMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ChoiceService {

    @Resource
    private ChoiceMapper choiceMapper;
    @Resource
    private CourseMapper courseMapper;
    @Autowired
    private StudentMapper studentMapper;

    /**
     * 新增
     */
    @Transactional(rollbackFor = Exception.class)
    public void add(Course course) {
        // 1. 获取课程的实际选课人数，确保数据准确性
        List<Choice> currentChoices = choiceMapper.selectByCourseId(course.getId());
        int actualCurrentCount = currentChoices.size();

        // 2. 先判断这个课程有没有满员（基于实际统计）
        if (actualCurrentCount >= course.getNum()) {
            throw new CustomException("该门课已经满员，请选择其他课程");
        }

        // 3. 判断该学生有没有选过该门课
        List<Choice> list = choiceMapper.selectByCourseIdAndStudentId(course.getId(), course.getStudentId());
        if (CollectionUtil.isNotEmpty(list)) {
            throw new CustomException("您已经选过该门课了，请给其他小伙伴留点机会吧！");
        }

        // 4. 往选课信息表里面插入一条选课记录
        Choice choice = new Choice();
        choice.setName(course.getName());
        choice.setTeacherId(course.getTeacherId());
        choice.setStudentId(course.getStudentId());
        choice.setCourseId(course.getId());
        choiceMapper.insert(choice);

        // 5. 更新课程已选人数（基于实际统计+1）
        course.setAlreadyNum(actualCurrentCount + 1);
        courseMapper.updateById(course);

        // 6. 该学生学分增加
        Student student = studentMapper.selectById(course.getStudentId());
        student.setScore(student.getScore() + course.getScore());
        studentMapper.updateById(student);
    }

    public PageInfo<Choice> selectPage(Choice choice, Integer pageNum, Integer pageSize) {
        List<Choice> list;
        PageHelper.startPage(pageNum, pageSize);
        if (ObjectUtil.isNotEmpty(choice.getStudentId())) {
            // 说明这是学生登录进行分页查询
            if (ObjectUtil.isNotEmpty(choice.getName())) {
                list = choiceMapper.selectByNameAndStudentId(choice.getName(), choice.getStudentId());
            } else {
                list = choiceMapper.selectAllByStudentId(choice.getStudentId());
            }
        } else if (ObjectUtil.isNotEmpty(choice.getTeacherId())) {
            // 说明这是教师登录进行分页查询
            if (ObjectUtil.isNotEmpty(choice.getName())) {
                list = choiceMapper.selectByNameAndTeacherId(choice.getName(), choice.getTeacherId());
            } else {
                list = choiceMapper.selectAllByTeacherId(choice.getTeacherId());
            }
        } else {
            // 说明这是管理员登录进行分页查询
            if (ObjectUtil.isNotEmpty(choice.getName())) {
                list = choiceMapper.selectByName(choice.getName());
            } else {
                list = choiceMapper.selectAll();
            }
        }
        return PageInfo.of(list);
    }

    public void updateById(Choice choice) {
        choiceMapper.updateById(choice);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteById(Integer id) {
        Choice choice = choiceMapper.selectById(id);
        if (choice == null) {
            throw new CustomException("选课记录不存在");
        }

        // 获取课程信息
        Course course = courseMapper.selectById(choice.getCourseId());
        if (course == null) {
            throw new CustomException("关联的课程不存在");
        }

        // 获取学生信息
        Student student = studentMapper.selectById(choice.getStudentId());
        if (student == null) {
            throw new CustomException("关联的学生不存在");
        }

        // 先计算删除前的实际选课人数，确保数据准确性
        List<Choice> currentChoices = choiceMapper.selectByCourseId(course.getId());
        int actualCurrentCount = currentChoices.size();

        // 删除选课记录
        choiceMapper.deleteById(id);

        // 更新课程已选人数：基于实际统计减1，确保不会变成负数
        int newCount = Math.max(0, actualCurrentCount - 1);
        course.setAlreadyNum(newCount);
        courseMapper.updateById(course);

        // 更新学生学分：确保不会变成负数
        if (student.getScore() >= course.getScore()) {
            student.setScore(student.getScore() - course.getScore());
        } else {
            // 如果学分不足，设置为0
            student.setScore(0);
        }
        studentMapper.updateById(student);
    }

    public List<Choice> selectAll() {
        return choiceMapper.selectAll();
    }

    /**
     * 修复所有课程的已选人数数据
     * 重新计算每门课程的实际选课人数
     */
    @Transactional(rollbackFor = Exception.class)
    public void fixCourseAlreadyNum() {
        // 获取所有课程
        List<Course> allCourses = courseMapper.selectAll();
        int fixedCount = 0;
        int totalCourses = allCourses.size();

        System.out.println("开始修复课程已选人数数据，共 " + totalCourses + " 门课程");

        for (Course course : allCourses) {
            // 统计该课程的实际选课人数
            List<Choice> choices = choiceMapper.selectByCourseId(course.getId());
            int actualCount = choices.size();

            // 如果数据不一致，则更新
            if (actualCount != course.getAlreadyNum()) {
                int oldCount = course.getAlreadyNum();
                course.setAlreadyNum(actualCount);
                courseMapper.updateById(course);
                fixedCount++;
                System.out.println("修复课程 [" + course.getName() + "] 的已选人数：" + oldCount + " -> " + actualCount);
            }
        }

        System.out.println("课程已选人数数据修复完成！共修复 " + fixedCount + " 门课程，总计 " + totalCourses + " 门课程");
    }
}
