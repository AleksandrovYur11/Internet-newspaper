import { defineStore } from "pinia"

export const useAuthStore = defineStore("auth",  {
    state: () => ({
        JWT: null
    }),
    actions: {
        setAuthUser(responseData){
            this.JWT = responseData.accessToken
            // console.log(responseData)
        }
    },
})

    
        