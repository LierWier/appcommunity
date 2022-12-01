import {createRouter, createWebHistory} from 'vue-router'
import HomeView from '../views/home/HomeView.vue'
import SquareView from '../views/square/SquareView.vue'
import RankView from '../views/rank/RankView.vue'
import AppView from '../views/app/AppView.vue'
import AdminView from '../views/admin/AdminView.vue'
import UserManageView from '../views/admin/UserManageView.vue'
import BlogManageView from '../views/admin/BlogManageView.vue'
import NewsManageView from '../views/admin/NewsManageView.vue'
import AppManageView from '../views/admin/AppManageView.vue'
import LoginView from '../views/user/LoginView.vue'
import RegisterView from '../views/user/RegisterView.vue'
import UserView from '../views/user/UserView.vue'
import NotFoundView from '../views/notFound/NotFoundView.vue'
import TextView from '../views/TextView.vue'

const routes = [
    {path: '/', name: 'index', component: HomeView},
    {path: '/home', name: 'home', component: HomeView},
    {path: '/square', name: 'square', component: SquareView},
    {path: '/rank', name: 'rank', omponent: RankView},
    {path: '/app', name: 'app', component: AppView},
    {path: '/user', name: 'user', component: UserView},
    {
        path: '/admin', name: 'admin', component: AdminView, redirect: '/admin/usermanage', children: [
            {path: '/admin/usermanage', name: 'user_manage', component: UserManageView},
            {path: '/admin/blogmanage', name: 'blog_manage', component: BlogManageView},
            {path: '/admin/newsmanage', name: 'news_manage', component: NewsManageView},
            {path: '/admin/appmanageview', name: 'app_manage', component: AppManageView}
        ]
    },
    {path: '/login', name: 'login', component: LoginView},
    {path: '/register', name: 'register', component: RegisterView},
    {path: '/404', name: '404', component: NotFoundView},
    {path: '/text', name: 'text', component: TextView}
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

export default router
