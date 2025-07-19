
// src/api/subtitleFilesApi.ts
import axiosInstance from './axiosConfig';
import { SubtitleFile, SubtitleFileCreateUpdate } from '../types';
import axios from 'axios';

export const subtitleFilesApi = {
  getAll: async (): Promise<SubtitleFile[]> => {
    const response = await axiosInstance.get('/api/subtitle-files/');
    return response.data;
  },
  
  getById: async (id: number): Promise<SubtitleFile> => {
    const response = await axiosInstance.get(`/api/subtitle-files/${id}`);
    return response.data;
  },
  
  getByUuid: async (uuid: string): Promise<SubtitleFile> => {
    const response = await axiosInstance.get(`/api/subtitle-files/by-uuid`, {
      params: {"uuid": uuid}
    } );
    return response.data;
  },
  
  create: async (subtitleFile: SubtitleFileCreateUpdate): Promise<SubtitleFile> => {
    const response = await axiosInstance.post('/api/subtitle-files/', subtitleFile);
    return response.data;
  },
  uploadFile: async (formdata: FormData, id: number ): Promise<SubtitleFile> =>{
  
  const axiosInstance1 = axios.create({
    baseURL: axiosInstance.defaults.baseURL,
    headers:{
      'Content-Type': 'multipart/form-data'
    }
  });
  const response = await axiosInstance1.post(`/api/subtitle-files/upload`, formdata, {
    params: {"id": id},
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
    return response.data;
  },
  
  update: async (id: number, subtitleFile: SubtitleFileCreateUpdate): Promise<SubtitleFile> => {
    const response = await axiosInstance.put(`/api/subtitle-files/${id}`, subtitleFile);
    return response.data;
  },
  
  updateByUuid: async (uuid: string, subtitleFile: SubtitleFileCreateUpdate): Promise<SubtitleFile> => {
    const response = await axiosInstance.put(`/api/subtitle-files/by-uuid`, subtitleFile,{
      params:{"uuid": uuid}
    } );
    return response.data;
  },
  
  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/api/subtitle-files/${id}`);
  },
  
  deleteByUuid: async (uuid: string): Promise<void> => {
    await axiosInstance.delete(`/api/subtitle-files/by-uuid`, {
      params:
      {"uuid": uuid}
    });
  },

  parseFile: async (id: string| number): Promise<void> => {
    const response = await axiosInstance.post(`/api/subtitle-files/parserows/${id}`);
    return response.data;
  },

  deleteAllRows: async (id: string| number): Promise<void> => {
    const response = await axiosInstance.delete(`/api/subtitle-files/delete-all-rows/${id}`);
    return response.data;
  },



  
};
