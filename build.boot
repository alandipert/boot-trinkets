(set-env!
 :dependencies '[[adzerk/bootlaces "0.1.12" :scope "test"]]
  :source-paths #{"src"})

(require '[adzerk.bootlaces :refer :all]
         '[alandipert.boot-trinkets :refer [run]])

(def +version+ "2.0.0")

(bootlaces! +version+)

(task-options!
 pom  {:project     'alandipert/boot-trinkets
       :version     +version+
       :description "Alan's odd collection of boot tasks and utilities"
       :url         "https://github.com/alandipert/boot-trinkets"
       :scm         {:url "https://github.com/alandipert/boot-trinkets"}
       :license     {"Eclipse Public License" "http://www.eclipse.org/legal/epl-v10.html"}})
