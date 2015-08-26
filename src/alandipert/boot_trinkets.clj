(ns alandipert.boot-trinkets
  {:boot/export-tasks true}
  (:require [boot.pod        :as pod]
            [boot.core       :as core]
            [boot.util       :as util]
            [clojure.java.io :as io]))

(defn copy [tf dir]
  (let [f (core/tmp-file tf)]
    (io/copy f (doto (io/file dir (:path tf)) io/make-parents))))

(defn run-main
  [pod classes-dir class-name argv]
  {:pre [(every? string? argv)]}
  (let [main-sym (symbol (str class-name) "main")]
    (pod/with-eval-in pod
      (boot.pod/add-classpath ~(.getPath classes-dir))
      (~main-sym (into-array String ~(vec argv))))))

(core/deftask run
  "Add classes to a pod and run a main method."
  [m main CLASSNAME sym   "The main class"
   a args ARGUMENTS [str] "String arguments to pass to the main class's main method"]
  (let [classdir (core/tmp-dir!)
        runners  (pod/pod-pool (core/get-env))]
    (core/with-pre-wrap fileset
      (let [class-files (->> fileset
                             core/output-files
                             (core/by-ext [".class"]))]
        (core/empty-dir! classdir)
        (doseq [tmpfile class-files] (copy tmpfile classdir))
        (run-main (runners :refresh) classdir main args)
        fileset))))
