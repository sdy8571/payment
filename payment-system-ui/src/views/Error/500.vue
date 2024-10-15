<template>
  <Error type="500" @error-click="push('/')" />
</template>
<script lang="ts" setup>
defineOptions({ name: 'Error500' })
import {useRouter} from 'vue-router'
const { push } = useRouter()
</script>
