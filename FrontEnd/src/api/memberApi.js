import { localAxios } from "@/util/http-commons";

const local = localAxios();

async function memberLoginApi(param, success, fail) {
    console.log("param", param);
    await local.post(`/member/login`, param).then(success).catch(fail);
}

export {
    memberLoginApi,
};