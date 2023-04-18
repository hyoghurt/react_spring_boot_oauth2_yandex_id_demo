import { Box, Button, Divider, Paper, Typography } from "@mui/material";
import React from "react";
import { useAppDispatch, useAppSelector } from "../../app/common/redux/hooks";
import { fetchGet, fetchPost } from "./redux/requestSlice";

export default function InfoPaper() {
  const auth = useAppSelector((state) => state.auth);
  const request = useAppSelector((state) => state.request);
  const dispatch = useAppDispatch();

  const handleClickGet = () => {
    dispatch(fetchGet());
  };

  const handleClickPost = () => {
    dispatch(fetchPost("pong"));
  };

  return (
    <Paper sx={{ p: 3, width: "100%" }} elevation={3}>
      <Box sx={{ textAlign: "center", mb: 1 }}>
        <Typography variant={"body2"}>Request private endpoint</Typography>
      </Box>
      <Box sx={{ textAlign: "center", mb: 1 }}>
        <Button variant={"outlined"} onClick={handleClickGet} sx={{ mr: 1 }}>
          get
        </Button>
        <Button variant={"outlined"} onClick={handleClickPost}>
          post
        </Button>
      </Box>
      <Box sx={{ textAlign: "center", mb: 1 }}>
        {request.isLoading ? (
          <Typography variant={"body2"}>Loading...</Typography>
        ) : !!request.error ? (
          <Typography variant={"body2"} sx={{ color: "error.main" }}>
            {request.error}
          </Typography>
        ) : (
          <Typography variant={"body2"}>
            {!!request.data && JSON.stringify(request.data)}
          </Typography>
        )}
      </Box>
      <Box sx={{ mb: 1 }}>
        <Divider variant={"middle"} />
      </Box>
      <Box sx={{ textAlign: "center", mb: 1 }}>
        {auth.isLoading ? (
          <Typography variant={"body2"}>Loading...</Typography>
        ) : auth.isAuth ? (
          <>
            <Typography variant={"h4"}>You auth</Typography>
            <Typography variant={"body2"} sx={{ overflowWrap: "break-word" }}>
              {JSON.stringify(auth.data)}
            </Typography>
          </>
        ) : (
          <Typography variant={"h4"}>You NO auth</Typography>
        )}
      </Box>
    </Paper>
  );
}