import http from '@/utils/httpRequest.js'
export function policy(dir) {
   return  new Promise((resolve,reject)=>{
        http({
            url: http.adornUrl("/api/thirdparty/oss/policy"),
            method: "get",
            params: http.adornParams({
                'dir': dir,
            })
        }).then(({ data }) => {
            resolve(data);
        })
    });
}