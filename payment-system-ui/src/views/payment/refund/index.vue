<template>
  <ContentWrap>
    <el-form :model="queryParams" ref="queryFormRef" label-position="right" :inline="true"
             label-width="68px" class="form-inline-search">
      <el-form-item label="应用名称" prop="appId">
        <el-select v-model="queryParams.appId" clearable placeholder="请选择应用" @change="changeApp">
          <el-option v-for="item in appList" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="支付渠道" prop="channelId">
        <el-select v-model="queryParams.channelId" clearable placeholder="请选择支付渠道">
          <el-option v-for="item in channelList" :key="item.id" :label="item.channelName" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="业务单号" prop="merchantOrderNo">
        <el-input v-model="queryParams.merchantOrderNo" placeholder="请输入业务订单号" clearable
                  maxlength="50" @keyup.enter="handleQuery"/>
      </el-form-item>
      <el-form-item label="业务退单号" prop="merchantRefundNo" label-width="85px">
        <el-input v-model="queryParams.merchantRefundNo" placeholder="请输入业务退款单号" clearable
                  maxlength="50" @keyup.enter="handleQuery"/>
      </el-form-item>
      <el-form-item label="渠道单号" prop="transactionId">
        <el-input v-model="queryParams.transactionId" placeholder="请输入渠道订单号" clearable
                  maxlength="50" @keyup.enter="handleQuery"/>
      </el-form-item>
      <el-form-item label="渠道退单号" prop="refundId" label-width="85px">
        <el-input v-model="queryParams.refundId" placeholder="请输入渠道退款单号" clearable
                  maxlength="50" @keyup.enter="handleQuery"/>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" clearable placeholder="请选择订单状态">
          <el-option v-for="item in Object.keys(COMMON_ENUMS.REFUND_STATUS)"
                     :key="item" :value="item"
                     :label="COMMON_ENUMS.REFUND_STATUS[item]" />
        </el-select>
      </el-form-item>
      <el-form-item label="退款时间" prop="successTime">
        <el-date-picker v-model="queryParams.successTime"
                        type="daterange"
                        unlink-panels
                        range-separator="至"
                        value-format="YYYY-MM-DD"
                        start-placeholder="开始日期"
                        end-placeholder="结束日期"
                        :shortcuts="shortcuts"
                        @change="changeSuccessTime" />
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
      <el-table-column min-width="153" label="业务退款单号" align="center" prop="outRefundNo">
        <template #default="scope">
          <el-button type="primary" link @click="openDetail(scope.row.id)">
            {{scope.row.outRefundNo}}
          </el-button>
        </template>
      </el-table-column>
      <el-table-column min-width="153" label="渠道退款单号" align="center" prop="refundId"/>
      <el-table-column min-width="153" label="业务订单号" align="center" prop="outTradeNo"/>
      <el-table-column min-width="153" label="渠道订单号" align="center" prop="transactionId"/>
      <el-table-column min-width="80" label="应用名" align="center" prop="appName"/>
      <el-table-column min-width="80" label="退款渠道" align="center" prop="channelName"/>
      <el-table-column min-width="80" label="支付金额" align="center" prop="payPriceY">
        <template #default="scope">
          ￥{{scope.row.payPriceY}}
        </template>
      </el-table-column>
      <el-table-column min-width="80" label="退款金额" align="center" prop="refundPriceY">
        <template #default="scope">
          ￥{{scope.row.refundPriceY}}
        </template>
      </el-table-column>
      <el-table-column min-width="80" label="状态" align="center" prop="refundStatus">
        <template #default="scope">
          <el-tag>{{ COMMON_ENUMS.REFUND_STATUS[scope.row.refundStatus] }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column min-width="120" label="退款时间" align="center" prop="successTime"
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

  <refund-detail ref="detailRef"/>
</template>

<script setup lang="ts">
import { showLoading } from '@/views/composables';
import {dateFormatter} from "@/utils/formatTime";
import {COMMON_ENUMS} from "@/views/contants/enums/Common";
import * as RefundApi from "@/api/refund";
import * as ChannelApi from "@/api/channel";
import * as AppApi from "@/api/app";
import mixins from "@/utils/mixins";
import RefundDetail from "./detail.vue";

defineOptions({name: 'Refund'})

const {t} = useI18n()
const message = useMessage()

const total = ref(0)
const list = ref([])
const queryFormRef = ref()
const queryParams = reactive({
  pageNo: 1,
  pageSize: 10,
  appId: undefined,
  channelId: undefined,
  merchantOrderNo: undefined,
  merchantRefundNo: undefined,
  transactionId: undefined,
  refundId: undefined,
  status: undefined,
  successTime: undefined,
})
const appList = ref([])
const channelList = ref([])
const { shortcuts, formaterYmdHms } = mixins()
const detailRef = ref()

onMounted(async () => {
  await getApps()
  await getList()
})

const getApps = async () => {
  const data = await AppApi.getAllList();
  appList.value = data
}
/**获取列表*/
const getList = async () => {
  let loading = showLoading()
  try {
    const data = await RefundApi.getPage(queryParams);
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

const changeApp = async (val: number) => {
  queryParams.channelId = undefined
  if (val) {
    const data = await ChannelApi.getChannelList(val);
    channelList.value = data
  } else {
    channelList.value = []
  }
}

const changeSuccessTime = () => {
  if (queryParams.successTime) {
    queryParams.successTime = formaterYmdHms(queryParams.successTime)
  }
}

const openDetail = (id: number) => {
  detailRef.value.open(id)
}
</script>

<style scoped>

</style>
