import { createAsyncThunk, createSlice } from "@reduxjs/toolkit";
import { AxiosError } from "axios";
import { BaseState } from "./types";
import service from "../../../service/service";
import {FulfilledAction, PendingAction, RejectedAction} from "../../../app/common/redux/types";

const initialState: BaseState = {
  data: null,
  isLoading: false,
  error: null,
};

export const requestSlice = createSlice({
  name: "request",
  initialState,
  reducers: {},
  extraReducers: (builder) => {
    builder
      .addMatcher<PendingAction>(
        (action) =>
          action.type.startsWith("request/") &&
          action.type.endsWith("/pending"),
        (state) => {
            state.error = null;
            state.isLoading = true;
        }
      )
      .addMatcher<FulfilledAction>(
        (action) =>
          action.type.startsWith("request/") &&
          action.type.endsWith("/fulfilled"),
        (state,action) => {
            state.data = action.payload;
            state.isLoading = false;
        }
      )
      .addMatcher<RejectedAction>(
        (action) =>
          action.type.startsWith("request/") &&
          action.type.endsWith("/rejected"),
        (state,action) => {
            state.data = null;
            state.error = action.payload as string;
            state.isLoading = false;
        }
      );
  },
});

export const {} = requestSlice.actions;
export default requestSlice.reducer;

export const fetchPost = createAsyncThunk(
  "request/fetchPost",
  async (data: string | undefined, thunkAPI) => {
    try {
      const response = await service().postRequest(data);
      return response.data;
    } catch (e) {
      const err = e as AxiosError;
      return thunkAPI.rejectWithValue(service().errMessage(err));
    }
  }
);

export const fetchGet = createAsyncThunk(
  "request/fetchGet",
  async (_, thunkAPI) => {
    try {
      const response = await service().getRequest();
      return response.data;
    } catch (e) {
      const err = e as AxiosError;
      return thunkAPI.rejectWithValue(service().errMessage(err));
    }
  }
);

