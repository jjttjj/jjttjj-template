(ns clj.new.jjttjj-template
  (:require [clj.new.templates :refer [renderer project-data ->files]]))

(defn jjttjj-template
  "FIXME: write documentation"
  [name]
  (let [render (renderer "jjttjj-template")
        data   (project-data name)]
    (println "Generating fresh 'clj new' jjttjj base project.")
    (->files data
             ["deps.edn" (render "deps.edn" data)]
             ["shadow-cljs.edn" (render "shadow-cljs.edn" data)]
             ["dev/dev.clj" (render "dev.clj" data)]
             ["dev/dev.cljs" (render "dev.cljs" data)]
             ["src/{{nested-dirs}}/core.clj" (render "core.clj" data)])))
