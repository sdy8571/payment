
export default function() {
  const shortcuts = [
    {
      text: '最近一周',
      value: () => shortcutsValue(7),
    },
    {
      text: '最近一个月',
      value: () => shortcutsValue(30),
    },
    {
      text: '最近三个月',
      value: () => shortcutsValue(90),
    },
  ]
  const shortcutsValue = (offset: number) => {
    const end = new Date()
    const start = new Date()
    start.setTime(start.getTime() - 3600 * 1000 * 24 * offset)
    return [start, end]
  }

  const formaterYmdHms = (_val: string[]) => {
    const start = _val[0] + " 00:00:00"
    const end = _val[1] + " 23:59:59"
    return [start, end]
  }

  return {
    shortcuts,
    formaterYmdHms
  }
}

