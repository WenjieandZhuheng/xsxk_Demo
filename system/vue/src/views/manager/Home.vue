<template>
  <div>

    <div class="card" style="line-height: 30px">
      <div>欢迎您，{{ data.user.name }} 祝您今天过得开心！</div>
    </div>
    <div style="display: flex">
      <div class="card" style="flex: 50%">
        <div style="font-size: 17px; font-weight: bold; padding: 10px 10px 20px 10px">系统公告</div>
        <el-timeline>
          <el-timeline-item
              v-for="(item, index) in data.noticeData"
              :key="index"
              :timestamp="item.time"
          >
            {{ item.content }}
          </el-timeline-item>
        </el-timeline>
      </div>
      <div style="flex: 50%; margin-left: 5px" class="card">
        <!--        <div>-->
        <!--          <span style="font-weight: bold">作者：</span>-->
        <!--          <span><a href="https://space.bilibili.com/432113931" target="_blank">武哥聊编程</a></span>-->
        <!--        </div>-->
        <div style="margin-top: 10px">
          <span style="font-weight: bold">项目标题：</span>
          <span>学生选课管理系统</span>
        </div>
        <div style="margin-top: 10px">
          <span style="font-weight: bold">项目技术栈：</span>
          <span>Springboot3、MyBatis、Vue3、Element-Plus、MySQL</span>
        </div>
        <div style="margin-top: 10px">
          <span style="font-weight: bold">项目功能描述：</span>
          <span>xxxx</span>
        </div>
        <div style="margin-top: 10px">项目功能描述：</div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { reactive } from "vue";
import request from "@/utils/request";
import {ElMessage} from "element-plus";

const data = reactive({
  user: JSON.parse(localStorage.getItem('system-user') || '{}'),
  noticeData: []
})

const loadNotice = () => {
  request.get('/notice/selectAll').then(res => {
    if (res.code === '200') {
      data.noticeData = res.data
    } else {
      ElMessage.error(res.msg)
    }
  })
}

loadNotice()
</script>