export function loadImage(src) {
    return new Promise((resolve, reject) => {
        let img = new Image();
        // 在线图片设置 crossOrigin 跨域
        if (src.indexOf(src) === 0) {
        img.crossOrigin = '*';
    }
    img.src = src;
    img.onload = () => {
        resolve(img)
    };
    img.onerror = () => {
        reject(new Error('图片解析失败'))
    }
})
}