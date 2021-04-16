const imgPreloader = url => {
  return new Promise((resolve, reject) => {
    let image = new Image();
    image.onload = () => {
      resolve(`加载成功${url}`);
    };
    image.onerror = () => {
      reject(`加载失败${url}`);
    };
    image.src = url;
  });
};
export const imgsPreloader = imgs => {
  let promiseArr = [];
  imgs.forEach(element => {
    console.log(16, element)
    promiseArr.push(imgPreloader(element));
  });
  return Promise.all(promiseArr);
};
