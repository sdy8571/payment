<template>
  <Error type="403" @error-click="push('/')" />
</template>
<script lang="ts" setup>
defineOptions({ name: 'Error403' })

import {useRouter} from 'vue-router'
const { push } = useRouter()

</script>
