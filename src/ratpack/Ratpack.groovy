import ratpack.func.Action
import ratpack.groovy.template.TextTemplateModule
import ratpack.rx.RxRatpack
import ratpack.server.ServerConfig
import ratpack.server.Service
import ratpack.server.StartEvent
import reactivegroovydemo.modules.movies.MoviesModule
import reactivegroovydemo.modules.movies.MoviesUrlMappings
import reactivegroovydemo.modules.movies.config.MoviesConfig

import static ratpack.groovy.Groovy.groovyTemplate
import static ratpack.groovy.Groovy.ratpack
import static ratpack.rx.RxRatpack.initialize


ratpack {
    bindings {


        ServerConfig serverConfig = ServerConfig.builder()
            .onError(Action.throwException()).yaml("$serverConfig.baseDir.file/config.yaml")
            .build()
        bindInstance(MoviesConfig, serverConfig.get("/movies", MoviesConfig))

        initialize()
//        bindInstance Service, new Service() {
//            @Override
//            void onStart(StartEvent event) throws Exception {
//                println "Init RX"
//                initialize()
//            }
//        }
        module TextTemplateModule, { TextTemplateModule.Config config -> config.staticallyCompile = false }
        module MoviesModule
    }


    handlers {
        files { dir "public" }
        prefix("api") {
            all chain(registry.get(MoviesUrlMappings))
        }

        path {
            render groovyTemplate("index.html", title: "My Ratpack App")
        }
        path("author") {
            render groovyTemplate("index.html", title: "My Ratpack App")
        }
    }
}