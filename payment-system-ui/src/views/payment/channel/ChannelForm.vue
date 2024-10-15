<template>
  <Dialog v-model="dialogVisible" :title="dialogTitle" width="860">
    <el-form
      ref="formRef"
      v-loading="formLoading"
      :model="formData"
      :rules="formRules"
      label-width="120px"
    >
      <el-form-item label="支付应用" prop="appId">
        <el-select v-model="formData.appId" clearable placeholder="选择应用支付" :disabled="formData.id">
          <el-option v-for="item in appList" :key="item.id" :label="item.name" :value="item.id"/>
        </el-select>
      </el-form-item>
      <el-form-item label="支付渠道" prop="code">
        <el-select v-model="formData.code" clearable placeholder="选择支付渠道" @change="changeChannelCode" :disabled="formData.id">
          <el-option v-for="item in typeList" :key="item.code" :label="item.name" :value="item.code"/>
        </el-select>
      </el-form-item>
      <WxChannelForm ref="wechatForm" v-if="formData.channel === 'WECHAT'" :formData="formData"
                     @key="handleKey" @privateKey="handlePrivateKey"
                     @privateCert="handlePrivateCert"/>
      <AliChannelForm ref="alipayForm" v-if="formData.channel === 'ALIPAY'" :formData="formData"
                      @appCert="handleAppCert" @alipayPublicCert="handleAlipayPublicCert"
                      @rootCert="handleRootCert"/>
      <MockChannelForm ref="mockForm" v-if="formData.channel === 'MOCK'" :formData="formData"/>
      <OfflineChannelForm ref="offlineForm" v-if="formData.channel === 'OFFLINE'" :formData="formData"/>
      <WalletChannelForm ref="walletForm" v-if="formData.channel === 'WALLET'" :formData="formData"/>
    </el-form>
    <template #footer>
      <el-button type="primary" @click="submitForm">确 定</el-button>
      <el-button @click="dialogVisible = false">取 消</el-button>
    </template>
  </Dialog>
</template>

<script setup lang="ts">
import * as AppApi from "@/api/app"
import * as ChannelApi from "@/api/channel"
import WxChannelForm from "./components/WxChannelForm.vue";
import MockChannelForm from "./components/MockChannelForm.vue";
import OfflineChannelForm from "./components/OfflineChannelForm.vue";
import WalletChannelForm from "./components/WalletChannelForm.vue";
import AliChannelForm from "./components/AliChannelForm.vue";

defineOptions({name: "ChannelForm"})

const { t } = useI18n()
const message = useMessage()

const dialogVisible = ref(false)
const dialogTitle = ref('')

const formRef = ref()
const formLoading = ref(false)
const formData = ref<any>({
  appId: '',
  code: '',
  channel: '',
  remark: '',
  config: {},
})
let formRules = reactive({
  appId: [{required: true, message: '名称不能为空', trigger: 'blur'}],
  code: [{required: true, message: '支付通知地址不能为空', trigger: 'blur'}],
})

const appList = ref([])
const typeList = ref([])

const wechatForm = ref()
const alipayForm = ref()
const mockForm = ref()
const offlineForm = ref()
const walletForm = ref()

const emit = defineEmits(['success'])

const submitForm = async () => {
  if (!formRef) return
  const valid = await formRef.value.validate()
  if (!valid) return
  // 提交
  formLoading.value = true
  try {
    let data = formData.value || {}
    data.config = JSON.stringify(formData.value.config)
    if (!data.id) {
      await ChannelApi.create(data)
      message.success(t('common.createSuccess'))
    } else {
      await ChannelApi.edit(data)
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
  formLoading.value = true
  resetForm()
  try {
    dialogTitle.value = t('action.' + type) + '支付渠道'
    // 查询应用
    appList.value = await AppApi.getAllList()
    typeList.value = await ChannelApi.getTypeList()
    // 查询渠道信息
    if (id) {
      const data = await ChannelApi.detail(id)
      formData.value = data
      formData.value.config = JSON.parse(data.config)
      // 设置渠道校验信息
      const _channel = getChannel(data.code)
      formData.value.channel = _channel.channel
      initChannelFormRules(_channel)
      nextTick(() => {
        console.log("====> data")
        console.log(formData.value)
        console.log("====> rule")
        console.log(formRules)
      })
    }
  } finally {
    formLoading.value = false
  }
}

/** 重置表单 */
const resetForm = (appId?: string, code?: string) => {
  formData.value = {
    appId: appId || '',
    code: code || '',
    feeRate: undefined,
    remark: '',
    config: {},
  }
  formRef.value?.resetFields()
}

const changeChannelCode = (val: string) => {
  const _channel = getChannel(val)
  if (formData.value.channel && _channel && formData.value.channel !== _channel.channel) {
    resetForm(formData.value.appId, formData.value.code)
  }
  formData.value.channel = _channel.channel
  initChannelFormData(_channel)
  initChannelFormRules(_channel)
}

const getChannel: any = (val: string) => {
  return typeList.value.filter((item: any) => item.code === val)[0] || {}
}

const initChannelFormData = (_channel: any) => {
  if (_channel.channel === 'WECHAT') {
    nextTick(() => {
      formData.value = Object.assign(formData.value, wechatForm.value.channelFormData)
    })
  } else if (_channel.channel === 'ALIPAY') {
    nextTick(() => {
      formData.value = Object.assign(formData.value, alipayForm.value.channelFormData)
    })
  }
}

const initChannelFormRules = (_channel: any) => {
  if (_channel.channel === 'WECHAT') {
    nextTick(() => {
      formRules = Object.assign(formRules, wechatForm.value.channelFormRules)
    })
  } else if (_channel.channel === 'ALIPAY') {
    nextTick(() => {
      formRules = Object.assign(formRules, alipayForm.value.channelFormRules)
    })
  }
}

/** 微信回调 */
const handleKey = (val: string) => {
  formData.value.config.keyContent =  val
}

const handlePrivateKey = (val: string) => {
  formData.value.config.privateKeyContent = val
}

const handlePrivateCert = (val: string) => {
  formData.value.config.privateCertContent = val
}

/** 支付宝回调 */
const handleAppCert = (val: string) => {
  formData.value.config.appCertContent = val
}

const handleAlipayPublicCert = (val: string) => {
  formData.value.config.alipayPublicCertContent = val
}

const handleRootCert = (val: string) => {
  formData.value.config.rootCertContent = val
}

defineExpose({open})
</script>

<style scoped>

</style>
