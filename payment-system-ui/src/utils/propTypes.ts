import { createTypes, VueTypesInterface, VueTypeValidableDef } from 'vue-types'
import { CSSProperties } from 'vue'

// 自定义扩展vue-types
type PropTypes = VueTypesInterface & {
  readonly style: VueTypeValidableDef<CSSProperties>
}

const propTypes = createTypes({
  func: undefined,
  bool: undefined,
  string: undefined,
  number: undefined,
  object: undefined,
  integer: undefined
}) as PropTypes

export { propTypes }
