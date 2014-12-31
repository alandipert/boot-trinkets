(ns alandipert.boot-trinkets
  {:boot/export-tasks true}
  (:require [boot.pod        :as pod]
            [boot.core       :as core]
            [boot.util       :as util]
            [clojure.java.io :as io]))

(defmacro without-exiting
  "Evaluates body in a context where System/exit doesn't work.
  Returns result of evaluating body, or nil if code in body attempted to exit."
  [& body]
  `(let [old-sm# (System/getSecurityManager)
         new-sm# (proxy [SecurityManager] []
                   (checkPermission [p#])
                   (checkExit [s#] (throw (SecurityException.))))]
     (System/setSecurityManager ^SecurityManager new-sm#)
     (try ~@body
          (catch SecurityException e#)
          (finally (System/setSecurityManager old-sm#)))))

(defn copy [tf dir]
  (let [f (core/tmpfile tf)]
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
  (let [classdir (core/temp-dir!)
        runners  (pod/pod-pool (core/get-env))]
    (core/with-pre-wrap fileset
      (let [class-files (->> fileset
                             core/output-files
                             (core/by-ext [".class"]))]
        (core/empty-dir! classdir)
        (doseq [tmpfile class-files] (copy tmpfile classdir))
        (run-main (runners :refresh) classdir main args)
        fileset))))
