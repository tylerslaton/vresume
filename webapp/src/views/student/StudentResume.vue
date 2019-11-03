<template>
    <div>
        <div class="resume-container">
            <div v-if="!userResume && !loading">
                <h5 class="center">You have not submitted a resume</h5>
            </div>
            <img class="resume" v-if="userResume" :src="userResume" alt />
            <br />
            <button
                v-if="!loading"
                :disabled="uploading"
                class="btn blue darken-1"
                @click="chooseFile"
            >{{ buttonText }}</button>
            <input
                type="file"
                id="resume"
                hidden
                accept=".pdf, .jpeg, .jpg, .png"
                @change="uploadResume"
            />
        </div>
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
        },
        buttonText() {
            return this.userResume ? 'Update resume' : 'Upload resume';
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
            await this.uploadRef.put(this.resumeToUpload);
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
.resume-container {
    display: flex;
    flex-direction: column;
    align-items: center;
}

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