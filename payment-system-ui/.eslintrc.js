module.exports = {
    root: true,
    env: {
        //指定代码的运行环境
        browser: true,
        node: true,
    },
    parser: '@typescript-eslint/parser', //定义ESLint的解析器
    plugins: ['vue'],
    parserOptions: {
        "ecmaVersion": 2020,
        "sourceType": 'module',
        "jsxPragma": 'React',
        "ecmaFeatures": {
            jsx: true
        }
    },
    extends: [
        'prettier/@typescript-eslint',  // 使@typescript-eslint中的样式规范失效，遵循prettier中的样式规范
        'plugin:prettier/recommended' // 使用prettier中的样式规范，且如果使ESLint会检测prettier的格式问题，同样将格式问题以error的形式抛出
    ],
};