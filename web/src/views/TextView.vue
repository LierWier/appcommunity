<template>
  <div>
    <h3>Upload Image</h3>
    <input type="file" id="fileInput" />
    <button @click="uploadFile">Upload</button>
    <div v-if="imageUrl">
      <img :src="imageUrl" alt="uploaded image" />
    </div>
  </div>
</template>

<script>
// import $ from 'jquery'

export default {
  data() {
    return {
      imageUrl: null
    };
  },
  methods: {
    uploadFile() {
      console.log('upload')
      // const fileInput = document.getElementById("fileInput");
      // const file = fileInput.files[0];
      // const formData = new FormData();
      // formData.append("file", file);
      // $.ajax({
      //   url: "http://127.0.0.1:3001/api/common/upload",
      //   type: "POST",
      //   data: formData,
      //   processData: false,
      //   contentType: false,
      //   success: (response) => {
      //     this.imageUrl = response.url;
      //   },
      //   error: (jqXHR, textStatus, errorThrown) => {
      //     console.log(errorThrown);
      //   }
      // });
    }
  }
};
</script>
