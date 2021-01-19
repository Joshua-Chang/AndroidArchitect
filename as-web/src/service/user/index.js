import * as api from '../../service/api'
import {get,put} from "../index";
export function userList(params) {
    return get(api.api.userList)(params)
}
export function updateUser(params) {
    return put(api.api.updateUser)(params)
}