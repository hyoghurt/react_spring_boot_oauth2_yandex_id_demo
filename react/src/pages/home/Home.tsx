import React from "react";
import { useAppDispatch, useAppSelector } from "../../app/common/redux/hooks";
import {
  Box,
  Button,
  CssBaseline,
  Divider,
  Grid,
  Typography,
} from "@mui/material";
import { URL_BACK } from "../../app/constants";
import LoginForm from "./LoginForm";
import AuthServices from "./AuthServices";
import InfoPaper from "./InfoPaper";
import { fetchLogin, fetchLogout, fetchMe } from "./redux/authSlice";

export default function Home() {
  const auth = useAppSelector((state) => state.auth);
  const dispatch = useAppDispatch();

  React.useEffect(() => {
    if (!auth.data) {
      dispatch(fetchMe());
    }
  }, [auth.isAuth]);

  const handleOAuth2 = (data: string) => {
    window.location.href = `${URL_BACK}/oauth2/authorization/${data}`;
  };

  const handleLogin = (data: { username: string; password: string }) => {
    dispatch(fetchLogin(data));
  };

  const handleLogout = () => {
    dispatch(fetchLogout());
  };

  return (
    <>
      <CssBaseline />
      <Grid container justifyContent={"center"} sx={{ width: "100%", p: 2 }}>
        <Grid item xs={11} sm={6} sx={{ m: 0, mt:2, p: 0 }}>
          <Box sx={{ mb: 3 }}>
            <InfoPaper />
          </Box>
          <Box sx={{ width: "50%", margin: "auto", textAlign: "center" }}>
            {auth.isLoading ? (
              <Typography>Loading...</Typography>
            ) : auth.isAuth ? (
              <Button variant={"outlined"} onClick={handleLogout}>
                Выйти
              </Button>
            ) : (
              <>
                <LoginForm handleSubmit={handleLogin} error={auth.error} />
                <Divider variant="middle" sx={{ my: 1 }}>
                  or
                </Divider>
                <AuthServices handleClick={handleOAuth2} />
              </>
            )}
          </Box>
        </Grid>
      </Grid>
    </>
  );
}
