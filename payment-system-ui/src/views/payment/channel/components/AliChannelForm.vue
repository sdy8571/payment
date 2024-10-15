<template>
  <div>
    <el-form-item label="渠道费率" prop="feeRate">
      <el-input v-model="formData.feeRate" placeholder="请输入渠道费率" clearable>
        <template #append>%</template>
      </el-input>
    </el-form-item>
    <el-form-item label="开放平台APPID" prop="config.appId">
      <el-input v-model="formData.config.appId" placeholder="请输入开放平台 APPID" clearable />
    </el-form-item>
    <el-form-item label="网关地址" prop="config.serverUrl">
      <el-radio-group v-model="formData.config.serverUrl">
        <el-radio value="https://openapi.alipay.com/gateway.do">
          线上环境
        </el-radio>
        <el-radio value="https://openapi-sandbox.dl.alipaydev.com/gateway.do">
          沙箱环境
        </el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="算法类型" prop="config.signType">
      <el-radio-group v-model="formData.config.signType">
        <el-radio key="RSA2" value="RSA2">RSA2</el-radio>
      </el-radio-group>
    </el-form-item>
    <el-form-item label="公钥类型" prop="config.mode">
      <el-radio-group v-model="formData.config.mode">
        <el-radio key="公钥模式" :value="1">公钥模式</el-radio>
        <el-radio key="证书模式" :value="2">证书模式</el-radio>
      </el-radio-group>
    </el-form-item>
    <div v-if="formData.config.mode === 1">
      <el-form-item label="应用私钥" prop="config.privateKey">
        <el-input v-model="formData.config.privateKey" type="textarea" clearable
                  placeholder="请输入应用私钥" :autosize="{ minRows: 2, maxRows: 4 }"/>
      </el-form-item>
      <el-form-item label="支付宝公钥" prop="config.alipayPublicKey">
        <el-input v-model="formData.config.alipayPublicKey" type="textarea" clearable
                  placeholder="请输入支付宝公钥" :autosize="{ minRows: 2, maxRows: 4 }"/>
      </el-form-item>
    </div>
    <div v-if="formData.config.mode === 2">
      <el-form-item label="商户公钥应用证书" prop="config.appCertContent">
        <el-input v-model="formData.config.appCertContent" type="textarea" readonly
                  placeholder="请上传商户公钥应用证书" :autosize="{ minRows: 8, maxRows: 8 }"/>
      </el-form-item>
      <el-form-item label="">
        <el-upload ref="privateKeyContentFile" action="" :limit="1" :accept="fileAccept"
                   :before-upload="fileBeforeUpload" :http-request="appCertUpload">
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            <span>点击上传</span>
          </el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="支付宝公钥证书" prop="config.alipayPublicCertContent">
        <el-input v-model="formData.config.alipayPublicCertContent" type="textarea" readonly
          placeholder="请上传支付宝公钥证书" :autosize="{ minRows: 8, maxRows: 8 }" />
      </el-form-item>
      <el-form-item label="">
        <el-upload ref="privateCertContentFile" action="" :limit="1" :accept="fileAccept"
                   :before-upload="fileBeforeUpload" :http-request="alipayPublicCertUpload">
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            <span>点击上传</span>
          </el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="根证书" prop="config.rootCertContent">
        <el-input v-model="formData.config.rootCertContent" type="textarea" readonly
          placeholder="请上传根证书" :autosize="{ minRows: 8, maxRows: 8 }" />
      </el-form-item>
      <el-form-item label="">
        <el-upload ref="privateCertContentFile" action="" :limit="1" :accept="fileAccept"
                   :before-upload="fileBeforeUpload" :http-request="rootCertUpload">
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            <span>点击上传</span>
          </el-button>
        </el-upload>
      </el-form-item>
    </div>
    <el-form-item label="备注" prop="remark">
      <el-input v-model="formData.remark" placeholder="请输入备注" />
    </el-form-item>
  </div>
</template>

<script setup lang="ts">
defineOptions({name: "AliChannelForm"})

const message = useMessage()

defineProps({
  formData: {
    type: Object,
    default: {}
  }
})

const fileAccept = '.crt'
const channelFormData = ref({
  feeRate: null,
  config: {
    appId: '',
    serverUrl: null,
    signType: '',
    mode: null,
    privateKey: '',
    alipayPublicKey: '',
    appCertContent: '',
    alipayPublicCertContent: '',
    rootCertContent: ''
  }
})
const channelFormRules = reactive({
  feeRate: [{ required: true, message: '请输入渠道费率', trigger: 'blur' }],
  'config.appId': [{ required: true, message: '请输入开放平台上创建的应用的 ID', trigger: 'blur' }],
  'config.serverUrl': [{ required: true, message: '请传入网关地址', trigger: 'blur' }],
  'config.signType': [{ required: true, message: '请传入签名算法类型', trigger: 'blur' }],
  'config.mode': [{ required: true, message: '公钥类型不能为空', trigger: 'blur' }],
  'config.privateKey': [{ required: true, message: '请输入商户私钥', trigger: 'blur' }],
  'config.alipayPublicKey': [
    { required: true, message: '请输入支付宝公钥字符串', trigger: 'blur' }
  ],
  'config.appCertContent': [{ required: true, message: '请上传商户公钥应用证书', trigger: 'blur' }],
  'config.alipayPublicCertContent': [
    { required: true, message: '请上传支付宝公钥证书', trigger: 'blur' }
  ],
  'config.rootCertContent': [{ required: true, message: '请上传指定根证书', trigger: 'blur' }]
})

const emit = defineEmits(['appCert', 'alipayPublicCert', 'rootCert'])

const fileBeforeUpload = (file) => {
  let format = '.' + file.name.split('.')[1]
  if (format !== fileAccept) {
    message.error(`请上传指定格式"${fileAccept}"文件`)
    return false
  }
  let isRightSize = file.size / 1024 / 1024 < 2
  if (!isRightSize) {
    message.error('文件大小超过 2MB')
  }
  return isRightSize
}

const appCertUpload = (event) => {
  const readFile = new FileReader()
  readFile.onload = (e: any) => {
    emit("appCert", e.target.result)
  }
  readFile.readAsText(event.file)
}

const alipayPublicCertUpload = (event) => {
  const readFile = new FileReader()
  readFile.onload = (e: any) => {
    emit("alipayPublicCert", e.target.result)
  }
  readFile.readAsText(event.file)
}

const rootCertUpload = (event) => {
  const readFile = new FileReader()
  readFile.onload = (e: any) => {
    emit("rootCert", e.target.result)
  }
  readFile.readAsText(event.file)
}

/**
 * 初始化，对外提供方法
 */
defineExpose({channelFormData, channelFormRules})
</script>

<style scoped>

</style>
