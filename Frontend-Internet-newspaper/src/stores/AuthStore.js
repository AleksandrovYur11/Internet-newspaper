import { defineStore } from "pinia"

export const useAuthStore = defineStore("auth",  {
    state: () => ({
        JWT: null, 
        selectedRole: null,
        modal: false,

    }),
    actions: {
        setAuthUser(responseData){
            this.JWT = responseData.accessToken
            // console.log(responseData)
        },
        selectRole(role){
            this.selectedRole = role
        },
        sbrosRole(){
            this.selectedRole = null
            console.log(this.selectedRole)
        },
        showModal(){
            this.modal = true
        },
        closeModal(){
            this.modal = false
        }
    },
})

    
        