<template>
    <div>
        <ul class="collapsible">
            <li v-for="(resume, index) in resumes" :key="index">
                <div class="collapsible-header">Resume #{{ resume }}</div>
                <div class="collapsible-body">
                    <span>Lorem ipsum dolor sit amet.</span>
                </div>
            </li>
        </ul>
    </div>
</template>

<script>
export default {
    name: 'ViewResumes',
    data() {
        return {
            events: [1, 2, 3, 4],
            resumes: [5, 6, 7, 9]
        };
    }
};
</script>