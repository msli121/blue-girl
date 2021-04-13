let apiBaseUrl = "";
switch (process.env.NODE_ENV) { //注意变量名是自定义的
  case 'development':
    // 开发环境url
    apiBaseUrl = "http://localhost:8088/api/v1";
    break;
  case 'production':
    // 生产环境url
    apiBaseUrl = "http://106.15.204.9:8088/api/v1";
    break;
}
export default apiBaseUrl;