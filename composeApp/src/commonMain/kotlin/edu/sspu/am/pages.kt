package edu.sspu.am

object MetaPages {
    val app = Page("@app", "AquaMaster", null, "/")
    val exit = Page("@exit", "EXIT", null, "/")
    val splash = Page("@splash", "Splash", null, "/")
    val surprise = Page("@surprise", "Surprise", null, "/0/surprise")
}


object MetaScreens {
    val app = Screen(listOf(Page("@app", "AquaMaster", null, "/")))
    val exit = Screen(listOf(Page("@exit", "EXIT", null, "/")))
    val splash = Screen(listOf(Page("@splash", "Splash", null, "/")))
    val surprise = Screen(listOf(Page("@surprise", "Surprise", null, "/0/surprise")))
}


object RootPages {
    val List = Page("List", "列表", null, "/List")
    val Cloud = Page("Cloud", "云端", null, "/Cloud")
    val Community = Page("Community", "社区", null, "/Community")
    val Personal = Page("Personal", "我的", null, "/Personal")
    val Settings = Page("Settings", "设置", null, "/Settings")
}


object RootScreens {
    val List = Screen(listOf(MetaPages.app, RootPages.List))
    val Cloud = Screen(listOf(MetaPages.app, RootPages.Cloud))
    val Community = Screen(listOf(MetaPages.app, RootPages.Community))
    val Personal = Screen(listOf(MetaPages.app, RootPages.Personal))
    val Settings = Screen(listOf(MetaPages.app, RootPages.Settings))
}


object Pages {
    val app = object {
        inline fun get() = MetaPages.app

        val List = object {
            inline fun get() = RootPages.List
        }

        val Cloud = object {
            inline fun get() = RootPages.Cloud
        }

        val Community = object {
            inline fun get() = RootPages.Community
        }

        val Personal = object {
            inline fun get() = RootPages.Personal
        }
    }

    val exit = object {
        inline fun get() = MetaPages.exit
    }
    val surprise = object {
        inline fun get() = MetaPages.surprise
    }
}


