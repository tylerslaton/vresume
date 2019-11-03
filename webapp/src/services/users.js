import db from '../firebase/init';
import firebase from 'firebase/app';

export const setCurrentUser = user => {
    const userToAdd = { role: user.role };
    if (user.role === 'student') {
        userToAdd.firstName = user.firstName;
        userToAdd.lastName = user.lastName;
    } else {
        userToAdd.companyName = user.companyName;
    }

    localStorage.setItem('user', JSON.stringify(userToAdd));
};

export const getCurrentUser = () => {
    return db
        .collection('users')
        .where('uid', '==', firebase.auth().currentUser.uid)
        .get();
};
