<script setup>
import { ref } from 'vue';

import {useAuthStore} from '@/stores/AuthStore.js'
const Auth = useAuthStore()

import { useNewsStore } from '@/stores/NewsStore'
const NewsStore = useNewsStore()

const showModal = ref(true);
const microThemes = ref('');
const postTitle = ref('');
const postText = ref('');
const allowComments = ref(false);
const selectedImage = ref(null);

const openModal = () => {
  showModal.value = true;
};

const closeModal = () => {
    Auth.closeModal()
  //showModal.value = false;
};

//буду отправлять на сервер а потом при обновлении он будет выгружать все это добро автоматически)))

  const postData = {
    microThemes: microThemes.value.split(',').map(theme => theme.trim()),
    postTitle: postTitle.value,
    postText: postText.value,
    allowComments: allowComments.value,
    selectedImage: selectedImage.value,
  }


const handleImageUpload = (event) => {
  const file = event.target.files[0];
  selectedImage.value = file;
};
</script>


  <template>
    <div style = "min-height: 100vh; min-width: 100vw; max-width: 100vw;">
        <div class="container"  @click.self="closeModal">
      <div class="modal-content">
        <span class="close" @click="closeModal">&times;</span>
        <h2>Создание поста</h2>
        <form @submit.prevent="submitForm">
          <div class="input-group">
            <label for="postTitle">Название поста:</label>
            <input type="text" id="postTitle" v-model="postTitle">
          </div>
          <div class="input-group">
            <label for="microThemes">Микро-темы (через запятую):</label>
            <input type="text" id="microThemes" v-model="microThemes">
          </div>
          <div class="input-group">
            <label for="postText">Основной текст:</label>
            <textarea id="postText" v-model="postText"></textarea>
          </div>
          <div class="input-group">  
            <label for="imageUpload">Загрузка изображения:</label>
            <input type="text" id="imageUpload">
          </div>
          <b-button type="submit" @click = "NewsStore.addNews(postData)">Создать пост</b-button>
        </form>
      </div>
    </div>
        
    </div>
</template>

<style scoped> 

.container {
    background-color: #a298d3;
    min-width: 100vw; 
    max-width: 100vw; 
    min-height: 100vh;
    position: absolute;
    display: flex !important;
    justify-content: center;
    align-items: center;
    background-color: rgba(0, 0, 0, 0.5);
}

.modal {
  display: none;
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  overflow: auto;
  background-color: rgba(0, 0, 0, 0.5);
}

.modal-content {
  background-color: #fefefe;
  margin: 15% auto;
  padding: 20px;
  border: 1px solid #888;
  width: 50%;
  animation: modal-show 0.3s;
}

@keyframes modal-show {
  from { opacity: 0 }
  to { opacity: 1 }
}

.close {
  color: #aaa;
  float: right;
  font-size: 28px;
  font-weight: bold;
}

.close:hover,
.close:focus {
  color: black;
  text-decoration: none;
  cursor: pointer;
}
</style>