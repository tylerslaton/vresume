<template>
    <div>
        <div class="file-field input-field">
            <div class="btn">
                <span>Browse</span>
                <input type="file" id="resume" accept=".pdf, .jpeg, .jpg, .png" @change="getResume" />
            </div>
            <div class="file-path-wrapper">
                <input class="file-path validate" type="text" placeholder="Upload resume" />
            </div>
        </div>
        <div :disabled="!resume || uploading" @click="uploadResume" class="btn blue">Upload</div>
        <br />
        <img style="max-width: 520px" v-if="resumeImage" :src="resumeImage" alt />
    </div>
</template>

<script>
import firebase from 'firebase';

export default {
    name: 'Resume',
    data() {
        return {
            resume: null,
            resumeImage: null,
            storageRef: firebase.storage().ref(),
            uploading: false
        };
    },
    computed: {
        uploadRef() {
            return firebase
                .storage()
                .ref()
                .child(`resumes/${firebase.auth().currentUser.uid}/${this.resume.name}`);
        }
    },
    async created() {
        const resumes = await this.storageRef
            .child(`resumes/${firebase.auth().currentUser.uid}`)
            .listAll();
    },
    methods: {
        getResume() {
            this.resume = document.querySelector('#resume').files[0];
        },
        async uploadResume() {
            if (!this.resume) return;
            this.uploading = true;
            const snapshot = await this.uploadRef.put(this.resume);
            this.resumeImage = await this.uploadRef.getDownloadURL();
            this.uploading = false;
        }
    }
};
</script>