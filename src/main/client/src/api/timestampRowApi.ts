
// src/api/timestampRowsApi.ts
import axiosInstance from './axiosConfig';
import { TimestampRow, TimestampRowPageable } from '../types';

const baseHandle = "/api/timestamprow";

export const timestampRowsApi = {
  getAllByMainId: async (id: number): Promise<TimestampRow[]> => {

    const response = await axiosInstance.get(`${baseHandle}/by-main`,
      {params:{"mainId":id}}
    );
    return response.data;
  },
  
  getById: async (id: number): Promise<TimestampRow> => {
    const url  = `${baseHandle}/${id}`;
    const response = await axiosInstance.get(url);
    return response.data;
  },
  
  getByUuid: async (uuid: string): Promise<TimestampRow> => {
    const response = await axiosInstance.get(`${baseHandle}/uuid/${uuid}`);
    return response.data;
  },
  
  create: async ( item: TimestampRow): Promise<TimestampRow> => {
    const response = await axiosInstance.post(`${baseHandle}/`, item);
    return response.data;
  },
  
  update: async (id: number, item: TimestampRow): Promise<TimestampRow> => {
    const response = await axiosInstance.put(`${baseHandle}/${id}`, item);
    return response.data;
  },
  
  updateByUuid: async (uuid: string,  item: TimestampRow): Promise<TimestampRow> => {
    const response = await axiosInstance.put(`${baseHandle}/uuid/${uuid}`, item);
    return response.data;
  },
  
  delete: async (id: number): Promise<void> => {
    await axiosInstance.delete(`${baseHandle}/${id}`);
  },
  
  deleteByUuid: async (uuid: string): Promise<void> => {
    await axiosInstance.delete(`${baseHandle}/uuid/${uuid}`);
  },

  getPageByMain: async (mainId: string|number, page: number, size: number): Promise<TimestampRowPageable>=>{
    const response = await axiosInstance.get(`${baseHandle}/by-main/page`,
      {params:{"mainId":mainId,
        "page": page, 
        "size": size
      }}
    );
    return response.data;

  }
};