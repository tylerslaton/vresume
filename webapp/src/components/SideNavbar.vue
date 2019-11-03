<template>
    <div id="slide-out" class="sidenav sidenav-fixed">
        <div v-if="!loading && user" class="sidebar-header red lighten-1 valign-wrapper">
            <img class="center" :src="profilePic" @click="chooseFile" />
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
import firebase from 'firebase';
import db from '../firebase/init';
import { mapActions } from 'vuex';
import { getCurrentUser } from '../services/users';

export default {
    name: 'SideNavbar',
    data() {
        return {
            user: null,
            userImage: null,
            userID: null,
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
        profilePic() {
            return (
                this.userImage || 'http://tbfsa.co.za/wp-content/uploads/2016/09/profile-placeholder.jpg'
            );
        }
    },
    created() {
        firebase.auth().onAuthStateChanged(async user => {
            this.loading = true;
            if (user) {
                M.AutoInit();
                const snapshot = await getCurrentUser();
                this.userID = firebase.auth().currentUser.uid;
                snapshot.forEach(doc => (this.user = doc.data()));
                if (this.user && this.user.role === 'student') {
                    this.displayName = `${this.user.firstName} ${this.user.lastName}`;
                } else if (this.user && this.user.role === 'employer') {
                    this.displayName = this.user.companyName;
                }
                this.userImage = this.user.profilePicture;
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
            await this.uploadRef.put(this.pictureToUpload);
            this.userImage = await this.uploadRef.getDownloadURL();
            await db
                .collection('users')
                .doc(this.userID)
                .set(
                    {
                        profilePicture: this.userImage
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

li,
.sidebar-header span {
    cursor: pointer;
    font-size: 18px;
}
</style>