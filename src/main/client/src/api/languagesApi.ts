
// src/api/languagesApi.ts
import axiosInstance from './axiosConfig';
import {Language, LanguageCreateUpdate } from '../types';

export const languagesApi = {
  getAll: async (): Promise<Language[]> => {
    const response = await axiosInstance.get('/api/languages/');
    return response.data;
  },
  
  getById: async (id: number): Promise<Language> => {
    const response = await axiosInstance.get(`/api/languages/${id}`);
    return response.data;
  },
  
  getByUuid: async (uuid: string): Promise<Language> => {
    const response = await axiosInstance.get(`/api/languages/by-uuid`, {
      params: { uuid }
    });
    return response.data;
  },
  
  create: async (language: LanguageCreateUpdate): Promise<Language> => {
    const response = await axiosInstance.post('/api/languages/', language);
    return response.data;
  },
  
  update: async (id: number, language: LanguageCreateUpdate): Promise<Language> => {
    const response = await axiosInstance.put(`/api/languages/${id}`, language);
    return response.data;
  },
  
  patch: async (id: number, language: Partial<LanguageCreateUpdate>): Promise<Language> => {
    const response = await axiosInstance.patch(`/api/languages/${id}`, language);
    return response.data;
  },
  
  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/api/languages/${id}`);
  }
};
