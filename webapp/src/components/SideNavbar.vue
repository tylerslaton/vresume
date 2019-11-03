<template>
    <div v-show="showSideNav">
        <ul id="slide-out" class="sidenav sidenav-fixed">
            <div class="sidebar-header red lighten-1">
                <img
                    class="center"
                    src="https://cdn.discordapp.com/attachments/639928720274227231/640337425142644757/largeg.jpg"
                />
            </div>
        </ul>
    </div>
</template>

<script>
export default {
    name: 'SideNavbar'
}
</script>