(defproject connections-app "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [org.clojure/clojurescript "0.0-3211"]
                 [reagent "0.5.0"]
                 [re-frame "0.4.1"]
                 [cljs-ajax "0.3.14"]]

  :source-paths ["src/clj"]

  :plugins [[lein-cljsbuild "1.0.6"]
            [lein-figwheel "0.3.3" :exclusions [cider/cider-nrepl]]]

  :clean-targets ^{:protect false} ["resources/public/js/compiled" "target"]

  :cljsbuild {:builds [{:id           "dev"
                        :source-paths ["src/cljs"]

                        :figwheel     {:on-jsload "connections-app.core/mount-root"}

                        :compiler     {:main                 connections-app.core
                                       :output-to            "resources/public/js/compiled/app.js"
                                       :output-dir           "resources/public/js/compiled/out"
                                       :asset-path           "js/compiled/out"
                                       ;:output-to            "../web-app/connections-app/app.js"
                                       ;:output-dir           "../web-app/connections-app/out"
                                       ;:asset-path           "/vanity-cms/static/connections-app/out"
                                       :source-map-timestamp true}}

                       {:id           "min"
                        :source-paths ["src/cljs"]
                        :compiler     {:main          connections-app.core
                                       :output-to     "resources/public/js/compiled/app.js"
                                       ;:output-to     "../web-app/connections-app/app.js"
                                       :optimizations :advanced
                                       :pretty-print  false}}]})
