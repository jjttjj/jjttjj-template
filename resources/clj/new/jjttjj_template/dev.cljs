(ns dev
  (:require [jjttjj.base.cmpnt.sente :as sente]
            [jjttjj.base.util.dom :refer [$ $$]]))

(def SENTE (sente/mk))

(sente/on-open SENTE
  (println "sente connection established")
  ((:send-fn SENTE) [::hello {:hello :world}]))

(defn ^:dev/after-load start []
  (set! (.-innerHTML ($ "#app")) "")
  (.append ($ "#app") "hello, world!"))

(start)
