export default class DataStore {
    async fetchData(url) {
        let header = { "auth-token": "MTU5Mjfaodf",'boarding-pass':'dfadsfailsd' }
        return new Promise((resolve, reject) => {
            fetch(url, { headers: header })
                .then((response) => {
                    if (response.ok) {
                        return response.json();
                    } else if (response.status === 401) {
                        console.log('response.status===401.')
                        return response.text();
                    }
                    throw new Error('network response was not ok.');
                })
                .then((responseData) => {
                    if (typeof responseData === 'string') {
                        reject(responseData);
                    } else {
                        resolve(responseData['data']);
                    }
                })
                .catch((error) => { reject(error); });
        });
    }
}