import axios from "axios";
import { URL_BACK } from "../app/constants";

export default function client() {
  return axios.create({
    withCredentials: true, // цеплять куки пользователя
    // timeout: 10000,
    responseType: "json",
    baseURL: URL_BACK,
    headers: {
      "X-Requested-With": "XMLHttpRequest", // отключает попап авторизации в браузере
    },
  });
}
