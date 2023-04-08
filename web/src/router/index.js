import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/home/HomeView.vue'
import SquareView from '../views/square/SquareView.vue'
import RankView from '../views/rank/RankView.vue'
import AppView from '../views/app/AppView.vue'
import AppContentView from '../views/app/AppContentView.vue'
import AdminView from '../views/admin/AdminView.vue'
import UserManageView from '../views/admin/UserManageView.vue'
import BlogManageView from '../views/admin/BlogManageView.vue'
import NewsManageView from '../views/admin/NewsManageView.vue'
import AppManageView from '../views/admin/AppManageView.vue'
import BlogView from '../views/square/BlogView'
import LoginView from '../views/user/LoginView.vue'
import RegisterView from '../views/user/RegisterView.vue'
import UserView from '../views/user/UserView.vue'
import NotFoundView from '../views/notFound/NotFoundView.vue'
import TextView from '../views/TextView.vue'
import {AjaxUtils} from "@/assets/utils/ajaxUtils";
import store from "@/store";

const routes = [
    {path: '/', name: 'index', component: HomeView, meta: {title: "AppHub"}},
    {path: '/home', name: 'home', component: HomeView, meta: {title: "AppHub"}},
    {path: '/square', name: 'square', component: SquareView, meta: {title: "AppHub - square"}},
    {path: '/rank', name: 'rank', component: RankView, meta: {title: "AppHub - rank"}},
    {path: '/app', name: 'app', component: AppView, meta: {title: "AppHub - app"}},
    {path: '/app/:id', name: 'app_content', component: AppContentView, meta: {title: "AppHub - app"}},
    {path: '/user', name: 'user', component: UserView, meta: {title: "AppHub - user"}},
    {
        path: '/admin', name: 'admin', component: AdminView, redirect: '/admin/usermanage', children: [
            {path: '/admin/usermanage', name: 'user_manage', component: UserManageView},
            {path: '/admin/blogmanage', name: 'blog_manage', component: BlogManageView},
            {path: '/admin/newsmanage', name: 'news_manage', component: NewsManageView},
            {path: '/admin/appmanageview', name: 'app_manage', component: AppManageView}
        ]
    },
    {path: '/blog/:id', name: 'blog_content', component: BlogView, meta: {title: "AppHub - Blog"}},
    {path: '/login', name: 'login', component: LoginView},
    {path: '/register', name: 'register', component: RegisterView},
    {path: '/404', name: '404', component: NotFoundView},
    {path: '/text', name: 'text', component: TextView},
    {path: '/:catchAll(.*)', redirect: '/404'}
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach(async (to, from, next) => {
    window.document.title = to.meta.title || "AppHub"
    if (!store.state.user.is_login) {
        const token = localStorage.getItem("jwt_token")
        if (token) {
            store.commit("updateToken", token)
            const resp = await AjaxUtils.getInfo()
            if (resp.msg === "success") {
                store.commit("updateInfo", resp.data)
                store.commit("updateLogin", true)
                next()
            } else {
                localStorage.removeItem("jwt_token")
                store.commit("updateLoginDialogVisible", true)
            }
        } else next()
    } else next()
})

export default router
