import axios from "axios";
// import { useCookies } from "vue3-cookies";

const { VITE_VUE_API_URL, VITE_ELECTRIC_CHARGING_STATION_URL } = import.meta.env;

// station vue api axios instance
function stationAxios() {
  const instance = axios.create({
    baseURL: VITE_ELECTRIC_CHARGING_STATION_URL,
    withCredentials: true,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  return instance;
}

// local vue api axios instance
function localAxios() {
//   const { cookies } = useCookies();

  const instance = axios.create({
    withCredentials: true,
    baseURL: VITE_VUE_API_URL,
    headers: {
      "Content-Type": "application/json;charset=utf-8",
    },
  });
  // Request 발생 시 적용할 내용.
  instance.defaults.headers.common["Authorization"] = "";
  instance.defaults.headers.post["Content-Type"] = "application/json";
  instance.defaults.headers.put["Content-Type"] = "application/json";

  // Request, Response 시 설정한 내용을 적용.
  instance.interceptors.request.use((config) => {
    // vue3-cookies를 사용하여 쿠키에서 accessToken 가져오기

    // 'this.$cookies'를 사용하여 쿠키에 접근
    // const accessToken = cookies.get("accessToken");

    // if (accessToken) {
    //   config.headers["Authorization"] = `Bearer ${accessToken}`;
    // }

    
    // console.log(accessToken);
    // console.log(config.headers);
    return config;
  }),
    (error) => {
      return Promise.reject(error);
    };


  return instance;
}

export { localAxios, stationAxios };