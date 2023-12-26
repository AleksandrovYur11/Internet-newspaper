import { defineStore } from "pinia"
import router from '@/router/index.js'

import { useNewsStore } from "@/stores/NewsStore"

import router from '@/router/index.js'

export const useAuthStore = defineStore("auth",  {
    state: () => ({
        JWT: null, 
        selectedRole: null,
        user: null,
        role: null,
        reg_users: [],
        news: null,
        user_role: ''
    }),
    actions: {
      async updateAccessToken() {
        const refreshToken = {
            refreshToken: sessionStorage.getItem("jwtRefreshToken"),
        }
        try {
            const response = await fetch(
                "http://localhost:8085/auth/refresh-token",
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(refreshToken),
                }
            )
            const responseData = await response.json()
            if (responseData.refreshToken === `Failed for [${refreshToken.refreshToken}]: Refresh token was expired. Please make a new signin request`) {
                router.push('/auth/sign-in')
                sessionStorage.removeItem("jwtToken")
                sessionStorage.removeItem("jwtRefreshToken", responseData.accessToken)
                throw new Error("refresh ---- failed")
            }

            sessionStorage.setItem("jwtToken", responseData.accessToken)
        } catch (error) {
            console.error("not refresh error:", error)
        }
    },
      ///////////////////////////////////////////////////////////////

        async login(textEmail, textPassword) {
            const loginData = {
                email: textEmail,
                password: textPassword,
            }
            console.log(loginData)
            try {
                const response = await fetch("http://localhost:8085/auth/sign-in", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                    },
                    body: JSON.stringify(loginData),
                })
        
                if (!response.ok) {
                    alert('Неправильный вход!')
                    textEmail = null
                    textPassword = null
                    throw new Error("Authentication failed")
                }
        
                const responseData = await response.json()

                sessionStorage.setItem('user_id', responseData.id)
                sessionStorage.setItem('user_role', responseData.roles[0])
              
                this.user = sessionStorage.getItem('user_id')
                this.role = sessionStorage.getItem('user_role')

                console.log(this.user)
                console.log(this.role)

                //console.log()
                
                const jwtToken = responseData.accessToken
                const jwtRefreshToken = responseData.refreshToken
                sessionStorage.setItem('jwtRefreshToken', jwtRefreshToken)

                //const name = responseData.name
                console.log(responseData)
        
                this.selectRole(responseData.roles[0])
                console.log(responseData.roles[0])
                
                sessionStorage.setItem('jwtToken', jwtToken);
                
                router.push('/news/fresh-news')
            } catch (error) {
                console.error("Authentication error:", error)
            }
        },
        // updateData(user) {  //что это???
        //     // Обновление состояния хранилища данными из запроса
        //     // Например, сохранение данных в состоянии
        //     this.$patch(user); // Предположим, что данные являются объектом
        // },
        async register(regData) {
            //console.log(regData)
            const userData = {
              name: regData.textName,
              surname: regData.textSurname,
              email: regData.textEmail, 
              password: regData.textPassword, 
            };
            console.log(userData)
            try {
              const response = await fetch("http://localhost:8085/auth/sign-up", {
                method: "POST",
                headers: {
                  "Content-Type": "application/json",
                },
                body: JSON.stringify(userData),
              });
          
              if (!response.ok) {
                // Обработка ошибок при регистрации
                throw new Error("Registration failed");
              }

            //   const News = useNewsStore()
            // this.news = News.getnews()
              //const responseData = await response.json()
              //this.reg_users.push(responseData)
              router.push('/auth/sign-in')
              
            } catch (error) {
              console.error("Registration error:", error);
            }
          },
        // setAuthUser(responseData){
        //     this.JWT = responseData.accessToken
        //     // console.log(responseData)
        // },
        selectRole(role){
            this.role = role
        },
        sbrosRole(){
            this.role = null
            sessionStorage.removeItem('user_id')
            sessionStorage.removeItem('user_role')
            sessionStorage.removeItem('jwtToken')

            const NewsStore = useNewsStore()
            NewsStore.likes = []
            //sessionStorage.removeItem('news_for_edit')
        },
        // hasUser(user_id){
        //     return this.users.some((user) => user.id === user_id)
        // }

      //   async updateAccessToken() {
      //     const refreshToken = {
      //       jwtRefreshToken: sessionStorage.getItem('jwtRefreshToken'),
      //     }

      //     console.log(refreshToken)

      //     try {
      //         const response = await fetch("http://localhost:8085/auth/refresh-token", {
      //             method: "POST",
      //             headers: {
      //                 "Content-Type": "application/json",
      //             },
      //             body: JSON.stringify(refreshToken),
      //         })
      
      //         if (!response.ok) {
      //             alert('Неправильный запрос!')
      //             throw new Error("Authentication failed")
      //         }
      
      //         const responseData = await response.json()
              
      //         sessionStorage.setItem('jwtToken', responseData.accessToken)
      //         console.log(responseData.accessToken)

      //         //sessionStorage.setItem('user_role', responseData.roles[0])
            
      //         //this.user = sessionStorage.getItem('user_id')
      //         //this.role = sessionStorage.getItem('user_role')

      //         // console.log(this.user)
      //         // console.log(this.role)
              
      //         // const jwtToken = responseData.accessToken
      //         // const jwtRefreshToken = responseData.jwtRefreshToken
      //         // sessionStorage.setItem('jwtRefreshToken', jwtRefreshToken)

      //         // //const name = responseData.name
      //         // console.log(responseData)
      
      //         // this.selectRole(responseData.roles[0])
      //         // console.log(responseData.roles[0])
              
      //         // sessionStorage.setItem('jwtToken', jwtToken)
      //     } catch (error) {
      //         console.error("not refresh error:", error)
      //     }
      // },
    },
})

    
        