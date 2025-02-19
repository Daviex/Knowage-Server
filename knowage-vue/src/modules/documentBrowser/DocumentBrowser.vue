<template>
    <div class="kn-page">
        <div class="document-browser-tab-container kn-page-content p-d-flex p-flex-column">
            <TabView id="document-browser-tab-view" class="p-d-flex p-flex-column kn-flex" v-model:activeIndex="activeIndex" @tab-change="onTabChange">
                <TabPanel>
                    <template #header>
                        <i class="fa fa-folder-open"></i>
                    </template>

                    <DocumentBrowserHome @itemSelected="onItemSelect($event)"></DocumentBrowserHome>
                </TabPanel>

                <TabPanel v-for="(tab, index) in tabs" :key="index">
                    <template #header>
                        <span>{{ tab.item?.name ? tab.item?.name : 'new dashboard' }}</span>
                    </template>

                    <DocumentBrowserTab :item="tab.item" :mode="tab.mode"></DocumentBrowserTab>
                </TabPanel>
            </TabView>
            <div id="document-browser-tab-icon-container" v-if="activeIndex !== 0">
                <i id="document-browser-tab-icon" class="fa fa-times-circle" @click="toggle($event)"></i>
                <Menu ref="menu" :model="menuItems" :popup="true" />
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import { defineComponent } from 'vue'
import DocumentBrowserHome from './documentBrowserHome/DocumentBrowserHome.vue'
import DocumentBrowserTab from './DocumentBrowserTab.vue'
import Menu from 'primevue/menu'
import TabView from 'primevue/tabview'
import TabPanel from 'primevue/tabpanel'

export default defineComponent({
    name: 'document-browser',
    components: { DocumentBrowserHome, DocumentBrowserTab, Menu, TabView, TabPanel },
    data() {
        return {
            tabs: [] as any[],
            activeIndex: 0,
            menuItems: [] as any[]
        }
    },
    created() {
        if (this.$route.name === 'document-execution' && this.$route.params.id) {
            this.tabs.push({ item: null, mode: 'execute' })
            this.activeIndex = 1
        }
    },
    methods: {
        onTabChange() {
            if (this.activeIndex === 0) {
                this.$router.push('/document-browser')
                return
            }

            const id = this.tabs[this.activeIndex - 1].item ? this.tabs[this.activeIndex - 1].item.id : 'new-dashboard'
            this.$router.push('/document-browser/document-execution/' + id)
        },
        onItemSelect(payload: any) {
            this.tabs.push(payload)
            const id = payload.item ? payload.item.id : 'new-dashboard'
            this.$router.push('/document-browser/document-execution/' + id)
            this.activeIndex = this.tabs.length
        },
        toggle(event: any) {
            this.createMenuItems()
            const menu = this.$refs.menu as any
            menu.toggle(event)
        },
        createMenuItems() {
            this.menuItems = []
            this.menuItems.push({ label: this.$t('documentBrowser.closeCurrentDocument'), command: () => this.closeDocumentConfirm('current') })
            this.menuItems.push({ label: this.$t('documentBrowser.closeOtherDocuments'), command: () => this.closeDocumentConfirm('other') })
            this.menuItems.push({ label: this.$t('documentBrowser.closeDocumentRight'), command: () => this.closeDocumentConfirm('right') })
            this.menuItems.push({ label: this.$t('documentBrowser.closeAllDocuments'), command: () => this.closeDocumentConfirm('all') })
        },
        closeDocumentConfirm(mode: string) {
            this.$confirm.require({
                message: this.$t('documentBrowser.closeDocumentConfirmMessage'),
                header: this.$t('documentBrowser.closeDocumentConfirmTitle', { numberOfDocuments: this.getNumberOfDocuments(mode) }),
                accept: () => this.closeDocument(mode)
            })
        },
        getNumberOfDocuments(mode: string) {
            switch (mode) {
                case 'current':
                    return 1
                case 'other':
                    return this.tabs.length - 1
                case 'right':
                    return this.tabs.length - this.activeIndex
                case 'all':
                    return this.tabs.length
            }
        },
        closeDocument(mode: string) {
            switch (mode) {
                case 'current':
                    this.tabs.splice(this.activeIndex - 1, 1)
                    this.activeIndex = 0
                    this.$router.push('/document-browser')
                    break
                case 'other':
                    this.tabs = [this.tabs[this.activeIndex - 1]]
                    this.activeIndex = 1
                    break
                case 'right':
                    this.tabs.splice(this.activeIndex)
                    break
                case 'all':
                    this.$router.push('/document-browser')
                    this.tabs = []
                    this.activeIndex = 0
            }
        }
    }
})
</script>

<style lang="scss">
#document-browser-tab-view .p-tabview-panels {
    padding: 0;
}

#document-browser-tab-icon-container {
    position: absolute;
    top: 0.8rem;
    right: 2rem;
}

#document-browser-tab-icon {
    font-size: 1.2rem;
    color: rgba(0, 0, 0, 0.6);
    cursor: pointer;
}

.document-browser-tab-container .p-tabview .p-tabview-panel,
.document-browser-tab-container .p-tabview .p-tabview-panels {
    display: flex;
    flex-direction: column;
    flex: 1;
}
</style>
