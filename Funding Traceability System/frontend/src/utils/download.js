/**
 * 通用文件下载：将后端返回的 blob 流保存为本地文件
 * 兼容服务端用 JSON 返回错误信息（被 axios 当作 blob 接收）的情况
 */
export async function downloadFile(blob, filename) {
  if (!blob) return
  // 服务端可能以 JSON 形式返回错误体，但被 responseType:'blob' 包成了 Blob
  if (blob.type && blob.type.includes('application/json')) {
    let msg = '导出失败'
    try {
      const text = await blob.text()
      const obj = JSON.parse(text)
      msg = (obj && (obj.message || obj.error)) || msg
    } catch (e) {
      // 忽略解析异常
    }
    throw new Error(msg)
  }
  const url = window.URL.createObjectURL(new Blob([blob]))
  const link = document.createElement('a')
  link.href = url
  link.download = filename
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  window.URL.revokeObjectURL(url)
}
