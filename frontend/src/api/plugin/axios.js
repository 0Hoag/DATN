import axios from "./axiosConfig";
export const axiosCall = {
 get: async (url, params = {}, headers = {}) => {
  try {
   const response = await axios.get(url, {
    params,
    headers,
   });
   return response.data;
  } catch (error) {
   throw error;
  }
 },

 post: async (url, data = {}, headers = {}) => {
  try {
   const response = await axios.post(url, data, {
    headers,
   });
   return response.data;
  } catch (error) {
   throw error;
  }
 },

 put: async (url, data = {}, headers = {}) => {
  try {
   const response = await axios.put(url, data, {
    headers,
   });
   return response.data;
  } catch (error) {
   throw error;
  }
 },

 delete: async (url, config = {}) => {
  try {
   const response = await axios.delete(url, config);
   return response.data;
  } catch (error) {
   throw error;
  }
 },

 download: async (url, params = {}) => {
  try {
   const response = await axios.get(url, {
    params,
    responseType: "blob",
   });
   return response;
  } catch (error) {
   throw error;
  }
 },
};
