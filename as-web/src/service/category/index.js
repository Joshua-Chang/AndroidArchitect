import * as api from'../../service/api';
import {del, get,post} from "../index";
export function categoryList(params) {
    return get(api.api.categoryList)(params)
}
export function removeCategory(params) {
    return del(api.api.removeCategory)(params)
}
export function addCategory(params) {
    return post(api.api.addCategory)(params)
}