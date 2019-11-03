<template>
    <div>
        <div class="resume-container">
            <div id="QRCodeModal" class="modal">
                <div class="modal-content">
                    <div class="modal-header">
                        <h4>QR Code</h4>
                    </div>
                    <hr />
                    <img v-if="generatedCode" :src="generatedCode" />
                </div>
            </div>
            <div v-if="!userResume && !loading">
                <h5 class="center">You have not submitted a resume</h5>
            </div>
            <img class="resume" v-if="userResume" :src="userResume" alt />
            <br />
            <div class="buttons">
                <button
                    v-if="!loading"
                    :disabled="uploading"
                    class="btn blue darken-1"
                    @click="chooseFile"
                >{{ buttonText }}</button>
                <a
                    v-if="!loading && userResume"
                    :disabled="uploading"
                    class="btn modal-trigger"
                    href="#QRCodeModal"
                    style="margin-left: 15px;"
                    @click="generateQRCode"
                >QR Code</a>
            </div>

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
            loading: false,
            generatedCode: null,
            generating: false
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
            return this.userResume ? 'Update' : 'Upload resume';
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
        M.AutoInit();
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
                .set({ resume: this.userResume }, { merge: true });
            this.uploading = false;
        },
        generateQRCode() {
            const userID = encodeURIComponent(this.userID);
            this.generatedCode = `https://api.qrserver.com/v1/create-qr-code/?data=${userID}&size=200x200`;
        }
    }
};
</script>

<style scoped>
.modal {
    left: auto;
    right: auto;
    width: 345px;
    height: 340px;
}

.modal-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.resume-container {
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: -20px;
}

.card-panel {
    max-width: 750px;
    min-height: 650px;
    margin: 0 auto;
}

.resume {
    max-width: 530px;
    margin-bottom: 7px;
}

@media (max-width: 500px) {
    .resume {
        max-width: 385px;
    }
}
</style>