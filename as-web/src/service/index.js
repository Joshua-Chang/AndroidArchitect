import {url} from "./api";
import {userList, updateUser} from "./user";
import {categoryList,removeCategory,addCategory} from "./category";

export default {
    userList,
    updateUser,
    categoryList,
    removeCategory,
    addCategory
}

const AUTH_TOKEN = "A6F00031B6F90CCBEB98C13423F84DDE"

export function get(api) {
    /*get函数返回另一个方法fetch，切接收一个参数params*/
    /*函数可理化，将一个函数里的多个参数，拆成多步调用*/
    return params => fetch(buildParams(url + api, params),
        {
            headers: {
                'auth-token': AUTH_TOKEN
            }
        }
    )
}

/*api.updateUser({forbid})(record.uid)*/
export function put(api) {
    /*第一个参数params为body，第二个参数queryParams为url path或查询参数*/
    return params => {
        const formData = new FormData();
        Object.entries(params).forEach(([k, v]) => {
            formData.append(k, v);
        });
        return queryParams => fetch(buildParams(url + api, queryParams),
            {
                method: 'PUT',
                body: formData,
                headers: {
                    'auth-token': AUTH_TOKEN
                }
            }
        )
    }
}
export function post(api) {
    /**
     * 第一个参数作为body参数，第二个参数作为URL path或者查询参数
     */
    return params => {
        return queryParams => fetch(buildParams(url + api, queryParams),
            {
                method: 'POST',
                body: JSON.stringify(params),
                headers: {
                    'content-type': 'application/json',
                    'auth-token': AUTH_TOKEN
                }
            }
        )
    }
}
export function del(api) {
    return params => fetch(buildParams(url + api, params),
        {
            method: 'DELETE',
            headers: {
                'auth-token': AUTH_TOKEN
            }
        }
    )
}



function buildParams(url, params = {}) {
    let newUrl = new URL(url);
    if (typeof params === 'object') {//适配普通查询参数
        Object.keys(params).forEach(
            key => {
                newUrl.searchParams.append(key, params[key])
            }
        );
        return newUrl.toString()
    } else {
        //适配path参数
        return url.endsWith("/") ? url + params : url + "/" + params;
    }
}