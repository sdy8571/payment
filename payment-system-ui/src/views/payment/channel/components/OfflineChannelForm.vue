<template>
  <div>
    <el-form-item label="备注" prop="remark">
      <el-input v-model="formData.remark" placeholder="请输入渠道备注" clearable />
    </el-form-item>
  </div>
</template>

<script setup lang="ts">

defineOptions({name: "OfflineChannelForm"})

defineProps({
  formData: {
    type: Object,
    default: {}
  }
})

</script>

<style scoped>

</style>
