<template>
  <Error @error-click="push('/')" />
</template>
<script lang="ts" setup>
defineOptions({ name: 'Error404' })
import {useRouter} from 'vue-router'
const { push } = useRouter()
</script>
