import axios from "axios";
import apiBaseUrl from "./baseUrl";

const instance = axios.create({
    timeout: 10000, // 请求超时时间 10s
    baseURL: apiBaseUrl, // api 的 base_url,
    withCredentials: true // 允许跨域使用 cookie
});
instance.defaults.headers.post["Content-Type"] =
    "application/json;charset=UTF-8";

// request 拦截器
// instance.interceptors.request.use(config => {
//     if(config.url === "/login") {
//         // login接口无需token，直接调用
//         return config;
//     } else {
//         // 接口注入token
//         if(localStorage.getItem('token')){
//             config.headers.token = localStorage.getItem('token');
//         }
//         return config;
//     }
// }, error => {
//     return Promise.reject(error);
// })

// response 拦截器
// instance.interceptors.response.use((response) => {
//     // 获取登录接口的返回头中的token
//     console.log("response 拦截器", response)
//     return response;
// }, error => {
//     return Promise.reject(error);
// })
/**
 * 封装get方法
 * @param url
 * @returns {Promise<unknown>}
 */
export function get(url) {
    return new Promise((resolve, reject) => {
        instance.get(url).then(res => {
            resolve(res.data);
        }).catch(e => {
            reject(e.data);
        })
    })
}

export function getWithParam(url, params) {
    return new Promise((resolve, reject) => {
        instance.get(url, {params: params})
            .then(res => {
                resolve(res.data)
            })
            .catch(e => {
                reject(e.data);
            })
    })
}

/**
 * post方法
 * @param url
 * @param data
 * @returns {Promise<unknown>}
 */
export function post(url, data) {
    return new Promise((resolve, reject) => {
        instance.post(url, data)
            .then(res => {
                resolve(res.data)
            })
            .catch(e => {
                reject(e.data);
            })
    })
}


export default instance;
