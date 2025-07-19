
// src/api/fileEntityApi.ts
import axiosInstance from './axiosConfig';
import { SubtitleFile, SubtitleFileCreateUpdate } from '../types';
import axios from 'axios';

export const fileEntityApi = {
  getAll: async (): Promise<SubtitleFile[]> => {
    const response = await axiosInstance.get('/api/files/');
    return response.data;
  },
  
  getById: async (id: number): Promise<SubtitleFile> => {
    const response = await axiosInstance.get(`/api/files/${id}`);
    return response.data;
  },
  
  getByUuid: async (uuid: string): Promise<SubtitleFile> => {
    const response = await axiosInstance.get(`/api/files/by-uuid`, {
      params: {"uuid": uuid}
    } );
    return response.data;
  },
  
  create: async (subtitleFile: SubtitleFileCreateUpdate): Promise<SubtitleFile> => {
    const response = await axiosInstance.post('/api/files/', subtitleFile);
    return response.data;
  },
  uploadFile: async (formdata: FormData, id: string ): Promise<SubtitleFile> =>{
  
  const axiosInstance1 = axios.create({
    baseURL: axiosInstance.defaults.baseURL,
    headers:{
      'Content-Type': 'multipart/form-data'
    }
  });
  const response = await axiosInstance1.post(`/api/files/upload/subtitle/${id}`, formdata, {
    
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
    return response.data;
  },
  
  update: async (id: number, subtitleFile: SubtitleFileCreateUpdate): Promise<SubtitleFile> => {
    const response = await axiosInstance.put(`/api/files/${id}`, subtitleFile);
    return response.data;
  },
  
  updateByUuid: async (uuid: string, subtitleFile: SubtitleFileCreateUpdate): Promise<SubtitleFile> => {
    const response = await axiosInstance.put(`/api/files/by-uuid`, subtitleFile,{
      params:{"uuid": uuid}
    } );
    return response.data;
  },
  
  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/api/files/${id}`);
  },
  
  deleteByUuid: async (uuid: string): Promise<void> => {
    await axiosInstance.delete(`/api/files/by-uuid`, {
      params:
      {"uuid": uuid}
    });
  },
  
};
