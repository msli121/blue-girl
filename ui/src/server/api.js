import { post } from "./request";
// demo
export function test(data) {
    return post("/test", data);
}

export function saveSticker(data) {
    return post("https://www.performercn.com/api/file/tag",data)
}