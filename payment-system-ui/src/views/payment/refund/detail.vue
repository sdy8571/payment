<template>
  <div>
    <el-drawer v-model="drawer" title="退单详情" size="40%">
      <el-descriptions :column="1">
        <el-descriptions-item label="业务退单号：">{{detail.outRefundNo}}</el-descriptions-item>
        <el-descriptions-item label="渠道退单号：">{{detail.refundId}}</el-descriptions-item>
        <el-descriptions-item label="业务订单号：">{{detail.outTradeNo}}</el-descriptions-item>
        <el-descriptions-item label="渠道订单号：">{{detail.transactionId}}</el-descriptions-item>
        <el-descriptions-item label="状态：">
          <el-tag>{{ COMMON_ENUMS.REFUND_STATUS[detail.refundStatus] }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="应用名称：">{{detail.appName}}</el-descriptions-item>
        <el-descriptions-item label="退款渠道：">{{detail.channelName}}</el-descriptions-item>
        <el-descriptions-item label="支付金额：">￥{{detail.payPriceY}}</el-descriptions-item>
        <el-descriptions-item label="退款金额：">￥{{detail.refundPriceY}}</el-descriptions-item>
        <el-descriptions-item label="支付备注：">{{detail.reason}}</el-descriptions-item>
        <el-descriptions-item label="退款时间：">{{detail.successTime}}</el-descriptions-item>
        <el-descriptions-item label="通知业务系统地址：">{{detail.notifyUrl}}</el-descriptions-item>
        <el-descriptions-item label="通知业务系统时间：">{{detail.notifyTime}}</el-descriptions-item>
      </el-descriptions>
      <el-descriptions :column="1" title=" ">
        <el-descriptions-item label="创建时间：">{{detail.createTime}}</el-descriptions-item>
        <el-descriptions-item label="更新时间：">{{detail.updateTime}}</el-descriptions-item>
      </el-descriptions>
    </el-drawer>
  </div>
</template>

<script setup lang="ts">
import * as RefundApi from "@/api/refund"
import {COMMON_ENUMS} from "@/views/contants/enums/Common";

defineOptions({name: 'RefundDetail'})

const drawer = ref(false)
const detail = ref({})

const open = async (id: number) => {
  const data = await RefundApi.detail(id)
  detail.value = data
  console.log(data)
  drawer.value = true
}

defineExpose({open})
</script>

<style scoped>
:deep .el-drawer__header {
  border-bottom: 1px solid #f1f1f1;
  margin-bottom: 0;
  padding-bottom: 20px;
}
</style>
