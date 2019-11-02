<template>
    <div>
        <label>Resume</label>
        <div class="file-field input-field">
            <div class="btn">
                <span>Browse</span>
                <input type="file" id="resume" accept=".pdf, .jpeg, .jpg, .png" @change="getResume" />
            </div>
            <div class="file-path-wrapper">
                <input class="file-path validate" type="text" placeholder="Upload resume" />
            </div>
        </div>
        <button :disabled="!resume || uploading" @click="uploadResume" class="btn blue">Upload</button>
    </div>
</template>

<script>
import firebase from 'firebase';

export default {
    name: 'Resume',
    data() {
        return {
            resume: null,
            storageRef: firebase.storage().ref(),
            uploading: false
        };
    },
    methods: {
        getResume() {
            this.resume = document.querySelector('#resume').files[0];
        },
        async uploadResume() {
            if (!this.resume) return;
            this.uploading = true;
            const resumeRef = this.storageRef.child(
                `resumes/${firebase.auth().currentUser.uid}/${this.resume.name}`
            );
            const snapshot = await resumeRef.put(this.resume);
            this.uploading = false;
        }
    }
};
</script>