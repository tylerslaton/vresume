<template>
    <div>
        <div v-if="!userResume && !loading" class="card-panel">
            <h3 class="center">Please upload your resume</h3>
        </div>

        <img class="resume" v-if="userResume" :src="userResume" alt />
        <br>
        <button v-if="!loading" :disabled="uploading" class="btn blue darken-1" @click="chooseFile">Upload</button>
        <input type="file" id="resume" hidden accept=".pdf, .jpeg, .jpg, .png" @change="uploadResume" />
    </div>
</template>

<script>
import firebase from 'firebase';
import db from '../../firebase/init';

export default {
    name: 'Resume',
    data() {
        return {
            resumeToUpload: null,
            userResume: null,
            storageRef: firebase.storage().ref(),
            uploading: false,
            userID: firebase.auth().currentUser.uid,
            loading: false
        };
    },
    computed: {
        uploadRef() {
            return firebase
                .storage()
                .ref()
                .child(`resumes/${this.userID}/${this.resumeToUpload.name}`);
        }
    },
    async created() {
        this.loading = true;
        const user = await db
            .collection('users')
            .doc(this.userID)
            .get();
        this.userResume = user.data().resume;
        this.loading = false;
    },
    methods: {
        chooseFile() {
            document.querySelector('#resume').click();
        },
        getResume() {
            this.resumeToUpload = document.querySelector('#resume').files[0];
        },
        async uploadResume() {
            this.resumeToUpload = document.querySelector('#resume').files[0];
            if (!this.resumeToUpload) return;
            this.uploading = true;
            const snapshot = await this.uploadRef.put(this.resumeToUpload);
            this.userResume = await this.uploadRef.getDownloadURL();
            await db
                .collection('users')
                .doc(this.userID)
                .set(
                    {
                        resume: this.userResume
                    },
                    { merge: true }
                );
            this.uploading = false;
        }
    }
};
</script>

<style scoped>
.card-panel {
    max-width: 650px;
    min-height: 650px;
    margin: 0 auto;
}

.resume {
    max-width: 500px;
    margin-bottom: 15px;
}
</style>