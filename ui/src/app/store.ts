import { configureStore } from "@reduxjs/toolkit";
import authReducer from "../pages/home/redux/authSlice";
import requestReducer from "../pages/home/redux/requestSlice";

export const store = configureStore({
  reducer: {
    auth: authReducer,
    request: requestReducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;
export type AppDispatch = typeof store.dispatch;
