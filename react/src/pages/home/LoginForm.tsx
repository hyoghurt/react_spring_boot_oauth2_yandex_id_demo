import { Box, Button, TextField, Typography } from "@mui/material";
import React from "react";
import { AuthState } from "./redux/authSlice";

interface LoginFormProps {
  handleSubmit: (data: { username: string; password: string }) => void;
  error: string | null | undefined;
}

export default function LoginForm({ handleSubmit, error }: LoginFormProps) {
  const [username, setUsername] = React.useState<string>("");
  const [password, setPassword] = React.useState<string>("");

  const mb = 1;
  const p = 0;
  const size = "small";

  return (
    <Box sx={{ textAlign: "center", width: "100%" }}>
      <TextField
        sx={{ p, mb }}
        fullWidth
        label={"username"}
        size={size}
        value={username}
        onChange={(e) => setUsername(e.target.value)}
      />
      <TextField
        sx={{ p, mb }}
        fullWidth
        label={"password"}
        size={size}
        value={password}
        onChange={(e) => setPassword(e.target.value)}
      />
      {!!error && (
        <Typography variant={"body2"} sx={{ color: "error.main", mb }}>
          {error}
        </Typography>
      )}
      <Button
        sx={{ p }}
        variant={"outlined"}
        onClick={() => handleSubmit({ username, password })}
      >
        Войти
      </Button>
    </Box>
  );
}
