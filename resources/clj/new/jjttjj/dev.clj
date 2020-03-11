(ns dev
  (:require [jjttjj.base.cmpnt.sente :as sente]
            [jjttjj.base.cmpnt.http-kit :as http]
            [jjttjj.base.util :refer :all]
            [objection.core :as obj]
            [shadow.cljs.devtools.api :as shadow]
            [shadow.cljs.devtools.server :as shadow-server]
            [compojure.core :as compojure]
            [taoensso.timbre.tools.logging :refer [use-timbre]]
            [taoensso.timbre :as log]))

(set! *print-meta* false)
(set! *print-length* 30)
(set! *print-level* 10)

(use-timbre)

(def CFG {:shadow {:build :app}})

(defn start-shadow []
  (shadow-server/start!)
  (shadow/watch (-> CFG :shadow :build)))

(defn reset-shadow []
  (shadow-server/stop!)
  (start-shadow))

(defn cljs-repl [] (shadow/repl (:build CFG)))

(defn start []
  (let [shadow (obj/register (start-shadow)
                             {:stopfn (fn [_]
                                        (log/info "Stopping Shadow")
                                        (shadow-server/stop!))
                              :name   "shadow-cljs server"})
        sente  (obj/register (sente/mk))
        routes (compojure/routes (sente/routes sente) (http/dflt-routes))
        srv    (obj/construct
                {:name   "http-kit server"
                 :stopfn (fn [this]
                           (log/info "Stopping http-kit")
                           (this :timeout 100))}
                (http/http-server (http/wrap-defaults routes) {:browse! true}))
        sente-router
        (obj/construct {:stopfn (fn [this]
                                  (log/info "Stopping sente router")
                                  (this))
                        :deps   [sente]}
                       (sente/basic-router sente))]
    {:shadow shadow
     :sente  sente
     :routes routes
     :server srv}))


(defn reset []
  (obj/stop-all!)
  (start))
