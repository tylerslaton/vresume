import Vue from 'vue';
import VueRouter from 'vue-router';
import firebase from 'firebase/app';

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
        }
    ]
});

router.beforeEach((to, from, next) => {
    // check if route requires auth
    if (to.matched.some(rec => rec.meta.requiresAuth)) {
        if (firebase.auth().currentUser) {
            next();
        } else {
            next({ name: 'login' });
        }
    } else {
        next();
    }
});

export default router;
