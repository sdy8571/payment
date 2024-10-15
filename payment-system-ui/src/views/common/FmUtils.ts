export const formatIdCode = (id: number, length: number) => {
  if (!id) {
    return ''
  }
  if (!length) {
    return id.toString()
  }
  return id.toString().padStart(length, '0')
}
