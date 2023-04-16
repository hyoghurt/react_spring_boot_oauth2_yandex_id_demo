import ya_icon from "./data/ya-icon.svg";
import React from "react";
import {Box} from "@mui/material";

interface AuthServicesProps {
  handleClick: (data: string) => void
}

export default function AuthServices({handleClick}: AuthServicesProps) {
  return (
    <Box sx={{width:"100%"}}>
      <img
        src={ya_icon}
        alt={"sign_up_ya"}
        onClick={() => handleClick("yandex")}
        style={{ cursor: "pointer", width:"100%" }}
      />
    </Box>
  );
}