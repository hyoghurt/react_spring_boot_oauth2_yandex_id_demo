import client from "./client";
import { AxiosError } from "axios";

export default function service() {
  function me() {
    return client().get("/me");
  }

  function getRequest() {
    return client().get("private/ping");
  }

  function postRequest(data?: string | undefined) {
    return client().post("private/ping", data);
  }

  function login(data: { username: string; password: string }) {
    // https://axios-http.com/docs/urlencodedhttps://axios-http.com/docs/urlencoded
    const dataReq = `username=${data.username}&password=${data.password}`;
    return client().post("/login", dataReq, {
      headers: {
        "Content-type": "application/x-www-form-urlencoded"
      }
    });
  }

  function logout() {
    return client().post("/logout");
  }

  function errMessage(err: AxiosError) {
    return `${err.response?.status}: ${err.response?.data}`;
  }

  return { me, login, logout, errMessage, postRequest, getRequest };
}
