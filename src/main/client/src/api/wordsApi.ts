// src/api/wordsApi.ts
import { Word, WordCreateUpdate } from '../types';
import axiosInstance from './axiosConfig';

//import { Word, WordCreateUpdate } from '../types

export const wordsApi = {
  getAll: async (): Promise<Word[]> => {
    const response = await axiosInstance.get('/api/words/');
    return response.data;
  },
  
  getById: async (id: number): Promise<Word> => {
    const response = await axiosInstance.get(`/api/words/${id}`);
    return response.data;
  },
  
  getByUuid: async (uuid: string): Promise<Word> => {
    const response = await axiosInstance.get(`/api/words/by-uuid`, {
      params: { uuid }
    });
    return response.data;
  },
  
  create: async (word: WordCreateUpdate): Promise<Word> => {
    const response = await axiosInstance.post('/api/words/', word);
    return response.data;
  },
  
  update: async (id: number, word: Word): Promise<Word> => {
    const response = await axiosInstance.put(`/api/words/${id}`, word);
    return response.data;
  },
  
  patch: async (id: number, word: Partial<WordCreateUpdate>): Promise<Word> => {
    const response = await axiosInstance.patch(`/api/words/${id}`, word);
    return response.data;
  },
  
  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`/api/words/${id}`);
  }
};
