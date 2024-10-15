<template>
  <div>
    <el-form-item label="渠道费率" prop="feeRate">
      <el-input v-model="formData.feeRate" placeholder="请输入渠道费率" clearable >
        <template #append>%</template>
      </el-input>
    </el-form-item>
    <el-form-item label="渠道APPID" prop="config.appId">
      <el-input v-model="formData.config.appId" placeholder="请输入渠道 APPID" clearable />
    </el-form-item>
    <el-form-item label="商户号" prop="config.mchId">
      <el-input v-model="formData.config.mchId" placeholder="请输入商户号" clearable />
    </el-form-item>
    <el-form-item label="API 版本" prop="config.apiVersion">
      <el-radio-group v-model="formData.config.apiVersion">
        <el-radio value="v2">v2</el-radio>
        <el-radio value="v3">v3</el-radio>
      </el-radio-group>
    </el-form-item>
    <div v-if="formData.config.apiVersion === 'v2'">
      <el-form-item label="商户密钥" prop="config.mchKey">
        <el-input v-model="formData.config.mchKey" placeholder="请输入商户密钥" clearable
          type="textarea" :autosize="{ minRows: 2, maxRows: 4 }"/>
      </el-form-item>
      <el-form-item label="apiclient_cert.p12 证书" prop="config.keyContent" >
        <el-input v-model="formData.config.keyContent" type="textarea" readonly
          placeholder="请上传 apiclient_cert.p12 证书" :autosize="{ minRows: 8, maxRows: 8 }"/>
      </el-form-item>
      <el-form-item label="">
        <el-upload :limit="1" accept=".p12" action=""
          :before-upload="p12FileBeforeUpload" :http-request="keyContentUpload">
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            <span>点击上传</span>
          </el-button>
        </el-upload>
      </el-form-item>
    </div>
    <div v-if="formData.config.apiVersion === 'v3'">
      <el-form-item label="API V3 密钥" prop="config.apiV3Key">
        <el-input v-model="formData.config.apiV3Key" placeholder="请输入 API V3 密钥" clearable
          type="textarea" :autosize="{ minRows:2, maxRows: 4 }" />
      </el-form-item>
      <el-form-item label="apiclient_key.pem 证书" prop="config.privateKeyContent">
        <el-input v-model="formData.config.privateKeyContent" type="textarea" readonly
          placeholder="请上传 apiclient_key.pem 证书" :autosize="{ minRows: 8, maxRows: 8 }"/>
      </el-form-item>
      <el-form-item label="" prop="privateKeyContentFile">
        <el-upload ref="privateKeyContentFile" :limit="1" accept=".pem" action=""
          :before-upload="pemFileBeforeUpload" :http-request="privateKeyContentUpload">
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            <span>点击上传</span>
          </el-button>
        </el-upload>
      </el-form-item>
      <el-form-item label="apiclient_cert.pem证书" prop="config.privateCertContent">
        <el-input v-model="formData.config.privateCertContent" type="textarea" readonly
          placeholder="请上传apiclient_cert.pem证书" :autosize="{ minRows: 8, maxRows: 8 }"/>
      </el-form-item>
      <el-form-item label="" prop="privateCertContentFile">
        <el-upload ref="privateCertContentFile" :limit="1" accept=".pem" action=""
          :before-upload="pemFileBeforeUpload" :http-request="privateCertContentUpload">
          <el-button type="primary">
            <el-icon><Upload /></el-icon>
            <span>点击上传</span>
          </el-button>
        </el-upload>
      </el-form-item>
    </div>
    <el-form-item label="备注" prop="remark">
      <el-input v-model="formData.remark" placeholder="请输入渠道备注" clearable />
    </el-form-item>
  </div>
</template>

<script setup lang="ts">

defineOptions({name: "WxChannelForm"})

const message = useMessage()

defineProps({
  formData: {
    type: Object,
    default: {}
  }
})

const channelFormData = ref({
  feeRate: undefined,
  config: {
    appId: '',
    mchId: '',
    apiVersion: '',
    mchKey: '',
    keyContent: '',
    privateKeyContent: '',
    privateCertContent: '',
    apiV3Key: ''
  }
})
const channelFormRules = reactive({
  'config.mchId': [{ required: true, message: '请传入商户号', trigger: 'blur' }],
  'config.appId': [{ required: true, message: '请输入公众号APPID', trigger: 'blur' }],
  'config.apiVersion': [{ required: true, message: 'API版本不能为空', trigger: 'blur' }],
  'config.mchKey': [{ required: true, message: '请输入商户密钥', trigger: 'blur' }],
  'config.keyContent': [
    { required: true, message: '请上传 apiclient_cert.p12 证书', trigger: 'blur' }
  ],
  'config.privateKeyContent': [
    { required: true, message: '请上传 apiclient_key.pem 证书', trigger: 'blur' }
  ],
  'config.privateCertContent': [
    { required: true, message: '请上传 apiclient_cert.pem证 书', trigger: 'blur' }
  ],
  'config.apiV3Key': [{ required: true, message: '请上传 api V3 密钥值', trigger: 'blur' }]
})

/**
 * apiclient_cert.p12、apiclient_cert.pem、apiclient_key.pem 上传前的校验
 */
const fileBeforeUpload = (file, fileAccept) => {
  let format = '.' + file.name.split('.')[1]
  if (format !== fileAccept) {
    message.error('请上传指定格式"' + fileAccept + '"文件')
    return false
  }
  let isRightSize = file.size / 1024 / 1024 < 2
  if (!isRightSize) {
    message.error('文件大小超过 2MB')
  }
  return isRightSize
}

const p12FileBeforeUpload = (file) => {
  fileBeforeUpload(file, '.p12')
}

const pemFileBeforeUpload = (file) => {
  fileBeforeUpload(file, '.pem')
}

const emit = defineEmits(['key', 'privateKey', 'privateCert'])
/**
 * 读取 apiclient_cert.p12 到 keyContent 字段
 */
const keyContentUpload = (event: any) => {
  const readFile = new FileReader()
  readFile.onload = (e: any) => {
    emit('key', e.target.result.split(',')[1])
  }
  readFile.readAsDataURL(event.file) // 读成 base64
}

/**
 * 读取 apiclient_key.pem 到 privateKeyContent 字段
 */
const privateKeyContentUpload = (event) => {
  const readFile = new FileReader()
  readFile.onload = (e: any) => {
    emit('privateKey', e.target.result)
  }
  readFile.readAsText(event.file)
}

/**
 * 读取 apiclient_cert.pem 到 privateCertContent 字段
 */
const privateCertContentUpload = (event) => {
  const readFile = new FileReader()
  readFile.onload = (e: any) => {
    emit('privateCert', e.target.result)
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
