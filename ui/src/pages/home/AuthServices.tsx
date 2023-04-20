import yandex_btn from "./data/ya-icon.svg";
import google_btn from "./data/btn_google_signin_dark_normal_web@2x.png";
import React from "react";
import { Box } from "@mui/material";

interface AuthServicesProps {
  handleClick: (data: string) => void;
}

export default function OAuthButtons({ handleClick }: AuthServicesProps) {
  return (
    <Box sx={{ width: "100%" }}>
      <Box sx={{ mb: 1 }}>
        {/*https://yandex.ru/dev/id/doc/ru/codes/buttons-design*/}
        <img
          src={yandex_btn}
          alt={"sign_in_yandex"}
          onClick={() => handleClick("yandex")}
          style={{ cursor: "pointer", width: "100%", display: "block" }}
        />
      </Box>
      <Box>
        {/*https://developers.google.com/identity/gsi/web/guides/display-button*/}
        <img
          src={google_btn}
          alt={"sign_in_google"}
          onClick={() => handleClick("google")}
          style={{ cursor: "pointer", width: "100%", display: "block" }}
        />
      </Box>
    </Box>
  );
}