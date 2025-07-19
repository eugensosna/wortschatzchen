
// src/api/examplesApi.ts
import axiosInstance from './axiosConfig';
import { Example, ExampleCreateUpdate } from '../types';

export const examplesApi = {
  getAllByWordId: async (id: number): Promise<Example[]> => {
    const response = await axiosInstance.get('/api/examples/by-word',
      {params:{"wordId":id}}
    );
    return response.data;
  },
  
  getById: async (id: number): Promise<Example> => {
    const response = await axiosInstance.get(`/api/examples/${id}`);
    return response.data;
  },
  
  getByUuid: async (uuid: string): Promise<Example> => {
    const response = await axiosInstance.get(`/api/examples/uuid/${uuid}`);
    return response.data;
  },
  
  create: async (example: ExampleCreateUpdate): Promise<Example> => {
    const response = await axiosInstance.post('/api/examples/', example);
    return response.data;
  },
  
  update: async (id: number, example: Example): Promise<Example> => {
    const response = await axiosInstance.put(`/api/examples/${id}`, example);
    return response.data;
  },
  
  updateByUuid: async (uuid: string, example: Example): Promise<Example> => {
    const response = await axiosInstance.put(`/api/examples/uuid/${uuid}`, example);
    return response.data;
  },
  
  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/api/examples/${id}`);
  },
  
  deleteByUuid: async (uuid: string): Promise<void> => {
    await axiosInstance.delete(`/api/examples/uuid/${uuid}`);
  }
};