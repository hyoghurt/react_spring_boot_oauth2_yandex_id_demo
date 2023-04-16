import {createAsyncThunk, createSlice} from "@reduxjs/toolkit";
import { BaseState } from "./types";
import { PendingAction } from "../../../app/common/redux/types";
import service from "../../../service/service";
import {AxiosError} from "axios";

export interface AuthState extends BaseState {
  isAuth: boolean;
}

const initialState: AuthState = {
  data: null,
  isAuth: false,
  isLoading: false,
  error: null,
};

export const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addCase(fetchMe.fulfilled, (state, action) => {
        state.isAuth = true;
        state.data = action.payload;
        state.isLoading = false;
      })
      .addCase(fetchMe.rejected, (state) => {
        state.isAuth = false;
        state.data = null;
        state.isLoading = false;
      })
      .addCase(fetchLogin.fulfilled, (state) => {
        state.isAuth = true;
        state.isLoading = false;
      })
      .addCase(fetchLogin.rejected, (state, action) => {
        state.isAuth = false;
        state.error = action.payload as string;
        state.isLoading = false;
      })
      .addCase(fetchLogout.fulfilled, (state) => {
        state.isAuth = false;
        state.data = null;
        state.isLoading = false;
      })
      .addCase(fetchLogout.rejected, (state) => {
        state.isLoading = false;
      })
      .addMatcher<PendingAction>(
        (action) =>
          action.type.startsWith("auth/") && action.type.endsWith("/pending"),
        (state) => {
          state.error = null;
          state.isLoading = true;
        }
      );
  },
});

export const {} = authSlice.actions;
export default authSlice.reducer;

export const fetchMe = createAsyncThunk("auth/fetchMe", async (_, thunkAPI) => {
  try {
    const response = await service().me();
    return response.data;
  } catch (e) {
    const err = e as AxiosError;
    return thunkAPI.rejectWithValue(service().errMessage(err));
  }
});

export const fetchLogout = createAsyncThunk(
    "auth/fetchLogout",
    async (_, thunkAPI) => {
      try {
        await service().logout();
      } catch (e) {
        const err = e as AxiosError;
        return thunkAPI.rejectWithValue(service().errMessage(err));
      }
    }
);

export const fetchLogin = createAsyncThunk(
    "auth/fetchLogin",
    async (data: { username: string; password: string }, thunkAPI) => {
      try {
        await service().login(data);
      } catch (e) {
        const err = e as AxiosError;
        return thunkAPI.rejectWithValue(service().errMessage(err));
      }
    }
);

