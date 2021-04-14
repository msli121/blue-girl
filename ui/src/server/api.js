import { post } from "./request";
// demo
export function test(data) {
    return post("/test", data);
}