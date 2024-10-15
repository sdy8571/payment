<template>
  <ContentWrap>
    <el-form :model="queryParams" ref="queryFormRef" label-position="right" :inline="true"
             label-width="68px" class="-mb-15px">
      <el-form-item label="应用名称" prop="name">
        <el-input v-model="queryParams.name"
                  placeholder="请输入应用名称"
                  clearable
                  maxlength="50"
                  @keyup.enter="handleQuery"
        />
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
        <el-button type="primary" plain @click="openForm('create')">
          <Icon icon="Plus"/>
          新增
        </el-button>
      </el-form-item>
    </el-form>
  </ContentWrap>
  <ContentWrap>
    <el-table :data="list">
      <el-table-column min-width="80" label="应用编号" align="center" prop="id"/>
      <el-table-column min-width="80" label="应用名" align="center" prop="name"/>
      <el-table-column min-width="150" label="状态" align="center" prop="status">
        <template #default="scope">
          <el-tag>{{ COMMON_ENUMS.STATUS[scope.row.status] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column min-width="150" label="备注" align="center" prop="remark"/>
      <el-table-column min-width="120" label="创建时间" align="center" prop="createTime"
                       :formatter="dateFormatter"/>
      <el-table-column width="150" label="操作" align="center" fixed="right">
        <template #default="scope">
          <el-button type="primary" link @click="openForm('update', scope.row.id)">
            编辑
          </el-button>
          <el-button type="danger" link @click="handleDelete(scope.row.id)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页 -->
    <Pagination
      :total="total"
      v-model:page="queryParams.pageNo"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </ContentWrap>

  <AppForm ref="formRef" @success="getList"/>
</template>
<script setup lang="ts">
  import { showLoading } from '@/views/composables';
  import { COMMON_ENUMS } from '@/views/contants/enums/Common';
  import { dateFormatter } from '@/utils/formatTime';
  import * as AppApi from '@/api/app';
  import AppForm from './AppForm.vue';

  defineOptions({name: 'App'})

  const {t} = useI18n()
  const message = useMessage()

  const total = ref(0)
  const list = ref([])
  const queryFormRef = ref()
  const queryParams = reactive({
    pageNo: 1,
    pageSize: 10,
    name: undefined,
  })
  const formRef = ref()

  onMounted(async () => {
    await getList()
  })

  /**获取列表*/
  const getList = async () => {
    let loading = showLoading()
    try {
      const data = await AppApi.getPage(queryParams);
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

  const openForm = (type: string, id?: number) => {
    formRef.value.open(type, id)
  }

  const handleDelete = async (id: number) => {
    try {
      // 二次确认
      await message.delConfirm()
      // 删除
      await AppApi.remove(id)
      message.success(t('common.delSuccess'))
      // 刷新列表
      await getList()
    } catch {
    }
  }

</script>
<style scoped>

</style>
