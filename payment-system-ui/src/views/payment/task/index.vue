<template>
  <ContentWrap>
    <el-form :model="queryParams" ref="queryFormRef" label-position="right" :inline="true"
             label-width="68px" class="form-inline-search">
      <el-form-item label="应用名称" prop="appName">
        <el-input v-model="queryParams.appName"
                  placeholder="请输入应用名称"
                  clearable
                  maxlength="50"
                  @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="任务类型" prop="type">
        <el-select v-model="queryParams.type" clearable placeholder="请选择任务类型">
          <el-option label="支付" :value="1"/>
          <el-option label="退款" :value="2"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button @click="handleQuery">
          <Icon icon="Search"/>
          搜索
        </el-button>
        <el-button @click="resetQuery">
          <Icon icon="Refresh"/>
          重置
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>
  <ContentWrap>
    <el-table :data="list">
      <el-table-column min-width="80" label="编号" align="center" prop="id"/>
      <el-table-column min-width="80" label="应用名" align="center" prop="appName"/>
      <el-table-column min-width="130" label="任务类型" align="center" prop="type">
        <template #default="scope">
          <el-tag>{{scope.row.type === 1 ? '支付' : '退款'}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column min-width="130" label="支付单号" align="center" prop="merchantOrderId"/>
      <el-table-column min-width="130" label="退款单号" align="center" prop="merchantRefundId"/>
      <el-table-column min-width="120" label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag>{{ COMMON_ENUMS.TASK_STATUS[scope.row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column min-width="130" label="通知次数" align="center" prop="notifyTimes"/>
      <el-table-column min-width="150" label="最后通知时间" align="center" prop="nextNotifyTime"
                       :formatter="dateFormatter"/>
      <el-table-column min-width="150" label="更新时间" align="center" prop="createTime"
                       :formatter="dateFormatter"/>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>
</template>

<script setup lang="ts">
import { showLoading } from '@/views/composables';
import {dateFormatter} from "@/utils/formatTime";
import {COMMON_ENUMS} from "@/views/contants/enums/Common";
import * as TaskApi from "@/api/task";

defineOptions({name: 'Task'})

const {t} = useI18n()
const message = useMessage()

const total = ref(0)
const list = ref([])
const queryFormRef = ref()
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  appName: undefined,
  type: undefined,
})

onMounted(async () => {
  await getList()
})

/**获取列表*/
const getList = async () => {
  let loading = showLoading()
  try {
    const data = await TaskApi.getPage(queryParams);
    list.value = data.list;
    total.value = data.total;
  } finally {
    loading.close()
  }
}
/**查询*/
const handleQuery = () => {
  queryParams.pageNo = 1
  getList()
}
/**重置*/
const resetQuery = () => {
  queryFormRef.value.resetFields()
  handleQuery()
}

</script>

<style scoped>

</style>
