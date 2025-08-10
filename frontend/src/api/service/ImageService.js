import { API } from "../domain";
import { axiosCall } from "../plugin/axios";
export const ImageService = {
    fetchListImage: (params) => {
        return axiosCall.get(API.LIST_IMAGE, params);
    },
    uploadImage: (params) => {
        return axiosCall.post(API.UPLOAD_IMAGE, params);
    },
    uploadImage2: (data, config = {}) => {
        return axiosCall.upload(API.UPLOAD_IMAGE, data, config);
    },

    deleteImages: (params) => {
        return axiosCall.delete(`${API.DELETE_IMAGES}`, params);
    },
};
