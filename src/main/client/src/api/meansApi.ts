
// src/api/meansApi.ts
import axiosInstance from './axiosConfig';
import { Means, MeansCreateUpdate } from './types';

export const meansApi = {
  getAll: async (): Promise<Means[]> => {
    const response = await axiosInstance.get('/api/means');
    return response.data;
  },
  
  getById: async (id: number): Promise<Means> => {
    const response = await axiosInstance.get(`/api/means/${id}`);
    return response.data;
  },
  
  getByUuid: async (uuid: string): Promise<Means> => {
    const response = await axiosInstance.get(`/api/means/uuid/${uuid}`);
    return response.data;
  },
  
  getByWordUuid: async (wordUuid: string): Promise<Means[]> => {
    const response = await axiosInstance.get(`/api/means/${wordUuid}`);
    return response.data;
  },
  
  create: async (means: MeansCreateUpdate): Promise<Means> => {
    const response = await axiosInstance.post('/api/means', means);
    return response.data;
  },
  
  update: async (id: number, means: MeansCreateUpdate): Promise<Means> => {
    const response = await axiosInstance.put(`/api/means/${id}`, means);
    return response.data;
  },
  
  patch: async (id: number, means: Partial<MeansCreateUpdate>): Promise<Means> => {
    const response = await axiosInstance.patch(`/api/means/${id}`, means);
    return response.data;
  },
  
  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/api/means/${id}`);
  }
};
