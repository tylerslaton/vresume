import Vue from 'vue';
import VueRouter from 'vue-router';
import firebase from 'firebase/app';
import db from '../firebase/init';

Vue.use(VueRouter);

const router = new VueRouter({
    mode: 'history',
    routes: [
        {
            path: '/register',
            name: 'register',
            component: () => import('../views/Register.vue')
        },
        {
            path: '/login',
            name: 'login',
            component: () => import('../views/Login.vue')
        },
        {
            path: '/student-home',
            name: 'student-home',
            component: () => import('../views/student/HomeStudent.vue'),
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '/employer-home',
            name: 'employer-home',
            component: () => import('../views/employer/HomeEmployer.vue'),
            meta: {
                requiresAuth: true
            }
        },
        {
            path: '*',
            redirect: '/login'
        }
    ]
});

router.beforeEach(async (to, from, next) => {
    if (to.matched.some(rec => rec.meta.requiresAuth)) {
        const currentUser = firebase.auth().currentUser;

        if (to.name === 'login' || to.name === 'register') {
            firebase.auth().signOut();
            return next();
        }

        if (!currentUser) {
            firebase.auth().signOut();
            next({ name: 'login' });
        } else {
            const user = await db
                .collection('users')
                .doc(currentUser.uid)
                .get();
                
            const role = user.data().role;

            if (
                (to.name === 'student-home' && role !== 'student') ||
                (to.name === 'employer-home' && role !== 'employer')
            ) {
                return next({ name: `${role}-home` });
            }
            next();
        }
    } else {
        next();
    }
});

export default router;
