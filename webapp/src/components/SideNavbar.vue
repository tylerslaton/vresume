<template>
    <div id="slide-out" class="sidenav sidenav-fixed">
        <div v-if="!loading" class="sidebar-header red lighten-1 valign-wrapper">
            <img class="center" :src="userImage" @click="chooseFile" />
            <input
                type="file"
                id="profile-picture"
                hidden
                accept=".jpeg, .jpg, .png"
                @change="uploadImage"
            />
            <span class="white-text">{{ displayName }}</span>
        </div>
        <div class="container">
            <ul v-if="user && user.role === 'student'">
                <li @click="setCurrentComponent('StudentResume')">View resume</li>
                <li @click="setCurrentComponent('ResumeTips')">Resume tips</li>
                <li @click="setCurrentComponent('InterviewTips')">Interview tips</li>
                <li @click="setCurrentComponent('BrandAwareness')">Brand awareness</li>
                <li @click="setCurrentComponent('HowItWorks')">How it works</li>
            </ul>
            <ul v-if="user && user.role === 'employer'">
                <li @click="setCurrentComponent('ViewResumes')">View all resumes</li>
                <li @click="setCurrentComponent('ConfigureBot')">Configure bot</li>
                <li @click="setCurrentComponent('HowItWorks')">How it works</li>
            </ul>
        </div>
    </div>
</template>

<script>
import firebase from 'firebase/app';
import db from '../firebase/init';
import { mapActions } from 'vuex';
import { getCurrentUser } from '../services/users';

export default {
    name: 'SideNavbar',
    data() {
        return {
            user: null,
            userID: firebase.auth().currentUser.uid,
            displayName: null,
            loading: true
        };
    },
    computed: {
        uploadRef() {
            return firebase
                .storage()
                .ref()
                .child(`profile_pictures/${this.userID}/${this.pictureToUpload.name}`);
        },
        userImage() {
            return this.user && this.user.profilePicture
                ? this.user.profilePicture
                : 'http://tbfsa.co.za/wp-content/uploads/2016/09/profile-placeholder.jpg';
        }
    },
    created() {
        firebase.auth().onAuthStateChanged(async user => {
            this.loading = true;
            if (user) {
                const snapshot = await getCurrentUser();
                snapshot.forEach(doc => (this.user = doc.data()));
                if (this.user && this.user.role === 'student') {
                    this.displayName = `${this.user.firstName} ${this.user.lastName}`;
                } else if (this.user && this.user.role === 'employer') {
                    this.displayName = this.user.companyName;
                }
            }
            this.loading = false;
        });
    },
    methods: {
        ...mapActions(['setCurrentComponent']),
        async uploadImage() {
            this.pictureToUpload = document.querySelector('#profile-picture').files[0];
            if (!this.pictureToUpload) return;
            this.uploading = true;
            const snapshot = await this.uploadRef.put(this.pictureToUpload);
            this.user.profilePicture = await this.uploadRef.getDownloadURL();
            await db
                .collection('users')
                .doc(this.userID)
                .set(
                    {
                        profilePicture: this.user.profilePicture
                    },
                    { merge: true }
                );
            this.uploading = false;
        },
        chooseFile() {
            document.querySelector('#profile-picture').click();
        }
    }
};
</script>

<style scoped>
.sidebar-header {
    height: 350px;
    display: flex;
    flex-direction: column;
    justify-content: center;
}

.sidebar-header img {
    border-radius: 100px;
    margin-bottom: 15px;
    width: 175px;
    height: 175px;
    object-fit: cover;
    cursor: pointer;
}

li {
    cursor: pointer;
}
</style>