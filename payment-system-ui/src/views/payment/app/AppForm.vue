<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="100px"
      style="max-width: 460px"
    >
      <el-form-item label="名称" prop="name">
        <el-input v-model="formData.name" maxlength="50" placeholder="请输入名称"/>
      </el-form-item>
      <el-form-item label="支付回调" prop="orderNotifyUrl">
        <el-input v-model="formData.orderNotifyUrl" maxlength="256" placeholder="请输入支付回调URL"/>
      </el-form-item>
      <el-form-item label="退款回调" prop="refundNotifyUrl">
        <el-input v-model="formData.refundNotifyUrl" maxlength="256" placeholder="请输入退款回调URL"/>
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="formData.remark" maxlength="256" placeholder="请输入备注"/>
      </el-form-item>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import * as AppApi from "@/api/app"
import { isHttp } from "@/utils/is";

defineOptions({name: "TaskForm"})

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')

const formRef = ref()
const formType = ref('')
const formLoading = ref(false)
const formData = ref({
  id: undefined,
  name: undefined,
  orderNotifyUrl: undefined,
  refundNotifyUrl: undefined,
  remark: undefined,
})
const checkUrl = (_rule: any, value: any, callback: any) => {
  if (isHttp(value)) {
    callback()
  } else {
    callback(new Error('回调地址格式错误'))
  }
}
const formRules = reactive({
  name: [{required: true, message: '名称不能为空', trigger: 'blur'}],
  orderNotifyUrl: [
    {required: true, message: '支付通知地址不能为空', trigger: 'blur'},
    {required: true, validator: checkUrl, trigger: 'blur'}
  ],
  refundNotifyUrl: [{required: true, message: '退款通知地址不能为空', trigger: 'blur'}],
})

const emit = defineEmits(['success'])

const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交
  formLoading.value = true
  try {
    const data = formData.value || {}
    if (formType.value === 'create') {
      await AppApi.create(data)
      message.success(t('common.createSuccess'))
    } else if (formType.value === 'update') {
      await AppApi.edit(data)
      message.success(t('common.updateSuccess'))
    }
    dialogVisible.value = false
    // 发送操作成功的事件
    emit('success')
  } finally {
    formLoading.value = false
  }
}

const open = async (type: string, id?: number) => {
  dialogVisible.value = true
  dialogTitle.value = t('action.' + type)
  formType.value = type
  if (type === 'update' && id) {
    const data = await AppApi.detail(id)
    formData.value = data
  }
}

defineExpose({open})
</script>

<style scoped>

</style>
