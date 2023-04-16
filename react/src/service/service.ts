import client from "./client";
import { AxiosError, AxiosRequestConfig } from "axios";

const postWithCsrf = async (
  url: string,
  data?: any,
  config?: AxiosRequestConfig<void | undefined>
) => {
  // get token csrf
  const csrf = await client().get("/csrf");

  // request post
  return client().post(url, data, {
    ...config,
    headers: {
      ...config?.headers,
      "X-CSRF-TOKEN": csrf.data.token, //put token csrf
    },
  });
};

export default function service() {
  function me() {
    return client().get("/me");
  }

  function getRequest() {
    return client().get("private/ping");
  }

  function postRequest(data?: string | undefined) {
    return postWithCsrf("private/ping", data);
  }

  function login(data: { username: string; password: string }) {
    // https://axios-http.com/docs/urlencodedhttps://axios-http.com/docs/urlencoded
    const dataReq = `username=${data.username}&password=${data.password}`;
    return postWithCsrf("/login", dataReq, {
      headers: {
        "Content-type": "application/x-www-form-urlencoded",
      },
    });
  }

  function logout() {
    return postWithCsrf("/logout");
  }

  function errMessage(err: AxiosError) {
    return `${err.response?.status}: ${err.response?.data}`;
  }

  return { me, login, logout, errMessage, postRequest, getRequest };
}
