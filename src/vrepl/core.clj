(ns vrepl.core
  (:require [clojure.string :as s]
            [clojure.java.io :as io]
            [nrepl.server :as nrepl]))

(defn sh [& args]
  (let [ps (.. (ProcessBuilder. args) inheritIO start)]
    (.addShutdownHook (Runtime/getRuntime) (Thread. #(.destroy ps)))
    ps))

(defn has-bin? [bin]
  (->> (s/split (System/getenv "PATH") #":")
       (map io/file)
       (mapcat file-seq)
       (some #(and (= (.getName %) bin) (.canExecute %)))))

(defn -main [& args]
  (let [vi (if (has-bin? "mvim") ["mvim" "-f"] ["vim"])
        port (:port (nrepl/start-server))
        ex ["+set ft=clojure"
            (str "+Connect nrepl://localhost:" port " .")
            "+insert"]
        ps (apply sh (concat vi ex args))]
    (System/exit (.waitFor ps))))
