(set-env!
  :dependencies '[[org.clojure/clojure       "1.6.0"     :scope "provided"]
                  [boot/core                 "2.0.0-rc4" :scope "provided"]
                  [adzerk/bootlaces          "0.1.8"     :scope "test"]]
  :source-paths #{"src"})

(require '[adzerk.bootlaces :refer :all]
         '[alandipert.boot-trinkets :refer [run]])

(def +version+ "1.0.0")

(bootlaces! +version+)

(task-options!
 pom  {:project     'alandipert/boot-trinkets
       :version     +version+
       :description "Alan's boot trinkets"
       :url         "https://github.com/alandipert/boot-trinkets"
       :scm         {:url "https://github.com/alandipert/boot-trinkets"}
       :license     {:name "Eclipse Public License"
                     :url  "http://www.eclipse.org/legal/epl-v10.html"}})
