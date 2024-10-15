import { resolve } from 'path'
import { loadEnv } from 'vite'
import type { UserConfig, ConfigEnv } from 'vite'
import { createVitePlugins } from "./build/vite";
import { include, exclude } from "./build/vite/optimize"
// 当前执行node命令时文件夹的地址(工作目录)
const root = process.cwd()

// 路径查找
function pathResolve(dir: string) {
  return resolve(root, '.', dir)
}

// https://vitejs.dev/config/
export default ({ command, mode }: ConfigEnv): UserConfig => {
  let env
  const isBuild = command === 'build'
  if (isBuild) {
    env = loadEnv((process.argv[3] === '--mode' ? process.argv[4] : process.argv[3]), root)
  } else {
    env = loadEnv(mode, root)
  }

  return {
    base: env.VITE_BASE_PATH,
    root: root,
    // 服务端渲染
    server: {
      host: "0.0.0.0",
      port: Number(env.VITE_PORT),
      open: env.VITE_OPEN === 'true'
    },
    plugins: createVitePlugins(),
    css: {
      preprocessorOptions: {
        scss: {
          additionalData: '@import "./src/styles/variables.scss";',
          javascriptEnabled: true
        }
      }
    },
    resolve: {
      extensions: ['.mjs', '.js', '.ts', '.jsx', '.tsx', '.json', '.scss', '.css'],
      alias: [
        {
          find: '@',
          replacement: pathResolve('src'),
        }
      ]
    },
    optimizeDeps: { include, exclude }
  }
}
