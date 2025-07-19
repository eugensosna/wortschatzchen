// src/api/TranslatedWordsApi.ts
import axiosInstance from "./axiosConfig";
import {
  SubtitleFileCreateUpdate,
  TranslatedWord,
  TranslatedWordToTranslate,
} from "../types";
import axios from "axios";
const Base_Api_Path = "/api/translatedwords/";

export const TranslatedWordsApi = {
  getAll: async (): Promise<TranslatedWord[]> => {
    const response = await axiosInstance.get("/api/translatedwords/");
    return response.data;
  },
  getByUuidClass: async (uuid: string): Promise<TranslatedWord[]> => {
    const response = await axiosInstance.get(`${Base_Api_Path}by-uuidClass/`, {
      params: { uuidClass: uuid },
    });
    return response.data;
  },

  getById: async (id: number): Promise<TranslatedWord> => {
    const response = await axiosInstance.get(`/api/translatedwords/${id}`);
    return response.data;
  },

  getByUuid: async (uuid: string): Promise<TranslatedWord> => {
    const response = await axiosInstance.get(`${Base_Api_Path}by-uuid`, {
      params: { uuid: uuid },
    });
    return response.data;
  },

  translate: async (
    toTranslate: TranslatedWordToTranslate
  ): Promise<string> => {
    const response = await axiosInstance.post(`${Base_Api_Path}`, toTranslate);
    return response.data;
  },
  translateByUuidClass: async (
    uuid: string,
    toTranslate: TranslatedWordToTranslate
  ): Promise<TranslatedWord[]> => {
    const response = await axiosInstance.post(
      `${Base_Api_Path}by-uuidClass/translate`,
      toTranslate,
      {
        params: { uuidClass: uuid },
      }
    );
    return response.data;
  },
  create: async (translatedWord: TranslatedWord): Promise<TranslatedWord> => {
    const response = await axiosInstance.post(
      `${Base_Api_Path}`,
      translatedWord
    );
    return response.data;
  },
  uploadFile: async (
    formdata: FormData,
    id: string
  ): Promise<TranslatedWord> => {
    const axiosInstance1 = axios.create({
      baseURL: axiosInstance.defaults.baseURL,
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });
    const response = await axiosInstance1.post(
      `${Base_Api_Path}upload/subtitle/${id}`,
      formdata,
      {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      }
    );
    return response.data;
  },

  update: async (
    id: number,
    subtitleFile: SubtitleFileCreateUpdate
  ): Promise<TranslatedWord> => {
    const response = await axiosInstance.put(
      `${Base_Api_Path}${id}`,
      subtitleFile
    );
    return response.data;
  },

  updateByUuid: async (
    uuid: string,
    subtitleFile: SubtitleFileCreateUpdate
  ): Promise<TranslatedWord> => {
    const response = await axiosInstance.put(
      `${Base_Api_Path}by-uuid`,
      subtitleFile,
      {
        params: { uuid: uuid },
      }
    );
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`${Base_Api_Path}${id}`);
  },

  deleteByUuid: async (uuid: string): Promise<void> => {
    await axiosInstance.delete(`${Base_Api_Path}by-uuid`, {
      params: { uuid: uuid },
    });
  },
};
